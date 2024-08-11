package com.typewrite.game.network.server;

import com.typewrite.game.util.LogUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/** Manages the state of the game, including players and game information. */
public class GameState {
  public static final Integer MAX_PLAYER = 4;

  private String leadPlayer;
  private GameInfo gameInfo;
  private Boolean started = Boolean.FALSE;

  private List<String> players = new CopyOnWriteArrayList<>();
  private Map<String, PlayerInfo> playerMap = new ConcurrentHashMap<>();

  /**
   * Adds a player to the game. Up to four players are allowed.
   *
   * @param playerName the name of the player to add.
   */
  public synchronized void addPlayer(String playerName, String selectedAvatar) {
    if (players.size() >= MAX_PLAYER) {
      LogUtil.warning("Only allowed 4 Player to Play the online Game!");
      return;
    }
    players.add(playerName);
    playerMap.put(playerName, new PlayerInfo(playerName, selectedAvatar));
    if (leadPlayer == null) {
      leadPlayer = playerName;
    }
  }

  /**
   * Checks if all players are ready.
   *
   * @return true if all players are ready, false otherwise.
   */
  public synchronized boolean allPlayersReady() {
    return playerMap.values().stream().allMatch(PlayerInfo::getReady);
  }

  /**
   * Checks if all players are kick out.
   *
   * @return true if all players are kick out, false otherwise.
   */
  public synchronized boolean allPlayersKickOut() {
    return playerMap.values().stream().allMatch(PlayerInfo::getKickOut);
  }

  /**
   * Checks if any player has finished the game.
   *
   * @return true if any player has finished, false otherwise.
   */
  public synchronized boolean anyPlayerFinished() {
    return playerMap.values().stream().anyMatch(PlayerInfo::getFinished);
  }

  /**
   * Removes a player from the game.
   *
   * @param playerName the name of the player to remove.
   */
  public synchronized void removePlayer(String playerName) {
    players.remove(playerName);
    playerMap.remove(playerName);
  }

  /** Resets the game information. */
  public synchronized void reset() {
    players = new CopyOnWriteArrayList<>();
    playerMap = new ConcurrentHashMap<>();
    leadPlayer = null;
    started = Boolean.FALSE;
    createNewGameInfo(gameInfo.getPort());
  }

  /**
   * Updates the player information.
   *
   * @param player the player information to update.
   */
  public synchronized void updatePlayer(PlayerInfo player) {
    PlayerInfo playerInfo = playerMap.get(player.getName());
    if (!started) {
      playerInfo.setReady(player.getReady());
      return;
    }

    playerInfo.setKickOut(player.getKickOut());
    playerInfo.setLastActivityTime(player.getLastActivityTime());
    playerInfo.setFinished(player.getFinished());
    playerInfo.setCurrTextSize(player.getCurrTextSize());
    playerInfo.setOnline(Boolean.TRUE);

    long currentTime = System.currentTimeMillis();
    double elapsedTimeInSeconds = (currentTime - gameInfo.getStartTime()) / 1000.0;

    if (playerInfo.getFinished() && playerInfo.getCompletedTime() == null) {
      playerInfo.setCompletedTime(currentTime - gameInfo.getStartTime());
      elapsedTimeInSeconds = playerInfo.getCompletedTime() / 1000.0;
    }

    int wordCount = playerInfo.getCurrTextSize() / 5;
    long currWpm = (long) ((wordCount * 60.0) / elapsedTimeInSeconds);

    if (playerInfo.getFinished() && playerInfo.getCompletedTime() != null) {
      playerInfo.setWpm(currWpm + " WPM");
    } else if (!playerInfo.getFinished()) {
      playerInfo.setWpm(currWpm + " WPM");
    }
  }

  /**
   * Creates a new game information instance.
   *
   * @param port the port number for the game.
   */
  public void createNewGameInfo(Integer port) {
    this.setGameInfo(new GameInfo("Username", "Localhost", port, null, null));
  }

  public synchronized void setGameInfo(GameInfo gameInfo) {
    this.gameInfo = gameInfo;
  }

  public synchronized List<String> getPlayers() {
    return new ArrayList<>(players);
  }

  public synchronized String getLeadPlayer() {
    return leadPlayer;
  }

  public synchronized void setStarted(Boolean started) {
    this.started = started;
  }

  public synchronized Boolean getStarted() {
    return started;
  }

  public Map<String, PlayerInfo> getPlayerMap() {
    return playerMap;
  }

  public synchronized GameInfo getGameInfo() {
    return gameInfo;
  }
}
