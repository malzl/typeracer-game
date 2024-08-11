package com.typewrite.game.controller.multiple.lobby.chat;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/** Represents a server message box in the game lobby, displaying messages from the server. */
public class ServerMessageBox extends HBox {

  /**
   * Constructs a ServerMessageBox with the given message.
   *
   * @param message the content of the server message
   */
  public ServerMessageBox(String message) {

    Label messageLabel = new Label(message);
    messageLabel.setWrapText(true);
    VBox messageBox = new VBox();
    messageLabel.setMaxWidth(Double.MAX_VALUE);
    messageLabel.setStyle(getServerStyle());
    messageBox.getChildren().add(messageLabel);

    this.getChildren().addAll(messageBox);
  }

  /**
   * Returns the CSS style for server messages.
   *
   * @return the CSS style string
   */
  private String getServerStyle() {
    return "-fx-padding: 5px; -fx-background-color: #808080; -fx-background-radius: 10px; "
        + "-fx-text-fill: white; -fx-font-size: 12px;";
  }
}
