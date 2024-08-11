package com.typewrite.game.network.server;

/** Represents player information in the game. */
public class PlayerInfo {

  private String name;
  private Boolean ready;
  private Boolean online;
  private Boolean kickOut;
  private Boolean finished;
  private Long completedTime;
  private Integer currTextSize;
  private String wpm;
  private long lastActivityTime;
  private boolean isLeadPlayer;

  private String selectedAvatar;

  /** Constructs an instance of PlayerInfo to track important attributes. */
  public PlayerInfo(String name, String selectedAvatar) {
    this.name = name;
    this.online = true;
    this.kickOut = false;
    this.ready = false;
    this.finished = false;
    this.currTextSize = 0;
    this.lastActivityTime = 0;
    this.isLeadPlayer = false;
    this.selectedAvatar = selectedAvatar;
  }

  /**
   * Gets the player's name.
   *
   * @return the player's name.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the player's name.
   *
   * @param name the new name for the player.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the player's ready status.
   *
   * @return true if the player is ready, false otherwise.
   */
  public Boolean getReady() {
    return ready;
  }

  /**
   * Sets the player's ready status.
   *
   * @param ready the new ready status for the player.
   */
  public void setReady(Boolean ready) {
    this.ready = ready;
  }

  /**
   * Gets the player's online status.
   *
   * @return true if the player is online, false otherwise.
   */
  public Boolean getOnline() {
    return online;
  }

  /**
   * Sets the player's online status.
   *
   * @param online the new online status for the player.
   */
  public void setOnline(Boolean online) {
    this.online = online;
  }

  /**
   * Gets the player's finished status.
   *
   * @return true if the player has finished, false otherwise.
   */
  public Boolean getFinished() {
    return finished;
  }

  /**
   * Sets the player's finished status.
   *
   * @param finished the new finished status for the player.
   */
  public void setFinished(Boolean finished) {
    this.finished = finished;
  }

  /**
   * Gets the player's current text size.
   *
   * @return the player's current text size.
   */
  public Integer getCurrTextSize() {
    return currTextSize;
  }

  /**
   * Sets the player's current text size.
   *
   * @param currTextSize the new current text size for the player.
   */
  public void setCurrTextSize(Integer currTextSize) {
    this.currTextSize = currTextSize;
  }

  /**
   * Gets the player's completed time.
   *
   * @return the player's completed time.
   */
  public Long getCompletedTime() {
    return completedTime;
  }

  /**
   * Sets the player's completed time.
   *
   * @param completedTime the new completed time for the player.
   */
  public void setCompletedTime(Long completedTime) {
    this.completedTime = completedTime;
  }

  /**
   * Gets the player's words per minute (WPM).
   *
   * @return the player's WPM.
   */
  public String getWpm() {
    return wpm;
  }

  /**
   * Sets the player's words per minute (WPM).
   *
   * @param wpm the new WPM for the player.
   */
  public void setWpm(String wpm) {
    this.wpm = wpm;
  }

  /**
   * Checks if the player is the lead player.
   *
   * @return true if the player is the lead player, false otherwise.
   */
  public boolean isLeadPlayer() {
    return isLeadPlayer;
  }

  /**
   * Sets the lead player status.
   *
   * @param leadPlayer the new lead player status.
   */
  public void setLeadPlayer(boolean leadPlayer) {
    isLeadPlayer = leadPlayer;
  }

  public String getSelectedAvatar() {
    return selectedAvatar;
  }

  public void setSelectedAvatar(String selectedAvatar) {
    this.selectedAvatar = selectedAvatar;
  }

  public long getLastActivityTime() {
    return lastActivityTime;
  }

  public void setLastActivityTime(long lastActivityTime) {
    this.lastActivityTime = lastActivityTime;
  }

  public Boolean getKickOut() {
    return kickOut;
  }

  public void setKickOut(Boolean kickOut) {
    this.kickOut = kickOut;
  }
}
