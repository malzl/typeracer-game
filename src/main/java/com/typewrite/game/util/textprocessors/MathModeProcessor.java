package com.typewrite.game.util.textprocessors;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * MathModeProcessor is a decorator that replaces specific letters with numbers or symbols according
 * to a predefined map. This processor modifies at most one character per word.
 */
public class MathModeProcessor extends TextProcessorDecorator {
  private static final Map<Character, Character> replacements = new HashMap<>();
  private static final Random random = new Random();

  static {
    replacements.put('a', '4');
    replacements.put('A', '4');
    replacements.put('e', '3');
    replacements.put('E', '3');
    replacements.put('l', '1');
    replacements.put('L', '1');
    replacements.put('o', '0');
    replacements.put('O', '0');
    replacements.put('s', '5');
    replacements.put('S', '5');
  }

  /**
   * Constructs a MathModeProcessor with the given underlying text processor.
   *
   * @param processor the underlying text processor
   */
  public MathModeProcessor(TextProcessor processor) {
    super(processor);
  }

  /**
   * Processes the given text by replacing specific letters with numbers or symbols.
   *
   * @param text the input text to be processed
   * @return the processed text with specific letters replaced by numbers or symbols
   */
  @Override
  public String process(String text) {
    text = super.process(text);
    StringBuilder transformedText = new StringBuilder();

    String[] words = text.split("\\s+");
    for (String word : words) {
      StringBuilder modifiedWord = new StringBuilder(word);
      boolean replaced = false;

      for (int i = 0; i < modifiedWord.length(); i++) {
        char currentChar = modifiedWord.charAt(i);
        if (replacements.containsKey(currentChar) && !replaced) {
          modifiedWord.setCharAt(i, replacements.get(currentChar));
          replaced = true; // Ensure only one replacement per word
        }
      }

      transformedText.append(modifiedWord).append(" ");
    }

    return transformedText
        .toString()
        .trim(); // Trim the trailing space to clean up the final string
  }
}
