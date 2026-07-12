package com.example.chessAI.ai;

import com.example.chessAI.ChessBoard;
import com.example.chessAI.Color;
import com.example.chessAI.Piece;
import com.example.chessAI.PieceType;

public class Evaluation {

    /**
     * Evaluates the board.
     *
     * Positive score  -> White is better
     * Negative score  -> Black is better
     */
    public int evaluate(ChessBoard board) {

        int score = 0;

        Piece[][] pieces = board.getBoard();

        for (int row = 0; row < ChessBoard.SIZE; row++) {

            for (int col = 0; col < ChessBoard.SIZE; col++) {

                Piece piece = pieces[row][col];

                if (piece == null) {
                    continue;
                }

                int value = getPieceValue(piece.getType());

                if (piece.getColor() == Color.WHITE) {
                    score += value;
                } else {
                    score -= value;
                }
            }
        }

        return score;
    }

    /**
     * Returns the value of a piece.
     */
    private int getPieceValue(PieceType type) {

        switch (type) {

            case PAWN:
                return 100;

            case KNIGHT:
                return 320;

            case BISHOP:
                return 330;

            case ROOK:
                return 500;

            case QUEEN:
                return 900;

            case KING:
                return 20000;

            default:
                return 0;
        }
    }
}