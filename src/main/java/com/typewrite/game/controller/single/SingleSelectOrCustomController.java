package com.typewrite.game.controller.single;

import com.typewrite.game.GameManager;
import com.typewrite.game.controller.AbstractSelectOrCustomController;
import com.typewrite.game.util.AudioUtil;
import com.typewrite.game.util.LogUtil;
import com.typewrite.game.util.TextUtil;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.ScaleTransition;
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
 * Controller for handling single player select or custom game settings. Manages the UI for
 * selecting built-in books or entering custom text.
 */
public class SingleSelectOrCustomController extends AbstractSelectOrCustomController
    implements Initializable {

  @FXML protected HBox builtInHbox;
  @FXML protected TextArea customArea;
  @FXML private ComboBox<String> difficultySelector;
  @FXML private Button startButton;
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
      nextView(sentences);
    } catch (Exception e) {
      LogUtil.error("onCustomStartClick: Error loading sentences: " + e.getMessage());
      throw new RuntimeException(e);
    }
  }

  /**
   * Initializes the controller. Sets up the difficulty selector and book options.
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

    startButton.setOnMouseEntered(event -> AudioUtil.playHoverSoundEffect());
    addHoverEffects();
    addStackOfBooksAnimation();
  }

  /**
   * Completes the book selection process and navigates to the next view.
   *
   * @param sentences the list of sentences loaded from the selected book or custom text.
   */
  @Override
  protected void completed(List<String> sentences) {
    nextView(sentences);
  }

  /**
   * Loads the next view with the provided sentences.
   *
   * @param sentences the list of sentences to pass to the next view.
   */
  private void nextView(List<String> sentences) {
    try {
      GameManager.getInstance().loadView(GameManager.SINGLE_GAMEPLAY_VIEW, sentences);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /** Adds hover effects to the startButton and book cards. */
  private void addHoverEffects() {
    addHoverEffect(startButton);
    for (var node : builtInHbox.getChildren()) {
      if (node instanceof Pane) {
        addHoverEffect((Pane) node);
      }
    }
  }

  /**
   * Adds a hover effect to the specified button.
   *
   * @param button the button to add the hover effect to.
   */
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

  /**
   * Adds a hover effect to the specified pane (book card).
   *
   * @param pane the pane to add the hover effect to.
   */
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
