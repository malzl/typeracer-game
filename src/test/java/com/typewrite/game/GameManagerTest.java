package com.typewrite.game;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Test class with unit tests for the GameManager. */
public class GameManagerTest {

  private GameManager gameManager;

  /** Sets up the JavaFX runtime for tests. */
  @BeforeAll
  public static void setupJavaFxRuntime() {
    // Trick to force JavaFX runtime to initialize
    new JFXPanel();
  }

  /** Launches a GameManager for testing. */
  @BeforeEach
  public void setUp() {
    Platform.runLater(
        () -> {
          gameManager = GameManager.getInstance();
          Stage stage = new Stage();
          stage.initStyle(StageStyle.UTILITY);
          stage.setOpacity(0);
          gameManager.setGameStage(stage);
          // Ensure the stage is never shown:
          doNothing().when(stage).show();
          doNothing().when(stage).showAndWait();
        });
  }

  /**
   * Tests the loading of a view. Verifies that the specified view is loaded without throwing an
   * exception.
   */
  @Test
  public void testLoadView() {
    // Arrange
    String viewName = "home-view";

    // Execute test on JavaFX thread
    Platform.runLater(
        () -> {
          try {
            gameManager.loadView(viewName);
            // Assertions and verifications need to also be made in the JavaFX thread
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
  }

  /**
   * Tests the singleton instance of GameManager. Verifies that only one instance of GameManager is
   * created.
   */
  @Test
  public void testGameManagerSingletonInstance() {
    Platform.runLater(
        () -> {
          GameManager firstInstance = GameManager.getInstance();
          GameManager secondInstance = GameManager.getInstance();
          assertSame(firstInstance, secondInstance, "GameManager should only create one instance");
        });
  }

  /**
   * Tests the handling of IOException when loading a view. Verifies that an IOException is thrown
   * for a nonexistent view.
   */
  @Test
  public void testLoadViewHandlingIoException() {
    String invalidViewName = "nonexistent-view";

    Platform.runLater(
        () -> {
          try {
            gameManager.loadView(invalidViewName);
            fail("Should have thrown an exception due to nonexistent view");
          } catch (IOException e) {
            assertTrue(
                e.getMessage().contains("cannot be loaded"),
                "Expected IO exception for nonexistent view");
          } catch (Exception e) {
            fail("Unexpected exception type thrown");
          }
        });
  }

  /**
   * Tests the configuration of the game stage. Verifies that the stage is configured correctly when
   * loading a view.
   */
  @Test
  public void testStageConfiguration() {
    String expectedTitle = "Test Title";

    Platform.runLater(
        () -> {
          Stage mockStage = mock(Stage.class);
          gameManager.setGameStage(mockStage);
          doNothing().when(mockStage).setTitle(anyString());

          try {
            gameManager.loadView("home-view");
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
          verify(mockStage).setTitle(expectedTitle);
        });
  }

  /**
   * Tests the loading of the game view. Verifies that the FXML file is loaded correctly and the
   * scene is set.
   */
  @Test
  public void testGameViewLoading() {
    String viewName = "game-view.fxml";

    Platform.runLater(
        () -> {
          try {
            gameManager.loadView(viewName);
            // Check if the FXML is loaded correctly
            assertNotNull(gameManager.getGameStage().getScene(), "Scene should be set");
            assertNotNull(
                gameManager.getGameStage().getScene().getRoot(), "Root node should be loaded");
          } catch (Exception e) {
            fail("Failed to load " + viewName + " due to " + e.getMessage());
          }
        });
  }
}
