package com.example.chessAI.ai;


/**
 * Manages chess engine thinking time.
 */
public class TimeManager {


    private long startTime;

    private long allocatedTime;

    private long stopTime;



    /**
     * Starts the timer.
     *
     * @param timeLimit milliseconds allowed for thinking
     */
    public void start(long timeLimit) {

        startTime =
                System.currentTimeMillis();

        allocatedTime = timeLimit;

        stopTime =
                startTime + timeLimit;
    }




    /**
     * Returns true if engine must stop searching.
     */
    public boolean isTimeUp() {


        return System.currentTimeMillis()
                >= stopTime;
    }




    /**
     * Returns elapsed time.
     */
    public long getElapsedTime() {


        return System.currentTimeMillis()
                - startTime;
    }




    /**
     * Returns remaining time.
     */
    public long getRemainingTime() {


        long remaining =
                stopTime
                -
                System.currentTimeMillis();


        return Math.max(
                0,
                remaining
        );
    }




    /**
     * Returns allocated thinking time.
     */
    public long getAllocatedTime() {

        return allocatedTime;
    }




    /**
     * Adds extra time.
     */
    public void addTime(long extraTime) {


        allocatedTime += extraTime;

        stopTime += extraTime;
    }




    /**
     * Resets timer.
     */
    public void reset() {


        startTime = 0;

        allocatedTime = 0;

        stopTime = 0;
    }




    /**
     * Calculates time per move.
     *
     * Example:
     *
     * 5 minutes / 40 moves
     */
    public static long calculateMoveTime(
            long remainingTime,
            int movesRemaining) {


        if(movesRemaining <= 0) {

            return remainingTime / 10;
        }


        long time =
                remainingTime
                /
                movesRemaining;



        /*
         * Keep some safety margin.
         */
        return (long)
                (time * 0.85);
    }
}