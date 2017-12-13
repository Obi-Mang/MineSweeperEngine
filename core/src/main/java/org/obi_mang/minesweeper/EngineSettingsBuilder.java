package org.obi_mang.minesweeper;

/**
 * Builder for {@link EngineSettings}.
 * <p>
 * This is a very simple builder which doesn't perform any type of logical checks.
 */
public class EngineSettingsBuilder {
  // EASY
  public static final int EASY_ROW_SIZE = 8;
  public static final int EASY_COLUMN_SIZE = 8;
  public static final int EASY_AMOUNT_OF_MINES = 10;
  // MEDIUM
  public static final int MEDIUM_ROW_SIZE = 16;
  public static final int MEDIUM_COLUMN_SIZE = 16;
  public static final int MEDIUM_AMOUNT_OF_MINES = 40;
  // DIFFICULT
  public static final int DIFFICULT_ROW_SIZE = 16;
  public static final int DIFFICULT_COLUMN_SIZE = 30;
  public static final int DIFFICULT_AMOUNT_OF_MINES = 99;
  
  private int rowSize;
  private int columnSize;
  private int amountOfMines;
  private long randomSeed;
  
  /**
   * Creates a builder with predefined values for an easy game board.
   */
  public EngineSettingsBuilder() {
    super();
    rowSize = EASY_ROW_SIZE;
    columnSize = EASY_COLUMN_SIZE;
    amountOfMines = EASY_AMOUNT_OF_MINES;
    randomSeed = System.currentTimeMillis();
  }
  
  /**
   * Set the wanted row size.
   * 
   * @param rowSize - how many rows the game board should have 
   * @return this builder
   */
  public EngineSettingsBuilder withRowSize(int rowSize) {
    this.rowSize = rowSize;
    return this;
  }
  
  /**
   * Set the wanted column size.
   * 
   * @param columnSize - how many columns the game board should have
   * @return this builder
   */
  public EngineSettingsBuilder withColumnSize(int columnSize) {
    this.columnSize = columnSize;
    return this;
  }
  
  /**
   * Set the wanted amount of mines.
   * 
   * @param amountOfMines - how many of the game board items should be mines.
   * @return this builder
   */
  public EngineSettingsBuilder withAmountOfMines(int amountOfMines) {
    this.amountOfMines = amountOfMines;
    return this;
  }
  
  /**
   * Set the random seed to use when placing the game board items.<br>
   * A game can be recreated with the help of two things:<br>
   * <ol>
   * <li>The same random seed</li>
   * <li>The number of calls to the {@link Random} object fed with the seed</li>
   * </ol>
   * 
   * @param randomSeed - 
   * @return
   */
  public EngineSettingsBuilder withRandomSeed(long randomSeed) {
    this.randomSeed = randomSeed;
    return this;
  }
  
  /**
   * Creates the engine settings based on the given input.
   * 
   * @return {@link EngineSettings}
   */
  public EngineSettings build() {
    return new EngineSettings(rowSize, columnSize, amountOfMines, randomSeed);
  }
}
