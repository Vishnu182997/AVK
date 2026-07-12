package com.example.chessAI.ai;

import java.util.List;

import com.example.chessAI.ChessBoard;
import com.example.chessAI.Color;
import com.example.chessAI.Move;
import com.example.chessAI.MoveGenerator;


public class ChessEngine {

    private int searchDepth;
    private Evaluation evaluation;
    private MoveGenerator moveGenerator;

    public ChessEngine() {
        this.searchDepth = 3; // Default depth
        this.evaluation = new Evaluation();
        this.moveGenerator = new MoveGenerator();
    }

    /**
     * Finds the best move for the current player.
     */
    public Move getBestMove(ChessBoard board, Color sideToMove) {

        List<Move> moves = moveGenerator.generateLegalMoves(board, sideToMove);

        if (moves == null || moves.isEmpty()) {
            return null;
        }

        Move bestMove = null;
        int bestScore = Integer.MIN_VALUE;

        AlphaBeta alphaBeta = new AlphaBeta(evaluation);

        for (Move move : moves) {

            board.makeMove(move);

            int score = alphaBeta.search(
                    board,
                    searchDepth - 1,
                    Integer.MIN_VALUE,
                    Integer.MAX_VALUE,
                    false
            );

            board.undoMove();

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }

    /**
     * Returns the evaluation of the current position.
     */
    public int evaluatePosition(ChessBoard board) {
        return evaluation.evaluate(board);
    }

    /**
     * Sets engine search depth.
     */
    public void setSearchDepth(int depth) {

        if (depth < 1) {
            depth = 1;
        }

        this.searchDepth = depth;
    }

    /**
     * Returns current search depth.
     */
    public int getSearchDepth() {
        return searchDepth;
    }
}