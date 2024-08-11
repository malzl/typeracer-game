package com.typewrite.game.controller.multiple.lobby.chat;

import com.typewrite.game.common.event.DefaultEventBus;
import com.typewrite.game.common.event.events.UpdateEvent;
import com.typewrite.game.controller.multiple.ClientView;
import com.typewrite.game.controller.multiple.lobby.LobbyView;
import com.typewrite.game.network.client.Client;
import com.typewrite.game.network.messages.TextMessage;
import com.typewrite.game.network.server.PlayerInfo;
import com.typewrite.game.util.AudioUtil;
import com.typewrite.game.util.LogUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/** Manages chat functionalities in the game lobby. */
public class ChatManager {

  /** Default constructor for ChatManager. */
  public ChatManager() {}

  /**
   * Displays a server message in the provided VBox.
   *
   * @param message the message to display
   * @param chatVbox1 the VBox where the message will be displayed
   */
  public void displayServerMessage(String message, VBox chatVbox1) {
    LogUtil.info("Attempting to display message: " + message);
    Platform.runLater(
        () -> {
          LogUtil.info("Inside Platform.runLater: " + message);
          ServerMessageBox serverMessageBox = new ServerMessageBox(message);
          serverMessageBox.setAlignment(Pos.CENTER);
          chatVbox1.getChildren().add(serverMessageBox);
          LogUtil.info("Message added to chatVbox");
        });
    AudioUtil.playServerSoundEffect();
  }

  /**
   * Initializes and styles the chat box.
   *
   * @param chatVbox the VBox representing the chat box
   * @param chatAnchor the AnchorPane to which the chat box will be added
   */
  public void instantiateChatBox(VBox chatVbox, AnchorPane chatAnchor) {
    chatVbox.setStyle(
        "-fx-border-width: 5px; -fx-border-radius: 20px;"
            + " -fx-background-radius: 20px; -fx-background-color: rgb(255,255,255);  "
            + "-fx-effect: dropshadow(gaussian, black, 10, 0.5, 0, 0);"
            + " -fx-text-fill: #bc43e5; "
            + "-fx-font-size: 20px; -fx-font-weight: bold;-fx-font-family: Arial");
    chatVbox.setLayoutX(10.0);
    chatVbox.setPrefHeight(360.0);
    chatVbox.setPrefWidth(355.0);
    chatVbox.setSpacing(10);

    chatAnchor.getChildren().clear();
    chatAnchor.getChildren().add(chatVbox);
  }

  /**
   * Adds a listener to the chat box to limit the number of displayed messages to the last 10.
   *
   * @param chatVbox the VBox representing the chat box
   */
  public void addChatBoxListener(VBox chatVbox) {
    chatVbox
        .getChildren()
        .addListener(
            (ListChangeListener<Node>)
                change -> {
                  if (chatVbox.getChildren().size() > 9) {
                    List<Node> lastTenMessages =
                        new ArrayList<>(
                            chatVbox
                                .getChildren()
                                .subList(
                                    chatVbox.getChildren().size() - 9,
                                    chatVbox.getChildren().size()));
                    Platform.runLater(
                        () -> {
                          chatVbox.getChildren().clear();
                          chatVbox.getChildren().addAll(lastTenMessages);
                        });
                  }
                });
  }

  /**
   * Creates and displays a message box with the current player's message.
   *
   * @param chatVbox the VBox representing the chat box
   * @param chatTextField the TextField where the player types the message
   */
  public void createOwnMessageBox(VBox chatVbox, TextField chatTextField) {
    Client client = Client.getInstance();
    PlayerInfo currPlayer = client.getCurrPlayer();
    String message = chatTextField.getText();
    if (!message.trim().isEmpty()) {
      MessageBox messageBox = new MessageBox(currPlayer.getName(), message, true, "1");
      chatVbox.getChildren().add(messageBox);
    }
    AudioUtil.playMessageSentEffect();
  }

  /**
   * Sends the text message from the chat text field to the server.
   *
   * @param chatTextField the TextField where the player types the message
   */
  public void sendTextMessageToServer(TextField chatTextField) {
    Client client = Client.getInstance();
    PlayerInfo currPlayer = client.getCurrPlayer();
    String name = currPlayer.getName();
    String messageText = chatTextField.getText();

    LogUtil.info("Message text: '" + messageText + "'");

    if (!messageText.trim().isEmpty()) {
      String avatar = ClientView.getInstance().getCurrentImage();
      TextMessage textMessage = new TextMessage(name, messageText, avatar);
      try {
        LogUtil.info("Sending message to server: " + messageText);
        Client.getInstance().sendMessage(textMessage);
      } catch (IOException e) {
        LogUtil.info("Failed to send message to server: " + e.getMessage());
        e.printStackTrace();
      }
    }
  }

  /**
   * Displays a received message in the lobby view.
   *
   * @param name the name of the sender
   * @param messageText the text of the message
   * @param selectedAvatar the avatar associated with the message
   */
  public static void displayReceivedMessage(
      String name, String messageText, String selectedAvatar) {
    Platform.runLater(
        () -> {
          if (LobbyView.getChatVbox() == null) {
            LogUtil.error("LobbyView is not initialized yet. Cannot display message.");
            return;
          }
          LogUtil.info("Received message from " + name + ": " + messageText);
          MessageBox messageBox = new MessageBox(name, messageText, false, selectedAvatar);
          LobbyView.getChatVbox().getChildren().add(messageBox);
          LobbyView.getChatVbox().requestLayout();
          LogUtil.info("Displayed the received message in the LobbyView");
        });
    AudioUtil.playMessageSoundEffect();
  }

  /**
   * Adds an event handler to send the message when the Enter key is pressed.
   *
   * @param chatTextField the TextField where the player types the message
   */
  public void addEnterKeyEventHandler(TextField chatTextField) {
    chatTextField.setOnKeyPressed(
        event -> {
          if (event.getCode() == KeyCode.ENTER) {
            LobbyView.getInstance().handleMessageSendButtonAction();
          }
        });
  }

  /**
   * Handles the action of the "Ready" button, toggling the player's ready status.
   *
   * @param readyButton the Button for indicating readiness
   */
  public void handleButtonReadyAction(Button readyButton) {
    Client client = Client.getInstance();
    PlayerInfo currPlayer = client.getCurrPlayer();
    boolean isReady = !currPlayer.getReady();
    currPlayer.setReady(isReady);
    readyButton.setText(isReady ? "Not Ready" : "Ready");
    DefaultEventBus.getInstance().publish(new UpdateEvent(currPlayer));
  }
}
