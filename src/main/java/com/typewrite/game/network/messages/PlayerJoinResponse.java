package com.typewrite.game.network.messages;

/** Represents a response message for a player joining a game. */
public class PlayerJoinResponse implements Message {
  private final String messageType = "PlayerJoinResponse";
  private final boolean success;
  private final String username;
  private final String selectedAvatar;

  /**
   * Constructs a PlayerJoinResponse message.
   *
   * @param username the username of the player
   * @param success whether the join was successful
   * @param selectedAvatar the selected avatar of the player
   */
  public PlayerJoinResponse(String username, boolean success, String selectedAvatar) {
    this.username = username;
    this.success = success;
    this.selectedAvatar = selectedAvatar;
  }

  /**
   * Gets the username of the player.
   *
   * @return the username of the player
   */
  public String getUsername() {
    return username;
  }

  /**
   * Indicates whether the join was successful.
   *
   * @return true if the join was successful, false otherwise
   */
  public boolean isSuccess() {
    return success;
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
