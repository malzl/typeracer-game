package com.typewrite.game.network.messages;

import com.google.gson.annotations.SerializedName;
import com.typewrite.game.network.server.PlayerInfo;
import java.util.List;

/** Represents a message indicating that a game has finished. */
public class GameFinished implements Message {

  @SerializedName("messageType")
  private final String messageType = "GameFinished";

  @SerializedName("playerInfos")
  private final List<PlayerInfo> playerInfos;

  /**
   * Constructs a GameFinished message.
   *
   * @param playerInfos the list of player information
   */
  public GameFinished(List<PlayerInfo> playerInfos) {
    this.playerInfos = playerInfos;
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
