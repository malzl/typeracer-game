package com.typewrite.game.network.messages;

import com.google.gson.annotations.SerializedName;
import com.typewrite.game.network.server.GameInfo;
import com.typewrite.game.network.server.PlayerInfo;
import java.util.List;

/** Represents a broadcast message of player progress during a game. */
public class PlayerProgressBroadcast implements Message {

  @SerializedName("messageType")
  private final String messageType = "PlayerProgressBroadcast";

  @SerializedName("gameInfo")
  private GameInfo gameInfo;

  @SerializedName("playerInfoList")
  private List<PlayerInfo> playerInfoList;

  /**
   * Constructs a PlayerProgressBroadcast message.
   *
   * @param gameInfo the game information
   * @param playerInfoList the list of player information
   */
  public PlayerProgressBroadcast(GameInfo gameInfo, List<PlayerInfo> playerInfoList) {
    this.gameInfo = gameInfo;
    this.playerInfoList = playerInfoList;
  }

  /**
   * Gets the game information.
   *
   * @return the game information
   */
  public GameInfo getGameInfo() {
    return gameInfo;
  }

  /**
   * Gets the list of player information.
   *
   * @return the list of player information
   */
  public List<PlayerInfo> getPlayerInfoList() {
    return playerInfoList;
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
