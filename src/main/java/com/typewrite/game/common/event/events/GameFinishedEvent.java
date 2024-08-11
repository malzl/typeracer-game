package com.typewrite.game.common.event.events;

import com.typewrite.game.common.event.Event;
import com.typewrite.game.network.server.PlayerInfo;
import java.util.List;

/** Event representing the end of a game with player information. */
public class GameFinishedEvent extends Event {

  private final List<PlayerInfo> playerInfos;

  /**
   * Constructor for GameFinishedEvent.
   *
   * @param playerInfos the list of player information at the end of the game.
   */
  public GameFinishedEvent(List<PlayerInfo> playerInfos) {
    this.playerInfos = playerInfos;
  }

  /**
   * Gets the list of player information.
   *
   * @return the list of player information.
   */
  public List<PlayerInfo> getPlayerInfos() {
    return playerInfos;
  }
}
