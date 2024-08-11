package com.typewrite.game.network.messages;

import com.google.gson.annotations.SerializedName;
import com.typewrite.game.network.server.GameInfo;
import com.typewrite.game.network.server.PlayerInfo;
import java.util.List;

/** Represents a notification message indicating that a game is starting. */
public class GameStartNotification implements Message {

  @SerializedName("messageType")
  private final String messageType = "GameStartNotification";

  @SerializedName("gameText")
  private final List<String> gameText;

  @SerializedName("gameInfo")
  private final GameInfo gameInfo;

  @SerializedName("playerInfos")
  private final List<PlayerInfo> playerInfos;

  /**
   * Constructs a GameStartNotification message.
   *
   * @param gameText the text of the game
   * @param gameInfo the information of the game
   * @param playerInfos the list of player information
   */
  public GameStartNotification(
      List<String> gameText, GameInfo gameInfo, List<PlayerInfo> playerInfos) {
    this.gameText = gameText;
    this.gameInfo = gameInfo;
    this.playerInfos = playerInfos;
  }

  /**
   * Gets the text of the game.
   *
   * @return the text of the game
   */
  public List<String> getGameText() {
    return gameText;
  }

  /**
   * Gets the information of the game.
   *
   * @return the information of the game
   */
  public GameInfo getGameInfo() {
    return gameInfo;
  }

  /**
   * Gets the list of player information.
   *
   * @return the list of player information
   */
  public List<PlayerInfo> getPlayerInfos() {
    return playerInfos;
  }

  /**
   * Gets the message type.
   *
   * @return the message type
   */
  @Override
  public String getMessageType() {
    return messageType;
  }
}
