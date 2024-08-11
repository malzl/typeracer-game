package com.typewrite.game;

import com.typewrite.game.common.enums.LogLevel;
import com.typewrite.game.controller.InitDataController;
import com.typewrite.game.util.LogUtil;
import com.typewrite.game.util.ThemeManager;
import java.io.IOException;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/** Manages the game, including loading views and managing the primary stage. */
public class GameManager {

  private static final GameManager instance = new GameManager();

  /**
   * Gets the singleton instance of the GameManager.
   *
   * @return the singleton instance of the GameManager.
   */
  public static synchronized GameManager getInstance() {
    return instance;
  }

  private GameManager() {}

  public static final double CANVAS_WIDTH = 500;
  public static final double CANVAS_HEIGHT = 220;

  public static final Map<Integer, String> BOOK_MAP =
      Map.of(
          1, "War and Peace \n(in English)",
          2, "Classic Poems \n(in German)",
          3, "Famous Song Lyrics (in English)");

  public static final String TITLE = "Typeracer";
  public static final LogLevel LOG_LEVEL = LogLevel.INFO;

  public static final String HOME_VIEW_NAME = "home-view";
  public static final String CLIENT_VIEW_NAME = "client-view";
  public static final String LOBBY_VIEW_NAME = "lobby-view";
  public static final String SINGLE_INPUT_VIEW = "single/single-input-name-view";
  public static final String SINGLE_SELECTOR_CUSTOM_VIEW = "single/single-select-or-custom-view";
  public static final String SINGLE_GAMEPLAY_VIEW = "single/single-game-play-view";
  public static final String MULTIPLE_GAMEPLAY_VIEW = "multiple/multiple-game-play-view";
  public static final String MULTIPLE_SELECTOR_CUSTOM_VIEW =
      "multiple/multiple-select-or-custom-view";
  public static final String MULTIPLE_RANK_VIEW = "multiple/rank-view";

  private Stage gameStage;
  private String mainPlayer;
  private String currPlayer;

  /**
   * Loads a view by its name. If special operations are needed, implement the {@link
   * javafx.fxml.Initializable} interface and override the {@link
   * javafx.fxml.Initializable#initialize} method.
   *
   * @param viewName the name of the view to load.
   * @throws IOException if an I/O error occurs.
   */
  public void loadView(String viewName) throws IOException {
    loadView(viewName, (Object) null);
  }

  /**
   * Loads a view by its name with additional arguments. If special operations are needed, implement
   * the {@link javafx.fxml.Initializable} interface and override the {@link
   * javafx.fxml.Initializable#initialize} method.
   *
   * @param viewName the name of the view to load.
   * @param args the arguments to pass for initialization. Implement {@link InitDataController} for
   *     controllers requiring data initialization.
   * @throws IOException if an I/O error occurs.
   */
  public void loadView(String viewName, Object... args) throws IOException {
    LogUtil.info("GameManager: Loading view " + viewName);
    FXMLLoader fxmlLoader = new FXMLLoader(GameApp.class.getResource(viewName + ".fxml"));

    LogUtil.info("GameManager: View loaded, setting scene to stage");
    Pane root = fxmlLoader.load();
    ThemeManager.applyCurrentTheme(root); // Apply the current theme

    if (args == null || args[0] == null) {
      Scene scene = new Scene(root);
      this.gameStage.setScene(scene);
      return;
    }

    InitDataController controller = fxmlLoader.getController();
    LogUtil.info("GameManager: View already set up, In initialization data");
    controller.initData(args);
    this.gameStage.setScene(new Scene(root));
  }

  /**
   * Gets the primary game stage.
   *
   * @return the primary game stage.
   */
  public Stage getGameStage() {
    return gameStage;
  }

  /**
   * Sets the primary game stage.
   *
   * @param gameStage the primary game stage to set.
   */
  public void setGameStage(Stage gameStage) {
    this.gameStage = gameStage;
  }

  /**
   * Gets the main player.
   *
   * @return the main player.
   */
  public String getMainPlayer() {
    return mainPlayer;
  }

  /**
   * Sets the main player.
   *
   * @param mainPlayer the main player to set.
   */
  public void setMainPlayer(String mainPlayer) {
    this.mainPlayer = mainPlayer;
  }

  /**
   * Gets the current player.
   *
   * @return the current player.
   */
  public String getCurrPlayer() {
    return currPlayer;
  }

  /**
   * Sets the current player.
   *
   * @param currPlayer the current player to set.
   */
  public void setCurrPlayer(String currPlayer) {
    this.currPlayer = currPlayer;
  }
}
