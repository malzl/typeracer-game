package com.typewrite.game.controller;

import com.typewrite.game.GameManager;
import com.typewrite.game.util.LogUtil;
import com.typewrite.game.util.TextUtil;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Abstract controller for selecting or customizing a book. Provides common functionalities for
 * controllers that manage book selections and difficulties.
 */
public abstract class AbstractSelectOrCustomController {
  protected volatile int difficultyIndex; // volatile to ensure updating across classes

  /**
   * Initializes the book selection UI.
   *
   * @param index the index of the book.
   * @param hbox the HBox container for displaying the book.
   */
  protected void initBooks(int index, HBox hbox) {
    Pane book = new Pane();
    book.getStyleClass().add("card-pane");
    book.setStyle(
        "-fx-border-radius: 20px; "
            + "-fx-background-color: #cc83ee; "
            + "-fx-padding: 10px; "
            + "-fx-effect: dropshadow(gaussian, black, 10, 0.5, 0, 0);");
    book.setPrefWidth(300);
    book.setPrefHeight(150);
    book.setId(String.valueOf(index));
    book.setOnMouseClicked(mouseEvent -> this.selectBook(book.getId()));

    Label title = new Label();
    title.setTextFill(Color.WHITE);
    title.setWrapText(true);
    title.setAlignment(Pos.TOP_CENTER);
    title.setLayoutX(25);
    title.setLayoutY(50);
    title.setPrefWidth(240);
    title.setPrefHeight(80);
    title.setFont(new Font("Arial Black", 20));
    title.setText(GameManager.BOOK_MAP.get(index));
    book.getChildren().addAll(title);
    hbox.getChildren().add(book);
  }

  /**
   * Selects a book based on its ID and loads its sentences.
   *
   * @param bookId the ID of the book to select.
   */
  protected void selectBook(String bookId) {
    int bookIndex = Integer.parseInt(bookId);
    try {
      System.out.println("Loading book " + bookIndex + " with difficulty: " + difficultyIndex);
      List<String> sentences = TextUtil.loadRandomSentences(bookIndex, difficultyIndex);
      completed(sentences);
    } catch (Exception e) {
      LogUtil.error("Error loading book: " + e.getMessage());
    }
  }

  /**
   * Updates the difficulty index based on the selected difficulty level.
   *
   * @param difficulty the selected difficulty level.
   */
  protected void updateDifficultyIndex(String difficulty) {
    switch (difficulty) {
      case "Easy":
        difficultyIndex = 0;
        break;
      case "Medium":
        difficultyIndex = 1;
        break;
      case "Hard":
        difficultyIndex = 2;
        break;
      case "Impossible":
        difficultyIndex = 3;
        break;
      case "I'm into math!":
        difficultyIndex = 4;
        break;
      case "Reverse":
        difficultyIndex = 5;
        break;
      default:
        difficultyIndex = 0; // Defaulting to "Easy"
    }
    LogUtil.info("Difficulty Index Updated: " + difficultyIndex);
  }

  /**
   * Completes the selection process and performs actions with the loaded sentences.
   *
   * @param sentences the list of sentences loaded from the selected book.
   */
  protected abstract void completed(List<String> sentences);
}
