package com.example.chessAI;

import java.io.Serializable;

/**
 * Represents a position (square) on the chess board.
 */
public class Position implements Serializable {

    private static final long serialVersionUID = 1L;

    private int row;
    private int col;

    /**
     * Creates a position.
     *
     * @param row Board row (0-7)
     * @param col Board column (0-7)
     */
    public Position(int row, int col) {

        if (!isValid(row, col)) {
            throw new IllegalArgumentException(
                    "Invalid board position: (" + row + "," + col + ")");
        }

        this.row = row;
        this.col = col;
    }

    /**
     * Copy constructor.
     */
    public Position(Position other) {
        this(other.row, other.col);
    }

    /**
     * Returns the row.
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column.
     */
    public int getCol() {
        return col;
    }

    /**
     * Sets the row.
     */
    public void setRow(int row) {

        if (!isValid(row, this.col)) {
            throw new IllegalArgumentException("Invalid row: " + row);
        }

        this.row = row;
    }

    /**
     * Sets the column.
     */
    public void setCol(int col) {

        if (!isValid(this.row, col)) {
            throw new IllegalArgumentException("Invalid column: " + col);
        }

        this.col = col;
    }

    /**
     * Sets both coordinates.
     */
    public void setPosition(int row, int col) {

        if (!isValid(row, col)) {
            throw new IllegalArgumentException(
                    "Invalid board position.");
        }

        this.row = row;
        this.col = col;
    }

    /**
     * Checks whether a board coordinate is valid.
     */
    public static boolean isValid(int row, int col) {
        return row >= 0 && row < 8 &&
               col >= 0 && col < 8;
    }

    /**
     * Converts the position to algebraic notation.
     * Example:
     * (7,0) -> a1
     * (0,4) -> e8
     */
    public String toAlgebraic() {

        char file = (char) ('a' + col);
        int rank = 8 - row;

        return "" + file + rank;
    }

    /**
     * Creates a Position from algebraic notation.
     * Example:
     * e2 -> (6,4)
     */
    public static Position fromAlgebraic(String square) {

        if (square == null || square.length() != 2) {
            throw new IllegalArgumentException("Invalid square: " + square);
        }

        char file = Character.toLowerCase(square.charAt(0));
        char rank = square.charAt(1);

        int col = file - 'a';
        int row = 8 - Character.getNumericValue(rank);

        return new Position(row, col);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (!(obj instanceof Position))
            return false;

        Position other = (Position) obj;

        return row == other.row &&
               col == other.col;
    }

    @Override
    public int hashCode() {

        int result = row;
        result = 31 * result + col;

        return result;
    }

    @Override
    public String toString() {
        return toAlgebraic();
    }
}