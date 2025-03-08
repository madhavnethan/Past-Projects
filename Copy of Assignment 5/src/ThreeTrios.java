import javax.swing.SwingUtilities;

import model.ComputerPlayer;
import model.Strategy;
import model.ThreeTriosController;
import model.CardColor;
import model.ReadonlyThreeTriosModel;
import model.ThreeTriosModel;
import model.HumanPlayer;
import model.Player;
import view.ThreeTriosViewGUI;

/**
 * Main class to run the Three Trios game.
 */
public class ThreeTrios {

  /**
   * Main method.
   * @param args String argument to configure game.
   */
  public static void main(String[] args) {

    SwingUtilities.invokeLater(() -> {
      // Initialize the model

      ThreeTriosModel model = new ThreeTriosModel();
      ReadonlyThreeTriosModel readonlyModel = new ReadonlyThreeTriosModel(model);
      model.startGame("./gridTouchingCs.txt", "./cardsTouchingCs.txt");

      // Create players
      Player redPlayer;
      Player bluePlayer;

      if (args.length < 2) {
        redPlayer = stringPlayer("human", model, CardColor.RED);
        bluePlayer = stringPlayer("human", model, CardColor.BLUE);
      } else {
        redPlayer = stringPlayer(args[0], model, CardColor.RED);
        bluePlayer = stringPlayer(args[1], model, CardColor.BLUE);
      }


      // Initialize the views for red and blue players
      ThreeTriosViewGUI redView = new ThreeTriosViewGUI(readonlyModel, "Red Hand");
      ThreeTriosViewGUI blueView = new ThreeTriosViewGUI(readonlyModel, "Blue Hand");

      // Initialize controllers for both players
      new ThreeTriosController(model, redPlayer, redView.getPanel());
      new ThreeTriosController(model, bluePlayer, blueView.getPanel());

    });
  }

  private static Player stringPlayer(String str, ThreeTriosModel model, CardColor color) {
    Strategy strategy = new Strategy(model);
    ReadonlyThreeTriosModel roModel = new ReadonlyThreeTriosModel(model);
    switch (str) {
      case "human":
        return new HumanPlayer(roModel, color);
      case "strategy1":
        return new ComputerPlayer(model, color, strategy.strat1());
      case "strategy2":
        return new ComputerPlayer(model, color, strategy.strat2());
      case "strategy3":
        return new ComputerPlayer(model, color, strategy.strat3());
      default:
        throw new IllegalArgumentException("Unrecognized player: " + str);
    }
  }
}
