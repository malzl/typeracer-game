package com.typewrite.game.util.textprocessors;

/**
 * Basic implementation of the TextProcessor interface. This processor replaces newline and tab
 * characters with a space.
 */
public class BaseTextProcessor implements TextProcessor {

  /**
   * Processes the input text by replacing newline and tab characters with a space.
   *
   * @param text the input text to process
   * @return the processed text with newline and tab characters replaced by spaces
   */
  @Override
  public String process(String text) {
    return text.replaceAll("[\\n\\t]", " ");
  }
}
