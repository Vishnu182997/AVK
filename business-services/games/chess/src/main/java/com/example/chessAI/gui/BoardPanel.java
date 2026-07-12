package com.example.chessAI.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import com.example.chessAI.ChessBoard;
import com.example.chessAI.Color;
import com.example.chessAI.Move;
import com.example.chessAI.MoveGenerator;
import com.example.chessAI.MoveValidator;
import com.example.chessAI.Piece;
import com.example.chessAI.Position;

public class BoardPanel extends JPanel {

    public static final int BOARD_SIZE = 8;

    private final SquarePanel[][] squares;
    private final MoveValidator moveValidator;
    private final MoveGenerator moveGenerator;

    private ChessBoard board;
    private Position selectedSquare;
    private List<Move> selectedLegalMoves;
    private boolean gameOver;
    private StatusListener statusListener;

    public BoardPanel() {

        squares = new SquarePanel[BOARD_SIZE][BOARD_SIZE];
        moveValidator = new MoveValidator();
        moveGenerator = new MoveGenerator();
        selectedLegalMoves = new ArrayList<Move>();
        board = new ChessBoard();

        PieceImages.loadImages();
        initializeBoard();
        updateBoard();
        updateStatus();
    }

    private void initializeBoard() {

        setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        setPreferredSize(new Dimension(640, 640));

        removeAll();

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                boolean lightSquare = (row + col) % 2 == 0;
                SquarePanel square = new SquarePanel(row, col, lightSquare);
                final int squareRow = row;
                final int squareCol = col;

                square.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent event) {
                        handleSquareClick(squareRow, squareCol);
                    }
                });

                squares[row][col] = square;
                add(square);
            }
        }
    }

    public ChessBoard getBoard() {
        return board;
    }

    public void setStatusListener(StatusListener statusListener) {
        this.statusListener = statusListener;
        updateStatus();
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
                squares[row][col].setCaptureTarget(false);
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

        board = new ChessBoard();
        selectedSquare = null;
        selectedLegalMoves.clear();
        gameOver = false;
        clearHighlights();
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
                SquarePanel square = squares[row][col];

                if (piece == null) {
                    square.clearPiece();
                } else {
                    square.setPiece(piece, PieceImages.get(piece));
                }
            }
        }

        revalidate();
        repaint();
    }

    private void handleSquareClick(int row, int col) {

        if (gameOver) {
            updateStatus();
            return;
        }

        Move selectedMove = findSelectedMove(row, col);
        if (selectedMove != null) {
            makeSelectedMove(selectedMove);
            return;
        }

        Piece piece = board.getPiece(row, col);
        if (piece != null && piece.getColor() == board.getSideToMove()) {
            selectSquare(row, col);
            return;
        }

        clearSelection();
        updateStatus();
    }

    private void selectSquare(int row, int col) {

        selectedSquare = new Position(row, col);
        selectedLegalMoves = new ArrayList<Move>();

        for (Move move : moveGenerator.generateLegalMoves(board, board.getSideToMove())) {
            if (move.getFromRow() == row && move.getFromCol() == col) {
                selectedLegalMoves.add(move);
            }
        }

        clearHighlights();
        squares[row][col].setSelected(true);

        for (Move move : selectedLegalMoves) {
            SquarePanel target = squares[move.getToRow()][move.getToCol()];
            Piece targetPiece = board.getPiece(move.getToRow(), move.getToCol());

            if ((targetPiece != null && targetPiece.getColor() != board.getSideToMove())
                    || move.isEnPassant()) {
                target.setCaptureTarget(true);
            } else {
                target.setHighlighted(true);
            }
        }

        updateStatus();
    }

    private Move findSelectedMove(int row, int col) {

        if (selectedSquare == null) {
            return null;
        }

        for (Move move : selectedLegalMoves) {
            if (move.getToRow() == row && move.getToCol() == col) {
                return move;
            }
        }

        return null;
    }

    private void makeSelectedMove(Move move) {

        Move legalMove = moveValidator.getLegalMove(board, move);
        if (legalMove == null) {
            clearSelection();
            updateStatus();
            return;
        }

        board.makeMove(legalMove);
        clearSelection();
        updateBoard();
        updateGameOverState();
        updateStatus();
    }

    private void clearSelection() {

        selectedSquare = null;
        selectedLegalMoves.clear();
        clearHighlights();
    }

    private void updateGameOverState() {

        Color sideToMove = board.getSideToMove();
        gameOver = !moveGenerator.hasLegalMoves(board, sideToMove);
    }

    private void updateStatus() {

        if (statusListener == null) {
            return;
        }

        Color sideToMove = board.getSideToMove();
        String status;

        if (gameOver) {
            if (moveValidator.isCheck(board, sideToMove)) {
                status = "Checkmate. " + sideToMove.opposite() + " wins.";
            } else {
                status = "Stalemate. Draw.";
            }
        } else if (moveValidator.isCheck(board, sideToMove)) {
            status = sideToMove + " to move - Check";
        } else {
            status = sideToMove + " to move";
        }

        statusListener.statusChanged(status);
    }

    public interface StatusListener {
        void statusChanged(String status);
    }
}
