package com.typewrite.game.network.messages;

import com.google.gson.annotations.SerializedName;

/** Represents a notification message indicating a player's readiness. */
public class PlayerReadyNotification implements Message {

  @SerializedName("messageType")
  private final String messageType = "PlayerReadyNotification";

  @SerializedName("ready")
  private final boolean ready;

  /**
   * Constructs a PlayerReadyNotification message.
   *
   * @param ready whether the player is ready
   */
  public PlayerReadyNotification(boolean ready) {
    this.ready = ready;
  }

  /**
   * Indicates whether the player is ready.
   *
   * @return true if the player is ready, false otherwise
   */
  public boolean isReady() {
    return ready;
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
