package com.typewrite.game.controller.multiple.avatar;

import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/** Represents a box displaying the selected avatar. */
public class SelectedAvatarBox extends VBox {
  private ImageView imageView;
  private String name;

  /**
   * Constructs a SelectedAvatarBox with the given avatar name.
   *
   * @param name the name of the selected avatar
   */
  public SelectedAvatarBox(String name) {
    this.name = name;

    Image image = new Image("com/typewrite/game/avatars/" + name + ".png");
    imageView = new ImageView(image);
    imageView.setFitHeight(200);
    imageView.setFitWidth(350);

    this.getChildren().addAll(imageView);
    this.setAlignment(Pos.CENTER);
    this.setMinSize(350, 350);

    this.setStyle(
        "-fx-border-radius: 15; "
            + "-fx-background-radius: 15; "
            + "-fx-border-color: black; "
            + "-fx-border-width: 2; "
            + "-fx-font-size: 14; "
            + "-fx-font-weight: bold; "
            + "-fx-text-fill: black; "
            + "-fx-padding: 10;");

    ScaleTransition st = new ScaleTransition(Duration.millis(1000), this);
    st.setByX(0.1);
    st.setByY(0.1);
    st.setCycleCount(Integer.MAX_VALUE);
    st.setAutoReverse(true);
    st.play();
  }

  /**
   * Gets the name of the selected avatar.
   *
   * @return the name of the selected avatar
   */
  public String getName() {
    return name;
  }
}
