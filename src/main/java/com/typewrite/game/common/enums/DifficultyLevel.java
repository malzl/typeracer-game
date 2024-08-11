package com.typewrite.game.common.enums;

/** Enum representing different difficulty levels for a game. */
public enum DifficultyLevel {
  EASY(0, new int[] {2, 10, 50}),
  MEDIUM(1, new int[] {3, 50, 70}),
  HARD(2, new int[] {5, 50, 100}),
  IMPOSSIBLE(3, new int[] {5, 15, 100}),
  MATH_MODE(4, new int[] {3, 30, 100}),
  REVERSE_MODE(5, new int[] {3, 30, 100}),
  DEFAULT(-1, new int[] {3, 50, 70});

  private final int index;
  private final int[] settings;

  /**
   * Constructor for DifficultyLevel.
   *
   * @param index the index of the difficulty level
   * @param settings the settings array containing specific parameters for the difficulty
   */
  DifficultyLevel(int index, int[] settings) {
    this.index = index;
    this.settings = settings;
  }

  /**
   * Get the settings array for this difficulty level.
   *
   * @return the settings array
   */
  public int[] getSettings() {
    return settings;
  }

  /**
   * Get the number of sentences associated with this difficulty level.
   *
   * @return the number of sentences
   */
  public int getNumberOfSentences() {
    return settings[0];
  }

  /**
   * Get the DifficultyLevel enum instance corresponding to the given index.
   *
   * @param index the index of the difficulty level to retrieve
   * @return the DifficultyLevel enum instance corresponding to the index, or DEFAULT if not found
   */
  public static DifficultyLevel fromIndex(int index) {
    for (DifficultyLevel difficulty : values()) {
      if (difficulty.index == index) {
        return difficulty;
      }
    }
    return DEFAULT;
  }
}
