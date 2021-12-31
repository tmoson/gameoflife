package com.github.tmoson.gameoflife;

/*
 * @author Tyler Moson
 */
public class Simulation {
  protected int width, length;
  int[][] board;

  /**
   * Create a new empty (completely dead) simulation with the given length and width
   * @param width the number of rows the simulation is going to have
   * @param length the number of columns that will be present in each row
   */
  public Simulation(int width, int length) {
    this.width = width; // not really a fan of using the 'this' keyword, but trying it out
    this.length = length;
    this.board = new int[width][length];
  }

  /**
   * Count the number of neighbors for a cell, using the {@link #getState(int x, int y)} method
   * @param x The row number of the cell you want to count the neighbors of
   * @param y The column number of the cell you want to count the neighbors of
   * @return int This sums up the number of neighbors
   */
  public int countNeighbors(int x, int y) {
    return getState(x - 1, y)
        + getState(x + 1, y)
        + getState(x, y + 1)
        + getState(x, y - 1)
        + getState(x - 1, y + 1)
        + getState(x - 1, y - 1)
        + getState(x + 1, y - 1)
        + getState(x + 1, y + 1);
  }

  /**
   * Get the state of a cell in a way that properly prevents IndexOutOfBounds exceptions
   * @param x the row number of the cell that you want to check the state of
   * @param y the column number of the cell that you want to check the state of
   * @return int 1 if a cell is alive, 0 if the cell is dead, or if the coordinate is out of bounds
   */
  public int getState(int x, int y) {
    if (x < 0 || x >= width || y < 0 || y >= length) {
      return 0;
    }
    return board[x][y];
  }

  /**
   * Run through a generation, marking cells as dead or alive,
   * as per the rules:
   *
   *       3 neighbors, a cell survives/comes back to live
   *       2 neighbors, an alive cell survives
   *       4 or more neighbors, a cell dies
   *       fewer than 2 neighbors, a cell dies.
   *
   * This really only requires two checks:
   * one to see if there are exactly three neighbors, and one to
   * see if a cell is alive and has exactly two neighbors, as in
   * all other cases it will die.
   */
  public void step() {
    int[][] newBoard = new int[width][length];
    for (int y = 0; y < length; ++y) {
      for (int x = 0; x < width; ++x) {
        int neighbors = countNeighbors(x, y);
        if (neighbors == 3) {
          newBoard[x][y] = 1;
        } else if (getState(x, y) == 1 && neighbors == 2) {
          newBoard[x][y] = 1;
        } else {
          newBoard[x][y] = 0;
        }
      }
    }
    board = newBoard;
  }

  /**
   * Manually set a cell as alive - doesn't check that you're within the bounds of the simulation
   * @param x The row of the cell you want to set alive
   * @param y The column of the cell you want to set alive
   */
  public void setAlive(int x, int y) {
    board[x][y] = 1;
  }

  /**
   * Manually set a cell as dead - doesn't check that you're within the bounds of the simulation
   * @param x The row of the cell you want to set dead
   * @param y The column of the cell you want to set dead
   */
  public void setDead(int x, int y) {
    board[x][y] = 0;
  }

  /**
   * Get the width of the board in the simulation, because you shouldn't need to have public access to the variable
   * @return int The width of the simulation's board
   */
  public int getWidth() {
    return width;
  }

  /**
   * Get the length of the board in the simulation, because you shouldn't need to have public access to the variable
   * @return int The length of the simulation's board
   */
  public int getLength() {
    return length;
  }
}
