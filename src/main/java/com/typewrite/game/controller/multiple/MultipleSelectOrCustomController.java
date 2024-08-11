package com.typewrite.game.controller.multiple;

import com.typewrite.game.GameManager;
import com.typewrite.game.common.event.DefaultEventBus;
import com.typewrite.game.common.event.Listener;
import com.typewrite.game.common.event.events.SelectedEvent;
import com.typewrite.game.common.event.events.StartGameEvent;
import com.typewrite.game.controller.AbstractSelectOrCustomController;
import com.typewrite.game.controller.InitDataController;
import com.typewrite.game.network.server.PlayerInfo;
import com.typewrite.game.util.AudioUtil;
import com.typewrite.game.util.LogUtil;
import com.typewrite.game.util.TextUtil;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Controller for handling multiple select or custom game settings. Manages the UI for selecting
 * built-in books or entering custom text.
 */
public class MultipleSelectOrCustomController extends AbstractSelectOrCustomController
    implements Initializable, InitDataController {

  @FXML protected HBox builtInHbox;
  @FXML protected TextArea customArea;
  @FXML protected Button startButton;
  @FXML private ComboBox<String> difficultySelector;
  @FXML private ImageView booksImage;

  /**
   * Handles the click event for the custom start button. Loads and processes the custom text
   * entered by the user.
   */
  @FXML
  protected void onCustomStartClick() {
    LogUtil.info("onCustomStartClick: Method called.");
    AudioUtil.playClickSoundEffect();

    String customText = customArea.getText();
    LogUtil.info("onCustomStartClick: Text from customArea = '" + customText + "'");

    if (customText == null || customText.trim().isEmpty()) {
      LogUtil.error("onCustomStartClick: No text entered or only whitespace found.");
      return;
    }

    try {
      List<String> sentences = TextUtil.loadCustomSentences(customText);
      LogUtil.info("onCustomStartClick: Sentences loaded, count = " + sentences.size());
      completed(sentences);
    } catch (Exception e) {
      LogUtil.error("onCustomStartClick: Error loading sentences: " + e.getMessage());
      throw new RuntimeException(e);
    }
  }

  private List<PlayerInfo> playerInfos;

  /**
   * Initializes the controller. Sets up the difficulty selector and book options, and subscribes to
   * start game events.
   *
   * @param url the location used to resolve relative paths for the root object, or null if not
   *     known.
   * @param resourceBundle the resources used to localize the root object, or null if not localized.
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    difficultySelector
        .getItems()
        .addAll("Easy", "Medium", "Hard", "Impossible", "I'm into math!", "Reverse");
    difficultySelector.setValue("Easy");
    difficultySelector
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              updateDifficultyIndex(newVal);
            });
    for (int i = 1; i <= 3; i++) {
      initBooks(i, builtInHbox);
    }
    DefaultEventBus.getInstance().subscribe(StartGameEvent.class, this::onStartGame);
    addHoverEffects();
    addStackOfBooksAnimation();
  }

  /**
   * Initializes the data for the controller. Sets the player information and adjusts the UI based
   * on the current player.
   *
   * @param args the arguments passed for initialization.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void initData(Object... args) {
    this.playerInfos = (List<PlayerInfo>) args[0];
    String currPlayer = (String) args[1];
    String leaderPlayer = (String) args[2];

    GameManager gameManager = GameManager.getInstance();
    gameManager.setMainPlayer(leaderPlayer);
    gameManager.setCurrPlayer(currPlayer);

    if (!Objects.equals(currPlayer, leaderPlayer)) {
      builtInHbox.setDisable(true);
      customArea.setDisable(true);
      startButton.setDisable(true);
      difficultySelector.setDisable(true);
      startButton.setText("Wait...");
    }
  }

  /**
   * Completes the book selection process and publishes the selected sentences event.
   *
   * @param sentences the list of sentences loaded from the selected book or custom text.
   */
  @Override
  protected void completed(List<String> sentences) {
    DefaultEventBus.getInstance().publish(new SelectedEvent(sentences));
  }

  /**
   * Listener method for handling the start game event. Loads the game view and sets up the game
   * with the provided information.
   *
   * @param event the StartGameEvent containing game information and sentences.
   */
  @Listener
  protected void onStartGame(StartGameEvent event) {
    GameManager gameManager = GameManager.getInstance();
    Platform.runLater(
        () -> {
          try {
            gameManager.loadView(
                GameManager.MULTIPLE_GAMEPLAY_VIEW,
                event.getGameInfo(),
                playerInfos,
                event.getSentences());
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
    DefaultEventBus.getInstance().unsubscribe(StartGameEvent.class, this::onStartGame);
  }

  private void addHoverEffects() {
    addHoverEffect(startButton);
    for (var node : builtInHbox.getChildren()) {
      if (node instanceof Pane) {
        addHoverEffect((Pane) node);
      }
    }
  }

  private void addHoverEffect(Button button) {
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

  private void addHoverEffect(Pane pane) {
    ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(300), pane);
    scaleTransition.setFromX(1.0);
    scaleTransition.setFromY(1.0);
    scaleTransition.setToX(1.05);
    scaleTransition.setToY(1.05);
    scaleTransition.setAutoReverse(true);
    scaleTransition.setCycleCount(2);

    pane.setOnMouseEntered(event -> scaleTransition.play());
    pane.setOnMouseExited(event -> scaleTransition.stop());
  }

  private void addStackOfBooksAnimation() {
    ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(1000), booksImage);
    scaleTransition.setFromX(1.0);
    scaleTransition.setFromY(1.0);
    scaleTransition.setToX(1.2);
    scaleTransition.setToY(1.2);
    scaleTransition.setAutoReverse(true);
    scaleTransition.setCycleCount(ScaleTransition.INDEFINITE);

    scaleTransition.play();
  }
}
