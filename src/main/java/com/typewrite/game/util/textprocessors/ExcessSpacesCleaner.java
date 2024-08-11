package com.typewrite.game.util.textprocessors;

/**
 * A text processor that removes excess spaces from the input text. It ensures that multiple spaces
 * are replaced by a single space.
 */
public class ExcessSpacesCleaner extends TextProcessorDecorator {

  /**
   * Constructs an ExcessSpacesCleaner with the given underlying text processor.
   *
   * @param processor the underlying text processor
   */
  public ExcessSpacesCleaner(TextProcessor processor) {
    super(processor);
  }

  /**
   * Processes the input text by removing excess spaces and trimming the text.
   *
   * @param text the input text to process
   * @return the processed text with excess spaces removed
   */
  @Override
  public String process(String text) {
    text = super.process(text);
    return text.replaceAll("\\s{2,}", " ").trim();
  }
}
