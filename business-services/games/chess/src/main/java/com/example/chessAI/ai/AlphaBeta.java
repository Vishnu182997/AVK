package com.example.chessAI.ai;

import java.util.List;

import com.example.chessAI.ChessBoard;
import com.example.chessAI.Color;
import com.example.chessAI.Move;
import com.example.chessAI.MoveGenerator;



/**
 * Alpha-Beta pruning search algorithm.
 *
 * Positive evaluation  = White advantage
 * Negative evaluation  = Black advantage
 */
public class AlphaBeta {


    private Evaluation evaluation;

    private MoveGenerator moveGenerator;


    public AlphaBeta(Evaluation evaluation) {

        this.evaluation = evaluation;
        this.moveGenerator = new MoveGenerator();
    }


    /**
     * Starts alpha-beta search.
     *
     * @param board current position
     * @param depth search depth
     * @param alpha best score for maximizing player
     * @param beta best score for minimizing player
     * @param maximizingPlayer true for White, false for Black
     */
    public int search(
            ChessBoard board,
            int depth,
            int alpha,
            int beta,
            boolean maximizingPlayer) {


        if (depth == 0) {

            return evaluation.evaluate(board);
        }


        Color side =
                maximizingPlayer
                        ? Color.WHITE
                        : Color.BLACK;


        List<Move> moves =
                moveGenerator.generateLegalMoves(
                        board,
                        side
                );


        if (moves.isEmpty()) {

            return evaluation.evaluate(board);
        }



        if (maximizingPlayer) {


            int maxScore = Integer.MIN_VALUE;


            for (Move move : moves) {


                board.makeMove(move);


                int score =
                        search(
                            board,
                            depth - 1,
                            alpha,
                            beta,
                            false
                        );


                board.undoMove();


                if (score > maxScore) {

                    maxScore = score;
                }


                if (maxScore > alpha) {

                    alpha = maxScore;
                }


                // Beta cutoff
                if (beta <= alpha) {

                    break;
                }
            }


            return maxScore;


        } else {


            int minScore = Integer.MAX_VALUE;


            for (Move move : moves) {


                board.makeMove(move);


                int score =
                        search(
                            board,
                            depth - 1,
                            alpha,
                            beta,
                            true
                        );


                board.undoMove();


                if (score < minScore) {

                    minScore = score;
                }


                if (minScore < beta) {

                    beta = minScore;
                }


                // Alpha cutoff
                if (beta <= alpha) {

                    break;
                }
            }


            return minScore;
        }
    }
}