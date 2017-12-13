package org.obi_mang.minesweeper;

import static org.obi_mang.minesweeper.EngineSettingsBuilder.DIFFICULT_AMOUNT_OF_MINES;
import static org.obi_mang.minesweeper.EngineSettingsBuilder.DIFFICULT_COLUMN_SIZE;
import static org.obi_mang.minesweeper.EngineSettingsBuilder.DIFFICULT_ROW_SIZE;
import static org.obi_mang.minesweeper.EngineSettingsBuilder.EASY_AMOUNT_OF_MINES;
import static org.obi_mang.minesweeper.EngineSettingsBuilder.EASY_COLUMN_SIZE;
import static org.obi_mang.minesweeper.EngineSettingsBuilder.EASY_ROW_SIZE;
import static org.obi_mang.minesweeper.EngineSettingsBuilder.MEDIUM_AMOUNT_OF_MINES;
import static org.obi_mang.minesweeper.EngineSettingsBuilder.MEDIUM_COLUMN_SIZE;
import static org.obi_mang.minesweeper.EngineSettingsBuilder.MEDIUM_ROW_SIZE;

/**
 * Factory to get {@link Engine} with different settings.<br>
 * Easy, medium, and difficult standard settings, as well as an option for custom settings.
 */
public class EngineFactory {
  private EngineFactory() {
    // "You. Shall. Not. INSTANTIATE!" - Mangdalf
  }
  
  /**
   * Get an engine with settings for an easy game.
   * 
   * @return {@link Engine}
   */
  public static Engine easyEngine() {
    EngineSettings settings = new EngineSettingsBuilder()
        .withRowSize(EASY_ROW_SIZE)
        .withColumnSize(EASY_COLUMN_SIZE)
        .withAmountOfMines(EASY_AMOUNT_OF_MINES)
        .build();
    return new Engine(settings);
  }
  
  /**
   * Get an engine with settings for a medium game.
   * 
   * @return {@link Engine}
   */
  public static Engine mediumEngine() {
    EngineSettings settings = new EngineSettingsBuilder()
        .withRowSize(MEDIUM_ROW_SIZE)
        .withColumnSize(MEDIUM_COLUMN_SIZE)
        .withAmountOfMines(MEDIUM_AMOUNT_OF_MINES)
        .build();
    return new Engine(settings);
  }
  
  /**
   * Get an engine with settings for a difficult game.
   * 
   * @return {@link Engine}
   */
  public static Engine difficultEngine() {
    EngineSettings settings = new EngineSettingsBuilder()
        .withRowSize(DIFFICULT_ROW_SIZE)
        .withColumnSize(DIFFICULT_COLUMN_SIZE)
        .withAmountOfMines(DIFFICULT_AMOUNT_OF_MINES)
        .build();
    return new Engine(settings);
  }
  
  /**
   * Get an engine with settings for an easy game.
   * 
   * @return {@link Engine}
   */
  public static Engine defaultEngine() {
    return easyEngine();
  }
  
  /**
   * Get an engine with settings for a custom game.
   * 
   * @param engineSettings - the settings for the custom game
   * @return {@link Engine}
   */
  public static Engine customEngine(EngineSettings engineSettings) {
    return new Engine(engineSettings);
  }
}
