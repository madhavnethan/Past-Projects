package view;

import model.CardColor;

/**
 * An interface to represent the type of views of this game.
 */
public interface View {
  /**
   * Creates a text view of the current state of the game model.
   * @return text view as a String.
   */
  String toString();

  CardColor getColor();

}
