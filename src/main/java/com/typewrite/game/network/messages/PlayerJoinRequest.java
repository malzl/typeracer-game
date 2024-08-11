package com.typewrite.game.network.messages;

import com.google.gson.annotations.SerializedName;

/** Represents a request message for a player to join a game. */
public class PlayerJoinRequest implements Message {

  @SerializedName("messageType")
  private final String messageType = "PlayerJoinRequest";

  @SerializedName("username")
  private final String username;

  @SerializedName("selectedAvatar")
  private final String selectedAvatar;

  /**
   * Constructs a PlayerJoinRequest message.
   *
   * @param username the username of the player
   * @param selectedAvatar the selected avatar of the player
   */
  public PlayerJoinRequest(String username, String selectedAvatar) {
    this.username = username;
    this.selectedAvatar = selectedAvatar;
  }

  /**
   * Gets the username of the player.
   *
   * @return the username of the player
   */
  public String getUserName() {
    return username;
  }

  /**
   * Gets the selected avatar of the player.
   *
   * @return the selected avatar of the player
   */
  public String getSelectedAvatar() {
    return selectedAvatar;
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
