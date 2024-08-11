package com.typewrite.game.util;

import com.typewrite.game.Resources;
import javafx.scene.layout.Pane;

/** Manages the application theme, allowing for toggling between light and dark modes. */
public class ThemeManager {

  private static final String lightModeCss =
      Resources.getResourceThroughUrl("assets/style.css").toExternalForm();
  private static final String darkModeCss =
      Resources.getResourceThroughUrl("assets/dark-mode.css").toExternalForm();

  private static boolean isDarkMode = false;

  /**
   * Checks if the current theme is dark mode.
   *
   * @return true if the current theme is dark mode, false otherwise
   */
  public static boolean isDarkMode() {
    return isDarkMode;
  }

  /** Toggles the theme between light and dark modes. */
  public static void toggleDarkMode() {
    isDarkMode = !isDarkMode;
  }

  /**
   * Gets the CSS for light mode.
   *
   * @return the CSS file path for light mode
   */
  public static String getLightModeCss() {
    return lightModeCss;
  }

  /**
   * Gets the CSS for dark mode.
   *
   * @return the CSS file path for dark mode
   */
  public static String getDarkModeCss() {
    return darkModeCss;
  }

  /**
   * Applies the current theme to the given root pane.
   *
   * @param root the root pane to which the current theme will be applied
   */
  public static void applyCurrentTheme(Pane root) {
    root.getStylesheets().clear();
    if (isDarkMode) {
      root.getStylesheets().add(darkModeCss);
    } else {
      root.getStylesheets().add(lightModeCss);
    }
  }
}
