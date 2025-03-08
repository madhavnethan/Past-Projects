package view;

import model.ReadonlyModel;
import model.Cell;
import model.GameCard;
import model.CellType;
import model.CardColor;

import java.util.ArrayList;

/**
 * Renders a text view version of the game.
 */
public class ThreeTriosTextView implements View {
  private ReadonlyModel model;

  public ThreeTriosTextView(ReadonlyModel model) {
    this.model = model;
  }

  /**
   * Uses all the methods listed below and creates a string version of the game.
   * @return String version of the game.
   */
  @Override
  public String toString() {
    return player(model) + "\n"
            + board(model)
            + "Hand:" + "\n"
            + cards(model);
  }

  @Override
  public CardColor getColor() {
    return null;
  }

  public void selectCard(GameCard card) {
    //select card.
  }

  public GameCard getSelectedCard() {
    return null;
  }

  public void clearSelectedCard() {
    // clears selected card.
  }

  public boolean isHuman() {
    return false;
  }

  private String player(ReadonlyModel model) {
    if (model.getCurrentPlayer() == CardColor.BLUE) {
      return "Player: BLUE";
    }
    else if (model.getCurrentPlayer() == CardColor.RED) {
      return "Player: RED";
    } else {
      throw new IllegalArgumentException("Player needs a color");
    }
  }

  private ArrayList<String> symbols(ReadonlyModel model) {
    ArrayList<String> letters = new ArrayList<>();
    for (int i = 0; i < model.getBoard().getRows(); i++) {
      for (int j = 0; j < model.getBoard().getCols(); j++) {
        Cell cell = model.getBoard().getGrid()[j][i];
        if (cell.getType() == CellType.X) {
          letters.add(" ");
        } else if (cell.getType() == CellType.C) {
          if (cell.getIsCard()) {
            if (cell.getPlayedCard().getCard().getColor() == CardColor.BLUE) {
              letters.add("B");
            } else if (cell.getPlayedCard().getCard().getColor() == CardColor.RED) {
              letters.add("R");
            }
          } else {
            letters.add("_");
          }
        }
      }
    }
    return letters;
  }

  private String board(ReadonlyModel model) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < model.getBoard().getRows(); i++) {
      for (int j = 0; j < model.getBoard().getCols(); j++) {
        sb.append(symbols(model).get(i + j));
      }
      sb.append("\n");
    }
    return sb.toString();
  }

  private String cards(ReadonlyModel model) {
    ArrayList<GameCard> hand = new ArrayList<>();
    if (model.getCurrentPlayer() == CardColor.BLUE) {
      hand = model.getBlueHand();
    }
    if (model.getCurrentPlayer() == CardColor.RED) {
      hand = model.getRedHand();
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < hand.size(); i++) {
      if (i == hand.size() - 1) {
        sb.append(hand.get(i).toString());
      } else {
        sb.append(hand.get(i).toString());
        sb.append("\n");
      }
    }
    return sb.toString();
  }
}
