package com.example.chessAI.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import javax.swing.JPanel;
import javax.swing.Timer;

public class MoveAnimation {

    private static final int FPS = 60;
    private static final int DURATION = 250; // milliseconds

    private final JPanel boardPanel;

    private Image pieceImage;

    private Point start;
    private Point end;
    private Point current;

    private Timer timer;

    private long startTime;

    private boolean running;

    public MoveAnimation(JPanel boardPanel) {
        this.boardPanel = boardPanel;
    }

    /**
     * Starts the animation.
     */
    public void start(Image image, Point start, Point end) {

        if (image == null || start == null || end == null) {
            return;
        }

        this.pieceImage = image;
        this.start = start;
        this.end = end;
        this.current = new Point(start);

        running = true;

        startTime = System.currentTimeMillis();

        if (timer != null) {
            timer.stop();
        }

        timer = new Timer(1000 / FPS, e -> update());

        timer.start();
    }

    /**
     * Updates animation.
     */
    private void update() {

        long elapsed = System.currentTimeMillis() - startTime;

        double progress = Math.min(1.0, (double) elapsed / DURATION);

        current.x = (int) (start.x + (end.x - start.x) * progress);
        current.y = (int) (start.y + (end.y - start.y) * progress);

        boardPanel.repaint();

        if (progress >= 1.0) {
            running = false;
            timer.stop();
            boardPanel.repaint();
        }
    }

    /**
     * Paints the animated piece.
     */
    public void paint(Graphics g) {

        if (!running || pieceImage == null) {
            return;
        }

        g.drawImage(
                pieceImage,
                current.x,
                current.y,
                boardPanel
        );
    }

    /**
     * Returns whether animation is active.
     */
    public boolean isRunning() {
        return running;
    }
}