package com.typewrite.game.network.messages;

import com.google.gson.annotations.SerializedName;
import com.typewrite.game.network.server.PlayerInfo;

/** Represents an update message of a player's progress. */
public class PlayerProgressUpdate implements Message {

  @SerializedName("messageType")
  private final String messageType = "PlayerProgressUpdate";

  @SerializedName("player")
  private final PlayerInfo player;

  /**
   * Constructs a PlayerProgressUpdate message.
   *
   * @param player the player information
   */
  public PlayerProgressUpdate(PlayerInfo player) {
    this.player = player;
  }

  /**
   * Gets the player information.
   *
   * @return the player information
   */
  public PlayerInfo getPlayer() {
    return player;
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
