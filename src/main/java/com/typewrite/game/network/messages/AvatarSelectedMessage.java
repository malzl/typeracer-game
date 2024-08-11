package com.typewrite.game.network.messages;

import com.google.gson.annotations.SerializedName;

/** Represents a message indicating that an avatar has been selected along with a photo. */
public class AvatarSelectedMessage implements Message {

  @SerializedName("messageType")
  private final String messageType = "AvatarSelectedMessage";

  @SerializedName("name")
  private final String name;

  @SerializedName("photoChosen")
  private final String photoChosen;

  /**
   * Constructs an AvatarSelectedMessage.
   *
   * @param name the name of the selected avatar
   * @param photoChosen the chosen photo
   */
  public AvatarSelectedMessage(String name, String photoChosen) {
    this.name = name;
    this.photoChosen = photoChosen;
  }

  /**
   * Gets the name of the selected avatar.
   *
   * @return the name of the selected avatar
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the chosen photo.
   *
   * @return the chosen photo
   */
  public String getPhotoChosen() {
    return photoChosen;
  }

  /**
   * Gets the message type.
   *
   * @return the message type
   */
  @Override
  public String getMessageType() {
    return messageType;
  }
}
