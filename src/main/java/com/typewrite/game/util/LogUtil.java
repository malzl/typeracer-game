package com.typewrite.game.util;

import com.typewrite.game.GameManager;
import com.typewrite.game.common.enums.LogLevel;
import java.util.Date;

/** Utility class for logging. */
public class LogUtil {

  /**
   * Prints a debug log message.
   *
   * @param log the debug log message to be printed.
   */
  public static void debug(String log) {
    printLog(log, LogLevel.DEBUG);
  }

  /**
   * Prints an info log message.
   *
   * @param log the info log message to be printed.
   */
  public static void info(String log) {
    printLog(log, LogLevel.INFO);
  }

  /**
   * Prints a warning log message.
   *
   * @param log the warning log message to be printed.
   */
  public static void warning(String log) {
    printLog(log, LogLevel.WARNING);
  }

  /**
   * Prints an error log message.
   *
   * @param log the error log message to be printed.
   */
  public static void error(String log) {
    printLog(log, LogLevel.ERROR);
  }

  /**
   * Prints a log message at the specified log level.
   *
   * @param log the log message to be printed.
   * @param logLevel the level of the log message.
   */
  private static void printLog(String log, LogLevel logLevel) {
    if (logLevel.getLogLevel() >= GameManager.LOG_LEVEL.getLogLevel()) {
      if (logLevel.getLogLevel() >= LogLevel.ERROR.getLogLevel()) {
        System.err.printf("==> %s [%s] %s %n", new Date(), logLevel.name(), log);
      }
      System.out.printf("==> %s [%s] %s %n", new Date(), logLevel.name(), log);
    }
  }
}
