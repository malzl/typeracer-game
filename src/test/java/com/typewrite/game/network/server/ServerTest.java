package com.typewrite.game.network.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.typewrite.game.network.messages.JsonConverter;
import com.typewrite.game.network.messages.PlayerJoinResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServerTest {

  private Server server;
  private final int port = 12345;

  @BeforeEach
  void setUp() {
    server = new Server(port);
  }

  /** Tests server initialization. Verifies that the server is initialized with the correct port. */
  @Test
  void testServerInitialization() {
    assertEquals(port, server.getGameState().getGameInfo().getPort());
  }

  /**
   * Tests adding a player to the server. Verifies that the player is added correctly and a
   * PlayerJoinResponse message is sent.
   */
  @Test
  void testAddPlayer() throws IOException {
    ConnectionManager cm = mock(ConnectionManager.class);
    server.addPlayer("Player1", cm, "1");

    verify(cm).sendMessage(JsonConverter.toJson(new PlayerJoinResponse("Player1", true, "1")));
    assertEquals(1, server.getGameState().getPlayers().size());
    assertEquals("Player1", server.getGameState().getPlayers().get(0));
  }

  /**
   * Tests server initialization with a negative port number. Verifies that an
   * IllegalArgumentException is thrown with the correct message.
   */
  @Test
  void testInvalidPortNegative() {
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              new Server(-1);
            });

    String expectedMessage = "Port number must be between 0 and 65535";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  /**
   * Tests server initialization with a port number that is too high. Verifies that an
   * IllegalArgumentException is thrown with the correct message.
   */
  @Test
  void testInvalidPortTooHigh() {
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              new Server(70000);
            });

    String expectedMessage = "Port number must be between 0 and 65535";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }
}
