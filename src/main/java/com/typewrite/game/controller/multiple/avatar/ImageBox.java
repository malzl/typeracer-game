package com.typewrite.game.controller.multiple.avatar;

import com.typewrite.game.util.AudioUtil;
import javafx.animation.ScaleTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/** Represents an image box in the avatar selection view. */
public class ImageBox extends VBox {
  private ImageView imageView;
  private ScaleTransition st;
  private ChooseAvatarView correspondingView;
  private String imageName;

  /**
   * Constructs an ImageBox with the given image name and corresponding view.
   *
   * @param imageName the name of the image file
   * @param correspondingView the view to which this ImageBox belongs
   */
  public ImageBox(String imageName, ChooseAvatarView correspondingView) {
    this.correspondingView = correspondingView;
    this.imageName = imageName;

    Image image = new Image("com/typewrite/game/avatars/" + imageName + ".png");
    imageView = new ImageView(image);
    imageView.setFitHeight(100);
    imageView.setFitWidth(170);

    this.getChildren().addAll(imageView);

    this.setStyle(
        "-fx-border-radius: 15; "
            + "-fx-background-radius: 15; "
            + "-fx-border-color: black; "
            + "-fx-border-width: 2; "
            + "-fx-font-size: 14; "
            + "-fx-font-weight: bold; "
            + "-fx-text-fill: black; "
            + "-fx-padding: 10;");

    // Add hover effect
    st = new ScaleTransition(Duration.millis(200), this);
    st.setToX(1.1);
    st.setToY(1.1);

    this.setOnMouseEntered(
        e -> {
          st.playFromStart();
          AudioUtil.playHoverSoundEffect();
        });

    this.setOnMouseExited(
        e -> {
          st.stop();
          this.setScaleX(1.0);
          this.setScaleY(1.0);
        });

    this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> AudioUtil.playHoverSoundEffect());

    this.setOnMouseClicked(e -> handleMouseClicked());
  }

  /** Handles the mouse clicked event to change the avatar. */
  private void handleMouseClicked() {
    correspondingView.changeAvatar(imageName);
  }
}
