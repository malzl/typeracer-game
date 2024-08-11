package com.typewrite.game.controller.multiple.avatar;

import com.typewrite.game.controller.multiple.ClientView;
import com.typewrite.game.util.ThemeManager;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/** Class that enables the view where players can select their avatar. */
public class ChooseAvatarView {
  @FXML private Label titleLabel;
  @FXML private VBox avatars;
  @FXML private AnchorPane selectedAvatar;
  @FXML private Button selectButton;
  @FXML private Button cancelButton;
  @FXML private AnchorPane rootPane;
  private VBox selectedAvatarVbox = new VBox();

  private GridPane gridPane;

  /**
   * Initializes the ChooseAvatarView. This method is automatically called after the FXML file has
   * been loaded.
   */
  @FXML
  private void initialize() {
    ThemeManager.applyCurrentTheme(rootPane);
    selectedAvatar.getChildren().add(selectedAvatarVbox);
    initializeGridPane();
    avatars.getChildren().add(gridPane);
    selectedAvatarVbox
        .getChildren()
        .add(new SelectedAvatarBox(ClientView.getInstance().getCurrentImage()));

    addHoverEffectForCancelButton();
    addHoverEffectForSelectButton();
  }

  /** Handles the action when the select button is clicked. */
  @FXML
  private void handleOnSelectAction() {
    String name = ((SelectedAvatarBox) selectedAvatarVbox.getChildren().get(0)).getName();
    ClientView.getInstance().setCurrentImage(name);
    ClientView.getInstance().closeAvatarSelectionStage();
  }

  /** Handles the action when the cancel button is clicked. */
  @FXML
  private void handleOnCancelAction() {
    ClientView.getInstance().closeAvatarSelectionStage();
  }

  /** Initializes the GridPane for displaying avatars. */
  private void initializeGridPane() {
    gridPane = new GridPane();
    gridPane.setVgap(10);
    gridPane.setHgap(10);

    for (int i = 0; i < 12; i++) {
      ImageBox avatar = new ImageBox(String.valueOf(i + 1), this);
      gridPane.add(avatar, i % 4, i / 4);
    }
  }

  /**
   * Changes the avatar through selection.
   *
   * @param imageName the name of the selected avatar image
   */
  public void changeAvatar(String imageName) {
    selectedAvatarVbox.getChildren().clear();
    selectedAvatarVbox.getChildren().add(new SelectedAvatarBox(imageName));
  }

  /** Adds a hover effect to the select button. */
  private void addHoverEffectForSelectButton() {
    addHoverEffect(selectButton);
  }

  /** Adds a hover effect to the cancel button. */
  private void addHoverEffectForCancelButton() {
    addHoverEffect(cancelButton);
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
