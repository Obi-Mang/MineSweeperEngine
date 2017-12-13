package org.obi_mang.minesweeper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Heart of the game. Handles all important logic.
 * <p>
 * Use {@link EngineFactory} to obtain an instance. 
 */
public class Engine {
  private EngineSettings engineSettings;
  
  protected Engine(EngineSettings engineSettings) {
    this.engineSettings = engineSettings;
  }
  
  /**
   * Get the settings used by the engine.
   * 
   * @return {@link EngineSettings}
   */
  public EngineSettings getEngineSettings() {
    return engineSettings;
  }
  
  /**
   * Creates a new game board based on the engine settings.
   *  
   * @return a new crisp game board
   */
  public GameBoard getGameBoard() {
    int totalNumberOfItems = engineSettings.getRowSize() * engineSettings.getColumnSize();
    List<GameBoardItem> allItems = createListWithMines(totalNumberOfItems);
    
    Collections.shuffle(allItems, getRandom());
    
    List<List<GameBoardItem>> split = splitList(allItems, engineSettings.getRowSize(), engineSettings.getColumnSize());
    
    calculateAdjacentMines(split);
    
    return new GameBoard(split);
  }
  
  /**
   * Reveals the item located on the specified row and column.
   * <p>
   * It is also in charge of determine if the game is done by either death or victory.
   * The game board and its items are updated accordingly. 
   * 
   * @param board - the game board reveal items on
   * @param row - the row of the wanted game board item
   * @param column - the column of the wanted game board item
   */
  public void reveal(GameBoard board, int row, int column) {
    GameBoardItem item = board.getGameBoardItems().get(row).get(column);
    
    if (!item.isHidden() || item.isMarked()) {
      // Do nothing
      return;
    } else if (item.isMine()) {
      board.setDead();
      item.setCauseOfDeath();
      showMines(board);
    } else {
      item.setVisible();
      
      if(hasWon(board)) {
        board.setVictorious();
      } else if (item.getAdjacentMines() == 0) {
        // Voodoo magic to reveal additional items
        for (int r = row - 1; r < row + 2; r++) {
          if (r >= 0 && r < engineSettings.getRowSize()) {
            for (int c = column - 1; c < column + 2; c++) {
              if (c >= 0 && c < engineSettings.getColumnSize()) {
                reveal(board, r, c);
              }
            }
          }
        }
      }
    }
  }
  
  /**
   * Mark an item on the game board as a mine.
   * 
   * @param board - the game board to mark an item on
   * @param row - the row of the item to mark
   * @param column - the column of the item to mark
   */
  public void mark(GameBoard board, int row, int column) {
    doMark(board, row, column, true);
  }
  
  /**  
   * Unmark an item on the game board as a mine.
   * 
   * @param board - the game board to unmark an item on
   * @param row - the row of the item to unmark
   * @param column - the column of the item to unmark
   */
  public void unmark(GameBoard board, int row, int column) {
    doMark(board, row, column, false);
  }
  
  /**
   * Returns an instance of {@link Random} seeded with the seed appointed in the engine settings.
   * 
   * @return a new instance of Random
   */
  protected Random getRandom() {
    return new Random(engineSettings.getRandomSeed());
  }
  
  /**
   * Creates a list filled with game board items, where the first number of items are
   * marked as mines. The amount of mines are determined by the engine settings.
   * <p>
   * Note: this list is not in anyway shuffled.
   * 
   * @param numberOfItems - the total amount of game board items to be in the returning list
   * @return a list of game board items.
   */
  protected List<GameBoardItem> createListWithMines(int numberOfItems) {
    List<GameBoardItem> items = new ArrayList<>(numberOfItems);
    
    for (int i = 0; i < numberOfItems; i++) {
      boolean isMine = i < engineSettings.getAmountOfMines();
      GameBoardItem mine = new GameBoardItem(isMine);
      items.add(mine);
    }
    
    return items;
  }
  
