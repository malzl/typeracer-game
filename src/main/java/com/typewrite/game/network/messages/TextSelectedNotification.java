package com.typewrite.game.network.messages;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/** Represents a notification message indicating that text has been selected. */
public class TextSelectedNotification implements Message {

  @SerializedName("messageType")
  private final String messageType = "TextSelectedNotification";

  @SerializedName("sentences")
  private final List<String> sentences;

  /**
   * Constructs a TextSelectedNotification message.
   *
   * @param sentences the list of selected sentences
   */
  public TextSelectedNotification(List<String> sentences) {
    this.sentences = sentences;
  }

  /**
   * Gets the list of selected sentences.
   *
   * @return the list of selected sentences
   */
  public List<String> getSentences() {
    return sentences;
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
