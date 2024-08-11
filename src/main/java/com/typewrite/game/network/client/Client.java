package com.typewrite.game.network.client;

import com.typewrite.game.GameManager;
import com.typewrite.game.common.event.DefaultEventBus;
import com.typewrite.game.common.event.events.EmojiEvent;
import com.typewrite.game.common.event.events.GameFinishedEvent;
import com.typewrite.game.common.event.events.SelectedEvent;
import com.typewrite.game.common.event.events.SendEmojiEvent;
import com.typewrite.game.common.event.events.StartGameEvent;
import com.typewrite.game.common.event.events.SyncEvent;
import com.typewrite.game.common.event.events.UpdateEvent;
import com.typewrite.game.controller.multiple.ClientView;
import com.typewrite.game.controller.multiple.lobby.LobbyView;
import com.typewrite.game.network.messages.EmojiMessage;
import com.typewrite.game.network.messages.GameFinished;
import com.typewrite.game.network.messages.GameStartNotification;
import com.typewrite.game.network.messages.JsonConverter;
import com.typewrite.game.network.messages.Message;
import com.typewrite.game.network.messages.PlayerJoinRequest;
import com.typewrite.game.network.messages.PlayerJoinResponse;
import com.typewrite.game.network.messages.PlayerProgressBroadcast;
import com.typewrite.game.network.messages.PlayerProgressUpdate;
import com.typewrite.game.network.messages.PlayerReadyResponse;
import com.typewrite.game.network.messages.TextMessage;
import com.typewrite.game.network.messages.TextSelectedNotification;
import com.typewrite.game.network.server.PlayerInfo;
import com.typewrite.game.util.JsonUtil;
import com.typewrite.game.util.LogUtil;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * Client class for handling client-side network operations and communication with the server.
 * Manages event subscriptions, message handling, and socket communication.
 */
public class Client extends Thread {

  private final Socket socket;
  private final BufferedReader bufferedReader;
  private final BufferedWriter bufferedWriter;

  static Client instance; // Singleton instance
  private final PlayerInfo currPlayer;

  /**
   * Gets the singleton instance of the Client.
   *
   * @return the singleton instance of the Client.
   */
  public static synchronized Client getInstance() {
    return instance;
  }

