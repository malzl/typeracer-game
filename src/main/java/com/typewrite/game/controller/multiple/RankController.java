package com.typewrite.game.controller.multiple;

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
import com.typewrite.game.controller.InitDataController;
import com.typewrite.game.network.client.Client;
import com.typewrite.game.network.server.PlayerInfo;
import com.typewrite.game.util.LogUtil;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * Controller for the ranking screen in the game. Displays player rankings and allows navigation
 * back to the home screen.
 */
public class RankController implements Initializable, InitDataController {

  @FXML private AnchorPane player1;
  @FXML private AnchorPane player2;
  @FXML private AnchorPane player3;
  @FXML private AnchorPane player4;
  @FXML private Button backToHomeButton;
  @FXML private ImageView racingCarImage;

  private List<AnchorPane> players;

  /**
   * Navigates back to the home screen when triggered. Closes the client connection and loads the
   * home view.
   *
   * @throws IOException if an error occurs while loading the home view.
   */
  @FXML
  protected void toHome() throws IOException {
    LogUtil.info("Back to home page!");
    Client.getInstance().closeConnection();
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
   * Initializes data by sorting the list of players based on their current text size in descending
   * order, and updates the UI components with player information.
   *
   * @param args the arguments passed for initialization, expecting a list of PlayerInfo.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void initData(Object... args) {
    List<PlayerInfo> playerInfos = (List<PlayerInfo>) args[0];

    playerInfos =
        playerInfos.stream()
            .sorted(Comparator.comparingInt(PlayerInfo::getCurrTextSize).reversed())
            .toList();
    for (int idx = 0; idx < playerInfos.size(); idx++) {
      AnchorPane anchorPane = players.get(idx);
      PlayerInfo playerInfo = playerInfos.get(idx);

      anchorPane.setDisable(false);
      anchorPane.setVisible(true);

      Label name = (Label) (anchorPane.getChildren().get(1));
      Label wpm = (Label) (anchorPane.getChildren().get(2));
      ImageView avatar = (ImageView) (anchorPane.getChildren().get(4));

      String playerName = playerInfo.getName();
      String avatarPath = "/com/typewrite/game/avatars/" + playerInfo.getSelectedAvatar() + ".png";

      Image image = new Image(getClass().getResourceAsStream(avatarPath));
      avatar.setImage(image);
      Label ranking = (Label) (anchorPane.getChildren().get(3));

      name.setText(playerName);
      wpm.setText(playerInfo.getWpm());
      ranking.setText("NO." + (idx + 1));
    }
  }

  /**
   * Initializes the controller by setting up the player panes as disabled and invisible initially.
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
    addHoverEffects();
    animateRacingCar();
  }

  private void addHoverEffects() {
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

    button.setOnMouseEntered(event -> scaleTransition.play());
    button.setOnMouseExited(event -> scaleTransition.stop());
  }

  private void animateRacingCar() {
    if (racingCarImage != null) {
      double sceneWidth = 1280;
      TranslateTransition transition = new TranslateTransition();
      transition.setNode(racingCarImage);
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
}
