package com.example.chessAI.gui;

import com.example.chessAI.Color;
import com.example.chessAI.Piece;
import com.example.chessAI.PieceType;

import javax.swing.*;
import java.awt.*;

public class SquarePanel extends JPanel {

    private final int row;
    private final int col;
    private final boolean lightSquare;
    private boolean highlighted;
    private boolean selected;
    private ImageIcon pieceIcon;
    private Piece piece;

    public SquarePanel(int row, int col, boolean lightSquare) {
        this.row = row;
        this.col = col;
        this.lightSquare = lightSquare;
        this.highlighted = false;
        this.selected = false;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(80, 80));
        setOpaque(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        updateSquareColor();
    }

    public void setPiece(Piece piece, ImageIcon icon) {
        this.piece = piece;
        this.pieceIcon = icon;
        repaint();
    }

    public void setPiece(ImageIcon icon) {
        this.piece = null;
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

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        updateSquareColor();
    }

    public int getBoardRow() {
        return row;
    }

    public int getBoardColumn() {
        return col;
    }

    private void updateSquareColor() {
        if (selected) {
            setBackground(new java.awt.Color(110, 170, 255));
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
        } else if (piece != null) {
            drawUnicodePiece(g);
        }
    }

    private void drawUnicodePiece(Graphics g) {
        String symbol = unicodeSymbol(piece);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int fontSize = Math.max(24, Math.min(getWidth(), getHeight()) - 18);
        g2.setFont(new Font(Font.SERIF, Font.PLAIN, fontSize));
        g2.setColor(piece.getColor() == Color.WHITE ? java.awt.Color.WHITE : java.awt.Color.BLACK);
        FontMetrics metrics = g2.getFontMetrics();
        int x = (getWidth() - metrics.stringWidth(symbol)) / 2;
        int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
        g2.drawString(symbol, x, y);
        g2.dispose();
    }

    private String unicodeSymbol(Piece piece) {
        PieceType type = piece.getType();
        if (piece.getColor() == Color.WHITE) {
            switch (type) {
                case KING: return "♔";
                case QUEEN: return "♕";
                case ROOK: return "♖";
                case BISHOP: return "♗";
                case KNIGHT: return "♘";
                case PAWN: default: return "♙";
            }
        }
        switch (type) {
            case KING: return "♚";
            case QUEEN: return "♛";
            case ROOK: return "♜";
            case BISHOP: return "♝";
            case KNIGHT: return "♞";
            case PAWN: default: return "♟";
        }
    }
}
