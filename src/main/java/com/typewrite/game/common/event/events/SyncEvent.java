package com.typewrite.game.common.event.events;

import com.typewrite.game.common.event.Event;
import com.typewrite.game.network.server.GameInfo;
import com.typewrite.game.network.server.PlayerInfo;
import java.util.List;

/** Event representing the synchronization of game information and player information. */
public class SyncEvent extends Event {

  private final GameInfo gameInfo;
  private final List<PlayerInfo> playerInfoList;

  /**
   * Constructor for SyncEvent.
   *
   * @param gameInfo the game information to synchronize.
   * @param playerInfoList the list of player information to synchronize.
   */
  public SyncEvent(GameInfo gameInfo, List<PlayerInfo> playerInfoList) {
    this.gameInfo = gameInfo;
    this.playerInfoList = playerInfoList;
  }

  /**
   * Gets the game information.
   *
   * @return the game information.
   */
  public GameInfo getGameInfo() {
    return gameInfo;
  }

  /**
   * Gets the list of player information.
   *
   * @return the list of player information.
   */
  public List<PlayerInfo> getPlayerInfoList() {
    return playerInfoList;
  }
}
