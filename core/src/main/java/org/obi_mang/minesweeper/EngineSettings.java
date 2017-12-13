package org.obi_mang.minesweeper;

/**
 * The settings for an {@link Engine}.
 * <p>
 * Obtain an instance from {@link EngineSettingsBuilder}
 */
public class EngineSettings {
  private int rowSize;
  private int columnSize;
  private int amountOfMines;
  private long randomSeed;

  protected EngineSettings(int rowSize, int columnSize, int amountOfMines, long randomSeed) {
    super();
    this.rowSize = rowSize;
    this.columnSize = columnSize;
    this.amountOfMines = amountOfMines;
    this.randomSeed = randomSeed;
  }

  /**
   * Get the amount of rows on the game board.
   * 
   * @return the number of rows to have
   */
  public int getRowSize() {
    return rowSize;
  }

  /**
   * Get the amount of columns for each row on the game board.
   * 
   * @return the number of columns for each row to have
   */
  public int getColumnSize() {
    return columnSize;
  }

  /**
   * Get the amount of mines on the game board.
   * 
   * @return the number of game board items on the game board that are mines
   */
  public int getAmountOfMines() {
    return amountOfMines;
  }

  /**
   * Get the random seed to use when shuffling the game board items
   * 
   * @return the number to use as seed
   */
  public long getRandomSeed() {
    return randomSeed;
  }
}
