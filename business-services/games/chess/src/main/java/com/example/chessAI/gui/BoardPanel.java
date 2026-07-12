package com.example.chessAI.gui;

import com.example.chessAI.ChessBoard;
import com.example.chessAI.Color;
import com.example.chessAI.Move;
import com.example.chessAI.MoveGenerator;
import com.example.chessAI.MoveValidator;
import com.example.chessAI.Piece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class BoardPanel extends JPanel {

    public static final int BOARD_SIZE = 8;

    private final SquarePanel[][] squares;
    private final ChessBoard board;
    private final MoveGenerator moveGenerator;
    private final MoveValidator moveValidator;
    private final List<Move> selectedLegalMoves;

    private int selectedRow = -1;
    private int selectedCol = -1;
    private boolean gameOver;
    private JLabel statusLabel;

    public BoardPanel() {
        squares = new SquarePanel[BOARD_SIZE][BOARD_SIZE];
        board = new ChessBoard();
        moveGenerator = new MoveGenerator();
        moveValidator = new MoveValidator();
        selectedLegalMoves = new ArrayList<Move>();
        PieceImages.loadImages();
        initializeBoard();
        updateBoard();
    }

    private void initializeBoard() {
        setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        setPreferredSize(new Dimension(640, 640));
        removeAll();

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                boolean lightSquare = (row + col) % 2 == 0;
                SquarePanel square = new SquarePanel(row, col, lightSquare);
                square.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleSquareClick(square.getBoardRow(), square.getBoardColumn());
                    }
                });
                squares[row][col] = square;
                add(square);
            }
        }
    }

    public void setStatusLabel(JLabel statusLabel) {
        this.statusLabel = statusLabel;
        updateStatus();
    }

    public ChessBoard getChessBoard() {
        return board;
    }

    public SquarePanel getSquare(int row, int col) {
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
            return null;
        }
        return squares[row][col];
    }

    public void clearHighlights() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                squares[row][col].setHighlighted(false);
                squares[row][col].setSelected(false);
            }
        }
        repaint();
    }

    public void highlightSquare(int row, int col) {
        SquarePanel square = getSquare(row, col);
        if (square != null) {
            square.setHighlighted(true);
        }
        repaint();
    }

    public void resetBoard() {
        board.initializeBoard();
        gameOver = false;
        clearSelection();
        updateBoard();
        updateStatus();
    }

    public void refreshBoard() {
        updateBoard();
    }

    public void updateBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece piece = board.getPiece(row, col);
                squares[row][col].setPiece(piece, PieceImages.get(piece));
            }
        }
        revalidate();
        repaint();
    }

    private void handleSquareClick(int row, int col) {
        if (gameOver) {
            return;
        }

        Piece clickedPiece = board.getPiece(row, col);
        if (selectedRow >= 0) {
            Move selectedMove = findSelectedMove(row, col);
            if (selectedMove != null && moveValidator.isValidMove(board, selectedMove)) {
                board.makeMove(selectedMove);
                clearSelection();
                updateBoard();
                updateGameState();
                return;
            }
        }

        if (clickedPiece != null && clickedPiece.getColor() == board.getSideToMove()) {
            selectSquare(row, col);
        } else {
            clearSelection();
            updateBoard();
            updateStatus();
        }
    }

    private void selectSquare(int row, int col) {
        clearSelection();
        selectedRow = row;
        selectedCol = col;
        squares[row][col].setSelected(true);
        selectedLegalMoves.addAll(getLegalMovesFrom(row, col));
        for (Move move : selectedLegalMoves) {
            highlightSquare(move.getToRow(), move.getToCol());
        }
        updateStatus();
    }

    private List<Move> getLegalMovesFrom(int row, int col) {
        List<Move> moves = new ArrayList<Move>();
        for (Move move : moveGenerator.generateLegalMoves(board, board.getSideToMove())) {
            if (move.getFromRow() == row && move.getFromCol() == col) {
                moves.add(move);
            }
        }
        return moves;
    }

    private Move findSelectedMove(int row, int col) {
        for (Move move : selectedLegalMoves) {
            if (move.getToRow() == row && move.getToCol() == col) {
                return move;
            }
        }
        return null;
    }

    private void clearSelection() {
        selectedRow = -1;
        selectedCol = -1;
        selectedLegalMoves.clear();
        clearHighlights();
    }

    private void updateGameState() {
        Color turn = board.getSideToMove();
        List<Move> legalMoves = moveGenerator.generateLegalMoves(board, turn);
        if (legalMoves.isEmpty()) {
            gameOver = true;
        }
        updateStatus();
    }

    private void updateStatus() {
        if (statusLabel == null) {
            return;
        }
        Color turn = board.getSideToMove();
        boolean inCheck = board.isKingInCheck(turn);
        boolean noMoves = moveGenerator.generateLegalMoves(board, turn).isEmpty();
        String side = turn == Color.WHITE ? "White" : "Black";
        if (gameOver && inCheck && noMoves) {
            statusLabel.setText(side + " is checkmated. " + (turn.opposite() == Color.WHITE ? "White" : "Black") + " wins.");
        } else if (gameOver && noMoves) {
            statusLabel.setText("Stalemate. Game over.");
        } else if (inCheck) {
            statusLabel.setText(side + " to move - check!");
        } else {
            statusLabel.setText(side + " to move");
        }
    }
}
