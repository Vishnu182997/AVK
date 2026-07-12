package com.example.chessAI;

/**
 * Represents the type of a chess piece.
 */
public enum PieceType {

    PAWN(100, 'P'),
    KNIGHT(320, 'N'),
    BISHOP(330, 'B'),
    ROOK(500, 'R'),
    QUEEN(900, 'Q'),
    KING(20000, 'K');

    // Material value used by the evaluation function
    private final int value;

    // FEN symbol (uppercase for white)
    private final char fenSymbol;

    PieceType(int value, char fenSymbol) {
        this.value = value;
        this.fenSymbol = fenSymbol;
    }

    /**
     * Returns the material value of the piece.
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the uppercase FEN symbol.
     */
    public char getFenSymbol() {
        return fenSymbol;
    }

    /**
     * Returns the FEN symbol for the specified color.
     */
    public char getFenSymbol(Color color) {
        if (color == Color.WHITE) {
            return fenSymbol;
        }
        return Character.toLowerCase(fenSymbol);
    }

    /**
     * Returns the PieceType from a FEN character.
     */
    public static PieceType fromFen(char c) {

        switch (Character.toUpperCase(c)) {

            case 'P':
                return PAWN;

            case 'N':
                return KNIGHT;

            case 'B':
                return BISHOP;

            case 'R':
                return ROOK;

            case 'Q':
                return QUEEN;

            case 'K':
                return KING;

            default:
                throw new IllegalArgumentException("Invalid FEN piece: " + c);
        }
    }
}