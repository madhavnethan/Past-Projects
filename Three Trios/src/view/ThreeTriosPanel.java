package view;


import javax.swing.JPanel;
import java.awt.BorderLayout;
import model.ReadonlyThreeTriosModel;

/**
 * Panel combining the RedHandPanel, BlueHandPanel, and BoardPanel.
 */
public class ThreeTriosPanel extends JPanel {
  private final RedHandPanel redHandPanel;
  private final BlueHandPanel blueHandPanel;
  private final BoardPanel boardPanel;

  /**
   * Represents entire panel.
   * @param model Is a model.
   */
  public ThreeTriosPanel(ReadonlyThreeTriosModel model) {
    setLayout(new BorderLayout());
    redHandPanel = new RedHandPanel(model);
    blueHandPanel = new BlueHandPanel(model);
    boardPanel = new BoardPanel(model, redHandPanel, blueHandPanel);

    add(redHandPanel, BorderLayout.WEST);
    add(boardPanel, BorderLayout.CENTER);
    add(blueHandPanel, BorderLayout.EAST);

    // ** Make panel focusable to capture key presses **
    setFocusable(true);
    requestFocusInWindow(); // Requests the focus
  }

  public RedHandPanel getRedHandPanel() {
    return redHandPanel;
  }

  public BlueHandPanel getBlueHandPanel() {
    return blueHandPanel;
  }

  public BoardPanel getBoardPanel() {
    return boardPanel;
  }
}
