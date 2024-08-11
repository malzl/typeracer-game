package com.typewrite.game.controller.single;

import static com.typewrite.game.util.AudioUtil.playBackgroundMusic;
import static com.typewrite.game.util.AudioUtil.stopMusic;

import com.typewrite.game.GameManager;
import com.typewrite.game.Refresh;
import com.typewrite.game.controller.AbstractGamePlayerController;
import com.typewrite.game.util.LogUtil;
import com.typewrite.game.util.PlayerPerformance;
import com.typewrite.game.util.ThemeManager;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.ResourceBundle;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * Controller for managing the single player game play screen. Handles game updates, player
 * information, and game events.
 */
public class SingleGamePlayController extends AbstractGamePlayerController {

  private static final int SMOOTHING_WINDOW_SIZE =
      5; // For better experience in short practice games
  private static final double WARM_UP_DURATION = 0.5;
  Queue<Long> wpmHistory = new LinkedList<>();
  private long smoothedWpm = 0;
  private Map<Integer, Character> mistakes = new HashMap<>();

  private PlayerPerformance playerPerformance =
      new PlayerPerformance(); // Initialize PlayerPerformance

  @FXML AnchorPane gamePane;

  @FXML Canvas canvas;
  private GraphicsContext ctx;
  @FXML TextArea textArea;

  @FXML ImageView car;
  @FXML Rectangle progress;
  @FXML Label wpm;
  @FXML Label usedTime;
  @FXML Label nameInitials;
  @FXML Label name;
  @FXML Label mistakesLabel;
  @FXML StackPane rootPane;

  /**
   * Initializes the controller with the provided data.
   *
   * @param args the arguments passed for initialization, expecting a list of sentences.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void initData(Object... args) {
    setSentences((List<String>) args[0]);
    start();
  }

  /**
   * Initializes the controller. Sets up player information and canvas context.
   *
   * @param url the location used to resolve relative paths for the root object, or null if not
   *     known.
   * @param resourceBundle the resources used to localize the root object, or null if not localized.
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    try {
      String lightModeCss = ThemeManager.getLightModeCss();
      String darkModeCss = ThemeManager.getDarkModeCss();
      LogUtil.info("Loading GameView with dark mode: " + ThemeManager.isDarkMode());

      if (ThemeManager.isDarkMode()) {
        rootPane.getStylesheets().remove(darkModeCss);
        rootPane.getStylesheets().add(lightModeCss);
      } else {
        rootPane.getStylesheets().remove(lightModeCss);
        rootPane.getStylesheets().add(darkModeCss);
      }
    } catch (Exception e) {
      LogUtil.error("Failed to load dark or light mode view.");
    }
    GameManager gameManager = GameManager.getInstance();
    int initialsLength = gameManager.getMainPlayer().length() > 2 ? 2 : 1;
    nameInitials.setText(gameManager.getMainPlayer().substring(0, initialsLength));
    name.setText(gameManager.getMainPlayer());
    updateMistakesLabel(0);
    progress.setWidth(0);
    car.setLayoutX(20);
    ctx = canvas.getGraphicsContext2D();

    refresh = new Refresh(this::update);
  }

  /** Starts the game. */
  public void start() {
    stopMusic();
    refresh.start();
  }

  /** Stops the game. */
  public void stop() {
    refresh.stop();

    VBox vbox = new VBox(20);
    vbox.getStyleClass().add("vbox");
    vbox.setAlignment(Pos.CENTER);

    VBox performanceSummaryBox = new VBox(10);
    performanceSummaryBox.setAlignment(Pos.CENTER);
    performanceSummaryBox.getStyleClass().add("performance-summary-box");
    performanceSummaryBox.setMaxWidth(600); // Set the maximum width of the performance summary box

    Label tip = new Label("GAME COMPLETED!");
    tip.getStyleClass().add("tip-label");

    Label mistakesLabel = new Label("Mistakes: " + playerPerformance.getMistakes());
    mistakesLabel.setTextFill(Color.WHITE);
    mistakesLabel.setFont(new Font("Arial", 20));

    Label averageWpmLabel =
        new Label(String.format("Average WPM: %.1f", playerPerformance.getAverageWpm()));
    averageWpmLabel.setTextFill(Color.WHITE);
    averageWpmLabel.setFont(new Font("Arial", 20));

    Label medianWpmLabel =
        new Label(String.format("Median WPM: %.1f", playerPerformance.getMedianWpm()));
    medianWpmLabel.setTextFill(Color.WHITE);
    medianWpmLabel.setFont(new Font("Arial", 20));

    Label maxWpmLabel = new Label("Max WPM: " + playerPerformance.getMaxWpm());
    maxWpmLabel.setTextFill(Color.WHITE);
    maxWpmLabel.setFont(new Font("Arial", 20));

    Label minWpmLabel = new Label("Min WPM: " + playerPerformance.getMinWpm());
    minWpmLabel.setTextFill(Color.WHITE);
    minWpmLabel.setFont(new Font("Arial", 20));

    performanceSummaryBox
        .getChildren()
        .addAll(tip, mistakesLabel, averageWpmLabel, medianWpmLabel, maxWpmLabel, minWpmLabel);

    Button button = new Button("Back to Home Page");
    button.getStyleClass().add("button");
    button.setOnAction(
        event -> {
          try {
            GameManager.getInstance().loadView("home-view");
            playBackgroundMusic();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });

    addHoverAnimation(button);

    Platform.runLater(() -> gamePane.setDisable(true));
    vbox.getChildren().addAll(performanceSummaryBox, button);
    rootPane.getChildren().add(vbox);
  }

  private void addHoverAnimation(Button button) {
    ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(300), button);
    scaleTransition.setFromX(1.0);
    scaleTransition.setFromY(1.0);
    scaleTransition.setToX(1.1);
    scaleTransition.setToY(1.1);
    scaleTransition.setAutoReverse(true);
    scaleTransition.setCycleCount(2);

    button.setOnMouseEntered(event -> scaleTransition.play());
    button.setOnMouseExited(event -> scaleTransition.stop());
  }

