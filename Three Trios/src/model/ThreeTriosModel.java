package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Represents the model for the Three Trios game, implementing game logic and managing game state.
 */
public class ThreeTriosModel implements Model {
  private final Random random;
  private AbstractBoard board;                // The game board
  private AbstractBoard copyBoard;
  private CardColor currentPlayer;                // The current player's color
  private boolean isGameOver;                 // Game over status
  private boolean isGamePlaying;              // Game playing status
  private ArrayList<GameCard> redHand;        // Cards in the Red player's hand
  private ArrayList<GameCard> blueHand;       // Cards in the Blue player's hand
  private int cardCellCounter = 0;// Counter for card cells on the board
  private int numCardsFlipped = 0;
  private final List<ModelListener> listeners = new ArrayList<>();
  private boolean redHintsEnabled = false;  // Red player's hint mode status
  private boolean blueHintsEnabled = false; // Blue player's hint mode status
  private GameCard selectedCard; // The card the player has selected


  /**
   * Default constructor for ThreeTriosModel.
   */
  public ThreeTriosModel() {
    this.random = new Random();
  }

  public ThreeTriosModel(Random random) {
    this.random = random;
  }

  /**
   * Gets the game board.
   * @return The current game board.
   */
  public AbstractBoard getBoard() {
    return board;
  }

  public boolean getGameOver() {
    return isGameOver;
  }

  public CardColor getCurrentPlayer() {
    return currentPlayer;
  }

  public ArrayList<GameCard> getRedHand() {
    return redHand;
  }

  public ArrayList<GameCard> getBlueHand() {
    return blueHand;
  }

  public int getCardCellCounter() {
    return cardCellCounter;
  }

