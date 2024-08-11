package com.typewrite.game;

import com.typewrite.game.controller.multiple.ServerView;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/** The main application class for the server side of the Typewrite game. */
public class ServerApp extends Application {

  /**
   * The main entry point for the server application.
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
   * @throws IOException if an error occurs during startup.
   */
  @Override
  public void start(Stage stage) throws IOException {
    ServerView serverView = ServerView.getInstance();
    serverView.setGameStage(stage);

    serverView.loadView(ServerView.SERVER_VIEW_NAME);
    stage.setTitle(ServerView.TITLE);

    stage.setResizable(false);
    stage.setOnCloseRequest(t -> Platform.exit());

    Platform.runLater(
        () -> {
          stage.show();
          stage.requestFocus();
        });
  }
}
