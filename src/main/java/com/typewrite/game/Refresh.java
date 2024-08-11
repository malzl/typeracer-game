package com.typewrite.game;

import java.util.function.LongConsumer;
import javafx.animation.AnimationTimer;

/** A custom animation timer for refreshing the game state. */
public class Refresh extends AnimationTimer {

  private final LongConsumer handler;
  private final Integer interval = 1000 / 24;
  private Long lastUpdateTime = 0L;

  /**
   * Constructs a Refresh timer with the specified handler.
   *
   * @param handler the handler to be called on each tick.
   */
  public Refresh(LongConsumer handler) {
    this.handler = handler;
  }

  @Override
  public void handle(long now) {
    if (now - lastUpdateTime >= interval) {
      lastUpdateTime = now;
      handler.accept(now / 1000000);
    }
  }
}
