package com.example.chessAI.ai;

import java.util.List;

import com.example.chessAI.ChessBoard;
import com.example.chessAI.Color;
import com.example.chessAI.Move;
import com.example.chessAI.MoveGenerator;



/**
 * Basic Minimax chess search algorithm.
 */
public class Minimax {


    private Evaluation evaluation;

    private MoveGenerator moveGenerator;



    public Minimax() {

        evaluation =
                new Evaluation();

        moveGenerator =
                new MoveGenerator();
    }




    /**
     * Starts minimax search.
     *
     * @param board current board
     * @param depth search depth
     * @param maximizingPlayer true = White, false = Black
     */
    public int search(
            ChessBoard board,
            int depth,
            boolean maximizingPlayer) {


        /*
         * Stop condition
         */
        if(depth == 0) {

            return evaluation.evaluate(board);
        }



        Color side =
                maximizingPlayer
                ? Color.WHITE
                : Color.BLACK;



        List<Move> moves =
                moveGenerator
                .generateLegalMoves(
                        board,
                        side
                );



        /*
         * No legal moves
         */
        if(moves.isEmpty()) {

            return evaluation.evaluate(board);
        }





        if(maximizingPlayer) {


            int bestScore =
                    Integer.MIN_VALUE;



            for(Move move : moves) {


                board.makeMove(move);



                int score =
                        search(
                                board,
                                depth - 1,
                                false
                        );



                board.undoMove();



                if(score > bestScore) {

                    bestScore = score;
                }
            }



            return bestScore;



        } else {



            int bestScore =
                    Integer.MAX_VALUE;



            for(Move move : moves) {


                board.makeMove(move);



                int score =
                        search(
                                board,
                                depth - 1,
                                true
                        );



                board.undoMove();



                if(score < bestScore) {

                    bestScore = score;
                }
            }



            return bestScore;
        }
    }






    /**
     * Finds the best move.
     */
    public Move findBestMove(
            ChessBoard board,
            Color color,
            int depth) {



        List<Move> moves =
                moveGenerator
                .generateLegalMoves(
                        board,
                        color
                );



        Move bestMove = null;



        int bestScore =
                color == Color.WHITE
                ? Integer.MIN_VALUE
                : Integer.MAX_VALUE;




        for(Move move : moves) {


            board.makeMove(move);



            int score =
                    search(
                            board,
                            depth - 1,
                            color == Color.BLACK
                    );



            board.undoMove();




            if(color == Color.WHITE) {


                if(score > bestScore) {

                    bestScore = score;
                    bestMove = move;
                }


            } else {


                if(score < bestScore) {

                    bestScore = score;
                    bestMove = move;
                }
            }
        }



        return bestMove;
    }
}