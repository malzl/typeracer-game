module com.typewrite.game {
  requires javafx.controls;
  requires javafx.fxml;
  requires com.google.gson;
  requires javafx.swing;
  requires javafx.media;
  requires java.desktop;

  opens com.typewrite.game.controller to
      javafx.fxml;
  opens com.typewrite.game.controller.multiple.lobby to
      javafx.fxml;
  opens com.typewrite.game.network.messages to
      com.google.gson;

  exports com.typewrite.game;
  exports com.typewrite.game.controller;
  exports com.typewrite.game.common.enums;
  exports com.typewrite.game.common.event;
  exports com.typewrite.game.common.event.events;
  exports com.typewrite.game.network.messages;
  exports com.typewrite.game.controller.single;

  opens com.typewrite.game.controller.single to
      javafx.fxml;

  exports com.typewrite.game.controller.multiple;

  opens com.typewrite.game.controller.multiple to
      javafx.fxml;

  exports com.typewrite.game.network.server;

  opens com.typewrite.game.network.server to
      com.google.gson;

  exports com.typewrite.game.network.client;
  exports com.typewrite.game.controller.multiple.avatar;

  opens com.typewrite.game.controller.multiple.avatar to
      javafx.fxml;
  opens com.typewrite.game.controller.multiple.lobby.chat to
      javafx.fxml;
}
