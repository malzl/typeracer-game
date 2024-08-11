package com.typewrite.game.util.textprocessors;

/**
 * ReverseTextProcessor is a decorator that reverses each word in the text while preserving
 * punctuation and sentence structure.
 */
public class ReverseTextProcessor extends TextProcessorDecorator {

  /**
   * Constructs a ReverseTextProcessor with the given underlying text processor.
   *
   * @param processor the underlying text processor
   */
  public ReverseTextProcessor(TextProcessor processor) {
    super(processor);
  }

  /**
   * Processes the given text by reversing each word in the text.
   *
   * @param text the input text to be processed
   * @return the processed text with each word reversed
   */
  @Override
  public String process(String text) {
    text = super.process(text); // Apply underlying processor modifications first

    String[] words = text.split("\\b"); // Split by word boundaries
    StringBuilder reversedText = new StringBuilder();

    for (String word : words) {
      if (word.matches("\\w+")) { // Check if the segment is a word
        String reversedWord = new StringBuilder(word).reverse().toString();
        reversedText.append(reversedWord);
      } else {
        reversedText.append(word); // Preserve punctuation and spaces
      }
    }

    return reversedText.toString();
  }
}
