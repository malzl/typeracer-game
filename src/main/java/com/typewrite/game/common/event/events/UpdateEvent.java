package com.typewrite.game.common.event.events;

import com.typewrite.game.common.event.Event;
import com.typewrite.game.network.server.PlayerInfo;

/** Event representing the update of player information. */
public class UpdateEvent extends Event {

  private final PlayerInfo playerInfo;

  /**
   * Constructor for UpdateEvent.
   *
   * @param playerInfo the player information to update.
   */
  public UpdateEvent(PlayerInfo playerInfo) {
    this.playerInfo = playerInfo;
  }

  /**
   * Gets the player information.
   *
   * @return the player information.
   */
  public PlayerInfo getPlayerInfo() {
    return playerInfo;
  }
}
