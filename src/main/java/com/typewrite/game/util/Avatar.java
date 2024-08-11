package com.typewrite.game.util;

import javafx.scene.image.Image;

/** Handles avatars throughout multiplayer game. */
public class Avatar {
  private String name;
  private Image image;

  /**
   * Constructs an avatar object.
   *
   * @param name The name of the avatar
   * @param image The String with the corresponding image name
   */
  public Avatar(String name, Image image) {
    this.name = name;
    this.image = image;
  }

  /** Gets name for avatar. */
  public String getName() {
    return name;
  }

  /** Gets image for avatar. */
  public Image getImage() {
    return image;
  }
}
