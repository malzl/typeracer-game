package com.typewrite.game.network.server;

import com.typewrite.game.network.messages.JsonConverter;
import com.typewrite.game.network.messages.Message;
import com.typewrite.game.network.messages.PlayerJoinRequest;
import com.typewrite.game.network.messages.PlayerProgressUpdate;
import com.typewrite.game.network.messages.TextSelectedNotification;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class TestClient {
  private final Socket socket;
  private final BufferedReader reader;
  private final BufferedWriter writer;
  private final String name;
  private final List<String> receivedMessages = new ArrayList<>();

  TestClient(String name, int port) throws IOException {
    this.name = name;
    System.out.println("Creating TestClient: " + name);
    socket = new Socket("localhost", port);
    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

    // Start a thread to continuously read messages
    new Thread(
            () -> {
              try {
                String line;
                while ((line = reader.readLine()) != null) {
                  System.out.println(name + " received: " + line);
                  synchronized (receivedMessages) {
                    receivedMessages.add(line);
                  }
                }
              } catch (IOException e) {
                System.err.println(name + " reading thread stopped: " + e.getMessage());
              }
            })
        .start();
  }

  void joinGame() throws IOException {
    sendMessage(new PlayerJoinRequest(name, "1"));
  }

  void sendReady() throws IOException {
    PlayerInfo playerInfo = new PlayerInfo(name, "1");
    playerInfo.setReady(true);
    sendMessage(new PlayerProgressUpdate(playerInfo));
  }

  void selectText(List<String> sentences) throws IOException {
    sendMessage(new TextSelectedNotification(sentences));
  }

  void updateProgress(int progress) throws IOException {
    PlayerInfo playerInfo = new PlayerInfo(name, "1");
    playerInfo.setCurrTextSize(progress);
    sendMessage(new PlayerProgressUpdate(playerInfo));
  }

  void finishGame() throws IOException {
    PlayerInfo playerInfo = new PlayerInfo(name, "1");
    playerInfo.setFinished(true);
    sendMessage(new PlayerProgressUpdate(playerInfo));
  }

  void sendMessage(Message message) throws IOException {
    String jsonMessage = JsonConverter.toJson(message);
    System.out.println(name + " sending: " + jsonMessage);
    writer.write(jsonMessage);
    writer.newLine();
    writer.flush();
  }

  void close() throws IOException {
    System.out.println("Closing TestClient: " + name);
    socket.close();
    // Wait a bit to allow the server to process the disconnection
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  boolean waitForMessage(String messageType) throws InterruptedException {
    System.out.println(name + " waiting for message: " + messageType);
    long startTime = System.currentTimeMillis();
    while (System.currentTimeMillis() - startTime < 5000) {
      synchronized (receivedMessages) {
        for (String message : receivedMessages) {
          if (message.contains(messageType)) {
            System.out.println(name + " received expected message: " + message);
            return true;
          }
        }
      }
      Thread.sleep(100);
    }
    System.out.println(name + " did not receive expected message: " + messageType);
    return false;
  }

  String getMessageOfType(String messageType) {
    synchronized (receivedMessages) {
      for (String message : receivedMessages) {
        if (message.contains(messageType)) {
          return message;
        }
      }
    }
    System.out.println(name + " - All received messages: " + receivedMessages);
    return null;
  }
}
