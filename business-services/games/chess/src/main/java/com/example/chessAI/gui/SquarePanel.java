package com.example.chessAI.gui;

import javax.swing.*;
import java.awt.*;

public class SquarePanel extends JPanel {

    // Board coordinates
    private final int row;
    private final int col;

    // Square color
    private final boolean lightSquare;

    // Highlight state
    private boolean highlighted;

    // Chess piece icon
    private ImageIcon pieceIcon;

    public SquarePanel(int row, int col, boolean lightSquare) {

        this.row = row;
        this.col = col;
        this.lightSquare = lightSquare;
        this.highlighted = false;

        initialize();
    }

    /**
     * Initialize square properties.
     */
    private void initialize() {

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(80, 80));

        setOpaque(true);

        updateSquareColor();
    }

    /**
     * Sets the piece image displayed on this square.
     */
    public void setPiece(ImageIcon icon) {
        this.pieceIcon = icon;
        repaint();
    }

    /**
     * Removes the displayed piece.
     */
    public void clearPiece() {
        this.pieceIcon = null;
        repaint();
    }

    /**
     * Enable/Disable highlighting.
     */
    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
        updateSquareColor();
    }

    /**
     * Returns highlight state.
     */
    public boolean isHighlighted() {
        return highlighted;
    }

    /**
     * Board row.
     */
    public int getBoardRow() {
        return row;
    }

    /**
     * Board column.
     */
    public int getBoardColumn() {
        return col;
    }

    /**
     * Update background color.
     */
    private void updateSquareColor() {

        if (highlighted) {
            setBackground(new Color(255, 255, 120));
        } else {
            if (lightSquare) {
                setBackground(new Color(240, 217, 181));
            } else {
                setBackground(new Color(181, 136, 99));
            }
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (pieceIcon != null) {

            Image image = pieceIcon.getImage();

            g.drawImage(
                    image,
                    5,
                    5,
                    getWidth() - 10,
                    getHeight() - 10,
                    this
            );
        }
    }
}