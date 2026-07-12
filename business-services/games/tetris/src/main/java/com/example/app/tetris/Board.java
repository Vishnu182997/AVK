package com.example.app.tetris;

import java.awt.Color;
import java.awt.Graphics;

public class Board {

	// Board dimensions
	public static final int ROWS = 20;
	public static final int COLS = 10;

	// Cell size in pixels
	private final int tileSize;

	// Stores placed block colors (null = empty)
	private Color[][] grid;

	public Board(int tileSize) {

		this.tileSize = tileSize;

		grid = new Color[ROWS][COLS];

		clearBoard();
	}

	// Remove every block from the board
	public void clearBoard() {

		for (int row = 0; row < ROWS; row++) {

			for (int col = 0; col < COLS; col++) {

				grid[row][col] = null;

			}
		}
	}

	// Returns true if a cell contains a block
	public boolean isOccupied(int row, int col) {

		// Outside left/right/bottom is treated as occupied
		if (col < 0 || col >= COLS)
			return true;

		if (row >= ROWS)
			return true;

		// Above the board is allowed
		if (row < 0)
			return false;

		return grid[row][col] != null;
	}

	// Returns the color stored in a cell
	public Color getCellColor(int row, int col) {

		if (row < 0 || row >= ROWS)
			return null;

		if (col < 0 || col >= COLS)
			return null;

		return grid[row][col];
	}

	// Draw all locked blocks
	public void draw(Graphics g) {

		for (int row = 0; row < ROWS; row++) {

			for (int col = 0; col < COLS; col++) {

				if (grid[row][col] != null) {

					g.setColor(grid[row][col]);

					g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);

					g.setColor(Color.BLACK);

					g.drawRect(col * tileSize, row * tileSize, tileSize, tileSize);
				}
			}
		}

		// Draw grid lines
		g.setColor(Color.DARK_GRAY);

		for (int row = 0; row <= ROWS; row++) {

			g.drawLine(0, row * tileSize, COLS * tileSize, row * tileSize);
		}

		for (int col = 0; col <= COLS; col++) {

			g.drawLine(col * tileSize, 0, col * tileSize, ROWS * tileSize);
		}
	}

	// Gives access to the grid for later methods
	public Color[][] getGrid() {
		return grid;
	}

	// Returns true if the piece can be placed at its current position
	public boolean canPlace(Piece piece) {

		int[][] shape = piece.getCurrentShape();

		for (int row = 0; row < shape.length; row++) {

			for (int col = 0; col < shape[row].length; col++) {

				if (shape[row][col] == 0)
					continue;

				int boardRow = piece.y + row;
				int boardCol = piece.x + col;

				// Left wall
				if (boardCol < 0)
					return false;

				// Right wall
				if (boardCol >= COLS)
					return false;

				// Bottom
				if (boardRow >= ROWS)
					return false;

				// Existing locked block
				if (boardRow >= 0 && grid[boardRow][boardCol] != null)
					return false;
			}
		}

		return true;
	}

	// Copies the current piece into the board grid
	public void lockPiece(Piece piece) {

		int[][] shape = piece.getCurrentShape();

		for (int row = 0; row < shape.length; row++) {

			for (int col = 0; col < shape[row].length; col++) {

				if (shape[row][col] == 0)
					continue;

				int boardRow = piece.y + row;
				int boardCol = piece.x + col;

				if (boardRow >= 0 && boardRow < ROWS && boardCol >= 0 && boardCol < COLS) {
					grid[boardRow][boardCol] = piece.getColor();
				}
			}
		}
	}

	// Clears all completed lines.
	// Returns the number of lines removed.
	public int clearLines() {

		int linesCleared = 0;

		for (int row = ROWS - 1; row >= 0; row--) {

			boolean full = true;

			// Check whether the row is completely filled
			for (int col = 0; col < COLS; col++) {

				if (grid[row][col] == null) {
					full = false;
					break;
				}
			}

			// Remove the row
			if (full) {

				removeRow(row);

				linesCleared++;

				// Check this row again after shifting
				row++;
			}
		}

		return linesCleared;
	}

	private void removeRow(int rowToRemove) {

		// Shift every row above downward
		for (int row = rowToRemove; row > 0; row--) {

			for (int col = 0; col < COLS; col++) {

				grid[row][col] = grid[row - 1][col];

			}
		}

		// Clear the new top row
		for (int col = 0; col < COLS; col++) {

			grid[0][col] = null;

		}
	}
	
	public boolean isGameOver() {

	    // If any block reaches the top row,
	    // the game is over.

	    for (int col = 0; col < COLS; col++) {

	        if (grid[0][col] != null) {
	            return true;
	        }

	    }

	    return false;
	}
	
	public void reset() {

	    clearBoard();

	}
	
	public int getBlockCount() {

	    int count = 0;

	    for (int row = 0; row < ROWS; row++) {

	        for (int col = 0; col < COLS; col++) {

	            if (grid[row][col] != null) {
	                count++;
	            }

	        }
	    }

	    return count;
	}
}