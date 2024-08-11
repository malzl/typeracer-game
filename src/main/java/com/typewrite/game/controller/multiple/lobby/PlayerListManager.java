package com.typewrite.game.controller.multiple.lobby;

import com.typewrite.game.network.server.PlayerInfo;
import java.util.ArrayList;
import java.util.List;

/** Manages the list of players in the lobby, tracking changes and notifying the chat. */
public class PlayerListManager {
  private List<PlayerInfo> playerInfoList;
  private List<PlayerInfo> previousPlayerInfoList;

  /** Constructs a PlayerListManager with empty player lists. */
  public PlayerListManager() {
    this.playerInfoList = new ArrayList<>();
    this.previousPlayerInfoList = new ArrayList<>();
  }

  /**
   * Gets the current list of player information.
   *
   * @return the current list of player information
   */
  public List<PlayerInfo> getPlayerInfoList() {
    return playerInfoList;
  }

  /**
   * Sets the list of player information and checks for any changes.
   *
   * @param playerInfoList the new list of player information
   */
  public void setPlayerInfoList(List<PlayerInfo> playerInfoList) {
    this.previousPlayerInfoList = new ArrayList<>(this.playerInfoList);
    this.playerInfoList = playerInfoList;
    checkForChanges();
  }

  private void checkForChanges() {
    if (playerInfoList.size() > previousPlayerInfoList.size()) {
      PlayerInfo newPlayer = playerInfoList.get(playerInfoList.size() - 1);
      notifyChat(newPlayer.getName() + " has joined the game.");
    } else if (playerInfoList.size() < previousPlayerInfoList.size()) {
      PlayerInfo leftPlayer = previousPlayerInfoList.get(previousPlayerInfoList.size() - 1);
      notifyChat(leftPlayer.getName() + " has left the game.");
    }

    for (int i = 0; i < playerInfoList.size(); i++) {
      if (!playerInfoList.get(i).getReady().equals(previousPlayerInfoList.get(i).getReady())) {
        notifyChat(
            playerInfoList.get(i).getName()
                + (playerInfoList.get(i).getReady() ? " is ready." : " is not ready."));
      }
    }
  }

  private void notifyChat(String message) {
    LobbyView.getInstance().displayServerMessage(message);
  }
}
