package com.typewrite.game.controller.multiple.lobby;

import com.typewrite.game.common.event.DefaultEventBus;
import com.typewrite.game.common.event.events.SyncEvent;
import com.typewrite.game.controller.multiple.lobby.chat.ChatManager;
import com.typewrite.game.controller.multiple.lobby.chat.ClientBox;
import com.typewrite.game.network.server.PlayerInfo;
import com.typewrite.game.util.AudioUtil;
import com.typewrite.game.util.LogUtil;
import java.util.List;
import java.util.Random;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Manages the lobby view for the multiplayer game, including player lists, chat, and UI
 * interactions.
 */
public class LobbyView {
  private static LobbyView instance;

  private PlayerListManager playerListManager;
  private ChatManager chatManager;

  /** Constructs a lobby view instance. */
  public LobbyView() {
    // Public constructor
    chatVbox = new VBox();
    playerListManager = new PlayerListManager();
    chatManager = new ChatManager();
  }

  /**
   * Gets the singleton instance of the LobbyView.
   *
   * @return the instance of LobbyView
   */
  public static synchronized LobbyView getInstance() {
    if (instance == null) {
      instance = new LobbyView();
    }
    return instance;
  }

  @FXML private Label titleLabel;
  @FXML private ScrollPane playersPane;
  @FXML private Label playersLabel;
  @FXML private Button readyButton;
  @FXML public AnchorPane chatAnchor;
  @FXML private VBox fxmlChatVbox;
  @FXML private Button musicButton;
  @FXML private Button sfxButton;

  // Create a new VBox instance to solve null error problem
  private static VBox chatVbox;

  @FXML private VBox playersBox;
  @FXML private TextField chatTextField;
  @FXML private Button messageSend;
  @FXML private Button addPlayer;

  private boolean isInitialized = false;
  private String selectedAvatar = "1";

  /** Initializes the lobby view. */
  @FXML
  private void initialize() {
    instantiateChatBox();
    addEnterKeyEventHandler();
    addChatBoxListener();
    addHoverEffects();

    readyButton.setOnAction(event -> handleReadyButtonAction());
    messageSend.setOnAction(event -> handleMessageSendButtonAction());

    DefaultEventBus.getInstance().subscribe(SyncEvent.class, this::onSyncEventHandler);
    isInitialized = true;
  }

  /** Handles the action when the ready button is clicked. */
  @FXML
  private void handleReadyButtonAction() {
    AudioUtil.playClickSoundEffect();
    chatManager.handleButtonReadyAction(readyButton);
  }

  /** Sends a message upon pressing the send button in the lobby chat. */
  @FXML
  public void handleMessageSendButtonAction() {
    LogUtil.info("Send button pressed.");
    createOwnMessageBox();
    sendTextMessageToServer();
    chatTextField.clear();
  }

  /**
   * Handles the SyncEvent to update the player list in the lobby.
   *
   * @param event the SyncEvent containing player information
   */
  public void onSyncEventHandler(SyncEvent event) {
    Random random = new Random();

    Platform.runLater(
        () -> {
          if (random.nextInt(10) == 0) {
            List<PlayerInfo> playerInfoList = event.getPlayerInfoList();
            LogUtil.info("Received SyncEvent with " + playerInfoList.size() + " players");
            playersBox.getChildren().clear();

            playerInfoList.get(0).setLeadPlayer(true);

            LogUtil.info("Created PlayerListManager");

            for (PlayerInfo playerInfo : playerInfoList) {
              LogUtil.info(
                  "Adding player "
                      + playerInfo.getName()
                      + " with selected image: "
                      + playerInfo.getSelectedAvatar()
                      + " to the list");
              ClientBox box =
                  new ClientBox(
                      playerInfo.getName(),
                      playerInfo.isLeadPlayer(),
                      playerInfo.getSelectedAvatar());
              box.setReady(playerInfo.getReady());
              playersBox.getChildren().add(box);
              playersLabel.setText("Number of players: " + playerInfoList.size());
            }

            playerListManager.setPlayerInfoList(playerInfoList);
            LogUtil.info("Updated PlayerListManager with player info list");
          }
        });
  }

  private void createOwnMessageBox() {
    chatManager.createOwnMessageBox(chatVbox, chatTextField);
  }

  private void sendTextMessageToServer() {
    chatManager.sendTextMessageToServer(chatTextField);
  }

  /**
   * Displays a received chat message in the chat box.
   *
   * @param name the name of the sender
   * @param messageText the text of the message
   * @param selectedAvatar the avatar of the sender
   */
  public static void displayReceivedMessage(
      String name, String messageText, String selectedAvatar) {
    ChatManager.displayReceivedMessage(name, messageText, selectedAvatar);
  }

  private void instantiateChatBox() {
    chatManager.instantiateChatBox(chatVbox, chatAnchor);
  }

  private void addChatBoxListener() {
    chatManager.addChatBoxListener(chatVbox);
  }

  private void addEnterKeyEventHandler() {
    chatTextField.setOnKeyPressed(
        event -> {
          if (event.getCode() == KeyCode.ENTER) {
            handleMessageSendButtonAction();
          }
        });
  }

  private void addHoverEffects() {
    addHoverEffect(readyButton);
    addHoverEffect(messageSend);
    addHoverEffect(sfxButton);
    addHoverEffect(musicButton);
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

  /**
   * Displays a server message in the chat box.
   *
   * @param message the server message to display
   */
  public void displayServerMessage(String message) {
    chatManager.displayServerMessage(message, chatVbox);
  }

  /**
   * Gets the selected avatar.
   *
   * @return the selected avatar
   */
  public String getSelectedAvatar() {
    return selectedAvatar;
  }

  /**
   * Sets the selected avatar.
   *
   * @param selectedAvatar the selected avatar
   */
  public void setSelectedAvatar(String selectedAvatar) {
    this.selectedAvatar = selectedAvatar;
  }

  /**
   * Gets the chat VBox.
   *
   * @return the chat VBox
   */
  public static VBox getChatVbox() {
    return chatVbox;
  }

  /** Toggles the music settings globally. */
  @FXML
  public void toggleMusic(ActionEvent actionEvent) {
    if (AudioUtil.isMusicPlaying()) {
      AudioUtil.stopMusic();
      musicButton.setText("Music Off");
    } else {
      AudioUtil.playBackgroundMusic();
      musicButton.setText("Music On");
    }
  }

  /** Toggles the sound effect settings globally. */
  @FXML
  public void toggleSfx(ActionEvent actionEvent) {
    if (AudioUtil.isSoundEffect()) {
      AudioUtil.setSoundEffect(false);
      sfxButton.setText("SFX Off");
    } else {
      AudioUtil.setSoundEffect(true);
      sfxButton.setText("SFX On");
    }
  }
}
