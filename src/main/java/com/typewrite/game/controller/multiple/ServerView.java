package com.typewrite.game.controller.multiple;

import com.typewrite.game.GameApp;
import com.typewrite.game.network.server.Server;
import com.typewrite.game.util.ThemeManager;
import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/** Controller class for managing the server view UI. */
public class ServerView {

  public static final String SERVER_VIEW_NAME = "server-view";
  public static final String TITLE = "Server Typeracer";
  public static final double GAME_WIDTH = 1280;
  public static final double GAME_HEIGHT = 720;

  private Stage gameStage;
  private Server server;

  private static final ServerView instance = new ServerView();

  /**
   * Sets the primary stage of the application.
   *
   * @param gameStage the primary stage to set
   */
  public void setGameStage(Stage gameStage) {
    this.gameStage = gameStage;
  }

  @FXML private Button startButton;
  @FXML private Button stopButton;
  @FXML private Label messageLabel;
  @FXML private TextField portField;
  @FXML private ImageView serverImage;
  @FXML private ImageView typewriterImage;
  @FXML private Button toggleDarkModeButton;
  @FXML private AnchorPane rootPane;

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

  /**
   * Retrieves the singleton instance of ServerView.
   *
   * @return the ServerView instance
   */
  public static synchronized ServerView getInstance() {
    return instance;
  }

  /**
   * Loads the specified view into the game stage.
   *
   * @param viewName the name of the FXML view to load
   * @throws IOException if an error occurs during loading
   */
  public void loadView(String viewName) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(GameApp.class.getResource(viewName + ".fxml"));
    Scene scene = new Scene(fxmlLoader.load(), ServerView.GAME_WIDTH, ServerView.GAME_HEIGHT);
    this.gameStage.setScene(scene);
  }

  @FXML
  private void initialize() {
    startButton.setOnAction(
        event -> {
          try {
            handleStartButtonAction();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });

    stopButton.setOnAction(
        event -> {
          try {
            handleStopButtonAction();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
    animateImages();
    addHoverEffects();
  }

  @FXML
  private void handleStartButtonAction() throws IOException {
    try {
      int port = Integer.parseInt(portField.getText());
      server = new Server(port);

      new Thread(
              () -> {
                try {
                  server.start();
                } catch (IOException e) {
                  Platform.runLater(() -> logToLabel("Server error: " + e.getMessage()));
                }
              })
          .start();

      logToLabel("Server started successfully.");
      logToLabel("Server is running on port " + port);
      stopButton.setDisable(false);
      startButton.setDisable(true);
      stopButton.getStyleClass().remove("button-disabled");
    } catch (NumberFormatException e) {
      logToLabel("Invalid port number!");
    }
  }

  @FXML
  private void handleStopButtonAction() throws IOException {
    if (server != null) {
      new Thread(
              () -> {
                server.stop();
                Platform.runLater(
                    () -> {
                      logToLabel("Server stopped successfully.");
                      stopButton.setDisable(true);
                      startButton.setDisable(false);
                    });
              })
          .start();
    }
  }

  /**
   * Logs a message to the message label on the UI.
   *
   * @param message the message to log
   */
  private void logToLabel(String message) {
    Platform.runLater(
        () -> {
          messageLabel.setText(message);
          System.out.println(message);
        });
  }

  private void animateImages() {
    // Animate the server image
    TranslateTransition translateTransition =
        new TranslateTransition(Duration.seconds(2), serverImage);
    translateTransition.setFromX(-50);
    translateTransition.setToX(0);
    translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
    translateTransition.setAutoReverse(true);
    translateTransition.play();

    RotateTransition rotateTransition = new RotateTransition(Duration.seconds(2), serverImage);
    rotateTransition.setByAngle(20);
    rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
    rotateTransition.setAutoReverse(true);
    rotateTransition.play();

    // Animate the typewriter image
    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3), typewriterImage);
    fadeTransition.setFromValue(0);
    fadeTransition.setToValue(1);
    fadeTransition.setCycleCount(FadeTransition.INDEFINITE);
    fadeTransition.setAutoReverse(true);
    fadeTransition.play();
  }

  private void addHoverEffects() {
    addHoverEffect(startButton);
    addHoverEffect(stopButton);
    addHoverEffect(toggleDarkModeButton);
  }

  /**
   * Adds a hover effect to the specified button.
   *
   * @param button the button to add the hover effect to
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
}
