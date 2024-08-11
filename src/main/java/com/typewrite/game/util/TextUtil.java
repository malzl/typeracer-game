package com.typewrite.game.util;

import com.typewrite.game.Resources;
import com.typewrite.game.common.enums.DifficultyLevel;
import com.typewrite.game.util.textprocessors.BaseTextProcessor;
import com.typewrite.game.util.textprocessors.ExcessSpacesCleaner;
import com.typewrite.game.util.textprocessors.ImpossibleModeTextProcessor;
import com.typewrite.game.util.textprocessors.MathModeProcessor;
import com.typewrite.game.util.textprocessors.ReverseTextProcessor;
import com.typewrite.game.util.textprocessors.SpecialCharactersCleaner;
import com.typewrite.game.util.textprocessors.TextProcessor;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Utility class for processing text in the game. */
public class TextUtil {

  public static final String SENTENCE_DELIMITERS = "(?<=[.!?])\\s+(?=[\"'\\(\\[\\{]*[A-Z0-9])";

  /**
   * Loads random sentences from a specified book based on the difficulty index.
   *
   * @param bookIndex the index of the book to load sentences from
   * @param difficultyIndex the difficulty index to determine text processing and selection
   * @return a list of random sentences
   */
  public static List<String> loadRandomSentences(int bookIndex, int difficultyIndex) {
    if (!isValidBookIndex(bookIndex)) {
      LogUtil.debug("Invalid book index: " + bookIndex);
      return Collections.emptyList();
    }
    LogUtil.debug(
        "loadRandomSentences: Start loading with bookIndex="
            + bookIndex
            + ", difficultyIndex="
            + difficultyIndex);

    String content = readFileContent(Resources.MODULE_DIR + "assets/" + bookIndex + ".txt");
    LogUtil.debug(
        "Original content length: "
            + content.length()
            + " Content preview: "
            + content.substring(0, Math.min(50, content.length())));

    TextProcessor processor = buildTextProcessorChain(difficultyIndex);
    content = processor.process(content);
    LogUtil.debug(
        "Processed content length: "
            + content.length()
            + " Content preview: "
            + content.substring(0, Math.min(50, content.length())));

    List<String> sentences = splitIntoSentences(content, false);
    LogUtil.debug("Extracted sentences count: " + sentences.size());
    if (sentences.isEmpty()) {
      LogUtil.error("No sentences extracted from processed content.");
    }

    List<String> selectedSentences = selectRandomSentences(sentences, difficultyIndex);
    LogUtil.debug("Selected sentences count: " + selectedSentences.size());
    return selectedSentences;
  }

  /**
   * Loads custom sentences from the provided text.
   *
   * @param text the custom text to process and split into sentences
   * @return a list of sentences extracted from the custom text
   */
  public static List<String> loadCustomSentences(String text) {
    LogUtil.debug("loadCustomSentences: Processing custom text.");
    TextProcessor processor =
        buildTextProcessorChain(0); // Assume default processing for custom sentences
    text = processor.process(text);

    List<String> sentences = new ArrayList<>();
    sentences.addAll(splitIntoSentences(text, true));
    LogUtil.debug("Number of custom sentences extracted: " + sentences.size());
    return sentences;
  }

  /**
   * Gets the total size of the provided list of sentences.
   *
   * @param sentences the list of sentences.
   * @return the total size of all sentences combined.
   */
  public static Integer getTextSize(List<String> sentences) {
    int totalSize = 0;
    for (String sentence : sentences) {
      totalSize += sentence.length();
    }
    return totalSize;
  }

  static TextProcessor buildTextProcessorChain(int difficultyIndex) {
    DifficultyLevel difficulty = DifficultyLevel.fromIndex(difficultyIndex);
    TextProcessor baseProcessor = new BaseTextProcessor();
    baseProcessor = new ExcessSpacesCleaner(baseProcessor);
    baseProcessor = new SpecialCharactersCleaner(baseProcessor);

    if (difficulty == DifficultyLevel.IMPOSSIBLE) {
      baseProcessor = new ImpossibleModeTextProcessor(baseProcessor);
    }

    if (difficulty == DifficultyLevel.MATH_MODE) {
      baseProcessor = new MathModeProcessor(baseProcessor);
    }

    if (difficulty == DifficultyLevel.REVERSE_MODE) {
      baseProcessor = new ReverseTextProcessor(baseProcessor);
    }

    return baseProcessor;
  }

  static boolean isValidBookIndex(int bookIndex) {
    return bookIndex >= 1 && bookIndex <= 3;
  }

  static String readFileContent(String path) {
    StringBuilder content = new StringBuilder();
    try (InputStream is = TextUtil.class.getResourceAsStream(path);
        BufferedReader reader =
            new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
      if (is == null) {
        throw new FileNotFoundException("Resource not found: " + path);
      }
      String line;
      while ((line = reader.readLine()) != null) {
        content.append(line).append(" ");
      }
    } catch (IOException ex) {
      LogUtil.error("Error reading from file: " + ex.getMessage());
      return "";
    }
    return content.toString().trim();
  }

  static List<String> selectRandomSentences(List<String> sentences, int difficultyIndex) {
    int[] settings = DifficultyLevel.fromIndex(difficultyIndex).getSettings();
    Collections.shuffle(sentences);
    return filterSentencesByLength(sentences, settings[0], settings[1], settings[2]);
  }

  static List<String> filterSentencesByLength(
      List<String> sentences, int numSentences, int minLen, int maxLen) {
    List<String> selectedSentences = new ArrayList<>();
    for (String sentence : sentences) {
      int length = sentence.length();
      if (length >= minLen && length <= maxLen && selectedSentences.size() < numSentences) {
        selectedSentences.add(sentence);
      }
    }
    return selectedSentences;
  }

  static List<String> splitIntoSentences(String text, boolean segment) {
    List<String> sentences = new ArrayList<>();
    for (String sentence : text.split(SENTENCE_DELIMITERS)) {
      if (segment) {
        sentences.addAll(segmentSentence(sentence, 50));
      } else {
        if (!sentence.trim().isEmpty()) {
          sentences.add(sentence.trim());
        }
      }
    }
    return sentences;
  }

  static List<String> segmentSentence(String sentence, int maxSegmentLength) {
    List<String> segments = new ArrayList<>();
    int start = 0;
    while (start < sentence.length()) {
      int end = Math.min(start + maxSegmentLength, sentence.length());
      String segment = sentence.substring(start, end).trim();
      if (!segment.isEmpty()) {
        segments.add(segment);
      }
      start += maxSegmentLength;
    }
    return segments;
  }
}
