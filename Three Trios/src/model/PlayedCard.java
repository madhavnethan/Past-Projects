package model;

/**
 * This represents a card that is played on the board.
 */
public class PlayedCard {
  protected GameCard card;
  private int xposn;
  private int yposn;

  /**
   * This represents the GameCard that is on the board.
   * @param card GameCard that PlayedCard represents.
   * @param xposn x-position of the grid where card is placed.
   * @param yposn y-position of the grid where card is placed.
   */
  public PlayedCard(GameCard card, int xposn, int yposn) {
    this.card = card;
    this.xposn = xposn;
    this.yposn = yposn;
  }

  public GameCard getCard() {
    return card;
  }

  public CardColor getColor() {
    return this.card.getColor();
  }

  public int getXposn() {
    return xposn;
  }

  public int getYposn() {
    return yposn;
  }
}
