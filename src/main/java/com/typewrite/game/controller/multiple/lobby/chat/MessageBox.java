package com.typewrite.game.controller.multiple.lobby.chat;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/** Represents a message box in the game lobby, displaying messages from players. */
public class MessageBox extends HBox {

  /**
   * Constructs a MessageBox with the given player name, message, alignment, and selected avatar.
   *
   * @param playerName the name of the player sending the message
   * @param message the content of the message
   * @param isRightAligned true if the message should be right-aligned, false otherwise
   * @param selectedAvatar the selected avatar of the player
   */
  public MessageBox(
      String playerName, String message, boolean isRightAligned, String selectedAvatar) {

    ImageView avatarView = new ImageView();
    avatarView.setFitWidth(25);
    avatarView.setFitHeight(30);
    Image image = new Image("/com/typewrite/game/avatars/" + selectedAvatar + ".png");
    avatarView.setImage(image);

    VBox nameBox = new VBox();
    Label nameLabel = new Label(playerName);
    nameLabel.getStyleClass().add("name-label");
    nameLabel.setStyle("-fx-font-size: 12px;");
    nameBox.getChildren().add(nameLabel);

    Label messageLabel = new Label(message);
    messageLabel.setWrapText(true);
    messageLabel.setMaxWidth(200);
    messageLabel.setStyle(isRightAligned ? getRightAlignedStyle() : getLeftAlignedStyle());
    VBox messageBox = new VBox();
    messageBox.getChildren().add(messageLabel);

    Region filler = new Region();
    HBox.setHgrow(filler, Priority.ALWAYS);

    if (!isRightAligned) {
      this.getChildren().addAll(avatarView, nameBox, messageBox);
    } else {
      this.getChildren().addAll(filler, nameBox, messageBox);
    }
  }

  /**
   * Returns the CSS style for right-aligned messages.
   *
   * @return the CSS style string
   */
  private String getRightAlignedStyle() {
    return "-fx-padding: 5px; -fx-background-color: #a3a8a8; -fx-background-radius: 10px;"
        + " -fx-text-fill: white; -fx-font-size: 12px;";
  }

  /**
   * Returns the CSS style for left-aligned messages.
   *
   * @return the CSS style string
   */
  private String getLeftAlignedStyle() {
    return "-fx-padding: 5px; -fx-background-color: #148ded; -fx-background-radius: 10px; "
        + "-fx-text-fill: white; -fx-font-size: 12px;";
  }
}
