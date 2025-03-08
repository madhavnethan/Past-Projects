Overview: This codebase is trying to represent a two player card game in which the player that wins
          is determined by the player's color being the most cards on the board.
Quick start:
    model = new ThreeTriosModel();
    model.startGame("./gridAllCs.txt", "./cardsAllCs.txt");
Key components: This codebase is split up into 3 main components. The board represents the game
                board, which is made up of a grid of cells which can or cannot have cards placed on
                them depending on the type of cell. The AbstractBoard class contains methods that
                handle the changes applied to the game board. The model essentially models the game.
                The ThreeTriosModel class contains methods that handle the actions of the game that
                interact with the board. The view represents the physical view of the game. The
                ThreeTriosTextView class contains method that takes the current state of the model
                and turns it into a viewable string.
Key subcomponents: This codebase contains 2 main subcomponents. The Card class that can be
                   represented as a GameCard or a PlayedCard. A GameCard represents any card that
                   has not been played but can be played which contains information about the
                   values the card contains, the color, and the name. The PlayedCard is the card
                   that is played on the board contains the info of the GameCard and its location.
                   There is also the Cell class where it represents the information of each cell
                   on the board, especially if the cell contains the card.
AI Implementation: In order to simulate an AI player, we will assign the user as the RED player and
                   the AI as the BLUE player. We will then extend the model class to change the
                   play actions. The placeCard method will be changes so that if the currentPlayer
                   is RED, then the old implementation will run. If the currentPlayer is BLUE, then
                   the placeCard method will use random to place a card on the board. This way a
                   single user can play the game.

Changes for part 2:
New classes
- ReadonlyThreeTriosModel: This class is designed to handle all the non-mutable methods implemented by
                           ThreeTriosModel. This was created in order for the view to access only the
                           aspects of the model that contributes to generating the view. This helps
                           prevent any possibility of mutating data through the view.
- ThreeTriosPanel: This class helps to generate the view of the board, including the gamecards and cells.
                   Unlike ThreeTriosTextView, which creates a textual view of the current game, this
                   class creates a graphical view of the current game. It utilizes JSwing to create these
                   visuals.
- ThreeTriosViewGUI: This class is the graphical user interface, which allows the user to interact with the
                     graphical view of the game. Currently, as there is no controller, the GUI is not fully
                     implemented.
Altered classes:
- ThreeTriosModel: Was updated to include a copy of the grid, a method that returns the number of
                   surrounding cards flipped when a gamecard is placed at a position, and the computer
                   player turn, which is designated as the Blue player.
- CardColor: This enum was refactored as implementing JSwing causes some errors due to the confusion
             regarding the previous name of the enum, "Color".

Changes for part 3:
- Panels: Homework 2 originally had one panel for the entire screen. In other words, both hands and the board were
          under one panel. We changed it so that the red hand, blue hand, and board had their own panel. This was
          necessary for the implementation of the controller.
- Player interface and classes: We added a new interface called Player. This interface is implemented by 2 classes:
                                HumanPlayer and ComputerPlayer. We did this so the controller could know if the player
                                was a human user or an AI.
- Controller: We implemented the controller class, which included mouse listeners in order to play the game.
- Command Line: In the main function class we added a way to configure the game using the command line. It takes two
          parameters to distinguish the two types of players human or computer.
