package com.typewrite.game.common.enums;

/** Enum representing different levels of logging. */
public enum LogLevel {

  /** Debug level logging. */
  DEBUG(1),

  /** Info level logging. */
  INFO(2),

  /** Warning level logging. */
  WARNING(3),

  /** Error level logging. */
  ERROR(4),
  ;

  private final int logLevel;

  /**
   * Constructor for LogLevel.
   *
   * @param logLevel the integer value representing the log level.
   */
  LogLevel(int logLevel) {
    this.logLevel = logLevel;
  }

  /**
   * Gets the integer value representing the log level.
   *
   * @return the integer value of the log level.
   */
  public int getLogLevel() {
    return logLevel;
  }
}
