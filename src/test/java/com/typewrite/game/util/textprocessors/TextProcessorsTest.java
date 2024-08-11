package com.typewrite.game.util.textprocessors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for the text processor decorators. */
public class TextProcessorsTest {

  private TextProcessor baseProcessor;

  /** Setup method to initialize the base text processor before each test. */
  @BeforeEach
  public void setup() {
    baseProcessor = new BaseTextProcessor();
  }

  /** Tests the BaseTextProcessor to ensure it replaces tabs and new lines with spaces. */
  @Test
  public void testBaseTextProcessor() {
    String result = baseProcessor.process("Hello\tworld!\nNew line.");
    assertEquals(
        "Hello world! New line.",
        result,
        "Base processor should replace tabs and new lines with spaces.");
  }

  /** Tests the ExcessSpacesCleaner to ensure it removes excessive spaces. */
  @Test
  public void testExcessSpacesCleaner() {
    TextProcessor processor = new ExcessSpacesCleaner(baseProcessor);
    String result = processor.process("Hello   world!  New   line.");
    assertEquals(
        "Hello world! New line.", result, "ExcessSpacesCleaner should remove excessive spaces.");
  }

  /** Tests the SpecialCharactersCleaner to ensure it removes special characters. */
  @Test
  public void testSpecialCharactersCleaner() {
    TextProcessor processor = new SpecialCharactersCleaner(baseProcessor);
    String result = processor.process("Hello* world! {New} line.");
    assertEquals(
        "Hello world! New line.",
        result,
        "SpecialCharactersCleaner should remove special characters.");
  }

  /** Tests the ImpossibleModeTextProcessor to ensure it alters the text. */
  @Test
  public void testImpossibleModeTextProcessor() {
    TextProcessor processor = new ImpossibleModeTextProcessor(baseProcessor);
    String result = processor.process("Hello world!");
    assertNotEquals("Hello world!", result, "ImpossibleModeTextProcessor should alter the text.");
  }

  /** Tests the MathModeProcessor to ensure it replaces characters with numbers. */
  @Test
  public void testMathModeProcessor() {
    TextProcessor processor = new MathModeProcessor(baseProcessor);
    String result = processor.process("Hello world!");
    assertTrue(
        result.matches(".*[4301].*"), "MathModeProcessor should replace characters with numbers.");
  }

  /** Tests a composite text processor to ensure it applies all transformations. */
  @Test
  public void testCompositeTextProcessor() {
    TextProcessor processor =
        new MathModeProcessor(
            new ImpossibleModeTextProcessor(
                new SpecialCharactersCleaner(new ExcessSpacesCleaner(baseProcessor))));
    String result = processor.process("Hello*   world!\nNew line.");
    assertTrue(
        result.matches(".*[4301\\^\\*\\#\\@\\$\\%\\&\\~\\§].*"),
        "Composite processor should apply all transformations.");
  }
}
