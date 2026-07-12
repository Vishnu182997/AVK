package com.example.chessAI;

import java.util.ArrayList;
import java.util.List;

public class MoveGenerator {

    /**
     * Generates all legal moves for the given side.
     */
    public List<Move> generateLegalMoves(ChessBoard board, Color color) {

        List<Move> moves = new ArrayList<Move>();

        Piece[][] pieces = board.getBoard();

        for (int row = 0; row < ChessBoard.SIZE; row++) {

            for (int col = 0; col < ChessBoard.SIZE; col++) {

                Piece piece = pieces[row][col];

                if (piece == null) {
                    continue;
                }

                if (piece.getColor() != color) {
                    continue;
                }

                List<Move> pieceMoves =
                        piece.getLegalMoves(board, row, col);

                if (pieceMoves != null) {
                    moves.addAll(pieceMoves);
                }
            }
        }

        return moves;
    }

    /**
     * Returns true if the side has at least one legal move.
     */
    public boolean hasLegalMoves(ChessBoard board, Color color) {

        return !generateLegalMoves(board, color).isEmpty();
    }
}