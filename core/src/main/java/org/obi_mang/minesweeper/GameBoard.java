package org.obi_mang.minesweeper;

import java.util.List;

/**
 * The representation of a game board and its state.<br>
 * <p>
 * Obtain an instance from an {@link Engine}
 */
public class GameBoard {
  private List<List<GameBoardItem>> gameBoardItems;
  private boolean dead;
  private boolean victorious;
  
  protected GameBoard(List<List<GameBoardItem>> gameBoardItems) {
    this.gameBoardItems = gameBoardItems;
    dead = false;
    victorious = false;
  }

  /**
   * Get all the game board items.<br>
   * The sublists should be considered as rows on the game board, and the items in each sublist should be
   * considered as the columns of the game board.
   * 
   * @return a list of sublists containing the game board items
   */
  public List<List<GameBoardItem>> getGameBoardItems() {
    return gameBoardItems;
  }

  /**
   * Check if the game is over.<br>
   * A game is considered over if the player died or cleared all items on the board except for the mines.
   * 
   * @return true if either dead or victorious, otherwise false
   */
  public boolean isGameOver() {
    return (isDead() || isVictorious());
  }
  
  /**
   * Check if game board has been set to be dead.
   * 
   * @return true if game has ended by death, otherwise false
   */
  protected boolean isDead() {
    return dead;
  }
  
  /**
   * Set game board to dead. This is a one time operation, i.e. once called the dead state cannot be reverted.
   */
  protected void setDead() {
    dead = true;
  }
  
  /**
   * Check if the game board has been set to victorious.
   *  
   * @return true if the game ended victorious, otherwise false
   */
  protected boolean isVictorious() {
    return victorious;
  }
  
  /**
   * Set the game board to have ended victorious. This is a one time operation, i.e. once called the victorious
   * state cannot be reverted.
   */
  protected void setVictorious() {
    victorious = true;
  }
}
