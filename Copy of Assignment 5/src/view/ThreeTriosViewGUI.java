//package view;
//
//import java.awt.BorderLayout;
//import javax.swing.JFrame;
//
//import model.ReadonlyThreeTriosModel;
//
///**
// * The main GUI window for the Three Trios game.
// * This class extends JFrame and serves as the container for the game's panel.
// * It initializes and displays the game view by integrating the {@link ThreeTriosPanel}.
// */
//public class ThreeTriosViewGUI extends JFrame {
//  private final ThreeTriosPanel panel;
//
//  /**
//   * Constructs a ThreeTriosViewGUI window with the specified game model.
//   *
//   * @param model the {@link ReadonlyThreeTriosModel} instance representing the game state.
//   */
//  public ThreeTriosViewGUI(ReadonlyThreeTriosModel model) {
//    this.setTitle("Three Trios Game"); // Sets the window title
//    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    this.setLayout(new BorderLayout()); // Uses a BorderLayout for the JFrame
//
//    // Create and add the game panel to the center of the layout
//    panel = new ThreeTriosPanel(model);
//    this.add(panel, BorderLayout.CENTER);
//
//    // Size the window to fit its components and make it visible
//    this.pack();
//    this.setVisible(true);
//  }
//
//  /**
//   * Returns the main game panel.
//   *
//   * @return The {@link ThreeTriosPanel}.
//   */
//  public ThreeTriosPanel getPanel() {
//    return panel;
//  }
//}

package view;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import model.ReadonlyThreeTriosModel;

/**
 * The main GUI window for the Three Trios game.
 * This class extends JFrame and serves as the container for the game's panel.
 * It initializes and displays the game view by integrating the {@link ThreeTriosPanel}.
 */
public class ThreeTriosViewGUI extends JFrame {
  private final ThreeTriosPanel panel;

  /**
   * Constructs a ThreeTriosViewGUI window with the specified game model and title.
   *
   * @param model The {@link ReadonlyThreeTriosModel} instance representing the game state.
   * @param title The title to display for this view (e.g., "Red Player" or "Blue Player").
   */
  public ThreeTriosViewGUI(ReadonlyThreeTriosModel model, String title) {
    this.setTitle("Three Trios Game - " + title); // Sets the window title
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout()); // Uses a BorderLayout for the JFrame

    // Create and add the game panel to the center of the layout
    panel = new ThreeTriosPanel(model);
    this.add(panel, BorderLayout.CENTER);

    // Size the window to fit its components and make it visible
    this.pack();
    this.setVisible(true);
  }

  /**
   * Returns the main game panel.
   *
   * @return The {@link ThreeTriosPanel}.
   */
  public ThreeTriosPanel getPanel() {
    return panel;
  }
}