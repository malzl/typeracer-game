package com.typewrite.game.network.server;

/** Represents the game information. */
public class GameInfo {

  private String name;

  private Integer textTotalSize;

  private final String host;

  private final Integer port;

  private Long startTime;

  private Long currTime;

  /**
   * Constructs a GameInfo with the specified parameters.
   *
   * @param name the name of the game.
   * @param host the host of the game.
   * @param port the port number of the game.
   * @param startTime the start time of the game.
   * @param currTime the current time of the game.
   */
  public GameInfo(String name, String host, Integer port, Long startTime, Long currTime) {
    this.name = name;
    this.host = host;
    this.port = port;
    this.startTime = startTime;
    this.currTime = currTime;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setTextTotalSize(Integer textTotalSize) {
    this.textTotalSize = textTotalSize;
  }

  public String getHost() {
    return host;
  }

  public Integer getPort() {
    return port;
  }

  public Long getStartTime() {
    return startTime;
  }

  public void setStartTime(Long startTime) {
    this.startTime = startTime;
  }

  public Long getCurrTime() {
    return currTime;
  }

  public void setCurrTime(Long currTime) {
    this.currTime = currTime;
  }
}
