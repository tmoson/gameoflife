package com.github.tmoson.gameoflife;

public class Simulation {
  protected int width, height;
  int[][] board;

  public Simulation(int width, int height) {
    this.width = width;
    this.height = height;
    this.board = new int[width][height];
  }

  public void print() {
    for (int y = 0; y < height; ++y) {
      int tst = 100;
      StringBuilder sb = new StringBuilder("|");
      for (int x = 0; x < width; ++x) {
        if (board[x][y] == 0) {
          sb.append(".");
        } else {
          sb.append("*");
        }
      }
      System.out.println(sb);
    }
  }

  public int countNeighbors(int x, int y) {
    return getState(x - 1,y)
        + getState(x + 1,y)
        + getState(x,y + 1)
        + getState(x,y - 1)
        + getState(x - 1,y + 1)
        + getState(x - 1,y - 1)
        + getState(x + 1,y - 1)
        + getState(x + 1,y + 1);
  }

  public int getState(int x, int y){
    if(x < 0 || x >= width || y < 0 || y >= height){
      return 0;
    }
    return board[x][y];
  }

  public void step() {
    int[][] newBoard = new int[width][height];
    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        int neighbors = countNeighbors(x, y);
        if (getState(x,y) == 1) {
          if (neighbors < 2) {
            newBoard[x][y] = 0;
          } else if (neighbors == 2 || neighbors == 3) {
            newBoard[x][y] = 1;
          } else {
            newBoard[x][y] = 0;
          }
        } else {
          if (neighbors == 3) {
            newBoard[x][y] = 1;
          }
        }
      }
    }
    board = newBoard;
  }

  public void setAlive(int x, int y){
    board[x][y] = 1;
  }

  public void setDead(int x, int y){
    board[x][y] = 0;
  }

}
