package com.example.chessAI.ai;

import com.example.chessAI.Move;

/**
 * Stores the result of an AI search.
 */
public class SearchResult {


    // Best move selected by engine
    private Move bestMove;


    // Evaluation score
    private int score;


    // Depth reached
    private int depth;


    // Number of positions searched
    private long nodes;


    // Time consumed in milliseconds
    private long timeTaken;


    // True if search completed normally
    private boolean completed;





    /**
     * Default constructor.
     */
    public SearchResult() {

        this.bestMove = null;

        this.score = 0;

        this.depth = 0;

        this.nodes = 0;

        this.timeTaken = 0;

        this.completed = false;
    }





    /**
     * Full constructor.
     */
    public SearchResult(
            Move bestMove,
            int score,
            int depth,
            long nodes,
            long timeTaken,
            boolean completed) {


        this.bestMove = bestMove;

        this.score = score;

        this.depth = depth;

        this.nodes = nodes;

        this.timeTaken = timeTaken;

        this.completed = completed;
    }






    public Move getBestMove() {

        return bestMove;
    }



    public void setBestMove(
            Move bestMove) {

        this.bestMove = bestMove;
    }





    public int getScore() {

        return score;
    }



    public void setScore(
            int score) {

        this.score = score;
    }






    public int getDepth() {

        return depth;
    }



    public void setDepth(
            int depth) {

        this.depth = depth;
    }






    public long getNodes() {

        return nodes;
    }



    public void setNodes(
            long nodes) {

        this.nodes = nodes;
    }






    public long getTimeTaken() {

        return timeTaken;
    }



    public void setTimeTaken(
            long timeTaken) {

        this.timeTaken = timeTaken;
    }






    public boolean isCompleted() {

        return completed;
    }



    public void setCompleted(
            boolean completed) {

        this.completed = completed;
    }






    @Override
    public String toString() {


        return "SearchResult{" +

                "bestMove=" + bestMove +

                ", score=" + score +

                ", depth=" + depth +

                ", nodes=" + nodes +

                ", timeTaken=" + timeTaken +

                ", completed=" + completed +

                '}';
    }
}