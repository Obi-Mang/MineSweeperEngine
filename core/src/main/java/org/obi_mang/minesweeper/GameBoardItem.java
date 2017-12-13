package org.obi_mang.minesweeper;

/**
 * Representation of an item on the game board.
 */
public class GameBoardItem {
  private boolean mine;
  private boolean hidden;
  private boolean marked;
  private boolean causeOfDeath;
  private int adjacentMines;
  
  /**
   * An item has to be set to be a mine or not on creation. This cannot change.
   * 
   * @param isMine - true if the game board item is a mine
   */
  public GameBoardItem(boolean isMine) {
    this.mine = isMine;
    hidden = true;
    marked = false;
    causeOfDeath = false;
    adjacentMines = 0;
  }

  /**
   * Check if the game board item is a mine.
   * 
   * @return true if the game board item is a mine, otherwise false
   */
  public boolean isMine() {
    return mine;
  }
  
  /**
   * Check if the game board item is considered hidden (values cannot be seen).
   * 
   * @return true if the game board item is hidden, otherwise false
   */
  public boolean isHidden() {
    return hidden;
  }
  
  /**
   * Set the game board item to be visible. This is a one time operation, i.e. once the game board
   * item has been set to visible it cannot be hidden again.
   */
  public void setVisible() {
    this.hidden = false;
  }

  /**
   * Check if the game board item is marked as a potential mine.
   * 
   * @return true if the game board item is marked, otherwise false
   */
  public boolean isMarked() {
    return marked;
  }

  /**
   * Set the game board item to be marked as a potential mine.
   * 
   * @param marked - true if the game board item should be marked, otherwise false
   */
  public void setMarked(boolean marked) {
    this.marked = marked;
  }
  
  /**
   * Check if the game board item was the cause of death.
   * 
   * @return - true if the game board item was the cause of death, otherwise false
   */
  public boolean isCauseOfDeath() {
    return causeOfDeath;
  }
  
  /**
   * Set the game board item to be the cause of death. This is a one time operation, i.e.
   * once called the dead state cannot be reverted.
   */
  public void setCauseOfDeath() {
    this.causeOfDeath = true;
  }

  /**
   * Get how many mines there are adjacent to the game board item.
   * 
   * @return the number of adjacent mines
   */
  public int getAdjacentMines() {
    return adjacentMines;
  }

  /**
   * Set the amount of adjacent mines of the game board item.
   * 
   * @param adjacentMines - the number of adjacent mines
   */
  public void setAdjacentMines(int adjacentMines) {
    this.adjacentMines = adjacentMines;
  }
}