  /**
   * Splits a list into a list of sublists.
   * 
   * @param list - the initial list to obtain the sublists from.
   * @param rows - how many sublists to create
   * @param columns - how many items per sublist
   * @return a new list containing sublists
   */
  protected List<List<GameBoardItem>> splitList(List<GameBoardItem> list, int rows, int columns) {
    List<List<GameBoardItem>> split = new ArrayList<>(engineSettings.getRowSize());
    for (int row = 0; row < engineSettings.getRowSize(); row++) {
      int columnStart = row * engineSettings.getColumnSize();
      int columnEnd = columnStart + engineSettings.getColumnSize();
      split.add(list.subList(columnStart, columnEnd));
    }
    return split;
  }
  
  /**
   * Iterates through all the rows and columns that forms game board, and calculates the amount of adjacent
   * mines for each item.
   * <p>
   * <b>Example:</b> Say we translate the items to a text representation. Items marked as mines are an 'M'
   * and other items are represented with the number of adjacent mines.
   * <br>
   * With a bland game board the representation is this:<br>
   * <code>
   * |0|0|0|M|0|0|0|0|<br>
   * |M|M|0|0|0|0|0|0|<br>
   * |0|0|0|0|0|0|0|M|<br>
   * |M|M|0|0|0|0|0|0|<br>
   * |0|0|M|0|0|M|0|0|<br>
   * |0|M|0|0|0|0|0|M|<br>
   * |0|0|0|0|0|0|0|0|<br>
   * |0|0|0|0|0|0|0|0|<br>
   * </code>
   * <p>
   * Then, after this method the result is:<br>
   * <code>
   * |2|2|2|M|1|0|0|0|<br>
   * |M|M|2|1|1|0|1|1|<br>
   * |4|4|2|0|0|0|1|M|<br>
   * |M|M|2|1|1|1|2|1|<br>
   * |3|4|M|1|1|M|2|1|<br>
   * |1|M|2|1|1|1|2|M|<br>
   * |1|1|1|0|0|0|1|1|<br>
   * |0|0|0|0|0|0|0|0|<br>
   * </code>
   * 
   * @param list
   */
  protected void calculateAdjacentMines(List<List<GameBoardItem>> list) {
    for (int row = 0; row < list.size(); row++) {
      for (int column = 0; column < list.get(row).size(); column++) {
        int totalFoundMines = 0;
        totalFoundMines += countMinesOnRow(list, row - 1, column); // above
        totalFoundMines += countMinesOnRow(list, row, column);     // actual row
        totalFoundMines += countMinesOnRow(list, row + 1, column); // below
        list.get(row).get(column).setAdjacentMines(totalFoundMines);
      }
    }
  }
  
  /**
   * Helper method to calculate adjacent mines.<br>
   * Looks at a column at a row and the columns on each side of it.
   * 
   * @param list - the list of the game board items
   * @param row - what row to check
   * @param column - which column is the center
   * @return how many mines were found
   */
  protected int countMinesOnRow(List<List<GameBoardItem>> list, int row, int column) {
    int totalFoundMines = 0;
    if (row < 0 || row >= engineSettings.getRowSize()) {
      return totalFoundMines;
    }
    for (int c = column - 1; c < column + 2; c++) {
      if (c >= 0 && c < engineSettings.getColumnSize() && list.get(row).get(c).isMine()) {
        totalFoundMines++;
      }
    }
    
    return totalFoundMines;
  }
  
  /**
   * Commonality method for {@link Engine#mark(GameBoard, int, int)} and {@link Engine#unmark(GameBoard, int, int)}
   */
  protected void doMark(GameBoard board, int row, int column, boolean marked) {
    GameBoardItem item = board.getGameBoardItems().get(row).get(column);
    
    item.setMarked(marked);
  }
  
  /**
   * Helper method to show all mines on the game board.
   * 
   * @param board - the game board to show all mines on.
   */
  protected void showMines(GameBoard board) {
    board.getGameBoardItems().stream()
      .flatMap(List::stream)
      .filter(item -> item.isMine())
      .forEach(item -> item.setVisible());
  }
  
  /**
   * Determines if a game board has been cleared.
   * 
   * @param board - the game board to check
   * @return true if the only hidden items left on the board are mines, otherwise false.
   */
  protected boolean hasWon(GameBoard board) {
    return board.getGameBoardItems().stream()
        .flatMap(List::stream)
        .filter(item -> item.isHidden())
        .allMatch(item -> item.isMine());
  }
}
