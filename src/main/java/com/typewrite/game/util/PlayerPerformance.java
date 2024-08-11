package com.typewrite.game.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Represents the performance of a player, tracking words per minute (WPM) and mistakes. */
public class PlayerPerformance {
  private List<Long> wpmList = new ArrayList<>();
  private int mistakes = 0;

  /**
   * Adds a WPM value to the performance record.
   *
   * @param wpm the WPM value to add
   */
  public void addWpm(long wpm) {
    wpmList.add(wpm);
  }

  /** Increments the mistake count by one. */
  public void addMistake() {
    mistakes++;
  }

  /**
   * Gets the total number of mistakes.
   *
   * @return the number of mistakes
   */
  public int getMistakes() {
    return mistakes;
  }

  /**
   * Calculates the average WPM.
   *
   * @return the average WPM, or 0 if no WPM values are recorded
   */
  public double getAverageWpm() {
    if (wpmList.isEmpty()) {
      return 0;
    }
    long sum = 0;
    for (long wpm : wpmList) {
      sum += wpm;
    }
    return (double) sum / wpmList.size();
  }

  /**
   * Calculates the median WPM.
   *
   * @return the median WPM, or 0 if no WPM values are recorded
   */
  public double getMedianWpm() {
    if (wpmList.isEmpty()) {
      return 0;
    }
    Collections.sort(wpmList);
    int middle = wpmList.size() / 2;
    if (wpmList.size() % 2 == 0) {
      return (wpmList.get(middle - 1) + wpmList.get(middle)) / 2.0;
    } else {
      return wpmList.get(middle);
    }
  }

  /**
   * Gets the maximum WPM value recorded.
   *
   * @return the maximum WPM, or 0 if no WPM values are recorded
   */
  public long getMaxWpm() {
    return wpmList.isEmpty() ? 0 : Collections.max(wpmList);
  }

  /**
   * Gets the minimum WPM value recorded.
   *
   * @return the minimum WPM, or 0 if no WPM values are recorded
   */
  public long getMinWpm() {
    return wpmList.isEmpty() ? 0 : Collections.min(wpmList);
  }

  /**
   * Returns a string representation of the player's performance.
   *
   * @return a string summarizing the player's performance
   */
  @Override
  public String toString() {
    return String.format(
        "Mistakes: %d%nAverage WPM: %.1f%nMedian WPM: %.1f%nMax WPM: %d%nMin WPM: %d",
        getMistakes(), getAverageWpm(), getMedianWpm(), getMaxWpm(), getMinWpm());
  }
}
