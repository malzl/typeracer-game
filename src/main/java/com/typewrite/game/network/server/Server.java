package com.typewrite.game.network.server;

import com.typewrite.game.network.messages.GameFinished;
import com.typewrite.game.network.messages.GameStartNotification;
import com.typewrite.game.network.messages.JsonConverter;
import com.typewrite.game.network.messages.PlayerJoinResponse;
import com.typewrite.game.network.messages.PlayerProgressBroadcast;
import com.typewrite.game.network.messages.PlayerReadyResponse;
import com.typewrite.game.util.JsonUtil;
import com.typewrite.game.util.LogUtil;
import com.typewrite.game.util.TextUtil;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Server class for managing server-side operations and communication with clients. Handles player
 * connections, game state management, and message broadcasting.
 */
public class Server {
  private final int port;
  private final List<ConnectionManager> clients = new CopyOnWriteArrayList<>();
  private final GameState gameState;
  private final Map<ConnectionManager, String> connectionToPlayerMap = new ConcurrentHashMap<>();
  private volatile boolean isRunning = true;

  /**
   * Constructs a Server with the specified port.
   *
   * @param port the port number to bind the server to.
   * @throws IllegalArgumentException if the port number is not between 0 and 65535.
   */
  public Server(int port) {
    if (port < 0 || port > 65535) {
      throw new IllegalArgumentException("Port number must be between 0 and 65535");
    }
    this.port = port;
    this.gameState = new GameState();
    this.gameState.createNewGameInfo(port);
  }

  /** Stops the server and closes all client connections. */
  public void stop() {
    isRunning = false;
    for (ConnectionManager client : clients) {
      client.close();
    }
    clients.clear();
    connectionToPlayerMap.clear();
  }

  /**
   * Starts the server and listens for client connections. Broadcasts player progress updates at
   * regular intervals.
   *
   * @throws IOException if an I/O error occurs.
   */
  public void start() throws IOException {
    Timer syncTimer = new Timer(true);
    syncTimer.schedule(
        new TimerTask() {
          @Override
          public void run() {
            if (!isRunning) {
              syncTimer.cancel();
              return;
            }
            if (connectionToPlayerMap.isEmpty()) {
              return;
            }
            if (gameState.getGameInfo().getStartTime() != null) {
              gameState.getGameInfo().setCurrTime(System.currentTimeMillis());
            }
            try {
              broadcastMessage(
                  JsonConverter.toJson(
                      new PlayerProgressBroadcast(
                          gameState.getGameInfo(),
                          new ArrayList<>(gameState.getPlayerMap().values()))),
                  null);
            } catch (IOException e) {
              LogUtil.error("Failed to broadcast player progress updates: " + e.getMessage());
            }
          }
        },
        0,
        1000 / 30);

    ServerSocket serverSocket = new ServerSocket(port);
    serverSocket.setSoTimeout(100); // Set a timeout to allow checking isRunning

    while (isRunning) {
      try {
        Socket socket = serverSocket.accept();
        LogUtil.info("Client connected.");
        ConnectionManager cm = new ConnectionManager(socket, this);
        clients.add(cm);
        Thread thread = new Thread(cm);
        cm.setThread(thread);
        thread.start();
      } catch (SocketTimeoutException e) {
        // This is expected, do nothing
      }
    }

    serverSocket.close();
  }

  /**
   * Adds a player to the game.
   *
   * @param playerName the name of the player to add
   * @param cm the connection manager for sending messages
   * @param selectedAvatar the selected avatar of the player
   * @throws IOException if an I/O error occurs
   */
  public synchronized void addPlayer(String playerName, ConnectionManager cm, String selectedAvatar)
      throws IOException {
    if (gameState.getPlayers().size() >= GameState.MAX_PLAYER) {
      PlayerJoinResponse response = new PlayerJoinResponse(playerName, false, selectedAvatar);
      cm.sendMessage(JsonConverter.toJson(response));
      return;
    }

    if (gameState.getPlayers().contains(playerName)) {
      PlayerJoinResponse response = new PlayerJoinResponse(playerName, false, selectedAvatar);
      cm.sendMessage(JsonConverter.toJson(response));
      return;
    }

    gameState.addPlayer(playerName, selectedAvatar);
    connectionToPlayerMap.put(cm, playerName);
    PlayerJoinResponse response = new PlayerJoinResponse(playerName, true, selectedAvatar);
    cm.sendMessage(JsonConverter.toJson(response));
  }

