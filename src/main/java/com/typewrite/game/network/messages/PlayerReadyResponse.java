package com.typewrite.game.network.messages;

import com.google.gson.annotations.SerializedName;
import com.typewrite.game.network.server.PlayerInfo;
import java.util.List;

/** Represents a response message indicating the readiness of players. */
public class PlayerReadyResponse implements Message {

  @SerializedName("messageType")
  private final String messageType = "PlayerReadyResponse";

  @SerializedName("playerInfos")
  private final List<PlayerInfo> playerInfos;

  @SerializedName("leadPlayer")
  private final String leadPlayer;

  /**
   * Constructs a PlayerReadyResponse message.
   *
   * @param playerInfos the list of player information
   * @param leadPlayer the lead player
   */
  public PlayerReadyResponse(List<PlayerInfo> playerInfos, String leadPlayer) {
    this.playerInfos = playerInfos;
    this.leadPlayer = leadPlayer;
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
   * Gets the lead player.
   *
   * @return the lead player
   */
  public String getLeadPlayer() {
    return leadPlayer;
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
