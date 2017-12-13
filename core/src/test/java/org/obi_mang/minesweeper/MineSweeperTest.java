package org.obi_mang.minesweeper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MineSweeperTest {

  private Engine engine;
  private Engine easyEngine;

  @Before
  public void before() throws Exception {
    EngineSettingsBuilder customSettings = new EngineSettingsBuilder()
        .withRandomSeed(1L);
    engine = EngineFactory.customEngine(customSettings.build());
    easyEngine = EngineFactory.easyEngine();
  }

  @After
  public void after() throws Exception {
    engine = null;
    easyEngine = null;
  }

  @Test
  public void testEasySettings() throws Exception {
    int expectedRowSize = 8;
    int expectedColumnSize = 8;
    int expectedAmountOfMines = 10;

    EngineSettings settings = easyEngine.getEngineSettings();

    int actualRowSize = settings.getRowSize();
    int actualColumnSize = settings.getColumnSize();
    int actualAmountOfMines = settings.getAmountOfMines();

    assertEquals(expectedRowSize, actualRowSize);
    assertEquals(expectedColumnSize, actualColumnSize);
    assertEquals(expectedAmountOfMines, actualAmountOfMines);
  }

  @Test
  public void testGeneratedItems() throws Exception {
    GameBoard board = engine.getGameBoard();
    List<List<GameBoardItem>> rows = board.getGameBoardItems();

    int expectedRowSize = 8;
    int actualRowSize = rows.size();
    assertEquals(expectedRowSize, actualRowSize);
    
    int expectedColumnSize = 8;
    for (List<GameBoardItem> columns : rows) {
      int actualColumnSize = 0;
      for (GameBoardItem gameBoardItem : columns) {
        actualColumnSize++;
        assertNotNull(gameBoardItem);
      }
      assertEquals(expectedColumnSize, actualColumnSize);
    }
  }

  @Test
  public void testGeneratedMines() throws Exception {
    GameBoard board = engine.getGameBoard();

    int expectedAmountOfMines = 10;
    int actualAmountOfMines = board.getGameBoardItems().stream()
        .flatMap(List::stream)
        .map(item -> { if (item.isMine()) return 1; else return 0; })
        .reduce(0, Integer::sum);

    assertEquals(expectedAmountOfMines, actualAmountOfMines);
  }
  
  @Test
  public void testMineLocation() throws Exception {
    /* 
     * Since we use a fixed random seed the locations
     * should always be on the same locations
     */
    GameBoard board = engine.getGameBoard();
    List<List<GameBoardItem>> items = board.getGameBoardItems();
    
    List<GameBoardItem> expectedMines = new ArrayList<>();
    expectedMines.add(items.get(0).get(3));
    expectedMines.add(items.get(1).get(0));
    expectedMines.add(items.get(1).get(1));
    expectedMines.add(items.get(2).get(7));
    expectedMines.add(items.get(3).get(0));
    expectedMines.add(items.get(3).get(1));
    expectedMines.add(items.get(4).get(2));
    expectedMines.add(items.get(4).get(5));
    expectedMines.add(items.get(5).get(1));
    expectedMines.add(items.get(5).get(7));
    
    for (GameBoardItem expectedMine : expectedMines) {
      assertTrue(expectedMine.isMine());
    }
  }
  
  @Test
  public void testMineCount() throws Exception {
    GameBoard board = engine.getGameBoard();
    
    List<String> computedRows = board.getGameBoardItems().stream()
        .map(subList -> {
          return subList.stream()
            .map(item -> { if (item.isMine()) return "M"; else return String.valueOf(item.getAdjacentMines()); })
            .reduce("", (x,y) -> x+y);
        })
        .collect(Collectors.toList());
    
    assertEquals("222M1000", computedRows.get(0));
    assertEquals("MM211011", computedRows.get(1));
    assertEquals("4420001M", computedRows.get(2));
    assertEquals("MM211121", computedRows.get(3));
    assertEquals("34M11M21", computedRows.get(4));
    assertEquals("1M21112M", computedRows.get(5));
    assertEquals("11100011", computedRows.get(6));
    assertEquals("00000000", computedRows.get(7));
  }
  
  @Test
  public void testAllItemsStartHidden() throws Exception {
    GameBoard board = engine.getGameBoard();
    boolean allHidden = board.getGameBoardItems().stream()
      .flatMap(List::stream)
      .allMatch(item -> item.isHidden());
    
    assertTrue(allHidden);
  }
  
  @Test
  public void testGameOverByDeath() throws Exception {
    GameBoard board = engine.getGameBoard();
    
    GameBoardItem item = board.getGameBoardItems().get(0).get(3);
    
    assertFalse(board.isGameOver());
    
    // First mine is located at row 0 column 3. See testMineCount() for a "map"
    engine.reveal(board, 0, 3);
    
    assertTrue(board.isGameOver());
    assertTrue(item.isCauseOfDeath());
    
    boolean allMinesVisible = board.getGameBoardItems().stream()
        .flatMap(List::stream)
        .filter(itm -> itm.isMine())
        .allMatch(itm -> !itm.isHidden());
    
    assertTrue(allMinesVisible);
  }
  
  @Test
  public void testMarkAndUnmark() throws Exception {
    GameBoard board = engine.getGameBoard();
    
    GameBoardItem item = board.getGameBoardItems().get(0).get(0);
    
    assertFalse(item.isMarked());
    
    engine.mark(board, 0, 0);
    
    assertTrue(item.isMarked());
    
    engine.unmark(board, 0, 0);
    
    assertFalse(item.isMarked());
  }
  
  @Test
  public void testMarkedMineDoesNotKillOnReveal() throws Exception {
    GameBoard board = engine.getGameBoard();
    
    assertFalse(board.isGameOver());
    
    engine.mark(board, 0, 3);
    engine.reveal(board, 0, 3);
    
    assertFalse(board.isGameOver());
  }
  
  @Test
  public void testRevealANonZeroItemOnlyRevealsOneItem() throws Exception {
    GameBoard board = engine.getGameBoard();
    
    // borrow this test to verify all items start hidden
    testAllItemsStartHidden();
    
    engine.reveal(board, 0, 0);
    
    long expectedAmountOfVisibleItems = 1L;
    long actualAmountOfVisibleItems =  board.getGameBoardItems().stream()
      .flatMap(List::stream)
      .filter(item -> !item.isHidden())
      .count();
    
    assertEquals(expectedAmountOfVisibleItems, actualAmountOfVisibleItems);
  }
  
  @Test
  public void testRevealAZeroItemRevealsTheAdjacentItems() throws Exception {
    GameBoard board = engine.getGameBoard();
    
    // borrow this test to verify all items start hidden
    testAllItemsStartHidden();
    
    engine.reveal(board, 7, 0);
    
    long expectedAmountOfVisibleItems = 21L;
    long actualAmountOfVisibleItems =  board.getGameBoardItems().stream()
      .flatMap(List::stream)
      .filter(item -> !item.isHidden())
      .count();
    
    assertEquals(expectedAmountOfVisibleItems, actualAmountOfVisibleItems);
  }
}
