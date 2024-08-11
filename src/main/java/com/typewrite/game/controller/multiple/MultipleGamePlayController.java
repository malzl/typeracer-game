package com.typewrite.game.controller.multiple;

import static com.typewrite.game.util.AudioUtil.playExplosionSoundEffect;
import static com.typewrite.game.util.AudioUtil.stopMusic;

import com.typewrite.game.GameManager;
import com.typewrite.game.Refresh;
import com.typewrite.game.Resources;
import com.typewrite.game.common.enums.EmojiType;
import com.typewrite.game.common.event.DefaultEventBus;
import com.typewrite.game.common.event.Listener;
import com.typewrite.game.common.event.events.EmojiEvent;
import com.typewrite.game.common.event.events.GameFinishedEvent;
import com.typewrite.game.common.event.events.SendEmojiEvent;
import com.typewrite.game.common.event.events.SyncEvent;
import com.typewrite.game.common.event.events.UpdateEvent;
import com.typewrite.game.controller.AbstractGamePlayerController;
import com.typewrite.game.network.server.GameInfo;
import com.typewrite.game.network.server.PlayerInfo;
import com.typewrite.game.util.LogUtil;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Controller for managing the multiple player game play screen. Handles game updates, player
 * information, and events.
 */
public class MultipleGamePlayController extends AbstractGamePlayerController {

  @FXML private StackPane parentStack;

  @FXML private AnchorPane kickTipPane;
  @FXML private StackPane contentStack;
  @FXML private Canvas canvas;
  private GraphicsContext ctx;
  @FXML private TextArea textArea;
  @FXML private Label time;
  @FXML private Label host;
  @FXML private Label tickOutTime;

  @FXML private AnchorPane player1;
  @FXML private AnchorPane player2;
  @FXML private AnchorPane player3;
  @FXML private AnchorPane player4;
  @FXML private Button bomb;

  private long lastClickTime = 0L;
  private long lastActivityTime = 0L;
  private List<AnchorPane> players;

  /**
   * Initializes the controller. Sets up player panes, canvas context, refresh mechanism, and event
   * subscriptions.
   *
   * @param url the location used to resolve relative paths for the root object, or null if not
   *     known.
   * @param resourceBundle the resources used to localize the root object, or null if not localized.
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    players = List.of(player1, player2, player3, player4);
    players.forEach(
        player -> {
          player.setVisible(false);
          player.setDisable(true);
        });
    ctx = canvas.getGraphicsContext2D();
    refresh = new Refresh(this::update);

    DefaultEventBus.getInstance().subscribe(SyncEvent.class, this::onSyncEvent);
    DefaultEventBus.getInstance().subscribe(GameFinishedEvent.class, this::onGameFinishedEvent);
    DefaultEventBus.getInstance().subscribe(EmojiEvent.class, this::onEmojiEvent);

    textArea.setOnKeyPressed(this::handlerKeyActivityEvent);
    textArea.setOnKeyReleased(this::handlerKeyActivityEvent);

    bomb.setOnAction(
        event -> {
          if (System.currentTimeMillis() - lastClickTime < 5000) {
            return;
          }
          DefaultEventBus.getInstance()
              .publish(
                  new SendEmojiEvent(GameManager.getInstance().getCurrPlayer(), EmojiType.BOMB));
          lastClickTime = System.currentTimeMillis();
        });
    lastActivityTime = System.currentTimeMillis();
    tickOutTime.setVisible(false);
    kickTipPane.setDisable(true);
    kickTipPane.setVisible(false);
  }

  /**
   * Initializes the data for the controller. Sets up game information, player information, and
   * sentences.
   *
   * @param args - GameInfo, PlayerInfo, and sentences list.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void initData(Object... args) {
    GameInfo gameInfo = (GameInfo) args[0];
    List<PlayerInfo> playerInfos = (List<PlayerInfo>) args[1];
    List<String> sentences = (List<String>) args[2];

    setSentences(sentences);

    updateGameInfo(gameInfo);
    updatePlayerInfo(playerInfos);
    start();
  }

  /** Starts the game, stopping background music and starting the refresh mechanism. */
  @Override
  public void start() {
    stopMusic();
    refresh.start();
  }

