package com.typewrite.game.controller;

import com.typewrite.game.GameManager;
import com.typewrite.game.controller.multiple.ClientView;
import com.typewrite.game.util.AudioUtil;
import com.typewrite.game.util.LogUtil;
import com.typewrite.game.util.ThemeManager;
import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Controller class for the home view of the Typewrite game application. Manages user interactions
 * and animations on the home screen.
 */
public class HomeController {

  @FXML private AnchorPane rootPane;
  @FXML private Button toggleSfxButton;
  @FXML private Button toggleMusicButton;
  @FXML private ImageView racingcarImage;
  @FXML private ImageView targetImage;
  @FXML private ImageView typewriterImage;
  @FXML private Text welcomeText;
  @FXML private Text typeracerText;
  @FXML private Button singlePlayButton;
  @FXML private Button multiplePlayButton;
  @FXML private Button toggleDarkModeButton;

  /**
   * Turns on dark mode upon pressing the dark mode button.
   *
   * @param actionEvent the button clicked event in the FXML.
   */
  @FXML
  public void toggleDarkMode(ActionEvent actionEvent) {
    try {
      String lightModeCss = ThemeManager.getLightModeCss();
      String darkModeCss = ThemeManager.getDarkModeCss();

      if (ThemeManager.isDarkMode()) {
        rootPane.getStylesheets().remove(darkModeCss);
        rootPane.getStylesheets().add(lightModeCss);
      } else {
        rootPane.getStylesheets().remove(lightModeCss);
        rootPane.getStylesheets().add(darkModeCss);
      }
      ThemeManager.toggleDarkMode();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void initialize() {
    animateRacingCar();
    animateText(welcomeText, 1000, 0);
    animateText(typeracerText, 1000, 0);
    animateImageViews();
    addHoverEffects();
  }

  /**
   * Handles the click event for the Single Player button. Loads the single player view.
   *
   * @throws IOException if the view cannot be loaded.
   */
  @FXML
  protected void onSinglePlayClick() throws IOException {
    LogUtil.info("Single Player");
    AudioUtil.playClickSoundEffect();
    GameManager.getInstance().loadView(GameManager.SINGLE_INPUT_VIEW);
  }

  /**
   * Handles the click event for the Multiple Player button. Loads the multiple player (client)
   * view.
   *
   * @throws IOException if the view cannot be loaded.
   */
  @FXML
  protected void onMultiplePlayClick() throws IOException {
    LogUtil.info("HomeView: onMultiplePlayClick");
    AudioUtil.playClickSoundEffect();
    GameManager.getInstance().loadView(GameManager.CLIENT_VIEW_NAME);
    GameManager.getInstance().getGameStage().setTitle(ClientView.TITLE);
    LogUtil.info("HomeView: Passed main stage to ClientView");
    GameManager.getInstance().getGameStage().sizeToScene();
  }

  @FXML
  private void toggleMusic() {
    if (AudioUtil.isMusicPlaying()) {
      AudioUtil.stopMusic();
      toggleMusicButton.setText("Music Off");
      toggleMusicButton.setStyle(
          "-fx-background-color: grey; -fx-text-fill: white; "
              + "-fx-font-size: 24;"
              + "-fx-background-radius: 15; "
              + "-fx-effect: dropshadow(gaussian, black, 10, 0.5, 0, 0);");
    } else {
      AudioUtil.playBackgroundMusic();
      toggleMusicButton.setText("Music On");
      toggleMusicButton.setStyle(
          "-fx-background-color: #bc43e5; -fx-text-fill: white; "
              + "-fx-font-size: 24; -fx-background-radius: 15; "
              + "-fx-effect: dropshadow(gaussian, black, 10, 0.5, 0, 0);");
    }
  }

  /**
   * Controller method to toggle sound effects on/off.
   *
   * @param actionEvent the ActionEvent representing the user's action
   */
  @FXML
  public void toggleSfx(ActionEvent actionEvent) {
    if (AudioUtil.isSoundEffect()) {
      AudioUtil.setSoundEffect(false);
      toggleSfxButton.setText("SFX Off");
    } else {
      AudioUtil.setSoundEffect(true);
      toggleSfxButton.setText("SFX On");
    }
  }

  private void animateRacingCar() {
    if (racingcarImage != null) {
      double sceneWidth = 1280;
      TranslateTransition transition = new TranslateTransition();
      transition.setNode(racingcarImage);
      transition.setDuration(Duration.seconds(4));
      transition.setFromX(sceneWidth);
      transition.setToX(-sceneWidth);
      transition.setCycleCount(TranslateTransition.INDEFINITE);
      transition.setAutoReverse(false);
      transition.play();
    } else {
      LogUtil.error("racingcarImage is not initialized");
    }
  }

  private void animateText(Text text, double distance, int delay) {
    if (text != null) {
      TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), text);
      translateTransition.setFromX(-distance);
      translateTransition.setToX(0);
      translateTransition.setDelay(Duration.millis(delay));

      FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), text);
      fadeTransition.setFromValue(0);
      fadeTransition.setToValue(1);
      fadeTransition.setDelay(Duration.millis(delay));

      translateTransition.play();
      fadeTransition.play();
    } else {
      System.err.println("Text is not initialized");
    }
  }

  private void animateImageViews() {
    rotateImageView(targetImage, -20, Duration.seconds(1), RotateTransition.INDEFINITE);
    rotateImageView(typewriterImage, 20, Duration.seconds(1), RotateTransition.INDEFINITE);
  }

  private void rotateImageView(
      ImageView imageView, double angle, Duration duration, int cycleCount) {
    RotateTransition rotateTransition = new RotateTransition(duration, imageView);
    rotateTransition.setByAngle(angle);
    rotateTransition.setCycleCount(cycleCount);
    rotateTransition.setAutoReverse(true);
    rotateTransition.play();
  }

  private void addHoverEffects() {
    addHoverEffect(singlePlayButton);
    addHoverEffect(multiplePlayButton);
    addHoverEffect(toggleMusicButton);
    addHoverEffect(toggleSfxButton);
    addHoverEffect(toggleDarkModeButton);
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

    button.setOnMouseEntered(
        event -> {
          scaleTransition.play();
          AudioUtil.playHoverSoundEffect();
        });
    button.setOnMouseExited(event -> scaleTransition.stop());
  }
}
