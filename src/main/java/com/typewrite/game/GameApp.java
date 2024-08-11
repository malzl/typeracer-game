package com.typewrite.game;

import com.typewrite.game.util.AudioUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/** The main application class for the Typewrite game. */
public class GameApp extends Application {

  /**
   * The main entry point for the application.
   *
   * @param args the command line arguments.
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Starts the JavaFX application.
   *
   * @param stage the primary stage for this application.
   * @throws Exception if an error occurs during startup.
   */
  @Override
  public void start(Stage stage) throws Exception {
    GameManager gameManager = GameManager.getInstance();
    gameManager.setGameStage(stage);
    gameManager.loadView(GameManager.HOME_VIEW_NAME);
    stage.setTitle(GameManager.TITLE);

    stage.setResizable(false);
    stage.setOnCloseRequest(t -> Platform.exit());

    AudioUtil.playBackgroundMusic();

    Platform.runLater(
        () -> {
          stage.show();
          stage.requestFocus();
        });
  }
}
