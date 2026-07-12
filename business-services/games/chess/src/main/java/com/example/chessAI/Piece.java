package com.example.chessAI;

import java.util.List;

/**
 * Base class for all chess pieces.
 */
public abstract class Piece {

    protected Color color;
    protected PieceType type;

    // Tracks whether the piece has moved (important for castling and pawn movement)
    protected boolean hasMoved;

    public Piece(Color color, PieceType type) {
        this.color = color;
        this.type = type;
        this.hasMoved = false;
    }

    /**
     * Returns the color of the piece.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns the type of the piece.
     */
    public PieceType getType() {
        return type;
    }

    /**
     * Returns true if this piece has moved.
     */
    public boolean hasMoved() {
        return hasMoved;
    }

    /**
     * Updates the moved flag.
     */
    public void setMoved(boolean moved) {
        this.hasMoved = moved;
    }

    /**
     * Returns the material value of the piece.
     */
    public abstract int getValue();

    /**
     * Generates pseudo-legal moves for this piece.
     *
     * @param board Chess board
     * @param row   Current row
     * @param col   Current column
     * @return List of possible moves
     */
    public abstract List<Move> getLegalMoves(
            ChessBoard board,
            int row,
            int col
    );

    /**
     * Returns the FEN character for this piece.
     * Example:
     * White King -> 'K'
     * Black Queen -> 'q'
     */
    public abstract char getFenSymbol();

    @Override
    public String toString() {

        char symbol = getFenSymbol();

        return String.valueOf(symbol);
    }
}