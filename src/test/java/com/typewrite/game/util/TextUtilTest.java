package com.typewrite.game.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.typewrite.game.Resources;
import com.typewrite.game.common.enums.DifficultyLevel;
import com.typewrite.game.util.textprocessors.TextProcessor;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Unit tests for the TextUtil class. */
public class TextUtilTest {

  /** Tests loading random sentences with a valid book index. */
  @Test
  public void testLoadRandomSentences_ValidBookIndex() {
    List<String> sentences =
        TextUtil.loadRandomSentences(1, 0); // Testing with no special processors
    assertNotNull(sentences, "Sentences should not be null for a valid book index.");
    assertFalse(sentences.isEmpty(), "Sentences should not be empty for a valid book index.");
  }

  /** Tests loading random sentences with an invalid book index. */
  @Test
  public void testLoadRandomSentences_InvalidBookIndex() {
    List<String> sentences = TextUtil.loadRandomSentences(-1, 0);
    assertTrue(sentences.isEmpty(), "Sentences should be empty for an invalid book index.");

    sentences = TextUtil.loadRandomSentences(4, 0);
    assertTrue(sentences.isEmpty(), "Sentences should be empty for an invalid book index.");
  }

  /** Tests loading random sentences with the ImpossibleModeTextProcessor. */
  @Test
  public void testLoadRandomSentences_WithImpossibleProcessor() {
    List<String> sentences =
        TextUtil.loadRandomSentences(
            1, 3); // Assuming difficulty index 3 applies ImpossibleModeTextProcessor
    assertNotNull(sentences, "Sentences should not be null.");
    assertFalse(sentences.isEmpty(), "Sentences should contain transformed text.");
  }

  /** Tests loading random sentences with the MathModeProcessor. */
  @Test
  public void testLoadRandomSentences_WithMathModeProcessor() {
    List<String> sentences =
        TextUtil.loadRandomSentences(1, 4); // Assuming difficulty index 4 applies MathModeProcessor
    assertNotNull(sentences, "Sentences should not be null.");
    assertFalse(sentences.isEmpty(), "Sentences should contain transformed text.");
  }

  /** Tests loading custom sentences. */
  @Test
  public void testLoadCustomSentences() {
    String text = "Hello world. This is a test!";
    List<String> sentences = TextUtil.loadCustomSentences(text);
    assertEquals(2, sentences.size(), "Should split the text into two sentences.");
  }

  /** Tests splitting text into sentences without segmentation. */
  @Test
  public void testSplitIntoSentences_NoSegmentation() {
    String text = "This is a sentence. And another one!";
    List<String> sentences = TextUtil.splitIntoSentences(text, false);
    assertEquals(2, sentences.size(), "Should split into two sentences.");
  }

  /** Tests splitting text into sentences with segmentation. */
  @Test
  public void testSplitIntoSentences_WithSegmentation() {
    String text =
        "This is a long sentence that should be segmented because it exceeds the maximum length.";
    List<String> sentences = TextUtil.splitIntoSentences(text, true);
    assertTrue(sentences.size() > 1, "Should segment the long sentence.");
  }

  /** Tests segmenting a long sentence into smaller segments. */
  @Test
  public void testSegmentSentence() {
    String sentence = "This is a very long sentence that needs to be split into smaller segments.";
    List<String> segments = TextUtil.segmentSentence(sentence, 10);
    assertTrue(segments.size() > 1, "Should split sentence into multiple segments.");
  }

  /** Tests selecting random sentences based on difficulty level. */
  @Test
  public void testSelectRandomSentences() {
    List<String> sentences =
        Arrays.asList(
            "Short sentence.", "This is a longer sentence.", "This is an even longer sentence.");
    List<String> selectedSentences = TextUtil.selectRandomSentences(sentences, 0);
    assertEquals(
        DifficultyLevel.EASY.getNumberOfSentences(),
        selectedSentences.size(),
        "Should select one sentence for difficulty level 0.");
  }

  /** Tests filtering sentences by length and number. */
  @Test
  public void testFilterSentencesByLength() {
    List<String> sentences =
        Arrays.asList("Short.", "This is medium length.", "This is a really long sentence.");
    List<String> filteredSentences = TextUtil.filterSentencesByLength(sentences, 2, 5, 50);
    assertEquals(2, filteredSentences.size(), "Should filter sentences by length and number.");
  }

  /** Tests getting the difficulty settings for a specific difficulty level. */
  @Test
  public void testGetDifficultySettings() {
    int[] settings = DifficultyLevel.EASY.getSettings();
    assertArrayEquals(
        DifficultyLevel.EASY.getSettings(),
        settings,
        "Should return the correct settings for EASY difficulty level.");
  }

  /** Tests reading file content from a valid file path. */
  @Test
  public void testReadFileContent() {
    String content = TextUtil.readFileContent(Resources.MODULE_DIR + "assets/" + "1" + ".txt");
    assertNotNull(content, "Content should not be null for a valid file path.");
    assertFalse(content.isEmpty(), "Content should not be empty for a valid file path.");
  }

  /** Tests building a text processor chain with the base processor. */
  @Test
  public void testBuildTextProcessorChain_Base() {
    TextProcessor processor = TextUtil.buildTextProcessorChain(0);
    String result = processor.process("Hello   world!\nNew\tline.");
    assertEquals(
        "Hello world! New line.",
        result,
        "Base processor should handle spaces and special characters.");
  }

  /** Tests building a text processor chain with the ImpossibleModeTextProcessor. */
  @Test
  public void testBuildTextProcessorChain_ImpossibleMode() {
    TextProcessor processor = TextUtil.buildTextProcessorChain(3);
    String result = processor.process("Hello world!");
    assertNotEquals("Hello world!", result, "ImpossibleModeTextProcessor should alter the text.");
  }

  /** Tests building a text processor chain with the MathModeProcessor. */
  @Test
  public void testBuildTextProcessorChain_MathMode() {
    TextProcessor processor = TextUtil.buildTextProcessorChain(4);
    String result = processor.process("Hello world!");
    assertTrue(
        result.matches(".*[4301].*"), "MathModeProcessor should replace characters with numbers.");
  }

  /** Tests validating the book index. */
  @Test
  public void testIsValidBookIndex() {
    assertTrue(TextUtil.isValidBookIndex(1), "Book index 1 should be valid.");
    assertFalse(TextUtil.isValidBookIndex(0), "Book index 0 should be invalid.");
    assertFalse(TextUtil.isValidBookIndex(4), "Book index 4 should be invalid.");
  }
}