  /** Stops the game by stopping the refresh mechanism. */
  @Override
  public void stop() {
    refresh.stop();
  }

  /**
   * Updates the game state. Draws text on the canvas, checks input, and publishes player updates.
   *
   * @param timer the current timer value.
   */
  @Override
  protected void update(long timer) {
    LogUtil.debug("Update called at: " + System.nanoTime() + ", with timer: " + timer);
    String avatar = ClientView.getInstance().getCurrentImage();
    PlayerInfo playerInfo = new PlayerInfo(GameManager.getInstance().getCurrPlayer(), avatar);
    playerInfo.setLastActivityTime(lastActivityTime);

    if (System.currentTimeMillis() - lastActivityTime >= 20000) {
      tickOutTime.setVisible(true);
      if (System.currentTimeMillis() - lastActivityTime >= 30000) {
        playerInfo.setKickOut(true);
        textArea.setDisable(true);
        stop();
      }
    } else {
      tickOutTime.setVisible(false);
    }

    // draw
    if (currentStr == null || playerInfo.getFinished()) {
      return;
    }
    String inputText = textArea.getText();
    char[] charArray = currentStr.toCharArray();
    char[] inputArray;
    if (inputText != null) {
      inputArray = inputText.toCharArray();
    } else {
      inputArray = new char[0];
    }

    clearCanvas();
    inputArray = drawText(charArray, inputArray, inputText);

    playerInfo.setCurrTextSize(inputLength + inputArray.length);

    if (!hasNextSentence(inputArray, charArray)) {
      playerInfo.setFinished(true);
      textArea.setDisable(true);
      stop();
    }
    DefaultEventBus.getInstance().publish(new UpdateEvent(playerInfo));
  }

  @Override
  protected void updateGameUi(long timer, char[] inputArray) {}

  @Override
  protected GraphicsContext getCtx() {
    return ctx;
  }

  @Override
  protected TextArea getTextArea() {
    return textArea;
  }

  /**
   * Listener for handling synchronization events. Updates the game and player information.
   *
   * @param event the SyncEvent containing updated game and player information.
   */
  @Listener
  private void onSyncEvent(SyncEvent event) {
    Platform.runLater(
        () -> {
          updateGameInfo(event.getGameInfo());
          updatePlayerInfo(event.getPlayerInfoList());
        });
  }

