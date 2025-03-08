package model;

/**
 * This is a representation of a cell on the game board.
 */
public class Cell {
  private int xposn;
  private int yposn;
  private boolean isCard;
  private CellType type;
  private PlayedCard playedCard;

  /**
   * A cell has a x and y position on the board and can either be a
   *     place where a card can be placed or it can be a hole.
   * @param xposn x position of cell
   * @param yposn y position of cell
   * @param isCard is there a card on cell
   * @param type is it placeable or is it a hole
   * @param playedCard PlayedCard in the cell
   */
  public Cell(int xposn,
              int yposn,
              boolean isCard,
              CellType type,
              PlayedCard playedCard) {
    this.xposn = xposn;
    this.yposn = yposn;
    this.isCard = isCard;
    this.type = type;
    this.playedCard = playedCard;
  }

  /**
   * A cell has a x and y position on the board and can either be a
   *     place where a card can be placed or it can be a hole.
   * @param xposn x position of cell
   * @param yposn y position of cell
   * @param isCard is there a card on cell
   * @param type is it placeable or is it a hole
   */
  public Cell(int xposn,
              int yposn,
              boolean isCard,
              CellType type
              ) {
    this.xposn = xposn;
    this.yposn = yposn;
    this.isCard = isCard;
    this.type = type;
  }

  public CellType getType() {
    return type;
  }

  public Boolean getIsCard() {
    return isCard;
  }

  public PlayedCard getPlayedCard() {
    return playedCard;
  }

  public Cell cellCopy() {
    return new Cell(xposn, yposn, isCard, type, playedCard);
  }

  /**
   * This sets the PlayedCard to this specific cell.
   * @param card PlayedCard to set to the cell.
   */
  public void setCard(PlayedCard card) {
    this.playedCard = card;
    this.isCard = true;
  }
}
