package com.typewrite.game.util.textprocessors;

/**
 * SpecialCharactersCleaner is a decorator that removes specific unwanted special characters from
 * the text.
 */
public class SpecialCharactersCleaner extends TextProcessorDecorator {

  private static final String UNWANTED_CHARACTERS = "[*#{}+%]";

  /**
   * Constructs a SpecialCharactersCleaner with the given underlying text processor.
   *
   * @param processor the underlying text processor
   */
  public SpecialCharactersCleaner(TextProcessor processor) {
    super(processor);
  }

  /**
   * Processes the given text by removing unwanted special characters.
   *
   * @param text the input text to be processed
   * @return the processed text with unwanted special characters removed
   */
  @Override
  public String process(String text) {
    text = super.process(text);
    return text.replaceAll(UNWANTED_CHARACTERS, "").trim();
  }
}
