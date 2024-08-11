package com.typewrite.game.network.messages;

import com.google.gson.annotations.SerializedName;

/** Represents a message indicating that an avatar has been selected. */
public class AvatarSelected implements Message {

  @SerializedName("messageType")
  private final String messageType = "AvatarSelected";

  @SerializedName("sender")
  private final String sender;

  @SerializedName("name")
  private final String name;

  /**
   * Constructs an AvatarSelected message.
   *
   * @param sender the sender of the message
   * @param name the name of the selected avatar
   */
  public AvatarSelected(String sender, String name) {
    this.sender = sender;
    this.name = name;
  }

  /**
   * Gets the sender of the message.
   *
   * @return the sender of the message
   */
  public String getSender() {
    return sender;
  }

  /**
   * Gets the name of the selected avatar.
   *
   * @return the name of the selected avatar
   */
  public String getType() {
    return name;
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
