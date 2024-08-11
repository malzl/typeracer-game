package com.typewrite.game.network.messages;

import com.google.gson.annotations.SerializedName;
import com.typewrite.game.common.enums.EmojiType;

/** Represents a message containing an emoji. */
public class EmojiMessage implements Message {

  @SerializedName("messageType")
  private final String messageType = "EmojiMessage";

  @SerializedName("sender")
  private final String sender;

  @SerializedName("type")
  private final EmojiType type;

  /**
   * Constructs an EmojiMessage.
   *
   * @param sender the sender of the message
   * @param type the type of the emoji
   */
  public EmojiMessage(String sender, EmojiType type) {
    this.sender = sender;
    this.type = type;
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
   * Gets the type of the emoji.
   *
   * @return the type of the emoji
   */
  public EmojiType getType() {
    return type;
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
