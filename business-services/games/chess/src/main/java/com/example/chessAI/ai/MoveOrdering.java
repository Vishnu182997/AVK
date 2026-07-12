package com.example.chessAI.ai;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.example.chessAI.Move;


public class MoveOrdering {


    private KillerMoves killerMoves;

    private HistoryHeuristic history;



    public MoveOrdering(
            KillerMoves killerMoves,
            HistoryHeuristic history) {


        this.killerMoves = killerMoves;
        this.history = history;
    }




    /**
     * Sort moves from strongest to weakest.
     */
    public void orderMoves(
            List<Move> moves,
            int depth) {


        Collections.sort(
                moves,
                new Comparator<Move>() {

            @Override
            public int compare(
                    Move a,
                    Move b) {


                return scoreMove(
                        b,
                        depth)
                        -
                        scoreMove(
                        a,
                        depth);
            }
        });
    }





    private int scoreMove(
            Move move,
            int depth) {


        int score = 0;



        // Captures first
        if(move.isCapture()) {

            if(move.getCapturedPiece()!=null
                    && move.getMovedPiece()!=null) {


                score +=
                    10 *
                    move.getCapturedPiece()
                    .getValue()
                    -
                    move.getMovedPiece()
                    .getValue();

            }
            else {

                score += 1000;
            }
        }



        // Promotion
        if(move.isPromotion()) {

            score += 800;
        }



        // Killer move bonus
        if(killerMoves.isKiller(
                move,
                depth)) {


            score += 500;
        }



        // History score
        score += history.getScore(move);



        return score;
    }
}