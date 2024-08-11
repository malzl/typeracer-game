package com.typewrite.game.common.event.events;

import com.typewrite.game.common.enums.EmojiType;
import com.typewrite.game.common.event.Event;

/** Event representing the sending of an emoji. */
public class SendEmojiEvent extends Event {

  private final String sender;
  private final EmojiType type;

  /**
   * Constructor for SendEmojiEvent.
   *
   * @param sender the sender of the emoji.
   * @param type the type of emoji.
   */
  public SendEmojiEvent(String sender, EmojiType type) {
    this.sender = sender;
    this.type = type;
  }

  /**
   * Gets the sender of the emoji.
   *
   * @return the sender of the emoji.
   */
  public String getSender() {
    return sender;
  }

  /**
   * Gets the type of emoji.
   *
   * @return the type of emoji.
   */
  public EmojiType getType() {
    return type;
  }
}