  /**
   * Starts the game with the provided sentences.
   *
   * @param sentences the sentences for the game.
   */
  public void startGame(List<String> sentences) {
    GameInfo gameInfo = this.gameState.getGameInfo();
    gameInfo.setTextTotalSize(TextUtil.getTextSize(sentences));
    gameInfo.setStartTime(System.currentTimeMillis());
    gameInfo.setCurrTime(gameInfo.getStartTime());
    gameState.setStarted(Boolean.TRUE);
    try {
      broadcastMessage(
          JsonConverter.toJson(
              new GameStartNotification(
                  sentences, gameInfo, new ArrayList<>(gameState.getPlayerMap().values()))),
          null);
      LogUtil.info("Broadcast Message sentences:" + JsonUtil.serialize(sentences));
      LogUtil.info("Broadcast Message game info:" + JsonUtil.serialize(gameInfo));
    } catch (IOException e) {
      LogUtil.error("Failed to broadcast player progress updates: " + e.getMessage());
      throw new RuntimeException(e);
    }
  }

  /**
   * Updates the player's progress.
   *
   * @param player the player information.
   */
  public synchronized void updatePlayer(PlayerInfo player) {
    gameState.updatePlayer(player);
    if (!gameState.getStarted() && gameState.allPlayersReady()) {
      try {
        broadcastMessage(
            JsonConverter.toJson(
                new PlayerReadyResponse(
                    new ArrayList<>(gameState.getPlayerMap().values()), gameState.getLeadPlayer())),
            null);
        return;
      } catch (IOException e) {
        LogUtil.error("Failed to broadcast player progress updates: " + e.getMessage());
        throw new RuntimeException(e);
      }
    }

    if (gameState.anyPlayerFinished() || gameState.allPlayersKickOut()) {
      try {
        LogUtil.info("ONE PLAYER FINISHED");
        broadcastMessage(
            JsonConverter.toJson(
                new GameFinished(new ArrayList<>(gameState.getPlayerMap().values()))),
            null);
        closeCurrClients();
        gameState.reset();
      } catch (IOException e) {
        LogUtil.error("Failed to broadcast player progress updates: " + e.getMessage());
        throw new RuntimeException(e);
      }
    }
  }

  private void closeCurrClients() {
    for (ConnectionManager client : clients) {
      client.close();
    }
    connectionToPlayerMap.clear();
  }

  /**
   * Broadcasts a message to all clients, excluding the specified client.
   *
   * @param message the message to broadcast.
   * @param excludeClient the client to exclude from the broadcast.
   * @throws IOException if an I/O error occurs while sending the message.
   */
  public synchronized void broadcastMessage(String message, ConnectionManager excludeClient)
      throws IOException {
    List<ConnectionManager> disconnectedClients = new ArrayList<>();
    for (ConnectionManager client : clients) {
      if (client == excludeClient) {
        continue;
      }
      try {
        client.sendMessage(message);
      } catch (IOException e) {
        LogUtil.error("Failed to send message to client: " + e.getMessage());
        disconnectedClients.add(client);
      }
    }
    // Remove disconnected clients
    clients.removeAll(disconnectedClients);
    for (ConnectionManager client : disconnectedClients) {
      connectionToPlayerMap.remove(client);
    }
  }

  /**
   * Gets the game state.
   *
   * @return the game state.
   */
  public GameState getGameState() {
    return gameState;
  }
}
