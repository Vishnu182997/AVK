package com.example.chessAI.gui;

import javax.swing.*;
import java.awt.*;
import com.example.chessAI.Piece;
import com.example.chessAI.PieceType;

public class SquarePanel extends JPanel {

    private final int row;
    private final int col;
    private final boolean lightSquare;

    private boolean highlighted;
    private boolean selected;
    private boolean captureTarget;

    private ImageIcon pieceIcon;
    private Piece piece;

    public SquarePanel(int row, int col, boolean lightSquare) {

        this.row = row;
        this.col = col;
        this.lightSquare = lightSquare;
        this.highlighted = false;

        initialize();
    }

    private void initialize() {

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(80, 80));
        setOpaque(true);
        updateSquareColor();
    }

    public void setPiece(ImageIcon icon) {
        this.pieceIcon = icon;
        repaint();
    }

    public void setPiece(Piece piece, ImageIcon icon) {
        this.piece = piece;
        this.pieceIcon = icon;
        repaint();
    }

    public void clearPiece() {
        this.piece = null;
        this.pieceIcon = null;
        repaint();
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
        updateSquareColor();
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        updateSquareColor();
    }

    public void setCaptureTarget(boolean captureTarget) {
        this.captureTarget = captureTarget;
        updateSquareColor();
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public int getBoardRow() {
        return row;
    }

    public int getBoardColumn() {
        return col;
    }

    private void updateSquareColor() {

        if (selected) {
            setBackground(new java.awt.Color(100, 170, 255));
        } else if (captureTarget) {
            setBackground(new java.awt.Color(255, 120, 120));
        } else if (highlighted) {
            setBackground(new java.awt.Color(255, 255, 120));
        } else if (lightSquare) {
            setBackground(new java.awt.Color(240, 217, 181));
        } else {
            setBackground(new java.awt.Color(181, 136, 99));
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (pieceIcon != null) {
            Image image = pieceIcon.getImage();
            g.drawImage(image, 5, 5, getWidth() - 10, getHeight() - 10, this);
            return;
        }

        if (piece != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setFont(getFont().deriveFont(Font.PLAIN, Math.max(32f, getHeight() * 0.72f)));
                g2.setColor(piece.getColor() == com.example.chessAI.Color.WHITE ? java.awt.Color.WHITE : java.awt.Color.BLACK);

                String symbol = getUnicodeSymbol(piece);
                FontMetrics metrics = g2.getFontMetrics();
                int x = (getWidth() - metrics.stringWidth(symbol)) / 2;
                int y = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();

                g2.setColor(piece.getColor() == com.example.chessAI.Color.WHITE ? new java.awt.Color(35, 35, 35) : new java.awt.Color(240, 240, 240));
                g2.drawString(symbol, x + 2, y + 2);
                g2.setColor(piece.getColor() == com.example.chessAI.Color.WHITE ? java.awt.Color.WHITE : java.awt.Color.BLACK);
                g2.drawString(symbol, x, y);
            } finally {
                g2.dispose();
            }
        }
    }

    private String getUnicodeSymbol(Piece piece) {
        PieceType type = piece.getType();
        if (piece.getColor() == com.example.chessAI.Color.WHITE) {
            switch (type) {
                case KING:
                    return "♔";
                case QUEEN:
                    return "♕";
                case ROOK:
                    return "♖";
                case BISHOP:
                    return "♗";
                case KNIGHT:
                    return "♘";
                case PAWN:
                default:
                    return "♙";
            }
        }

        switch (type) {
            case KING:
                return "♚";
            case QUEEN:
                return "♛";
            case ROOK:
                return "♜";
            case BISHOP:
                return "♝";
            case KNIGHT:
                return "♞";
            case PAWN:
            default:
                return "♟";
        }
    }
}
