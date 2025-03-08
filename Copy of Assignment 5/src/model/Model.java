package model;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Represents the interface of the Three Trios game model.
 */
public interface Model extends ReadonlyModel {

  /**
   * Places a card on the board at a specified position.
   * @param card The card to place.
   * @param x The row position on the board.
   * @param y The column position on the board.
   * @throws IllegalStateException if the game is not running.
   * @throws IllegalArgumentException if the cell is not empty.
   */
  void placeCard(GameCard card, int x, int y);

  /**
   * Starts the game by initializing the board and dealing hands to each player.
   * @param boardFile Path to the file that contains the board configuration.
   * @param cardFile Path to the file that contains the card configuration.
   * @throws IllegalStateException if the number of cards is not even.
   */
  void startGame(String boardFile, String cardFile);

  /**
   * Reads all available cards from a file, shuffles them, and returns the list.
   * @param filePath Path to the card configuration file.
   * @return A shuffled list of GameCard objects.
   * @throws RuntimeException if the file is not found.
   */
  ArrayList<GameCard> getAllCards(String filePath);

  /**
   * Loads the board configuration from a file.
   * @param filePath Path to the board configuration file.
   * @return The initialized Board.
   * @throws RuntimeException if the file is not found or the configuration is invalid.
   */
  Board fileToBoard(String filePath);

  void addListener(ModelListener listener);

  void removeListener(ModelListener listener);

  void addTurnNotificationListener(Consumer<CardColor> listener);

  boolean isPlayersTurn(CardColor color);

}
