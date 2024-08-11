package com.typewrite.game.util.textprocessors;

import java.util.Random;

/**
 * ImpossibleModeTextProcessor is a decorator that makes the text difficult to read by replacing
 * random characters in words with special characters.
 */
public class ImpossibleModeTextProcessor extends TextProcessorDecorator {
  private static final char[] SPECIAL_CHARACTERS = "*^#@$%&~§".toCharArray();
  private static final Random RANDOM = new Random();

  /**
   * Constructs an ImpossibleModeTextProcessor with the given underlying text processor.
   *
   * @param processor the underlying text processor
   */
  public ImpossibleModeTextProcessor(TextProcessor processor) {
    super(processor);
  }

  /**
   * Processes the given text by replacing random characters in words with special characters.
   *
   * @param text the input text to be processed
   * @return the processed text with random characters replaced by special characters
   */
  @Override
  public String process(String text) {
    text = super.process(text); // Apply underlying processor modifications first
    StringBuilder filledText = new StringBuilder();
    String[] words = text.split("\\s+"); // Split by spaces to isolate words

    for (String word : words) {
      if (word.isEmpty()) {
        continue;
      }

      StringBuilder modifiedWord = new StringBuilder(word);
      if (word.length() > 1) {
        int modifyIndex = RANDOM.nextInt(word.length());
        char newChar = SPECIAL_CHARACTERS[RANDOM.nextInt(SPECIAL_CHARACTERS.length)];

        modifiedWord.setCharAt(modifyIndex, newChar);
      }

      filledText.append(modifiedWord).append(" ");
    }

    return filledText.toString().trim();
  }
}
