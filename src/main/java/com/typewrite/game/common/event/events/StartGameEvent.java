package com.typewrite.game.common.event.events;

import com.typewrite.game.common.event.Event;
import com.typewrite.game.network.server.GameInfo;
import com.typewrite.game.network.server.PlayerInfo;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/** Event representing the start of a game. */
public class StartGameEvent extends Event {

  private GameInfo gameInfo;
  private List<String> sentences;
  private List<PlayerInfo> playerInfos;

  /** Default constructor for StartGameEvent. */
  public StartGameEvent() {}

  /**
   * Constructor for StartGameEvent.
   *
   * @param sentences the list of sentences for the game.
   * @param gameInfo the game information.
   * @param playerInfos the list of player information.
   */
  public StartGameEvent(List<String> sentences, GameInfo gameInfo, List<PlayerInfo> playerInfos) {
    this.sentences = sentences;
    this.gameInfo = gameInfo;
    this.playerInfos = playerInfos;
  }

  /**
   * Gets the list of sentences for the game.
   *
   * @return the list of sentences.
   */
  public List<String> getSentences() {
    return Optional.of(sentences).orElse(Collections.emptyList());
  }

  /**
   * Sets the list of sentences for the game.
   *
   * @param sentences the new list of sentences.
   */
  public void setSentences(List<String> sentences) {
    this.sentences = sentences;
  }

  /**
   * Gets the game information.
   *
   * @return the game information.
   */
  public GameInfo getGameInfo() {
    return gameInfo;
  }
}
