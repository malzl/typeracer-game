package com.typewrite.game.controller.single;

import static com.typewrite.game.util.AudioUtil.playBackgroundMusic;

import com.typewrite.game.GameManager;
import com.typewrite.game.common.event.DefaultEventBus;
import com.typewrite.game.common.event.events.EmojiEvent;
import com.typewrite.game.common.event.events.GameFinishedEvent;
import com.typewrite.game.common.event.events.SelectedEvent;
import com.typewrite.game.common.event.events.SendEmojiEvent;
import com.typewrite.game.common.event.events.StartGameEvent;
import com.typewrite.game.common.event.events.SyncEvent;
import com.typewrite.game.common.event.events.UpdateEvent;
import com.typewrite.game.util.AudioUtil;
import com.typewrite.game.util.LogUtil;
import com.typewrite.game.util.ThemeManager;
import java.io.IOException;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * Controller for the username-input screen in the SinglePlayer Typewrite game. This controller
 * handles the input of the player's name and adds hover effects to the buttons.
 */
public class SingleInputNameController {

  @FXML protected TextField userNameField;
  @FXML private Button goButton;
  @FXML private AnchorPane rootPane;
  @FXML private Button backToHomeButton;

  /**
   * Navigates back to the home screen when triggered. Loads the home view.
   *
   * @throws IOException if an error occurs while loading the home view.
   */
  @FXML
  protected void toHome() throws IOException {
    LogUtil.info("Back to home page!");
    GameManager.getInstance().loadView(GameManager.HOME_VIEW_NAME);
    playBackgroundMusic();

    DefaultEventBus.getInstance().unsubscribeAll(SyncEvent.class);
    DefaultEventBus.getInstance().unsubscribeAll(UpdateEvent.class);
    DefaultEventBus.getInstance().unsubscribeAll(SelectedEvent.class);
    DefaultEventBus.getInstance().unsubscribeAll(StartGameEvent.class);
    DefaultEventBus.getInstance().unsubscribeAll(GameFinishedEvent.class);
    DefaultEventBus.getInstance().unsubscribeAll(SendEmojiEvent.class);
    DefaultEventBus.getInstance().unsubscribeAll(EmojiEvent.class);
  }

  /**
   * Handles the action when the "Go" button is clicked. Checks if the username field is not empty,
   * sets the main player name, and loads the next view.
   *
   * @throws IOException if an I/O error occurs.
   */
  @FXML
  protected void onInputNameClick() throws IOException {
    AudioUtil.playClickSoundEffect();
    String username = userNameField.getText();
    if (username == null || username.trim().isEmpty()) {
      return;
    }
    System.out.println("The player's name is " + username);
    GameManager.getInstance().setMainPlayer(username);
    GameManager.getInstance().loadView(GameManager.SINGLE_SELECTOR_CUSTOM_VIEW);
  }

  /**
   * Handles the action when "ENTER" key is hit after writing username.
   *
   * @param event the user pressing the ENTER key.
   */
  @FXML
  protected void handleEnterPressed(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER) {
      try {
        onInputNameClick();
      } catch (IOException e) {
        LogUtil.error("Error with username input upon pressing ENTER key.");
      }
      event.consume();
    }
  }

  @FXML
  private void initialize() {
    ThemeManager.applyCurrentTheme(rootPane); // Apply the current theme
    addHoverEffects();
  }

  private void addHoverEffects() {
    addHoverEffect(goButton);
    addHoverEffect(backToHomeButton);
  }

  private void addHoverEffect(Button button) {
    ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(300), button);
    scaleTransition.setFromX(1.0);
    scaleTransition.setFromY(1.0);
    scaleTransition.setToX(1.1);
    scaleTransition.setToY(1.1);
    scaleTransition.setAutoReverse(true);
    scaleTransition.setCycleCount(2);

    goButton.setOnMouseEntered(event -> AudioUtil.playHoverSoundEffect());

    button.setOnMouseEntered(event -> scaleTransition.play());
    button.setOnMouseExited(event -> scaleTransition.stop());
  }
}
