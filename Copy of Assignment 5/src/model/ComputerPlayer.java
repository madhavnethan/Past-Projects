package model;

import java.util.Optional;

/**
 * Represents a human player in the Three Trios game.
 */
public class ComputerPlayer implements Player {
  private final CardColor color;
  private final ThreeTriosModel model; // The color of this player
  private PlayedCard strat;
  private GameCard selectedCard;  // The currently selected card
  ModelListener listener;

  /**
   * Constructs a HumanPlayer with the given color.
   *
   * @param model The color of the player (e.g., RED or BLUE).
   */
  public ComputerPlayer(ThreeTriosModel model, CardColor color, PlayedCard strat) {
    this.model = model;
    this.color = color;
    this.strat = strat;
    this.selectedCard = null; // No card selected initially
  }

  public PlayedCard getStrat() {
    return strat;
  }

  @Override
  public CardColor getColor() {
    return color;
  }

  @Override
  public void play() {
    model.placeCard(strat.getCard(), strat.getXposn(), strat.getYposn());
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
    return true; // Indicates that this is a human player
  }

}