  public void setCurrentPlayer(CardColor currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  public int getNumCardsFlipped() {
    return numCardsFlipped;
  }

  /**
   * Starts the game by initializing the board and dealing hands to each player.
   * @param boardFile Path to the file that contains the board configuration.
   * @param cardFile Path to the file that contains the card configuration.
   * @throws IllegalStateException if the number of cards is not even.
   */
  public void startGame(String boardFile, String cardFile) {
    if (isGameOver || isGamePlaying) {
      throw new IllegalStateException("The game needs to have not already started.");
    }

    if ((getAllCards(cardFile).size() % 2) == 1) {
      throw new IllegalStateException("Number of cards must be even");
    }
    currentPlayer = CardColor.RED;
    board = fileToBoard(boardFile);
    copyBoard = new AbstractBoard(board.getCols(), board.getRows(), board.getGridCopy());

    redHand = createRedHand(cardFile);
    blueHand = createBlueHand(cardFile);

    isGamePlaying = true;
    notifyTurnChanged();
  }

  /**
   * Loads the board configuration from a file.
   * @param filePath Path to the board configuration file.
   * @return The initialized Board.
   * @throws RuntimeException if the file is not found or the configuration is invalid.
   */
  public AbstractBoard fileToBoard(String filePath) {
    try (Scanner scanner = new Scanner(new File(filePath))) {
      int rows = scanner.nextInt();
      int cols = scanner.nextInt();
      Cell[][] grid = new Cell[cols][rows];


      for (int i = 0; i < rows; i++) {
        String row = scanner.next();
        char[] characters = row.toCharArray();
        GameCard mockGC = new GameCard("mock", CardColor.NONE , -1, -1, -1, -1);
        for (int j = 0; j < cols; j++) {
          if (characters[j] == 'X') {
            grid[j][i] = new Cell(j, i, false, CellType.X, new PlayedCard(mockGC, -1, -1));
          } else if (characters[j] == 'C') {
            grid[j][i] = new Cell(j, i, false, CellType.C, new PlayedCard(mockGC, -1, -1));
            cardCellCounter++;
          }
        }
      }
      if (cardCellCounter % 2 == 0) {
        throw new IllegalStateException("Number of card cells must be odd");
      }
      board = new AbstractBoard(cols, rows, grid);
      return new AbstractBoard(cols, rows, grid);
    } catch (FileNotFoundException e) {
      throw new RuntimeException("Grid configuration file not found: " + filePath);
    }
  }

  /**
   * Converts the A from ace to 10.
   * @param character The letter A;
   * @return The number 10
   */
  private String aToNum(String character) {
    if (character.equals("A")) {
      return "10";
    }
    else {
      return character;
    }
  }

  /**
   * Reads all available cards from a file, shuffles them, and returns the list.
   * @param filePath Path to the card configuration file.
   * @return A shuffled list of GameCard objects.
   * @throws RuntimeException if the file is not found.
   */
  public ArrayList<GameCard> getAllCards(String filePath) {
    ArrayList<GameCard> cards = new ArrayList<>();
    try (Scanner scanner = new Scanner(new File(filePath))) {
      while (scanner.hasNextLine()) {
        String[] characters = scanner.nextLine().split(" ");
        String cardId = characters[0];
        int northNum = Integer.parseInt(aToNum(characters[1]));
        int southNum = Integer.parseInt(aToNum(characters[2]));
        int eastNum = Integer.parseInt(aToNum(characters[3]));
        int westNum = Integer.parseInt(aToNum(characters[4]));
        cards.add(new GameCard(cardId, CardColor.NONE, northNum, southNum, eastNum, westNum));
      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException("Card configuration file not found: " + filePath);
    }

    Collections.shuffle(cards, random);
    return cards;
  }

  /**
   * Places a card on the board at a specified position.
   * @param card The card to place.
   * @param x The row position on the board.
   * @param y The column position on the board.
   * @throws IllegalStateException if the game is not running.
   * @throws IllegalArgumentException if the cell is not empty.
   */
  @Override
  public void placeCard(GameCard card, int x, int y) {
    if (!isGamePlaying) {
      throw new IllegalStateException("The game is not running.");
    }

    if (!board.isCellEmpty(x, y)) {
      throw new IllegalArgumentException("Cell is not empty.");
    }

    if (board.getGrid()[x][y].getType() == CellType.X) {
      throw new IllegalArgumentException("Cell is a hole.");
    }

    if (currentPlayer == CardColor.RED) {
      redHand.remove(card);
    } else {
      blueHand.remove(card);
    }

    card.changeColor(currentPlayer);
    board.placeCard(card, x, y);
    copyBoard = new AbstractBoard(board.getCols(), board.getRows(), board.getGridCopy());

    checkBattlePhase(x, y);

    // Check game over condition
    isGameOver = board.isFull();
    notifyBoardUpdated();

    // SWITCHED ORDER OF notify AND currentplayer
    if (currentPlayer == CardColor.RED) {
      currentPlayer = CardColor.BLUE;
      notifyTurnChanged();
    }
    else if (currentPlayer == CardColor.BLUE) {
      currentPlayer = CardColor.RED;
      notifyTurnChanged();
    }
    if (isGameOver) {
      notifyGameOver();
    }
  }

  /**
   * Executes the battle phase for the card placed at the given position.
   * Compares adjacent cards and flips ownership if the placed card wins.
   * @param x The row position of the placed card.
   * @param y The column position of the placed card.
   */
  private void checkBattlePhase(int x, int y) {
    PlayedCard placedCard = board.getPlayedCardAt(x, y);
    Map<String, PlayedCard> adjacentCards = board.getAdjacentCards(x, y);
    CardColor ogColor = placedCard.getColor();

    if ((adjacentCards.containsKey("Top")) &&
            (placedCard.getColor() != adjacentCards.get("Top").getColor())) {
      nsComparePCBottom(adjacentCards.get("Top"), placedCard);
      if (ogColor == adjacentCards.get("Top").getColor()) {
        PlayedCard newPC = adjacentCards.get("Top");
        checkBattlePhase(newPC.getXposn(), newPC.getYposn());
      }
    }
    if ((adjacentCards.containsKey("Bottom")) &&
            (placedCard.getColor() != adjacentCards.get("Bottom").getColor())) {
      nsComparePCTop(placedCard, adjacentCards.get("Bottom"));
      if (ogColor == adjacentCards.get("Bottom").getColor()) {
        PlayedCard newPC = adjacentCards.get("Bottom");
        checkBattlePhase(newPC.getXposn(), newPC.getYposn());
      }
    }
    if ((adjacentCards.containsKey("Left")) &&
            (placedCard.getColor() != adjacentCards.get("Left").getColor())) {
      ewComparePCRight(placedCard, adjacentCards.get("Left"));
      if (ogColor == adjacentCards.get("Left").getColor()) {
        PlayedCard newPC = adjacentCards.get("Left");
        checkBattlePhase(newPC.getXposn(), newPC.getYposn());
      }
    }
    if ((adjacentCards.containsKey("Right")) &&
            (placedCard.getColor() != adjacentCards.get("Right").getColor())) {
      ewComparePCLeft(adjacentCards.get("Right"), placedCard);
      if (ogColor == adjacentCards.get("Right").getColor()) {
        PlayedCard newPC = adjacentCards.get("Right");
        checkBattlePhase(newPC.getXposn(), newPC.getYposn());
      }
    }
  }


  /**
   * Compares north and south attack values and flips cards if necessary.
   */

  private void nsComparePCBottom(PlayedCard top, PlayedCard bottom) {
    if (top.getYposn() != bottom.getYposn() - 1) {
      throw new IllegalArgumentException();
    }
    if (top.card.southNum < bottom.card.northNum) {
      top.card.color = bottom.card.getColor();
    }
  }

  /**
   * Compares north and south attack values and flips cards if necessary.
   */

  private void nsComparePCTop(PlayedCard top, PlayedCard bottom) {
    if (top.getYposn() != bottom.getYposn() - 1) {
      throw new IllegalArgumentException();
    }
    if (top.card.southNum > bottom.card.northNum) {
      bottom.card.color = top.card.getColor();
    }
  }

  /**
   * Compares east and west attack values and flips cards if necessary.
   */

  private void ewComparePCRight(PlayedCard right, PlayedCard left) {
    if (left.getXposn() != right.getXposn() - 1) {
      throw new IllegalArgumentException();
    }
    if (left.card.eastNum < right.card.westNum) {
      left.card.color = right.card.getColor();
    }
  }

  /**
   * Compares east and west attack values and flips cards if necessary.
   */

  private void ewComparePCLeft(PlayedCard right, PlayedCard left) {
    if (left.getXposn() != right.getXposn() - 1) {
      throw new IllegalArgumentException();
    }
    if (left.card.eastNum > right.card.westNum) {
      right.card.color = left.card.getColor();
    }
  }

  /**
   * Generates and returns a hand of cards for the Red player.
   * @param file Path to the card configuration file.
   * @return A list of GameCard objects for the Red player's hand.
   */
  public ArrayList<GameCard> createRedHand(String file) {
    ArrayList<GameCard> allCards = getAllCards(file);
    int handSize = (allCards.size() + 1) / 2;
    ArrayList<GameCard> cardSubset = new ArrayList<>(allCards.subList(0, handSize));
    ArrayList<GameCard> redHand = new ArrayList<>();

    for (GameCard card : cardSubset) {
      card.color = CardColor.RED;
      redHand.add(card);
    }
    return redHand;
  }

  /**
   * Generates and returns a hand of cards for the Blue player.
   * @param file Path to the card configuration file.
   * @return A list of GameCard objects for the Blue player's hand.
   */
  public ArrayList<GameCard> createBlueHand(String file) {
    ArrayList<GameCard> allCards = getAllCards(file);
    int handSize = (allCards.size() + 1) / 2;
    ArrayList<GameCard> cardSubset = new ArrayList<>(allCards.subList(handSize, allCards.size()));
    ArrayList<GameCard> blueHand = new ArrayList<>();

    for (GameCard card : cardSubset) {
      card.color = CardColor.BLUE;
      blueHand.add(card);
    }
    return blueHand;
  }

  /**
   * Determines if the game is over and calculates the winner based on card ownership.
   * @return The winning color, or null in case of a tie or if the game is not over.
   */
  @Override
  public CardColor gameOver() {
    if (!isGameOver) {
      return null;
    }

    isGameOver = true;
    isGamePlaying = false;

    int redCount = board.countCards(CardColor.RED);
    int blueCount = board.countCards(CardColor.BLUE) + 1;

    if (redCount > blueCount) {
      return CardColor.RED;
    } else if (blueCount > redCount) {
      return CardColor.BLUE;
    } else {
      return CardColor.NONE ; // Tie
    }
  }


  /**
   * Checks for how many cards on the board will be flipped if a card is placed.
   *
   * @param card is a GameCard.
   * @param x    is the x-coordinate of the intended cell that the card will be placed.
   * @param y    is the y-coordinate of the intended cell that the card will be placed.
   * @return an integer representing the number of flipped cards.
   */
//  public int checkFlipCount(GameCard card, int x, int y) {
//    if (copyBoard.getGrid()[x][y].getType() == CellType.X) {
//      throw new IllegalArgumentException("Cell is a hole");
//    } else if (copyBoard.getGrid()[x][y].getIsCard()) {
//      throw new IllegalArgumentException("Cell already has a card");
//    }
//    PlayedCard tempPC = new PlayedCard(card, x, y);
//    copyBoard.placeCard(card, x, y);
//    Map<String, PlayedCard> adjacentCards = copyBoard.getAdjacentCards(x, y);
//    CardColor ogColor = tempPC.getColor();
//
//    if ((adjacentCards.containsKey("Top")) &&
//            (tempPC.getColor() != adjacentCards.get("Top").getColor())) {
//      nsComparePCBottom(adjacentCards.get("Top"), tempPC);
//      if (ogColor == adjacentCards.get("Top").getColor()) {
//        numCardsFlipped = getNumCardsFlipped() + 1;
//        PlayedCard newPC = adjacentCards.get("Top");
//        checkFlipCount(newPC.getCard(), newPC.getXposn(), newPC.getYposn());
//      }
//    }
//    if ((adjacentCards.containsKey("Bottom")) &&
//            (tempPC.getColor() != adjacentCards.get("Bottom").getColor())) {
//      nsComparePCTop(tempPC, adjacentCards.get("Bottom"));
//      if (ogColor == adjacentCards.get("Bottom").getColor()) {
//        numCardsFlipped = getNumCardsFlipped() + 1;
//        PlayedCard newPC = adjacentCards.get("Bottom");
//        checkFlipCount(newPC.getCard(), newPC.getXposn(), newPC.getYposn());
//      }
//    }
//    if ((adjacentCards.containsKey("Left")) &&
//            (tempPC.getColor() != adjacentCards.get("Left").getColor())) {
//      ewComparePCRight(tempPC, adjacentCards.get("Left"));
//      if (ogColor == adjacentCards.get("Left").getColor()) {
//        numCardsFlipped = getNumCardsFlipped() + 1;
//        PlayedCard newPC = adjacentCards.get("Left");
//        checkFlipCount(newPC.getCard(), newPC.getXposn(), newPC.getYposn());
//      }
//    }
//    if ((adjacentCards.containsKey("Right")) &&
//            (tempPC.getColor() != adjacentCards.get("Right").getColor())) {
//      ewComparePCLeft(adjacentCards.get("Right"), tempPC);
//      if (ogColor == adjacentCards.get("Right").getColor()) {
//        numCardsFlipped = getNumCardsFlipped() + 1;
//        PlayedCard newPC = adjacentCards.get("Right");
//        checkFlipCount(newPC.getCard(), newPC.getXposn(), newPC.getYposn());
//      }
//    }
//    copyBoard = new AbstractBoard(board.getCols(), board.getRows(), board.getGridCopy());
//    return getNumCardsFlipped();
//  }

  public int checkFlipCount(GameCard card, int x, int y) {
    // Check if the cell is valid for placing the card

    if (card == null) {
      throw new IllegalArgumentException("Card is not selected");
    }

    if (board.getGrid()[x][y].getType() == CellType.X) {
      throw new IllegalArgumentException("Cell is a hole");
    } else if (board.getGrid()[x][y].getIsCard()) {
      throw new IllegalArgumentException("Cell already has a card");
    }

    // Create a temporary board to simulate the placement
    AbstractBoard tempBoard = new AbstractBoard(board.getCols(), board.getRows(), board.getGridCopy());
    PlayedCard tempPC = new PlayedCard(card, x, y);
    tempBoard.placeCard(card, x, y);

    // Get adjacent cards
    Map<String, PlayedCard> adjacentCards = tempBoard.getAdjacentCards(x, y);

    int flippedCount = 0;

    // Check each direction for potential flips
    for (Map.Entry<String, PlayedCard> entry : adjacentCards.entrySet()) {
      PlayedCard adjacentCard = entry.getValue();

      // Only consider cards of the opposite color
      if (adjacentCard != null && adjacentCard.getColor() != card.getColor()) {
        boolean flipped = false;

        // Check specific battle rules based on direction
        switch (entry.getKey()) {
          case "Top":
            flipped = card.getNorthNum() > adjacentCard.getCard().getSouthNum();
            break;
          case "Bottom":
            flipped = card.getSouthNum() > adjacentCard.getCard().getNorthNum();
            break;
          case "Left":
            flipped = card.getWestNum() > adjacentCard.getCard().getEastNum();
            break;
          case "Right":
            flipped = card.getEastNum() > adjacentCard.getCard().getWestNum();
            break;
        }

        // Count the flip if the condition is met
        if (flipped) {
          flippedCount++;
        }
      }
    }

    return flippedCount;
  }


  private List<Integer> blueCardChecker(GameCard gc) {
    List<Integer> list = new ArrayList<>();
    int highNum = 0;
    int highX = 0;
    int highY = 0;
    for (int i = 0; i < board.getRows(); i++) {
      for (int j = 0; j < board.getCols(); j++) {
        numCardsFlipped = 0;
        if ((board.getGrid()[j][i].getType() == CellType.C) &&
                !(board.getGrid()[j][i].getIsCard())) {
          if (checkFlipCount(gc, j, i) > highNum) {
            highNum = checkFlipCount(gc, j, i);
            highX = j;
            highY = i;
          }
        }
      }
    }
    list.add(highNum);
    list.add(highX);
    list.add(highY);
    System.out.println(highNum + " " + highX + " " + highY);
    return list;
  }

  private Map<PlayedCard, Integer> blueHandGetNumFlipped() {
    Map<PlayedCard, Integer> map = new HashMap<>();
    for (int i = 0; i < blueHand.size(); i++) {
      int numFlipped = blueCardChecker(blueHand.get(i)).get(0);
      int x = blueCardChecker(blueHand.get(i)).get(1);
      int y = blueCardChecker(blueHand.get(i)).get(2);
      PlayedCard pc = new PlayedCard(blueHand.get(i), x, y);
      map.put(pc, numFlipped);
    }
    return map;
  }

  private Map<PlayedCard, Integer> sortBlueHandByNumFlipped() {
    Map<PlayedCard, Integer> map = blueHandGetNumFlipped();
    List<Map.Entry<PlayedCard, Integer>> list = new ArrayList<>(map.entrySet());
    list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
    LinkedHashMap<PlayedCard, Integer> sortedMap = new LinkedHashMap<>();
    for (Map.Entry<PlayedCard, Integer> entry : list) {
      sortedMap.put(entry.getKey(), entry.getValue());
    }
    return sortedMap;
  }

  /**
   * Represents strategy 1 which is most cards flipped.
   * @return A played card.
   */
  public PlayedCard strategy1() {
    Map<PlayedCard, Integer> map = sortBlueHandByNumFlipped();
    List<Map.Entry<PlayedCard, Integer>> list = new ArrayList<>(map.entrySet());
    PlayedCard pc = null;
    System.out.println(list.get(0).getKey().getXposn());
    return list.get(0).getKey();
  }

  private Map<Integer, PlayedCard> blueHandAdj() {
    Map<PlayedCard, Integer> map = sortBlueHandByNumFlipped();
    Map<Integer, PlayedCard> mapAdjWithPC = new HashMap<>();
    for (Map.Entry<PlayedCard, Integer> entry : map.entrySet()) {
      int numAdj = board.getAdjacentCards(entry.getKey().getXposn(),
              entry.getKey().getYposn()).size();
      mapAdjWithPC.put(numAdj, entry.getKey());
    }
    return mapAdjWithPC;
  }

  /**
   * Represents strategy 2 that represents least amount of adjacent cards.
   * @return A played card.
   */
  public PlayedCard strategy2() {
    Map<Integer, PlayedCard> map = blueHandAdj();
    List<Map.Entry<Integer, PlayedCard>> list = new ArrayList<>(map.entrySet());
    list.sort(Map.Entry.comparingByKey());
    return list.get(0).getValue();
  }

  /**
   * Represents strategy 3 which is a combination of strategy 1 and 2.
   * @return A played card.
   */
  public PlayedCard strategy3() {
    Map<PlayedCard, Integer> map1 = blueHandGetNumFlipped();
    List<Map.Entry<PlayedCard, Integer>> list1 = new ArrayList<>(map1.entrySet());
    Map<Integer, PlayedCard> map2 = blueHandAdj();
    List<Map.Entry<Integer, PlayedCard>> list2 = new ArrayList<>(map2.entrySet());

    Map<PlayedCard, Integer> mapScore = new HashMap<>();
    // adjStrength is how valuable a card with fewer cards touching is for the algorithm.
    int adjStrength = 2;
    for (int i = 0; i < blueHand.size(); i++) {
      mapScore.put(list1.get(i).getKey(),
              (list1.get(i).getValue() + (adjStrength * (4 - list2.get(i).getKey()))));
    }

    List<Map.Entry<PlayedCard, Integer>> listMapScore = new ArrayList<>(mapScore.entrySet());
    listMapScore.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

    return listMapScore.get(0).getKey();
  }


  /**
   * Adds a listener to the model.
   *
   * @param listener The listener to add.
   */
  @Override
  public void addListener(ModelListener listener) {
    listeners.add(listener);
  }

  /**
   * Removes a listener from the model.
   *
   * @param listener The listener to remove.
   */
  @Override
  public void removeListener(ModelListener listener) {
    listeners.remove(listener);
  }

  @Override
  public void addTurnNotificationListener(Consumer<CardColor> listener) {
    // should add notification listener.
  }

  @Override
  public boolean isPlayersTurn(CardColor color) {
    return false;
  }

  /**
   * Notifies listeners that the active player's turn has changed.
   */
  private void notifyTurnChanged() {
    this.selectedCard = null;
    notifyBoardUpdated();
    for (ModelListener listener : listeners) {
      listener.onTurnChanged(currentPlayer);
    }
  }

  /**
   * Notifies listeners that the board has been updated.
   */
  private void notifyBoardUpdated() {
    for (ModelListener listener : listeners) {
      listener.onBoardUpdated();
    }
  }

  /**
   * Notifies listeners that the game is over.
   */
  private void notifyGameOver() {
    CardColor winner = determineWinner();
    for (ModelListener listener : listeners) {
      listener.onGameOver(winner);
    }
  }

  /**
   * Determines the winner based on the board state.
   *
   * @return The color of the winner, or NONE for a tie.
   */
  private CardColor determineWinner() {
    int redCount = board.countCards(CardColor.RED);
    int blueCount = board.countCards(CardColor.BLUE) + 1;

    if (redCount > blueCount) {
      return CardColor.RED;
    } else if (blueCount > redCount) {
      return CardColor.BLUE;
    } else {
      return CardColor.NONE; // Tie
    }
  }

  public void toggleHints(CardColor playerColor) {
    if (playerColor == CardColor.RED) {
      redHintsEnabled = !redHintsEnabled;
    } else if (playerColor == CardColor.BLUE) {
      blueHintsEnabled = !blueHintsEnabled;
    }
    notifyBoardUpdated(); // Trigger re-rendering in the view
  }

  public boolean areHintsEnabled(CardColor playerColor) {
    return playerColor == CardColor.RED ? redHintsEnabled : blueHintsEnabled;
  }

  public void setSelectedCard(GameCard card) {
    this.selectedCard = card;
    notifyBoardUpdated(); // Redraw the board so hints will appear
  }

  public GameCard getSelectedCard() {
    return this.selectedCard;
  }


}
