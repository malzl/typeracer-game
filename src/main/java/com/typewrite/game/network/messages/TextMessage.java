package com.typewrite.game.network.messages;

import com.google.gson.annotations.SerializedName;

/** Represents a message containing text. */
public class TextMessage implements Message {

  @SerializedName("messageType")
  private final String messageType = "TextMessage";

  @SerializedName("name")
  private final String name;

  @SerializedName("avatar")
  private String avatar;

  @SerializedName("messageText")
  private final String messageText;

  /**
   * Constructs a TextMessage.
   *
   * @param name the name of the sender
   * @param messageText the text of the message
   * @param avatar the avatar of the sender
   */
  public TextMessage(String name, String messageText, String avatar) {
    this.name = name;
    this.avatar = avatar;
    this.messageText = messageText;
  }

  /**
   * Gets the name of the sender.
   *
   * @return the name of the sender
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the text of the message.
   *
   * @return the text of the message
   */
  public String getMessageText() {
    return messageText;
  }

  /**
   * Gets the avatar of the sender.
   *
   * @return the avatar of the sender
   */
  public String getAvatar() {
    return avatar;
  }

  /**
   * Sets the avatar of the sender.
   *
   * @param avatar the avatar of the sender
   */
  public void setAvatar(String avatar) {
    this.avatar = avatar;
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
