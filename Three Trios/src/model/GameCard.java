package model;

/**
 * Represents every gamecard in the game, includes the name, color and values of the card.
 */
public class GameCard {
  protected String name;
  public CardColor color;
  protected int northNum;
  protected int southNum;
  protected int eastNum;
  protected int westNum;

  /**
   * Represents every gamecard in the game, includes the name, color and values of the card.
   *
   * @param name     Name of card.
   * @param color    Color of card to respective player.
   * @param northNum North value.
   * @param southNum South Value.
   * @param eastNum  East Value.
   * @param westNum  West Value.
   */
  public GameCard(String name, CardColor color,
                  int northNum, int southNum, int eastNum, int westNum) {
    this.name = name;
    this.color = color;
    this.northNum = northNum;
    this.southNum = southNum;
    this.eastNum = eastNum;
    this.westNum = westNum;
  }

  public void changeColor(CardColor color) {
    this.color = color;
  }

  public CardColor getColor() {
    return color;
  }

  public int getNorthNum() {
    return northNum;
  }

  public int getSouthNum() {
    return southNum;
  }

  public int getEastNum() {
    return eastNum;
  }

  public int getWestNum() {
    return westNum;
  }


  /**
   * Converts all the values from the card to a String.
   * @param num integer value from the card
   * @return A string version of the number, 10 is represented as A
   */
  public String getNumAsString(int num) {
    if (num == 10) {
      return "A";
    } else {
      return Integer.toString(num);
    }
  }

  /**
   * Turns a GameCard into a string.
   *
   * @return String that represents a GameCard
   */
  public String toString() {
    return this.name + " "
            + getNumAsString(northNum) + " "
            + getNumAsString(southNum) + " "
            + getNumAsString(eastNum) + " "
            + getNumAsString(westNum);
  }
}
