package com.example.chessAI.ai;


import java.util.List;

import com.example.chessAI.ChessBoard;
import com.example.chessAI.Color;
import com.example.chessAI.Move;
import com.example.chessAI.MoveGenerator;


/**
 * Quiescence Search
 */
public class QuiescenceSearch {


    private Evaluation evaluation;
    private MoveGenerator moveGenerator;



    public QuiescenceSearch() {

        evaluation = new Evaluation();

        moveGenerator =
                new MoveGenerator();
    }



    public int search(
            ChessBoard board,
            int alpha,
            int beta,
            Color color) {


        int standPat =
                evaluation.evaluate(board);



        if (standPat >= beta) {

            return beta;
        }


        if(alpha < standPat) {

            alpha = standPat;
        }



        List<Move> moves =
                moveGenerator.generateLegalMoves(
                        board,
                        color
                );



        for(Move move : moves) {


            // Search only tactical moves

            if(!move.isCapture()
                    && !move.isPromotion()) {

                continue;
            }



            board.makeMove(move);



            int score =
                    -search(
                            board,
                            -beta,
                            -alpha,
                            color.opposite()
                    );



            board.undoMove();



            if(score >= beta) {

                return beta;
            }


            if(score > alpha) {

                alpha = score;
            }
        }


        return alpha;
    }
}