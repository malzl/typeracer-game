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
import com.typewrite.game.network.client.Client;
import com.typewrite.game.util.AudioUtil;
import com.typewrite.game.util.LogUtil;
import java.io.IOException;
import java.net.Socket;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

/** Controller class for managing the client view UI. */
public class ClientView {

  public static final String TITLE = "Join Game";

  private Stage gameStage;
  private boolean avatarChosen = false;

  /**
   * Sets the game stage for the client view.
   *
   * @param gameStage the stage to set
   */
  public void setGameStage(Stage gameStage) {
    this.gameStage = gameStage;
    System.out.println("ClientView: Game stage set");
  }

  private static final ClientView instance = new ClientView();

  /**
   * Retrieves the singleton instance of ClientView.
   *
   * @return the ClientView instance
   */
  public static synchronized ClientView getInstance() {
    return instance;
  }

  @FXML private TextField playerNameField;
  @FXML private TextField serverPortField;
  @FXML private TextField serverAddress;
  @FXML private Button connectButton;
  @FXML private ImageView startImage;
  @FXML private Button chooseAvatar;
  @FXML private Button backToHomeButton;

  private String defaultAvatar = "1";
  private String currentImage = defaultAvatar;
  private static Stage avatarSelectionStage = new Stage();

  /** Initializes the client view. */
  @FXML
  private void initialize() {
    connectButton.setDisable(true);
    playerNameField.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
    serverPortField.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
    serverAddress.textProperty().addListener((observable, oldValue, newValue) -> checkFields());

    connectButton.setOnAction(
        event -> {
          try {
            handleConnectButtonAction();
          } catch (IOException e) {
            showErrorAlert("Connection Error", "Unable to connect to server: " + e.getMessage());
            throw new RuntimeException(e);
          }
        });
    chooseAvatar.setOnAction(
        actionEvent -> {
          try {
            handleChooseAvatarAction();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });

    animateStartImage();
    addHoverEffectForAvatarButton();
    addHoverEffectForHomeButton();
    addHoverEffectForConnectionButton();
  }

  /** Checks if the input fields are filled and enables/disables the connect button accordingly. */
  private void checkFields() {
    boolean disableButton =
        playerNameField.getText().trim().isEmpty()
            || serverPortField.getText().trim().isEmpty()
            || serverAddress.getText().trim().isEmpty();
    connectButton.setDisable(disableButton);
  }

  /**
   * Handles the action when the connect button is clicked.
   *
   * @throws IOException if an error occurs during connection
   */
  private void handleConnectButtonAction() throws IOException {
    AudioUtil.playClickSoundEffect();
    String playerName = playerNameField.getText();
    String address = serverAddress.getText();
    int serverPort = Integer.parseInt(serverPortField.getText());
    LogUtil.info(
        "ClientView: Connecting with Player Name: " + playerName + " on Port: " + serverPort);
    try {
      Socket socket = new Socket(address, serverPort);
      Client client = new Client(playerName, socket); // Pass the stage to Client
      client.start();
      LogUtil.info("ClientView: Created Client with stage");
      LogUtil.info("ClientView: Client has been started.");
    } catch (Exception e) {
      showErrorAlert("Connection Error", "Failed to connect to server: " + e.getMessage());
    }
  }

  /**
   * Loads the specified view into the game stage.
   *
   * @param viewName the name of the FXML view to load
   * @throws IOException if an error occurs during loading
   */
  public void loadView(String viewName) throws IOException {
    if (this.gameStage == null) {
      throw new IllegalStateException("ClientView: gameStage is not set");
    }
    LogUtil.info("ClientView: Loading view " + viewName);
    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getResource("/com/typewrite/game/" + viewName + ".fxml"));
    Scene scene = new Scene(fxmlLoader.load());
    this.gameStage.setScene(scene);
  }

  /**
   * Shows an error alert with the specified title and content.
   *
   * @param title the title of the alert
   * @param content the content of the alert
   */
  private void showErrorAlert(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
  }

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

  /** Animates the start image with a rotating effect. */
  private void animateStartImage() {
    Timeline timeline =
        new Timeline(
            new KeyFrame(Duration.seconds(0), new KeyValue(startImage.rotateProperty(), -20)),
            new KeyFrame(Duration.seconds(1), new KeyValue(startImage.rotateProperty(), 20)),
            new KeyFrame(Duration.seconds(2), new KeyValue(startImage.rotateProperty(), -20)));
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }

  /**
   * Handles the action when the choose avatar button is clicked.
   *
   * @throws IOException if an error occurs during loading
   */
  private void handleChooseAvatarAction() throws IOException {
    AudioUtil.playClickSoundEffect();
    avatarSelectionStage = new Stage();
    avatarSelectionStage.setMinWidth(1000);
    avatarSelectionStage.setMinHeight(600);
    LogUtil.info("ClientView: Created new stage for profile picture selection.");

    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getResource("/com/typewrite/game/choose_avatar-view.fxml"));
    LogUtil.info("ClientView: FXMLLoader created with the FXML file.");

    Parent root = fxmlLoader.load();
    LogUtil.info("ClientView: FXML file loaded into a Parent.");

    Scene scene = new Scene(root);
    LogUtil.info("ClientView: Scene created with the Parent as its root.");

    avatarSelectionStage.setScene(scene);
    avatarSelectionStage.setTitle("Choose Avatar");
    avatarSelectionStage.show();
    LogUtil.info("ClientView: Stage shown with the new scene.");
  }

  /**
   * Gets the current selected avatar image.
   *
   * @return the current selected avatar image
   */
  public String getCurrentImage() {
    LogUtil.info("Getting the current image.");
    return currentImage;
  }

  /**
   * Sets the current selected avatar image.
   *
   * @param currentImage the current selected avatar image
   */
  public void setCurrentImage(String currentImage) {
    LogUtil.info("Setting the current image." + currentImage);
    this.currentImage = currentImage;
  }

  /** Closes the avatar selection stage if it is currently showing. */
  public void closeAvatarSelectionStage() {
    if (avatarSelectionStage != null) {
      if (avatarSelectionStage.isShowing()) {
        LogUtil.info("Closing the avatar selection stage.");
        Platform.runLater(
            () -> {
              avatarSelectionStage.close();
              if (!avatarSelectionStage.isShowing()) {
                LogUtil.info("Avatar selection stage closed successfully.");
              } else {
                LogUtil.error("Failed to close avatar selection stage.");
              }
            });
      } else {
        LogUtil.info("Avatar selection stage is not currently showing.");
      }
    } else {
      LogUtil.error("Avatar selection stage is null.");
    }
  }

  /** Adds a hover effect to the choose avatar button. */
  private void addHoverEffectForAvatarButton() {
    addHoverEffect(chooseAvatar);
  }

  /** Adds a hover effect to the choose home button. */
  private void addHoverEffectForHomeButton() {
    addHoverEffect(backToHomeButton);
  }

  /** Adds a hover effect to the connect button. */
  private void addHoverEffectForConnectionButton() {
    addHoverEffect(connectButton);
  }

  /**
   * Adds a hover effect to the specified button.
   *
   * @param button the button to which the hover effect will be added
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
