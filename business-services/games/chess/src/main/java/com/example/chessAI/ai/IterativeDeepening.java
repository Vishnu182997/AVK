package com.example.chessAI.ai;


import java.util.List;

import com.example.chessAI.ChessBoard;
import com.example.chessAI.Color;
import com.example.chessAI.Move;
import com.example.chessAI.MoveGenerator;


/**
 * Iterative Deepening Search
 */
public class IterativeDeepening {


    private AlphaBeta alphaBeta;
    private MoveGenerator moveGenerator;

    private long endTime;


    public IterativeDeepening() {

        Evaluation evaluation = new Evaluation();

        alphaBeta = new AlphaBeta(evaluation);

        moveGenerator = new MoveGenerator();
    }


    /**
     * Finds best move within given time.
     *
     * @param board current position
     * @param color player
     * @param maxDepth maximum search depth
     * @param timeLimit milliseconds
     */
    public Move searchBestMove(
            ChessBoard board,
            Color color,
            int maxDepth,
            long timeLimit) {


        endTime =
                System.currentTimeMillis()
                + timeLimit;


        Move bestMove = null;


        for (int depth = 1;
             depth <= maxDepth;
             depth++) {


            if (timeExceeded()) {
                break;
            }


            Move currentBest =
                    searchDepth(
                            board,
                            color,
                            depth
                    );


            if (currentBest != null) {

                bestMove = currentBest;
            }
        }


        return bestMove;
    }



    private Move searchDepth(
            ChessBoard board,
            Color color,
            int depth) {


        List<Move> moves =
                moveGenerator.generateLegalMoves(
                        board,
                        color
                );


        Move bestMove = null;

        int bestScore =
                Integer.MIN_VALUE;



        for (Move move : moves) {


            if (timeExceeded()) {
                break;
            }


            board.makeMove(move);



            int score =
                    alphaBeta.search(
                            board,
                            depth - 1,
                            Integer.MIN_VALUE,
                            Integer.MAX_VALUE,
                            false
                    );



            board.undoMove();



            if(score > bestScore) {

                bestScore = score;
                bestMove = move;
            }
        }


        return bestMove;
    }



    private boolean timeExceeded() {

        return System.currentTimeMillis()
                >= endTime;
    }
}