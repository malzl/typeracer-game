package com.typewrite.game.network.messages;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/** Represents a message indicating that the server has closed. */
public class ServerClosedMessage implements Message, Serializable {

  @SerializedName("messageType")
  private final String messageType = "ServerClosedMessage";

  @SerializedName("reason")
  private final String reason;

  /**
   * Constructs a ServerClosedMessage.
   *
   * @param reason the reason for the server closure
   */
  public ServerClosedMessage(String reason) {
    this.reason = reason;
  }

  /**
   * Gets the reason for the server closure.
   *
   * @return the reason for the server closure
   */
  public String getReason() {
    return reason;
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
