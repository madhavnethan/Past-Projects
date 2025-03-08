package model;

import java.util.ArrayList;

/**
 * Read only version of the model.
 */
public class ReadonlyThreeTriosModel implements ReadonlyModel {
  ThreeTriosModel model = new ThreeTriosModel();

  public ReadonlyThreeTriosModel(ThreeTriosModel model) {
    this.model = model;
  }

  @Override
  public boolean getGameOver() {
    return model.getGameOver();
  }

  @Override
  public CardColor getCurrentPlayer() {
    return model.getCurrentPlayer();
  }

  @Override
  public AbstractBoard getBoard() {
    return model.getBoard();
  }

  @Override
  public ArrayList<GameCard> getRedHand() {
    return model.getRedHand();
  }

  @Override
  public ArrayList<GameCard> getBlueHand() {
    return model.getBlueHand();
  }

  @Override
  public CardColor gameOver() {
    return model.gameOver();
  }

  @Override
  public int getNumCardsFlipped() {
    return model.getNumCardsFlipped();
  }

  public boolean areHintsEnabled(CardColor currentPlayer) { return model.areHintsEnabled(model.getCurrentPlayer()); }

  public ThreeTriosModel getModel() { return model; }
}
