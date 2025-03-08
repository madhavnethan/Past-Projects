package model;

import java.util.Optional;

/**
 * Represents a human player in the Three Trios game.
 */
public class HumanPlayer implements Player {
  private final CardColor color;
  protected final ReadonlyThreeTriosModel model; // The color of this player
  private GameCard selectedCard; // The currently selected card

  /**
   * Constructs a HumanPlayer with the given color.
   *
   * @param model The color of the player (e.g., RED or BLUE).
   */
  public HumanPlayer(ReadonlyThreeTriosModel model, CardColor color) {
    this.model = model;
    this.color = color;
    this.selectedCard = null; // No card selected initially
  }

  @Override
  public CardColor getColor() {
    return color;
  }

  @Override
  public void play() {
    // does nothing because human.
  }

  /**
   * Selects a card from the player's hand.
   *
   * @param card The card to select.
   */
  public void selectCard(GameCard card) {
    this.selectedCard = card;
  }

  /**
   * Retrieves the currently selected card.
   *
   * @return The selected card, or null if no card is selected.
   */
  public Optional<GameCard> getSelectedCard() {
    return Optional.ofNullable(selectedCard);
  }

  /**
   * Clears the player's selection after a card is played.
   */
  public void clearSelection() {
    this.selectedCard = null;
  }

  @Override
  public boolean isMachine() {
    return false; // Indicates that this is a human player
  }

}

