package com.example.chessAI;

import java.util.ArrayList;
import java.util.List;

public class MoveGenerator {

    /**
     * Generates all legal moves for the given side.
     */
    public List<Move> generateLegalMoves(ChessBoard board, Color color) {

        List<Move> legalMoves = new ArrayList<Move>();

        for (Move move : generatePseudoLegalMoves(board, color)) {
            board.makeMove(move);
            if (!board.isKingInCheck(color)) {
                legalMoves.add(move);
            }
            board.undoMove();
        }

        return legalMoves;
    }

    /**
     * Generates moves that obey piece movement rules before king-safety filtering.
     */
    public List<Move> generatePseudoLegalMoves(ChessBoard board, Color color) {

        List<Move> moves = new ArrayList<Move>();
        Piece[][] pieces = board.getBoard();

        for (int row = 0; row < ChessBoard.SIZE; row++) {
            for (int col = 0; col < ChessBoard.SIZE; col++) {
                Piece piece = pieces[row][col];

                if (piece == null || piece.getColor() != color) {
                    continue;
                }

                List<Move> pieceMoves = piece.getLegalMoves(board, row, col);
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
