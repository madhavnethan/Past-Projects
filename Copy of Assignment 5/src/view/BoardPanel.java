package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

import javax.swing.JPanel;
import model.CardColor;
import model.CellType;
import model.GameCard;
import model.PlayedCard;
import model.ReadonlyThreeTriosModel;

/**
 * Panel for displaying and interacting with the game board.
 */
public class BoardPanel extends JPanel {
  private final ReadonlyThreeTriosModel model;
  private final RedHandPanel redHandPanel;
  private final BlueHandPanel blueHandPanel;
  private int selectedIndex = 0;
  private GameCard currentCard;

  public BoardPanel(ReadonlyThreeTriosModel model,
                    RedHandPanel redHandPanel,
                    BlueHandPanel blueHandPanel) {
    this.model = model;
    this.redHandPanel = redHandPanel;
    this.blueHandPanel = blueHandPanel;
    setBackground(Color.LIGHT_GRAY);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    drawBoard((Graphics2D) g);
  }

  private void drawBoard(Graphics2D g2) {
    int gridSizeRow = model.getBoard().getRows();
    int gridSizeCol = model.getBoard().getCols();
    int cellWidth = getWidth() / gridSizeCol;
    int cellHeight = getHeight() / gridSizeRow;

    for (int row = 0; row < gridSizeRow; row++) {
      for (int col = 0; col < gridSizeCol; col++) {
        // Draw cell background
        if (model.getBoard().getGrid()[col][row].getType().equals(CellType.C)) {
          g2.setColor(Color.YELLOW);
          g2.fillRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
        }

        if (model.getBoard().getGrid()[col][row].getType().equals(CellType.X)) {
          g2.setColor(Color.GRAY);
          g2.fillRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
        }

        // Draw cell border
        g2.setColor(Color.BLACK);
        g2.drawRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);

        // Check if a card is placed in the cell
        PlayedCard playedCard = model.getBoard().getGrid()[col][row].getPlayedCard();
        if (playedCard != null && model.getBoard().getGrid()[col][row].getIsCard()) {
          GameCard card = playedCard.getCard();

          // Draw card background color
          g2.setColor(card.getColor() == CardColor.RED ? Color.PINK : Color.CYAN);
          g2.fillRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);

          // Draw card values
          g2.setColor(Color.BLACK);
          g2.drawString(card.getNumAsString(card.getNorthNum()),
                  col * cellWidth + cellWidth / 2, row * cellHeight + cellHeight / 4);
          g2.drawString(card.getNumAsString(card.getSouthNum()),
                  col * cellWidth + cellWidth / 2, row * cellHeight + 3 * cellHeight / 4);
          g2.drawString(card.getNumAsString(card.getWestNum()),
                  col * cellWidth + cellWidth / 4, row * cellHeight + cellHeight / 2);
          g2.drawString(card.getNumAsString(card.getEastNum()),
                  col * cellWidth + 3 * cellWidth / 4, row * cellHeight + cellHeight / 2);

        }

        // Draw hint numbers if hint mode is enabled for the current player
        if (model.areHintsEnabled(model.getCurrentPlayer())) {
          if (!model.getBoard().getGrid()[col][row].getIsCard() &&
                  model.getBoard().getGrid()[col][row].getType() == CellType.C) {
//            GameCard currentCard = model.getCurrentPlayer() == CardColor.RED
//                    ? model.getRedHand().get(selectedIndex)
//                    : model.getBlueHand().get(selectedIndex);
//            currentCard = new GameCard("pc", CardColor.NONE, 0, 0 , 0, 0);
//            if (selectedIndex > 0) {
//              if (model.getCurrentPlayer() == CardColor.RED) {
//                currentCard = model.getRedHand().get(selectedIndex);
//              }
//              else if (model.getCurrentPlayer() == CardColor.BLUE) {
//                currentCard = model.getBlueHand().get(selectedIndex);
//              }
//            }
//            else {
//              currentCard = null;
//            }

            if (model.getModel().getSelectedCard() != null) {
              GameCard currentCard = model.getModel().getSelectedCard();
              int flipCount = model.getModel().checkFlipCount(currentCard, col, row);

              g2.drawString(String.valueOf(flipCount),
                      col * cellWidth + cellWidth / 2,
                      row * cellHeight + cellHeight / 2);
            }


          }
        }

      }
    }
  }

  public void setSelectedIndex(int selectedIndex) {
    if (selectedIndex > 0) {
      this.selectedIndex = selectedIndex;
    }
    else {
      currentCard = null;
    }
  }
}
