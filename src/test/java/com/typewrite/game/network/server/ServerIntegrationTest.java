package com.typewrite.game.network.server;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ServerIntegrationTest {

  private static final int TEST_PORT = 12345;
  private Server server;
  private ExecutorService executorService;

  @BeforeEach
  void setUp() {
    server = new Server(TEST_PORT);
    executorService = Executors.newSingleThreadExecutor();
    Future<?> future =
        executorService.submit(
            () -> {
              try {
                System.out.println("Starting server...");
                server.start();
              } catch (IOException e) {
                System.err.println("Error starting server: " + e.getMessage());
                e.printStackTrace();
              }
            });

    try {
      Thread.sleep(1000); // Spare time for the server to start
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Server started successfully");
  }

  @AfterEach
  void tearDown() throws InterruptedException {
    System.out.println("Tearing down test...");
    server.stop();
    executorService.shutdownNow();
    if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
      System.err.println("ExecutorService did not terminate in time");
    }
    Thread.sleep(1000); // Ensure port is released
    System.out.println("Test tear down complete");
  }

  /**
   * Tests player joining and game start. Verifies that players can join the game, send ready
   * notifications, and receive game start notifications.
   */
  @Test
  @Order(1)
  @Timeout(10)
  void testPlayerJoinAndGameStart() throws IOException, InterruptedException {
    System.out.println("Starting testPlayerJoinAndGameStart");
    TestClient client1 = new TestClient("Player1", TEST_PORT);
    TestClient client2 = new TestClient("Player2", TEST_PORT);

    client1.joinGame();
    client2.joinGame();

    assertTrue(client1.waitForMessage("PlayerJoinResponse"));
    assertTrue(client2.waitForMessage("PlayerJoinResponse"));

    client1.sendReady();
    client2.sendReady();

    assertTrue(client1.waitForMessage("PlayerReadyResponse"));
    assertTrue(client2.waitForMessage("PlayerReadyResponse"));

    client1.selectText(Arrays.asList("Test sentence 1", "Test sentence 2"));

    assertTrue(client1.waitForMessage("GameStartNotification"));
    assertTrue(client2.waitForMessage("GameStartNotification"));

    client1.close();
    client2.close();
    System.out.println("Finished testPlayerJoinAndGameStart");
  }

  /**
   * Tests game progress updates. Verifies that players can update their progress and receive
   * progress broadcasts.
   */
  @Test
  @Order(2)
  @Timeout(10)
  void testGameProgress() throws IOException, InterruptedException {
    System.out.println("Starting testGameProgress");
    TestClient client1 = new TestClient("Player1", TEST_PORT);
    TestClient client2 = new TestClient("Player2", TEST_PORT);

    client1.joinGame();
    client2.joinGame();
    client1.sendReady();
    client2.sendReady();
    client1.selectText(Arrays.asList("Test sentence"));

    assertTrue(client1.waitForMessage("GameStartNotification"));
    assertTrue(client2.waitForMessage("GameStartNotification"));

    client1.updateProgress(5);
    client2.updateProgress(3);

    assertTrue(client1.waitForMessage("PlayerProgressBroadcast"));
    assertTrue(client2.waitForMessage("PlayerProgressBroadcast"));

    client1.finishGame();

    assertTrue(client1.waitForMessage("GameFinished"));
    assertTrue(client2.waitForMessage("GameFinished"));

    client1.close();
    client2.close();
    System.out.println("Finished testGameProgress");
  }

  /**
   * Tests the maximum number of players. Verifies that the server correctly handles the maximum
   * number of players and rejects additional players.
   */
  @Test
  @Order(3)
  @Timeout(10)
  void testMaxPlayers() {
    System.out.println("Starting testMaxPlayers");
    List<TestClient> clients = new ArrayList<>();
    try {
      for (int i = 0; i < GameState.MAX_PLAYER; i++) {
        System.out.println("Creating player " + (i + 1));
        TestClient client = new TestClient("Player" + (i + 1), TEST_PORT);
        client.joinGame();
        boolean received = client.waitForMessage("PlayerJoinResponse");
        System.out.println("Player" + (i + 1) + " received response: " + received);
        assertTrue(received, "Player" + (i + 1) + " did not receive PlayerJoinResponse");
        String joinResponse = client.getMessageOfType("PlayerJoinResponse");
        System.out.println("Player" + (i + 1) + " join response: " + joinResponse);
        assertTrue(
            joinResponse.contains("\"success\":true"),
            "Player" + (i + 1) + " join was not successful");
        clients.add(client);
      }

      System.out.println("Creating extra player");
      TestClient extraClient = new TestClient("ExtraPlayer", TEST_PORT);
      extraClient.joinGame();
      boolean received = extraClient.waitForMessage("PlayerJoinResponse");
      System.out.println("ExtraPlayer received response: " + received);
      assertTrue(received, "ExtraPlayer did not receive PlayerJoinResponse");
      String joinResponse = extraClient.getMessageOfType("PlayerJoinResponse");
      System.out.println("ExtraPlayer join response: " + joinResponse);
      assertTrue(joinResponse.contains("\"success\":false"), "ExtraPlayer join was not rejected");

      extraClient.close();
    } catch (Exception e) {
      System.err.println("Exception in testMaxPlayers: " + e.getMessage());
      e.printStackTrace();
    } finally {
      for (TestClient client : clients) {
        try {
          client.close();
        } catch (Exception e) {
          System.err.println("Error closing client: " + e.getMessage());
        }
      }
    }
    System.out.println("Finished testMaxPlayers");
  }
}
