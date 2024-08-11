package com.typewrite.game.util.textprocessors;

/**
 * TextProcessorDecorator is an abstract class that decorates a TextProcessor. It provides a base
 * for creating decorators that modify the behavior of the underlying text processor.
 */
public abstract class TextProcessorDecorator implements TextProcessor {
  protected final TextProcessor processor;

  /**
   * Constructs a TextProcessorDecorator with the given underlying text processor.
   *
   * @param processor the underlying text processor
   */
  public TextProcessorDecorator(TextProcessor processor) {
    this.processor = processor;
  }

  /**
   * Processes the given text using the underlying text processor.
   *
   * @param text the input text to be processed
   * @return the processed text
   */
  @Override
  public String process(String text) {
    return processor.process(text);
  }
}
