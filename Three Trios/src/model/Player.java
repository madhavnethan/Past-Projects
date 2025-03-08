package model;

/**
 * Represents a player in the Three Trios game.
 */
public interface Player {

  /**
   * Returns the color of the player.
   *
   * @return The player's color (e.g., RED or BLUE).
   */
  CardColor getColor();

  void play();


  /**
   * Determines whether this player is controlled by a machine (AI).
   *
   * @return True if the player is a machine; false otherwise.
   */
  boolean isMachine();

}

