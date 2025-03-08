package model;

import java.util.Map;

/**
 * Represents a board of the game that allows cards to be played on.
 */
public interface Board {

  /**
   * Checks if a specific cell on the board is empty and available for placing a card.
   * @param x The row position of the cell.
   * @param y The column position of the cell.
   * @return True if the cell is a card cell and does not contain a card; false otherwise.
   */
  boolean isCellEmpty(int x, int y);

  /**
   * Places a card in a specified cell on the board if the cell is empty.
   * @param card The GameCard to place on the board.
   * @param x The row position of the cell.
   * @param y The column position of the cell.
   * @throws IllegalArgumentException if the cell is already occupied or is invalid.
   */
  void placeCard(GameCard card, int x, int y);

  /**
   * Checks if the board is full, meaning all card cells are occupied.
   * @return True if the board is full; false otherwise.
   */
  public boolean isFull();

  /**
   * Gets the cards adjacent to a specified cell position on the board.
   * This is used in the battle phase to determine neighboring cards.
   * @param x The row position of the cell.
   * @param y The column position of the cell.
   * @return A map containing the positions ("Top", "Bottom", "Left", "Right")
   *     and their adjacent PlayedCard objects.
   */
  Map<String, PlayedCard> getAdjacentCards(int x, int y);

  /**
   * Retrieves the card placed at a specific cell position on the board.
   * @param x The row position of the cell.
   * @param y The column position of the cell.
   * @return The PlayedCard at the specified position, or null if no card is placed.
   */
  PlayedCard getPlayedCardAt(int x, int y);

  /**
   * Counts the number of cards of a specific color on the board.
   * @param color The color of the cards to count.
   * @return The number of cards of the specified color.
   */
  int countCards(CardColor color);
}
