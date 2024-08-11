package com.typewrite.game.controller;

import com.typewrite.game.GameManager;
import com.typewrite.game.Refresh;
import com.typewrite.game.Resources;
import com.typewrite.game.util.TextUtil;
import com.typewrite.game.util.ThemeManager;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Abstract controller for game players. Provides common functionalities for game player
 * controllers.
 */
public abstract class AbstractGamePlayerController implements Initializable, InitDataController {

  public static final Font FONT =
      Font.loadFont(Resources.getResourceAsStream("assets/JetBrainsMono.ttf"), 20);

  protected Refresh refresh;
  protected String currentStr;
  protected Integer totalSize;
  protected Integer inputIndex;
  protected Integer currentIndex;
  protected Integer inputLength;
  protected long startTime;

  protected List<String> sentences;

  /** Constructor for AbstractGamePlayerController. Initializes common properties. */
  public AbstractGamePlayerController() {
    this.totalSize = 0;
    this.inputIndex = 0;
    this.currentIndex = 0;
    this.startTime = 0;
  }

  /**
   * Draws the text on the canvas and updates the input array based on user input.
   *
   * @param charArray the array of characters to draw.
   * @param inputArray the array of characters input by the user.
   * @param inputText the text input by the user.
   * @return the updated input array.
   */
  protected char[] drawText(char[] charArray, char[] inputArray, String inputText) {
    for (int i = 0; i < charArray.length; i++) {
      Color color = Color.BLACK;
      if (ThemeManager.isDarkMode()) {
        color = Color.WHITE;
      }

      if (inputArray.length > i) {
        if (Objects.equals(inputArray[i], charArray[i])) {
          color = Color.GREEN;
        } else {
          color = Color.RED;
          inputArray = Arrays.copyOf(inputArray, i);
          getTextArea().setText("");
          getTextArea().appendText(inputText.substring(0, i + 1));
        }
      }
      getCtx().save();
      getCtx().setFill(color);
      getCtx().setFont(AbstractGamePlayerController.FONT);

      char character = charArray[i];
      int x = (i % 50) * 10;
      int y = (i / 50) * 20 + 20;
      getCtx().fillText(String.valueOf(character), x, y);
      getCtx().restore();
    }
    return inputArray;
  }

  /**
   * Checks if there is a next sentence to display and update the state accordingly.
   *
   * @param inputArray the array of characters input by the user.
   * @param charArray the array of characters of the current sentence.
   * @return true if there is a next sentence, false otherwise.
   */
  protected boolean hasNextSentence(char[] inputArray, char[] charArray) {
    if (inputArray.length >= charArray.length) {
      inputIndex++;
      inputLength += charArray.length;
      if (inputIndex >= sentences.size()) {
        return false;
      }
      currentStr = sentences.get(inputIndex);
      currentIndex = Math.min(currentIndex + currentStr.length(), totalSize);
      Platform.runLater(() -> getTextArea().setText(""));
    }
    return true;
  }

  /** Clears the canvas. */
  protected void clearCanvas() {
    getCtx().clearRect(0, 0, GameManager.CANVAS_WIDTH, GameManager.CANVAS_HEIGHT);
  }

  /**
   * Sets the sentences to be used in the game.
   *
   * @param sentences the list of sentences.
   */
  public void setSentences(List<String> sentences) {
    this.sentences = sentences;
    this.currentStr = sentences.get(0);
    this.totalSize = TextUtil.getTextSize(sentences);

    this.inputIndex = 0;
    this.inputLength = 0;
    this.currentIndex = 1;
  }

  /** Starts the game. */
  public abstract void start();

  /** Stops the game. */
  public abstract void stop();

  /**
   * Updates the game state.
   *
   * @param timer the current timer value.
   */
  protected abstract void update(long timer);

  /**
   * Updates the game UI.
   *
   * @param timer the current timer value.
   * @param inputArray the array of characters input by the user.
   */
  protected abstract void updateGameUi(long timer, char[] inputArray);

  /**
   * Gets the GraphicsContext for drawing on the canvas.
   *
   * @return the GraphicsContext.
   */
  protected abstract GraphicsContext getCtx();

  /**
   * Gets the TextArea for user input.
   *
   * @return the TextArea.
   */
  protected abstract TextArea getTextArea();
}
