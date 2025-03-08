package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an abstract implementation of a game board for the Three Trios game.
 * This class defines methods to initialize, load, and interact with the board's cells and cards.
 */
public class AbstractBoard implements Board {

  private int rows;       // Number of rows in the board
  private int cols;       // Number of columns in the board
  private Cell[][] grid;  // 2D array representing the board cells

  /**
   * Constructs an AbstractBoard with specified dimensions and grid configuration.
   * @param rows The number of rows on the board.
   * @param cols The number of columns on the board.
   * @param grid The 2D array of Cell objects representing the board.
   */
  public AbstractBoard(int cols, int rows, Cell[][] grid) {
    this.rows = rows;
    this.cols = cols;
    this.grid = grid;
  }

  public int getRows() {
    return rows;
  }

  public int getCols() {
    return cols;
  }

  public Cell[][] getGrid() {
    return grid;
  }

  /**
   * Gets copy of the grid.
   * @return grid of board.
   */
  public Cell[][] getGridCopy() {
    Cell[][] newGrid = new Cell[rows][cols];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        newGrid[i][j] = grid[i][j].cellCopy();
      }
    }
    return newGrid;
  }

  /**
   * Checks if a specific cell on the board is empty and available for placing a card.
   * @param x The row position of the cell.
   * @param y The column position of the cell.
   * @return True if the cell is a card cell and does not contain a card; false otherwise.
   */
  public boolean isCellEmpty(int x, int y) {
    Cell cell = grid[x][y];
    return !cell.getIsCard();
  }

  public boolean isHole(int x, int y) {
    return (grid[x][y].getType() == CellType.X);
  }

  /**
   * Places a card in a specified cell on the board if the cell is empty.
   * @param card The GameCard to place on the board.
   * @param x The row position of the cell.
   * @param y The column position of the cell.
   * @throws IllegalArgumentException if the cell is already occupied or is invalid.
   */
  public void placeCard(GameCard card, int x, int y) {
    if (isCellEmpty(x, y)) {
      grid[x][y].setCard(new PlayedCard(card, x, y));
    } else {
      throw new IllegalArgumentException("Cell is occupied or invalid.");
    }
  }

  /**
   * Checks if the board is full, meaning all card cells are occupied.
   * @return True if the board is full; false otherwise.
   */
  public boolean isFull() {
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        if (grid[r][c].getType() == CellType.C && !grid[r][c].getIsCard()) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Counts the number of cards of a specific color on the board.
   * @param color The color of the cards to count.
   * @return The number of cards of the specified color.
   */
  public int countCards(CardColor color) {
    int count = 0;
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        PlayedCard card = grid[r][c].getPlayedCard();
        if (card != null && card.getColor() == color) {
          count++;
        }
      }
    }
    return count;
  }

  /**
   * Retrieves the card placed at a specific cell position on the board.
   * @param x The row position of the cell.
   * @param y The column position of the cell.
   * @return The PlayedCard at the specified position, or null if no card is placed.
   */
  public PlayedCard getPlayedCardAt(int x, int y) {
    return grid[x][y].getPlayedCard();
  }

  /**
   * Gets the cards adjacent to a specified cell position on the board.
   * This is used in the battle phase to determine neighboring cards.
   * @param x The row position of the cell.
   * @param y The column position of the cell.
   * @return A map containing the positions ("Top", "Bottom", "Left", "Right")
   *     and their adjacent PlayedCard objects.
   */
  public Map<String, PlayedCard> getAdjacentCards(int x, int y) {
    Map<String, PlayedCard> adjacentCards = new HashMap<>();

    if ((y > 0) && (grid[x][y - 1].getType() == CellType.C) &&
            (grid[x][y - 1].getIsCard())) {
      adjacentCards.put("Top", grid[x][y - 1].getPlayedCard());
    }
    if ((y < rows - 1) && (grid[x][y + 1].getType() == CellType.C) &&
            (grid[x][y + 1].getIsCard()) ) {
      adjacentCards.put("Bottom", grid[x][y + 1].getPlayedCard());
    }
    if ((x > 0) && (grid[x - 1][y].getType() == CellType.C) &&
            (grid[x - 1][y].getIsCard())) {
      adjacentCards.put("Left", grid[x - 1][y].getPlayedCard());
    }
    if ((x < cols - 1) && (grid[x + 1][y].getType() == CellType.C) &&
            (grid[x + 1][y].getIsCard())) {
      adjacentCards.put("Right", grid[x + 1][y].getPlayedCard());
    }
    return adjacentCards;
  }

}
