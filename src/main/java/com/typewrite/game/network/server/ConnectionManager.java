package com.typewrite.game.network.server;

import com.typewrite.game.network.messages.EmojiMessage;
import com.typewrite.game.network.messages.JsonConverter;
import com.typewrite.game.network.messages.Message;
import com.typewrite.game.network.messages.PlayerJoinRequest;
import com.typewrite.game.network.messages.PlayerProgressUpdate;
import com.typewrite.game.network.messages.TextMessage;
import com.typewrite.game.network.messages.TextSelectedNotification;
import com.typewrite.game.util.LogUtil;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Manages the connection between the server and a client. Handles incoming messages and sends
 * responses.
 */
public class ConnectionManager implements Runnable {
  private final Socket clientSocket;
  private final Server server;
  private final BufferedReader bufferedReader;
  private final BufferedWriter bufferedWriter;
  private Thread thread;

  /**
   * Constructs a ConnectionManager with the specified client socket and server.
   *
   * @param clientSocket the socket for the client connection.
   * @param server the server managing the connection.
   * @throws IOException if an I/O error occurs.
   */
  public ConnectionManager(Socket clientSocket, Server server) throws IOException {
    this.clientSocket = clientSocket;
    this.server = server;
    this.bufferedReader =
        new BufferedReader(
            new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
    this.bufferedWriter =
        new BufferedWriter(
            new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8));
  }

  /** Run method for the ConnectionManager thread. */
  @Override
  public void run() {
    try {
      String message;
      while ((message = bufferedReader.readLine()) != null) {
        if (clientSocket.isClosed()) {
          break;
        }
        LogUtil.info("client message: " + message);
        Message parsedMessage = JsonConverter.fromJson(message);
        if (parsedMessage == null) {
          LogUtil.error("Failed to parse message: " + message);
          continue;
        }
        handleIncomingMessage(parsedMessage);
      }
    } catch (IOException e) {
      LogUtil.error("socket error");
    } finally {
      try {
        clientSocket.close();
      } catch (IOException e) {
        LogUtil.error("Close socket error. ");
      }
    }
  }

  /**
   * Sends a message to the client.
   *
   * @param message the message to send.
   * @throws IOException if an I/O error occurs while sending the message.
   */
  public void sendMessage(String message) throws IOException {
    bufferedWriter.write(message);
    bufferedWriter.newLine();
    bufferedWriter.flush();
  }

  /**
   * Sets the thread for this connection manager.
   *
   * @param thread the thread to set.
   */
  public void setThread(Thread thread) {
    this.thread = thread;
  }

  /** Closes the client connection. */
  public void close() {
    try {
      clientSocket.close();
    } catch (IOException e) {
      LogUtil.error("Close socket error.");
      throw new RuntimeException(e);
    }
    thread.interrupt();
  }

  private void handleIncomingMessage(Message message) throws IOException {
    switch (message.getMessageType()) {
      case "PlayerJoinRequest":
        PlayerJoinRequest joinRequest = (PlayerJoinRequest) message;
        server.addPlayer(joinRequest.getUserName(), this, joinRequest.getSelectedAvatar());
        break;
      case "PlayerProgressUpdate":
        PlayerProgressUpdate progressUpdate = (PlayerProgressUpdate) message;
        server.updatePlayer(progressUpdate.getPlayer());
        break;
      case "TextSelectedNotification":
        TextSelectedNotification textSelectedNotification = (TextSelectedNotification) message;
        server.startGame(textSelectedNotification.getSentences());
        break;
      case "TextMessage":
        TextMessage textMessage = (TextMessage) message;
        handleTextMessage(textMessage);
        break;
      case "EmojiMessage":
        EmojiMessage emojiMessage = (EmojiMessage) message;
        handleEmojiMessage(emojiMessage);
        break;
      default:
        LogUtil.error("Unknown message type: " + message.getMessageType());
        break;
    }
  }

  private void handleTextMessage(TextMessage textMessage) throws IOException {
    String jsonMessage = JsonConverter.toJson(textMessage);
    LogUtil.info("Received text message: " + jsonMessage);
    server.broadcastMessage(jsonMessage, this);
    LogUtil.info("Message broadcasted: " + jsonMessage);
  }

  private void handleEmojiMessage(EmojiMessage emojiMessage) throws IOException {
    String jsonMessage = JsonConverter.toJson(emojiMessage);
    LogUtil.info("Received emoji message: " + jsonMessage);
    server.broadcastMessage(jsonMessage, this);
    LogUtil.info("Message broadcasted: " + jsonMessage);
  }
}
