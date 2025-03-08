package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

import javax.swing.JPanel;
import model.ReadonlyThreeTriosModel;
import model.GameCard;

/**
 * Panel for displaying and selecting cards in the Red player's hand.
 */
public class RedHandPanel extends JPanel {
  private final ReadonlyThreeTriosModel model;
  private int selectedIndex = -1;

  /**
   * Panel for displaying and selecting cards in the Red player's hand.
   */
  public RedHandPanel(ReadonlyThreeTriosModel model) {
    this.model = model;
    setPreferredSize(new Dimension(200, 1000));
    setBackground(Color.PINK);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    drawRedHand((Graphics2D) g);
  }

  private void drawRedHand(Graphics2D g2) {
    int cardHeight = 0;
    if (model.getRedHand().size() >= 1) {
      cardHeight = getHeight() / model.getRedHand().size();
    }
    else {
      g2.setColor(Color.PINK);
      g2.fillRect(0, 0, getWidth(), cardHeight);
    }
    for (int i = 0; i < model.getRedHand().size(); i++) {
      GameCard card = model.getRedHand().get(i);
      int yOffset = i * cardHeight;

      g2.setColor(Color.PINK);
      if (i == selectedIndex) {
        g2.setColor(Color.GRAY);
      }
      g2.fillRect(0, yOffset, getWidth(), cardHeight);

      g2.setColor(Color.BLACK);
      g2.drawRect(0, yOffset, getWidth(), cardHeight);

      g2.drawString(card.getNumAsString(card.getNorthNum()),
              getWidth() / 2, yOffset + cardHeight / 4);
      g2.drawString(card.getNumAsString(card.getSouthNum()),
              getWidth() / 2, yOffset + cardHeight * 3 / 4);
      g2.drawString(card.getNumAsString(card.getWestNum()),
              getWidth() / 4, yOffset + cardHeight / 2);
      g2.drawString(card.getNumAsString(card.getEastNum()),
              getWidth() * 3 / 4, yOffset + cardHeight / 2);

    }
  }

  public void setSelectedIndex(int index) {
    this.selectedIndex = index;
    repaint();
  }

  public int getSelectedIndex() {
    return selectedIndex;
  }
}
