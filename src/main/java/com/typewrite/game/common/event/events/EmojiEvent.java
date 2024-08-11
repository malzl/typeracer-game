package com.typewrite.game.common.event.events;

import com.typewrite.game.common.enums.EmojiType;
import com.typewrite.game.common.event.Event;

/** Event representing an emoji action in the game. */
public class EmojiEvent extends Event {

  private final String sender;
  private final EmojiType type;

  /**
   * Constructor for EmojiEvent.
   *
   * @param sender the sender of the emoji.
   * @param type the type of emoji.
   */
  public EmojiEvent(String sender, EmojiType type) {
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