  /**
   * Updates the game. Draws text on the canvas, checks input, and updates the game UI.
   *
   * @param timer the current timer value.
   */
  protected void update(long timer) {
    LogUtil.debug("Update called at: " + System.nanoTime() + ", with timer: " + timer);
    if (startTime == 0) {
      startTime = timer;
    }
    // draw
    if (currentStr == null) {
      return;
    }
    char[] charArray = currentStr.toCharArray();
    String inputText = textArea.getText();
    char[] inputArray;
    if (inputText != null) {
      inputArray = inputText.toCharArray();
    } else {
      inputArray = new char[0];
    }

    clearCanvas();
    inputArray = drawText(charArray, inputArray, inputText);

    if (!hasNextSentence(inputArray, charArray)) {
      updateGameUi(timer, inputArray);
      stop();
      return;
    }
    updateGameUi(timer, inputArray);
  }

  /**
   * Updates the game UI.
   *
   * @param timer the current timer value.
   * @param inputArray the array of characters input by the user.
   */
  protected void updateGameUi(long timer, char[] inputArray) {
    Platform.runLater(
        () -> {
          updateProgress(inputArray.length);
          long totalCharacters = inputLength + inputArray.length;
          double elapsedTimeInSeconds = updateElapsedTime(timer);

          if (isWarmUpPeriod(elapsedTimeInSeconds)) {
            wpm.setText("Calculating...");
          } else {
            updateWpmAndUi(totalCharacters, elapsedTimeInSeconds);
          }

          updateMistakesLabel(playerPerformance.getMistakes());
          updateUsedTimeLabel(elapsedTimeInSeconds);
        });
  }

  private void updateProgress(int inputArrayLength) {
    double percentage = ((double) (inputLength + inputArrayLength) / totalSize);
    car.setLayoutX(320 * percentage + 20);
    progress.setWidth(360 * percentage);
    if (inputLength + inputArrayLength >= totalSize) {
      car.setLayoutX(340);
      progress.setWidth(360);
    }
  }

  private double updateElapsedTime(long timer) {
    if (startTime == 0) {
      startTime = timer;
    }
    return (timer - startTime) / 1000.0;
  }

  private boolean isWarmUpPeriod(double elapsedTimeInSeconds) {
    if (elapsedTimeInSeconds < WARM_UP_DURATION) {
      return true;
    } else {
      return false;
    }
  }

  private void updateWpmAndUi(long totalCharacters, double elapsedTimeInSeconds) {
    if (elapsedTimeInSeconds > 0) {
      long currentWpm = calculateCurrentWpm(totalCharacters, elapsedTimeInSeconds);
      updateWpmHistory(currentWpm);
      smoothedWpm = calculateSmoothedWpm();
      playerPerformance.addWpm(currentWpm); // Add WPM to player performance
      wpm.setText(smoothedWpm + " WPM");
    }
  }

  long calculateCurrentWpm(long totalCharacters, double elapsedTimeInSeconds) {
    return (long) ((totalCharacters / 5.0) / (elapsedTimeInSeconds / 60.0));
  }

  void updateWpmHistory(long currentWpm) {
    wpmHistory.offer(currentWpm);
    if (wpmHistory.size() > SMOOTHING_WINDOW_SIZE) {
      wpmHistory.poll();
    }
  }

  long calculateSmoothedWpm() {
    long sum = 0;
    for (long wpm : wpmHistory) {
      sum += wpm;
    }
    return wpmHistory.isEmpty() ? 0 : sum / wpmHistory.size();
  }

  private void updateUsedTimeLabel(double elapsedTimeInSeconds) {
    DecimalFormat df = new DecimalFormat("#.##");
    String formatTime = df.format(elapsedTimeInSeconds);
    usedTime.setText(formatTime + " S");
  }

  private void updateMistakesLabel(int mistakes) {
    mistakesLabel.setText(mistakes + " Mistakes");
  }

  @Override
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
          if (!mistakes.containsKey(i)
              || (mistakes.get(i) != null && !mistakes.get(i).equals(inputArray[i]))) {
            playerPerformance.addMistake();
            mistakes.put(i, inputArray[i]);
          }
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
   * Gets current graphics context.
   *
   * @return the current context.
   */
  @Override
  protected GraphicsContext getCtx() {
    return ctx;
  }

  /**
   * Gets current text area for gameplay.
   *
   * @return the current text area.
   */
  @Override
  protected TextArea getTextArea() {
    return textArea;
  }
}
