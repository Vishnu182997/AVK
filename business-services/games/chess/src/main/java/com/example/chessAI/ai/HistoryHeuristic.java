package com.example.chessAI.ai;

import com.example.chessAI.Move;

/**
 * History heuristic for move ordering.
 *
 * Stores a score for moves:
 *
 * Higher score = search earlier
 */
public class HistoryHeuristic {


    /*
     * 64 x 64 board move history
     *
     * [from square][to square]
     */
    private int[][] history;



    public HistoryHeuristic() {

        history =
                new int[64][64];
    }




    /**
     * Records a successful move.
     *
     * Usually called when a move
     * causes a beta cutoff.
     */
    public void addHistory(
            Move move,
            int depth) {


        if(move == null) {
            return;
        }


        int from =
                getSquare(
                        move.getFromRow(),
                        move.getFromCol()
                );


        int to =
                getSquare(
                        move.getToRow(),
                        move.getToCol()
                );



        /*
         * Depth squared gives
         * deeper cutoffs more importance.
         */
        history[from][to]
                += depth * depth;



        /*
         * Prevent integer overflow.
         */
        if(history[from][to] > 1000000) {

            history[from][to] = 1000000;
        }
    }






    /**
     * Returns move score.
     */
    public int getScore(Move move) {


        if(move == null) {
            return 0;
        }



        int from =
                getSquare(
                        move.getFromRow(),
                        move.getFromCol()
                );


        int to =
                getSquare(
                        move.getToRow(),
                        move.getToCol()
                );


        return history[from][to];
    }






    /**
     * Reduces old history values.
     *
     * Called periodically.
     */
    public void decay() {


        for(int from = 0;
            from < 64;
            from++) {


            for(int to = 0;
                to < 64;
                to++) {


                history[from][to] /= 2;
            }
        }
    }






    /**
     * Clears all history.
     */
    public void clear() {


        for(int from = 0;
            from < 64;
            from++) {


            for(int to = 0;
                to < 64;
                to++) {


                history[from][to] = 0;
            }
        }
    }






    /**
     * Converts row/column
     * into square index.
     *
     * Example:
     *
     * a1 = 56
     * e4 = 36
     */
    private int getSquare(
            int row,
            int col) {


        return row * 8 + col;
    }
}