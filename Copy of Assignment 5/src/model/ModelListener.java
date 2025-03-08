package model;

/**
 * Interface for listening to events from the ThreeTriosModel.
 * Controllers implement this to react to changes in the model.
 */
public interface ModelListener {
  /**
   * Triggered when the active player's turn changes.
   *
   * @param currentPlayer The current active player.
   */
  void onTurnChanged(CardColor currentPlayer);

  /**
   * Triggered when the game ends.
   *
   * @param winner The winner of the game (or NONE in case of a tie).
   */
  void onGameOver(CardColor winner);

  /**
   * Triggered when the board state is updated.
   */
  void onBoardUpdated();
}

