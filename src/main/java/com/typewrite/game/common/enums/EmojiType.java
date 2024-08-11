package com.typewrite.game.common.enums;

/** Enum representing different types of emojis used in the game. */
public enum EmojiType {

  /** Emoji representing a bomb. */
  BOMB("assets/bomb.gif"),
  ;

  private final String path;

  /**
   * Constructor for EmojiType.
   *
   * @param path the file path of the emoji asset.
   */
  EmojiType(String path) {
    this.path = path;
  }

  /**
   * Gets the file path of the emoji asset.
   *
   * @return the file path of the emoji asset.
   */
  public String getPath() {
    return path;
  }
}