  /**
   * Constructor for Client.
   *
   * @param playerName the name of the player.
   * @param socket the socket for communication.
   * @throws IOException if an I/O error occurs.
   */
  public Client(String playerName, Socket socket) throws IOException {
    String selectedAvatar = ClientView.getInstance().getCurrentImage();
    this.currPlayer = new PlayerInfo(playerName, selectedAvatar);
    this.socket = socket;
    instance = this;
    LogUtil.info("Client: Stage set in constructor");

    this.bufferedReader =
        new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
    this.bufferedWriter =
        new BufferedWriter(
            new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
    DefaultEventBus.getInstance().subscribe(UpdateEvent.class, this::onUpdateEventHandler);
    DefaultEventBus.getInstance().subscribe(SelectedEvent.class, this::onSelectedEventHandler);
    DefaultEventBus.getInstance().subscribe(SendEmojiEvent.class, this::onSendEmojiEventHandler);
  }

  // Constructor for testing
  protected Client(String playerName, Socket socket, BufferedReader reader, BufferedWriter writer) {
    this.currPlayer = new PlayerInfo(playerName, "1");
    this.socket = socket;
    this.bufferedReader = reader;
    this.bufferedWriter = writer;
    instance = this;
    LogUtil.info("Client: Stage set in constructor");

    DefaultEventBus.getInstance().subscribe(UpdateEvent.class, this::onUpdateEventHandler);
    DefaultEventBus.getInstance().subscribe(SelectedEvent.class, this::onSelectedEventHandler);
    DefaultEventBus.getInstance().subscribe(SendEmojiEvent.class, this::onSendEmojiEventHandler);
  }

  /**
   * Gets the current player information.
   *
   * @return the current player information.
   */
  public PlayerInfo getCurrPlayer() {
    return currPlayer;
  }

  /**
   * Event handler for UpdateEvent.
   *
   * @param event the UpdateEvent.
   */
  void onUpdateEventHandler(UpdateEvent event) {
    PlayerInfo playerInfo = event.getPlayerInfo();
    if (socket.isClosed()) {
      DefaultEventBus.getInstance().unsubscribe(UpdateEvent.class, this::onUpdateEventHandler);
      LogUtil.debug("Socket is closed. Progress update task stopped.");
      return;
    }
    PlayerProgressUpdate progressUpdate = new PlayerProgressUpdate(playerInfo);
    try {
      sendMessage(progressUpdate);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Event handler for SelectedEvent.
   *
   * @param event the SelectedEvent.
   */
  void onSelectedEventHandler(SelectedEvent event) {
    List<String> sentences = event.getSentences();
    LogUtil.info("Game text is selected. text:" + JsonUtil.serialize(sentences));
    TextSelectedNotification notification = new TextSelectedNotification(sentences);
    try {
      sendMessage(notification);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Event handler for SendEmojiEvent.
   *
   * @param event the SendEmojiEvent.
   */
  void onSendEmojiEventHandler(SendEmojiEvent event) {
    if (socket.isClosed()) {
      DefaultEventBus.getInstance()
          .unsubscribe(SendEmojiEvent.class, this::onSendEmojiEventHandler);
      LogUtil.debug("Socket is closed. Progress update task stopped.");
      return;
    }
    EmojiMessage emojiMessage = new EmojiMessage(event.getSender(), event.getType());
    try {
      sendMessage(emojiMessage);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /** Runs the client thread, handling incoming messages and sending the initial join request. */
  @Override
  public void run() {
    try {
      PlayerJoinRequest joinRequest =
          new PlayerJoinRequest(currPlayer.getName(), currPlayer.getSelectedAvatar());
      sendMessage(joinRequest);

      String message;
      while ((message = bufferedReader.readLine()) != null) {
        if (Thread.currentThread().isInterrupted()) {
          throw new InterruptedException("Thread was interrupted, fail to read the line");
        }
        Message parsedMessage = JsonConverter.fromJson(message);
        handleIncomingMessage(parsedMessage);
      }
    } catch (SocketException e) {
      LogUtil.error("Socket closed unexpectedly.");
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      LogUtil.error("Thread was interrupted.");
    } finally {
      try {
        socket.close();
      } catch (IOException e) {
        LogUtil.error("Close socket error.");
      }
    }
  }

  /**
   * Sends a message to the server.
   *
   * @param message the message to send.
   * @throws IOException if an I/O error occurs.
   */
  public void sendMessage(Message message) throws IOException {
    if (!socket.isClosed()) {
      String jsonMessage = JsonConverter.toJson(message);
      LogUtil.debug("Client: Sending message to server: " + jsonMessage);
      try {
        bufferedWriter.write(jsonMessage);
        bufferedWriter.newLine();
        bufferedWriter.flush();
      } catch (IOException e) {
        LogUtil.error("Error sending message: " + e.getMessage());
        throw new RuntimeException(e);
      }
    } else {
      LogUtil.error("Socket is closed. Cannot send message.");
    }
  }

  /**
   * Handles incoming messages from the server.
   *
   * @param message the incoming message.
   */
  void handleIncomingMessage(Message message) {
    if (message == null) {
      LogUtil.error("Client: Received malformed message.");
      return;
    }

    LogUtil.debug("Client: Received message: " + message.getMessageType());

    switch (message.getMessageType()) {
      case "PlayerJoinResponse":
        PlayerJoinResponse joinResponse = (PlayerJoinResponse) message;
        handlePlayerJoinResponse(joinResponse);
        break;
      case "PlayerReadyResponse":
        PlayerReadyResponse readyResponse = (PlayerReadyResponse) message;
        handlePlayerReadyResponse(readyResponse);
        break;
      case "GameStartNotification":
        GameStartNotification startNotification = (GameStartNotification) message;
        handleGameStartNotification(startNotification);
        break;
      case "PlayerProgressBroadcast":
        PlayerProgressBroadcast progressBroadcast = (PlayerProgressBroadcast) message;
        handlePlayerProgressBroadcast(progressBroadcast);
        break;
      case "TextMessage":
        TextMessage textMessage = (TextMessage) message;
        handleTextMessage(textMessage);
        break;
      case "GameFinished":
        GameFinished gameFinished = (GameFinished) message;
        handleGameFinished(gameFinished);
        break;
      case "EmojiMessage":
        EmojiMessage emojiMessage = (EmojiMessage) message;
        handleEmojiMessage(emojiMessage);
        break;
      default:
        LogUtil.error("Unknown message type: " + message.getMessageType());
        break;
    }
  }

  /**
   * Handles the PlayerJoinResponse message.
   *
   * @param response the PlayerJoinResponse message.
   */
  private void handlePlayerJoinResponse(PlayerJoinResponse response) {
    if (response.isSuccess()) {
      Platform.runLater(
          () -> {
            try {
              LogUtil.info("Client: Loading LobbyView");
              currPlayer.setSelectedAvatar(response.getSelectedAvatar());
              LobbyView.getInstance().setSelectedAvatar(response.getSelectedAvatar());
              GameManager.getInstance().loadView(GameManager.LOBBY_VIEW_NAME);
              LogUtil.info("Client: Loaded LobbyView");
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          });
    } else {
      Platform.runLater(
          () -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Client: Join failed. Username already taken.");
            alert.showAndWait();
          });
      this.interrupt();
    }
  }

  /**
   * Handles the PlayerReadyResponse message.
   *
   * @param response the PlayerReadyResponse message.
   */
  private void handlePlayerReadyResponse(PlayerReadyResponse response) {
    // Handle the PlayerReadyResponse
    Platform.runLater(
        () -> {
          // Implement any additional UI or logic updates here
          try {
            GameManager.getInstance()
                .loadView(
                    GameManager.MULTIPLE_SELECTOR_CUSTOM_VIEW,
                    response.getPlayerInfos(),
                    currPlayer.getName(),
                    response.getLeadPlayer());
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
  }

  /**
   * Handles the GameStartNotification message.
   *
   * @param notification the GameStartNotification message.
   */
  private void handleGameStartNotification(GameStartNotification notification) {
    LogUtil.info(
        "Client: GameStartNotification received. Starting game with text: "
            + notification.getGameText());
    DefaultEventBus.getInstance()
        .publish(
            new StartGameEvent(
                notification.getGameText(),
                notification.getGameInfo(),
                notification.getPlayerInfos()));
  }

  private void handlePlayerProgressBroadcast(PlayerProgressBroadcast progressBroadcast) {
    LogUtil.debug("Client: PlayerProgressBroadcast received.");
    DefaultEventBus.getInstance()
        .publish(
            new SyncEvent(progressBroadcast.getGameInfo(), progressBroadcast.getPlayerInfoList()));
    LogUtil.debug("sync progress: " + JsonUtil.serialize(progressBroadcast));
  }

  private void handleTextMessage(TextMessage textMessage) {
    String name = textMessage.getName();
    String messageText = textMessage.getMessageText();
    String avatar = textMessage.getAvatar();

    LogUtil.debug("Received a TextMessage from " + name + ": " + messageText);

    Platform.runLater(
        () -> {
          LobbyView.displayReceivedMessage(name, messageText, avatar);
          LogUtil.debug("Displayed the received message in the LobbyView");
        });
  }

  private void handleGameFinished(GameFinished gameFinished) {
    LogUtil.info("Client: GameFinished.");
    DefaultEventBus.getInstance().publish(new GameFinishedEvent(gameFinished.getPlayerInfos()));
    closeConnection();
  }

  private void handleEmojiMessage(EmojiMessage emojiMessage) {
    LogUtil.info(
        "Client: emoji message, sender: "
            + emojiMessage.getSender()
            + ". type: "
            + emojiMessage.getType());
    DefaultEventBus.getInstance()
        .publish(new EmojiEvent(emojiMessage.getSender(), emojiMessage.getType()));
  }

  /** Closes the socket connection. */
  public void closeConnection() {
    try {
      if (!socket.isClosed()) {
        socket.close(); // closes socket connection
        LogUtil.info("Connection closed successfully.");
      }
    } catch (IOException e) {
      LogUtil.error("Error closing socket: " + e.getMessage());
    }
  }
}
