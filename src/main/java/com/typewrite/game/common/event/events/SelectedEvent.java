package com.typewrite.game.common.event.events;

import com.typewrite.game.common.event.Event;
import java.util.List;

/** Event representing the selection of sentences. */
public class SelectedEvent extends Event {

  private List<String> sentences;

  /**
   * Constructor for SelectedEvent.
   *
   * @param sentences the list of selected sentences.
   */
  public SelectedEvent(List<String> sentences) {
    this.sentences = sentences;
  }

  /**
   * Gets the list of selected sentences.
   *
   * @return the list of selected sentences.
   */
  public List<String> getSentences() {
    return sentences;
  }

  /**
   * Sets the list of selected sentences.
   *
   * @param sentences the new list of selected sentences.
   */
  public void setSentences(List<String> sentences) {
    this.sentences = sentences;
  }
}
