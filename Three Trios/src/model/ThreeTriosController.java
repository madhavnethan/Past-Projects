package model;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import view.ThreeTriosPanel;

/**
 * The controller for the Three Trios game.
 * Mediates interactions between the model, view, and players.
 */
public class ThreeTriosController implements ModelListener {
  private final ThreeTriosModel model;
  private final Player player;
  private final ThreeTriosPanel panel;
  private int selectedCardIndex = -1; // Tracks the currently selected card index

  /**
   * Constructs the controller for the Three Trios game.
   *
   * @param model  The game model.
   * @param player The player associated with this controller.
   * @param panel  The combined view for the game.
   */
  public ThreeTriosController(ThreeTriosModel model, Player player, ThreeTriosPanel panel) {
    this.model = model;
    this.player = player;
    this.panel = panel;

    // Register this controller as a listener to the model
    model.addListener(this);

    // Set up mouse listeners for panels
    setupMouseListeners();

    panel.setFocusable(true);
    panel.requestFocusInWindow();
  }


  /**
   * Configures mouse listeners for user interactions with the panels.
   */
  private void setupMouseListeners() {

    // Mouse listener for RedHandPanel
    panel.getRedHandPanel().addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (player.getColor().equals(CardColor.BLUE) ||
                (model.getCurrentPlayer().equals(CardColor.BLUE))) {
          return; // Prevent Blue player actions during Blue's turn
        }

        int cardHeight = panel.getRedHandPanel().getHeight() / model.getRedHand().size();
        int index = e.getY() / cardHeight;

        if (index >= 0 && index < model.getRedHand().size()) {
          selectedCardIndex = index;
          GameCard selectedCard = model.getRedHand().get(selectedCardIndex);
          model.setSelectedCard(selectedCard);
          panel.getRedHandPanel().setSelectedIndex(index);
          panel.getBoardPanel().setSelectedIndex(index);
          panel.repaint();
        }
      }
    });


    // Mouse listener for BlueHandPanel
    if (!player.isMachine()) {
      panel.getBlueHandPanel().addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          if (player.getColor().equals(CardColor.RED) ||
                  (model.getCurrentPlayer().equals(CardColor.RED))) {
            return; // Prevent Blue player actions during Red's turn
          }

          int cardHeight = panel.getBlueHandPanel().getHeight() / model.getBlueHand().size();
          int index = e.getY() / cardHeight;

          if (index >= 0 && index < model.getBlueHand().size()) {
            selectedCardIndex = index;
            GameCard selectedCard = model.getBlueHand().get(selectedCardIndex);
            model.setSelectedCard(selectedCard);
            panel.getBlueHandPanel().setSelectedIndex(index);
            panel.getBoardPanel().setSelectedIndex(index);
            panel.repaint();
          }
        }
      });
    }


    // Mouse listener for BoardPanel
    panel.getBoardPanel().addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {

        if (selectedCardIndex == -1) {
          JOptionPane.showMessageDialog(panel, "No card selected.",
                  "Error", JOptionPane.ERROR_MESSAGE);
          return;
        }

        int gridSizeRow = model.getBoard().getRows();
        int gridSizeCol = model.getBoard().getCols();
        int cellWidth = panel.getBoardPanel().getWidth() / gridSizeCol;
        int cellHeight = panel.getBoardPanel().getHeight() / gridSizeRow;

        int col = e.getX() / cellWidth;
        int row = e.getY() / cellHeight;

        try {
          if (selectedCardIndex != -2) {
            // Retrieve the selected card based on the player's hand
            GameCard selectedCard = player.getColor() == CardColor.RED
                    ? model.getRedHand().get(selectedCardIndex)
                    : model.getBlueHand().get(selectedCardIndex);

            // Place the card on the board
            model.placeCard(selectedCard, col, row);
          }

          // Clear the selected card index and update the panel
          selectedCardIndex = -1;
          panel.getRedHandPanel().setSelectedIndex(-1);
          panel.getBlueHandPanel().setSelectedIndex(-1);
          panel.getBoardPanel().setSelectedIndex(-1);

          // Repaint after placing a card
          panel.repaint();
        } catch (IllegalArgumentException ex) {
          JOptionPane.showMessageDialog(panel, "Invalid move: " + ex.getMessage(),
                  "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });



    panel.addKeyListener(new KeyListener() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_H) {
          System.out.println("H key pressed! Toggling hints...");
          model.toggleHints(model.getCurrentPlayer());
//          JOptionPane.showMessageDialog(panel, "Hints Enabled",
//                  "Hints", JOptionPane.INFORMATION_MESSAGE);
        }
      }

      @Override
      public void keyReleased(KeyEvent e) {
//        JOptionPane.showMessageDialog(panel, "Hints Disabled",
//                "Hints", JOptionPane.INFORMATION_MESSAGE);
      }

      @Override
      public void keyTyped(KeyEvent e) {}
    });

    panel.requestFocusInWindow();

  }




  /**
   * Handles turn changes in the model.
   *
   * @param currentPlayer The player whose turn it is.
   */
  @Override
  public void onTurnChanged(CardColor currentPlayer) {
    if (currentPlayer.equals(player.getColor())) {
      player.play();
      panel.repaint();
    }
  }

  /**
   * Handles game-over events.
   *
   * @param winner The color of the winning player, or NONE in case of a tie.
   */
  @Override
  public void onGameOver(CardColor winner) {
    String message = winner == CardColor.NONE ? "It's a tie!" : "Winner: " + winner;
    JOptionPane.showMessageDialog(panel, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Handles board updates in the model.
   */
  @Override
  public void onBoardUpdated() {
    panel.repaint();
  }

  /**
   * **Handles key press events.**
   */

}

