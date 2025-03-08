package model;

import java.util.ArrayList;

/**
 * This interface made to read all the methods that are implemented in the model.
 */
public interface ReadonlyModel {
  /**
   * Returns true if game is over.
   * @return a boolean referencing the state of game.
   */
  boolean getGameOver();

  /**
   * Gets the current player.
   * @return The color of the current player.
   */
  CardColor getCurrentPlayer();

  /**
   * Gets the game board.
   * @return The current game board.
   */
  AbstractBoard getBoard();

  /**
   * Gets the Red player's hand.
   * @return A list of GameCard objects for the Red player's hand.
   */
  ArrayList<GameCard> getRedHand();

  /**
   * Gets the Blue player's hand.
   * @return A list of GameCard objects for the Blue player's hand.
   */
  ArrayList<GameCard> getBlueHand();

  /**
   * A function that determines when the game is over.
   * When there are no spots left on the board the game will end and there will be a winner.
   * @return Winning color.
   */
  CardColor gameOver();

  /**
   * Returns the number of flipped cards when a card is placed at a specific cell.
   * @return an integer representing number of flipped cards.
   */
  int getNumCardsFlipped();
}
