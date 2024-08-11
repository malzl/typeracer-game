package com.typewrite.game.util.textprocessors;

/** TextProcessor is an interface for processing text. */
public interface TextProcessor {
  /**
   * Processes the given text and returns the processed text.
   *
   * @param text the input text to be processed
   * @return the processed text
   */
  String process(String text);
}
