package com.typewrite.game.common.event.events;

import com.typewrite.game.common.event.Event;

/** Event triggered when an avatar is selected. */
public class AvatarSelectedEvent extends Event {

  private final String name;
  private final String photoTitle;

  /**
   * Constructs an AvatarSelectedEvent with the specified name and photo title.
   *
   * @param name the name of the avatar selected
   * @param photoTitle the title of the photo associated with the avatar
   */
  public AvatarSelectedEvent(String name, String photoTitle) {
    this.name = name;
    this.photoTitle = photoTitle;
  }

  /**
   * Retrieves the name of the avatar selected.
   *
   * @return the name of the avatar
   */
  public String getName() {
    return name;
  }
}
