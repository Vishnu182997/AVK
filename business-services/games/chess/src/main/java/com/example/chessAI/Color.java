package com.example.chessAI;

/**
 * Represents the two player colors in a chess game.
 */
public enum Color {

    WHITE,
    BLACK;

    /**
     * Returns the opposite color.
     *
     * @return BLACK if this is WHITE, otherwise WHITE.
     */
    public Color opposite() {
        return this == WHITE ? BLACK : WHITE;
    }

    /**
     * Returns true if this color is White.
     */
    public boolean isWhite() {
        return this == WHITE;
    }

    /**
     * Returns true if this color is Black.
     */
    public boolean isBlack() {
        return this == BLACK;
    }

    @Override
    public String toString() {
        return this == WHITE ? "White" : "Black";
    }
}