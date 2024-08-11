package com.typewrite.game.controller.single;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javafx.embed.swing.JFXPanel;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SingleGamePlayControllerTest {

  private SingleGamePlayController controller;

  @BeforeEach
  void setUp() {
    // Initialize JavaFX environment
    new JFXPanel();
    controller = new SingleGamePlayController();
    controller.rootPane = new StackPane();
    controller.gamePane = new AnchorPane();
    controller.canvas = new Canvas();
    controller.textArea = new TextArea();
    controller.car = new ImageView();
    controller.progress = new Rectangle();
    controller.wpm = new Label();
    controller.usedTime = new Label();
    controller.nameInitials = new Label();
    controller.name = new Label();
  }

  @Test
  void testCalculateCurrentWpm() {
    long totalCharacters = 25;
    double elapsedTimeInSeconds = 60;
    long expectedWpm = 5;

    long wpm = controller.calculateCurrentWpm(totalCharacters, elapsedTimeInSeconds);
    assertEquals(expectedWpm, wpm);
  }

  @Test
  void testCalculateSmoothedWpm() {
    controller.wpmHistory.offer(5L);
    controller.wpmHistory.offer(10L);
    controller.wpmHistory.offer(15L);

    long expectedSmoothedWpm = 10;
    long smoothedWpm = controller.calculateSmoothedWpm();
    assertEquals(expectedSmoothedWpm, smoothedWpm);
  }

  @Test
  void testUpdateWpmHistory() {
    controller.wpmHistory.offer(5L);
    controller.wpmHistory.offer(10L);

    long newWpm = 15L;
    controller.updateWpmHistory(newWpm);

    assertEquals(3, controller.wpmHistory.size());
    assertEquals(Long.valueOf(5L), controller.wpmHistory.peek());
  }
}
