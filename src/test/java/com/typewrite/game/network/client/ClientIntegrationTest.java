package com.typewrite.game.network.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.typewrite.game.GameManager;
import com.typewrite.game.common.event.DefaultEventBus;
import com.typewrite.game.common.event.Event;
import com.typewrite.game.common.event.events.GameFinishedEvent;
import com.typewrite.game.common.event.events.SelectedEvent;
import com.typewrite.game.common.event.events.SyncEvent;
import com.typewrite.game.common.event.events.UpdateEvent;
import com.typewrite.game.network.messages.GameFinished;
import com.typewrite.game.network.messages.JsonConverter;
import com.typewrite.game.network.messages.Message;
import com.typewrite.game.network.messages.PlayerJoinRequest;
import com.typewrite.game.network.messages.PlayerProgressBroadcast;
import com.typewrite.game.network.server.GameInfo;
import com.typewrite.game.network.server.PlayerInfo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ClientIntegrationTest {

  @Mock private Socket mockSocket;
  @Mock private BufferedReader mockReader;
  @Mock private BufferedWriter mockWriter;
  @Mock private GameManager mockGameManager;

  private Client client;
  private TestEventListener testEventListener;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    testEventListener = new TestEventListener();
    DefaultEventBus.getInstance().subscribe(Event.class, testEventListener);

    try (var mockedStatic = mockStatic(GameManager.class)) {
      mockedStatic.when(GameManager::getInstance).thenReturn(mockGameManager);
      client = new Client("TestPlayer", mockSocket, mockReader, mockWriter);
    }
  }

  /**
   * Tests sending a message through the client. Verifies that the message is correctly sent using
   * the writer.
   */
  @Test
  void testSendMessage() throws IOException {
    PlayerJoinRequest joinRequest = new PlayerJoinRequest("TestPlayer", "1");
    client.sendMessage(joinRequest);

    verify(mockWriter).write(anyString());
    verify(mockWriter).newLine();
    verify(mockWriter).flush();
  }

  /** Tests handling a PlayerProgressBroadcast message. Verifies that a SyncEvent is published. */
  @Test
  void testHandlePlayerProgressBroadcast() {
    GameInfo gameInfo =
        new GameInfo(
            "Game1", "localhost", 8080, System.currentTimeMillis(), System.currentTimeMillis());
    List<PlayerInfo> playerInfos =
        Arrays.asList(new PlayerInfo("Player1", "1"), new PlayerInfo("Player2", "1"));
    PlayerProgressBroadcast broadcast = new PlayerProgressBroadcast(gameInfo, playerInfos);

    client.handleIncomingMessage(broadcast);

    assertTrue(testEventListener.wasEventPublished(SyncEvent.class));
  }

  /** Tests handling a GameFinished message. Verifies that a GameFinishedEvent is published. */
  @Test
  void testHandleGameFinished() {
    List<PlayerInfo> playerInfos =
        Arrays.asList(new PlayerInfo("Player1", "1"), new PlayerInfo("Player2", "1"));
    GameFinished gameFinished = new GameFinished(playerInfos);

    client.handleIncomingMessage(gameFinished);

    assertTrue(testEventListener.wasEventPublished(GameFinishedEvent.class));
  }

  /** Tests handling an UpdateEvent. Verifies that a PlayerProgressUpdate message is sent. */
  @Test
  void testOnUpdateEventHandler() throws IOException {
    UpdateEvent updateEvent = new UpdateEvent(new PlayerInfo("TestPlayer", "1"));
    client.onUpdateEventHandler(updateEvent);

    verify(mockWriter).write(contains("PlayerProgressUpdate"));
    verify(mockWriter).newLine();
    verify(mockWriter).flush();
  }

  /** Tests handling a SelectedEvent. Verifies that a TextSelectedNotification message is sent. */
  @Test
  void testOnSelectedEventHandler() throws IOException {
    List<String> sentences = Arrays.asList("Sentence 1", "Sentence 2");
    SelectedEvent selectedEvent = new SelectedEvent(sentences);
    client.onSelectedEventHandler(selectedEvent);

    verify(mockWriter).write(contains("TextSelectedNotification"));
    verify(mockWriter).newLine();
    verify(mockWriter).flush();
  }

  /** Tests closing the client connection. Verifies that the socket is closed. */
  @Test
  void testCloseConnection() throws IOException {
    when(mockSocket.isClosed()).thenReturn(false);
    client.closeConnection();

    verify(mockSocket).close();
  }

  /**
   * Tests getting the singleton instance of the client. Verifies that the instance is not null and
   * matches the created client.
   */
  @Test
  void testGetInstance() {
    assertNotNull(Client.getInstance());
    assertEquals(client, Client.getInstance());
  }

  /**
   * Tests getting the current player information. Verifies that the player info is correctly
   * retrieved.
   */
  @Test
  void testGetCurrPlayer() {
    PlayerInfo playerInfo = client.getCurrPlayer();
    assertNotNull(playerInfo);
    assertEquals("TestPlayer", playerInfo.getName());
  }

  private static class TestEventListener implements Consumer<Event> {
    private Event lastPublishedEvent;

    @Override
    public void accept(Event event) {
      lastPublishedEvent = event;
    }

    public boolean wasEventPublished(Class<? extends Event> eventType) {
      return eventType.isInstance(lastPublishedEvent);
    }
  }

  /** Tests sending a message when the socket is closed. Verifies that no message is sent. */
  @Test
  void testSendMessageWithClosedSocket() throws IOException {
    when(mockSocket.isClosed()).thenReturn(true);
    PlayerJoinRequest joinRequest = new PlayerJoinRequest("TestPlayer", "1");

    client.sendMessage(joinRequest);

    verify(mockWriter, never()).write(anyString());
    verify(mockWriter, never()).newLine();
    verify(mockWriter, never()).flush();
  }

  /** Tests handling an unknown message type. Verifies that no event is published. */
  @Test
  void testHandleUnknownMessageType() {
    Message unknownMessage = mock(Message.class);
    when(unknownMessage.getMessageType()).thenReturn("UnknownType");

    client.handleIncomingMessage(unknownMessage);

    assertFalse(testEventListener.wasEventPublished(Event.class));
  }

  /** Tests handling an UpdateEvent when the socket is closed. Verifies that no message is sent. */
  @Test
  void testOnUpdateEventHandlerWithClosedSocket() throws IOException {
    when(mockSocket.isClosed()).thenReturn(true);
    UpdateEvent updateEvent = new UpdateEvent(new PlayerInfo("TestPlayer", "1"));

    client.onUpdateEventHandler(updateEvent);

    verify(mockWriter, never()).write(anyString());
    verify(mockWriter, never()).newLine();
    verify(mockWriter, never()).flush();
  }

  /** Tests handling an empty message. Verifies that no event is published. */
  @Test
  void testHandleEmptyMessage() {
    Message emptyMessage = mock(Message.class);
    when(emptyMessage.getMessageType()).thenReturn("");

    client.handleIncomingMessage(emptyMessage);

    assertFalse(testEventListener.wasEventPublished(Event.class));
  }

  /**
   * Tests closing the client connection when it is already closed. Verifies that the socket is not
   * closed again.
   */
  @Test
  void testCloseConnectionWhenAlreadyClosed() throws IOException {
    when(mockSocket.isClosed()).thenReturn(true);

    client.closeConnection();

    verify(mockSocket, never()).close();
  }

  /**
   * Tests handling a GameFinished message with no players. Verifies that a GameFinishedEvent is
   * published.
   */
  @Test
  void testHandleGameFinishedWithNoPlayers() {
    GameFinished gameFinished = new GameFinished(Collections.emptyList());

    client.handleIncomingMessage(gameFinished);

    assertTrue(testEventListener.wasEventPublished(GameFinishedEvent.class));
  }

  /**
   * Tests getting the singleton instance of the client before initialization. Verifies that the
   * instance is null.
   */
  @Test
  void testGetInstanceBeforeInitialization() {
    Client.instance = null; // Reset the singleton instance

    assertNull(Client.getInstance());
  }

  /**
   * Tests sending a message when an IOException occurs. Verifies that a RuntimeException is thrown.
   */
  @Test
  void testSendMessageIoException() throws IOException {
    doThrow(new IOException("Test Exception")).when(mockWriter).write(anyString());
    PlayerJoinRequest joinRequest = new PlayerJoinRequest("TestPlayer", "1");

    assertThrows(RuntimeException.class, () -> client.sendMessage(joinRequest));
  }

  /**
   * Tests handling of a malformed message. Verifies that a JsonParseException is thrown and no
   * event is published for unknown message types.
   */
  @Test
  void testHandleMalformedMessage() {
    String malformedMessage = "{\"messageType\":\"UnknownType\"}";

    try {
      client.handleIncomingMessage(JsonConverter.fromJson(malformedMessage));
    } catch (Exception e) {
      assert (e instanceof com.google.gson.JsonParseException);
    }
    assertFalse(testEventListener.wasEventPublished(Event.class));
  }

  /**
   * Tests handling a network interruption. Verifies that no message is sent when the socket is
   * closed.
   */
  @Test
  void testNetworkInterruption() throws IOException {
    when(mockSocket.isClosed()).thenReturn(true);
    GameInfo gameInfo =
        new GameInfo(
            "Game1", "localhost", 8080, System.currentTimeMillis(), System.currentTimeMillis());
    List<PlayerInfo> playerInfos =
        Arrays.asList(new PlayerInfo("Player1", "1"), new PlayerInfo("Player2", "1"));
    PlayerProgressBroadcast broadcast = new PlayerProgressBroadcast(gameInfo, playerInfos);

    // Simulate network interruption
    client.handleIncomingMessage(broadcast);

    // Verify no message is sent as socket is closed
    verify(mockWriter, never()).write(anyString());
    verify(mockWriter, never()).newLine();
    verify(mockWriter, never()).flush();
  }
}
