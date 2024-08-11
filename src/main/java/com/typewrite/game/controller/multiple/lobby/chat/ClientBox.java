package com.typewrite.game.controller.multiple.lobby.chat;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/** Represents a client box in the game lobby, displaying player information. */
public class ClientBox extends HBox {
  private final Label readyLabel;
  private final Label adminLabel; // new label for admin
  private boolean isAdmin;
  private String selectedAvatar;
  private VBox avatarBox;

  /**
   * Constructs a ClientBox with the given player name, admin status, and selected avatar.
   *
   * @param name the name of the player
   * @param isAdmin true if the player is an admin, false otherwise
   * @param selectedAvatar the selected avatar of the player
   */
  public ClientBox(String name, boolean isAdmin, String selectedAvatar) {
    Image image = new Image("/com/typewrite/game/avatars/" + selectedAvatar + ".png");
    ImageView avatarView = new ImageView(image);
    avatarView.setFitWidth(100);
    avatarView.setFitHeight(60);

    avatarBox = new VBox(avatarView);

    readyLabel = new Label();
    adminLabel = new Label(); // initialize the admin label
    this.isAdmin = isAdmin;

    Label nameLabel = new Label(name);
    nameLabel.getStyleClass().add("name-label");
    nameLabel.setStyle("-fx-font-size: 16px;");

    this.getChildren().addAll(avatarView, nameLabel, readyLabel, adminLabel);
    this.setStyle(
        "-fx-padding: 10px; -fx-spacing: 10px; -fx-font-family: Arial; "
            + "-fx-alignment: center-left;");
    setReady(false);
    setAdmin(isAdmin);
    setImage(selectedAvatar);
  }

  /**
   * Sets the ready status of the player.
   *
   * @param isReady true if the player is ready, false otherwise
   */
  public void setReady(boolean isReady) {
    readyLabel.setText(isReady ? "Ready" : "Not Ready");
    readyLabel.setStyle(
        isReady
            ? "-fx-text-fill: green; -fx-font-family: Arial; -fx-font-weight: bold; "
                + "-fx-padding: 5px; -fx-border-radius: 5px; -fx-background-radius: 5px; "
                + "-fx-background-color: #e4ffe1; -fx-border-color: #27ae60; -fx-border-width: 2px;"
            : "-fx-text-fill: gray;-fx-font-family: Arial; -fx-font-weight: bold;"
                + " -fx-padding: 5px; "
                + "-fx-border-radius: 5px; -fx-background-radius: 5px; "
                + "-fx-background-color: #f2f2f2; "
                + "-fx-border-color: #a6a6a6; -fx-border-width: 2px;");
  }

  /**
   * Sets the admin status of the player.
   *
   * @param isAdmin true if the player is an admin, false otherwise
   */
  public void setAdmin(boolean isAdmin) {
    adminLabel.setText(isAdmin ? "Admin" : "");
    adminLabel.setStyle(
        isAdmin
            ? "-fx-text-fill: #e84393; -fx-font-family: Arial; -fx-font-weight: bold;"
                + " -fx-padding: 5px; -fx-border-radius: 5px; -fx-background-radius: 5px;"
                + " -fx-background-color: #ffe6e6; -fx-border-color: #e84393;"
                + " -fx-border-width: 2px;"
            : "");
  }

  /**
   * Sets the image for the player's avatar.
   *
   * @param imageName the name of the image file
   */
  public void setImage(String imageName) {
    Image image = new Image("/com/typewrite/game/avatars/" + "3" + ".png");
    ImageView avatarView = new ImageView();
    avatarView.setImage(image);
  }
}
