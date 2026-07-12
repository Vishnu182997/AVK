package com.example.chessAI.gui;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {

    public static final int BOARD_SIZE = 8;

    private final SquarePanel[][] squares;

    public BoardPanel() {

        squares = new SquarePanel[BOARD_SIZE][BOARD_SIZE];

        initializeBoard();
    }

    /**
     * Creates the 8x8 chessboard.
     */
    private void initializeBoard() {

        setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        setPreferredSize(new Dimension(640, 640));

        removeAll();

        for (int row = 0; row < BOARD_SIZE; row++) {

            for (int col = 0; col < BOARD_SIZE; col++) {

                boolean lightSquare = (row + col) % 2 == 0;

                SquarePanel square = new SquarePanel(row, col, lightSquare);

                squares[row][col] = square;

                add(square);
            }
        }
    }

    /**
     * Returns a square at the specified position.
     */
    public SquarePanel getSquare(int row, int col) {

        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
            return null;
        }

        return squares[row][col];
    }

    /**
     * Clears highlights from every square.
     */
    public void clearHighlights() {

        for (int row = 0; row < BOARD_SIZE; row++) {

            for (int col = 0; col < BOARD_SIZE; col++) {

                squares[row][col].setHighlighted(false);
            }
        }

        repaint();
    }

    /**
     * Highlights one square.
     */
    public void highlightSquare(int row, int col) {

        SquarePanel square = getSquare(row, col);

        if (square != null) {
            square.setHighlighted(true);
        }

        repaint();
    }

    /**
     * Resets the chessboard.
     */
    public void resetBoard() {

        clearHighlights();

        // Piece placement will be added after ChessBoard.java is implemented.

        repaint();
    }

    /**
     * Refreshes the board.
     */
    public void refreshBoard() {
        repaint();
    }

    /**
     * Draws pieces.
     * (Implemented after ChessBoard and Piece classes are available.)
     */
    public void updateBoard() {

        repaint();
    }
}