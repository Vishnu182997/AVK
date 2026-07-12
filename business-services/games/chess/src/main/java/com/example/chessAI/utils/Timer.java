package com.example.chessAI.utils;

/**
 * Simple utility timer for measuring execution time.
 */
public class Timer {

    private long startTime;
    private long endTime;
    private boolean running;

    /**
     * Creates a new timer.
     */
    public Timer() {
        running = false;
        startTime = 0;
        endTime = 0;
    }

    /**
     * Starts the timer.
     */
    public void start() {
        startTime = System.nanoTime();
        running = true;
    }

    /**
     * Stops the timer.
     */
    public void stop() {

        if (running) {
            endTime = System.nanoTime();
            running = false;
        }
    }

    /**
     * Resets the timer.
     */
    public void reset() {
        startTime = 0;
        endTime = 0;
        running = false;
    }

    /**
     * Returns elapsed nanoseconds.
     */
    public long getElapsedNano() {

        if (running) {
            return System.nanoTime() - startTime;
        }

        return endTime - startTime;
    }

    /**
     * Returns elapsed microseconds.
     */
    public long getElapsedMicro() {
        return getElapsedNano() / 1000;
    }

    /**
     * Returns elapsed milliseconds.
     */
    public long getElapsedMillis() {
        return getElapsedNano() / 1000000;
    }

    /**
     * Returns elapsed seconds.
     */
    public double getElapsedSeconds() {
        return getElapsedNano() / 1000000000.0;
    }

    /**
     * Returns whether timer is running.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Prints elapsed time.
     */
    public void print(String taskName) {

        System.out.println(
                taskName +
                " completed in " +
                getElapsedMillis() +
                " ms"
        );
    }

    /**
     * Returns formatted elapsed time.
     */
    @Override
    public String toString() {

        return getElapsedMillis() + " ms";
    }
}