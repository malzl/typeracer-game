package com.typewrite.game.controller;

/** Interface for controllers that require initialization with data. */
public interface InitDataController {

  /**
   * Initializes the controller with the provided data.
   *
   * @param args the arguments passed for initialization.
   */
  void initData(Object... args);
}
