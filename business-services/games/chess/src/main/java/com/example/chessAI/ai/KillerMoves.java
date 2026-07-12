package com.example.chessAI.ai;

import com.example.chessAI.Move;

/**
 * Stores killer moves for Alpha-Beta pruning.
 */
public class KillerMoves {


    /*
     * Number of killer moves stored per depth.
     * Usually 2 is enough.
     */
    private static final int MAX_KILLER_MOVES = 2;


    /*
     * killerMoves[depth][slot]
     */
    private Move[][] killerMoves;



    private int maxDepth;



    public KillerMoves(int maxDepth) {

        this.maxDepth = maxDepth;

        killerMoves =
                new Move[maxDepth + 1]
                        [MAX_KILLER_MOVES];
    }




    /**
     * Adds a killer move at a specific depth.
     */
    public void addKillerMove(
            Move move,
            int depth) {


        if(move == null) {
            return;
        }


        if(depth < 0 || depth > maxDepth) {
            return;
        }



        // Already exists
        if(isKiller(move, depth)) {
            return;
        }



        /*
         * Shift old killer move down
         */
        for(int i = MAX_KILLER_MOVES - 1;
            i > 0;
            i--) {


            killerMoves[depth][i]
                    =
                    killerMoves[depth][i - 1];
        }



        killerMoves[depth][0] = move;
    }






    /**
     * Checks whether move is a killer move.
     */
    public boolean isKiller(
            Move move,
            int depth) {


        if(move == null) {
            return false;
        }


        if(depth < 0 || depth > maxDepth) {
            return false;
        }



        for(int i = 0;
            i < MAX_KILLER_MOVES;
            i++) {


            Move killer =
                    killerMoves[depth][i];


            if(killer != null
                    && killer.equals(move)) {


                return true;
            }
        }


        return false;
    }






    /**
     * Returns killer move at index.
     */
    public Move getKillerMove(
            int depth,
            int index) {


        if(depth < 0
                || depth > maxDepth) {

            return null;
        }


        if(index < 0
                || index >= MAX_KILLER_MOVES) {

            return null;
        }


        return killerMoves[depth][index];
    }






    /**
     * Clears all stored killer moves.
     */
    public void clear() {


        for(int depth = 0;
            depth <= maxDepth;
            depth++) {


            for(int i = 0;
                i < MAX_KILLER_MOVES;
                i++) {


                killerMoves[depth][i] = null;
            }
        }
    }
}