  /**
   * Listener for handling game finished events. Loads the ranking view and unsubscribes from
   * events.
   *
   * @param event the GameFinishedEvent containing player information.
   */
  @Listener
  private void onGameFinishedEvent(GameFinishedEvent event) {
    GameManager gameManager = GameManager.getInstance();
    Platform.runLater(
        () -> {
          try {
            stop();
            gameManager.loadView(GameManager.MULTIPLE_RANK_VIEW, event.getPlayerInfos());
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
    DefaultEventBus.getInstance().unsubscribe(SyncEvent.class, this::onSyncEvent);
    DefaultEventBus.getInstance().unsubscribe(GameFinishedEvent.class, this::onGameFinishedEvent);
    DefaultEventBus.getInstance().unsubscribe(EmojiEvent.class, this::onEmojiEvent);

    textArea.setOnKeyPressed(null);
    textArea.setOnKeyReleased(null);
  }

  /**
   * Listener for handling emoji events. Displays the emoji on the screen and plays the sound
   * effect.
   *
   * @param event the EmojiEvent containing the emoji information.
   */
  @Listener
  private void onEmojiEvent(EmojiEvent event) {
    if (Objects.equals(event.getSender(), GameManager.getInstance().getCurrPlayer())) {
      return;
    }
    Platform.runLater(
        () -> {
          Image gifImage = new Image(Resources.getResourceAsStream(event.getType().getPath()));
          ImageView imageView = new ImageView(gifImage);
          imageView.setPreserveRatio(true);
          imageView.setFitWidth(contentStack.getWidth());
          imageView.setFitHeight(contentStack.getHeight());
          contentStack.getChildren().add(imageView);
          playExplosionSoundEffect();

          Timer timer = new Timer();
          timer.schedule(
              new TimerTask() {
                @Override
                public void run() {
                  timer.cancel();
                  Platform.runLater(() -> contentStack.getChildren().remove(imageView));
                }
              },
              600);
        });
  }

  /**
   * Updates the game information on the UI.
   *
   * @param gameInfo the GameInfo containing the game details.
   */
  private void updateGameInfo(GameInfo gameInfo) {
    host.setText(gameInfo.getHost() + ":" + gameInfo.getPort());
    DecimalFormat df = new DecimalFormat("#.##");
    String formatTime = df.format((gameInfo.getCurrTime() - gameInfo.getStartTime()) / 1000.0);
    time.setText(formatTime + " S");

    tickOutTime.setText("Inactive " + (System.currentTimeMillis() - lastActivityTime) / 1000 + "S");
  }

  /**
   * Updates the player information on the UI.
   *
   * @param playerInfos the list of PlayerInfo objects containing player details.
   */
  private void updatePlayerInfo(List<PlayerInfo> playerInfos) {
    for (int idx = 0; idx < playerInfos.size(); idx++) {
      AnchorPane anchorPane = players.get(idx);
      PlayerInfo playerInfo = playerInfos.get(idx);

      anchorPane.setVisible(true);
      anchorPane.setDisable(false);

      ImageView avatarView = new ImageView();
      Image avatarImage =
          new Image("/com/typewrite/game/avatars/" + playerInfo.getSelectedAvatar() + ".png");
      avatarView.setImage(avatarImage);
      avatarView.setFitWidth(100);
      avatarView.setFitHeight(80);

      anchorPane.getChildren().set(0, avatarView);

      Rectangle progress = (Rectangle) (anchorPane.getChildren().get(5));

      Label wpm = (Label) (anchorPane.getChildren().get(8));

      wpm.setText(playerInfo.getWpm());
      progress.setFill(Color.rgb(12, 152, 29));
      progress.setWidth(275 * ((double) playerInfo.getCurrTextSize() / totalSize));
      players.get(idx).getStyleClass().add("current-player");

      if (playerInfo.getFinished()) {
        progress.setWidth(275);
        wpm.setText(wpm.getText() + " Completed!");
        players.get(idx).getStyleClass().add("current-player");
      } else if (playerInfo.getKickOut()) {
        tickOutTime.setVisible(false);
        if (Objects.equals(playerInfo.getName(), GameManager.getInstance().getCurrPlayer())) {
          kickTipPane.setDisable(false);
          kickTipPane.setVisible(true);
        } else {
          progress.setFill(Color.rgb(130, 130, 130));
          ColorAdjust colorAdjust = new ColorAdjust();
          colorAdjust.setBrightness(-0.5);
          avatarView.setEffect(colorAdjust);
          players.get(idx).getStyleClass().add("current-player");
        }
      } else if (!playerInfo.getOnline()) {
        progress.setFill(Color.rgb(143, 11, 11));
        players.get(idx).getStyleClass().add("current-player");
      }
      if (Objects.equals(playerInfo.getName(), GameManager.getInstance().getCurrPlayer())) {
        players.get(idx).getStyleClass().add("current-player");
      }
      String playerName = playerInfo.getName();
      Label name = (Label) (anchorPane.getChildren().get(7));
      name.setText(playerName);
    }
  }

  private void handlerKeyActivityEvent(KeyEvent keyEvent) {
    lastActivityTime = System.currentTimeMillis();
  }
}
