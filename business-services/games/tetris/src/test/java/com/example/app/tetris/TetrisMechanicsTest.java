package com.example.app.tetris;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;

import org.junit.jupiter.api.Test;

import com.example.app.tetris.pieces.IPiece;
import com.example.app.tetris.pieces.OPiece;
import com.example.app.tetris.pieces.TPiece;

class TetrisMechanicsTest {

    @Test
    void rotationAdvancesAndCanBeReverted() {
        Piece piece = new TPiece(30);

        assertEquals(0, piece.getRotation());
        piece.rotate();
        assertEquals(1, piece.getRotation());
        assertEquals(2, piece.getCurrentShape()[0].length);

        piece.rotateBack();
        assertEquals(0, piece.getRotation());
    }

    @Test
    void squarePieceRotationIsStable() {
        Piece piece = new OPiece(30);

        piece.rotate();

        assertEquals(0, piece.getRotation());
        assertEquals(4, piece.getCells().length);
    }

    @Test
    void boardRejectsOutOfBoundsAndOccupiedPlacements() {
        Board board = new Board(30);
        Piece piece = new IPiece(30);

        piece.setPosition(-1, 0);
        assertFalse(board.canPlace(piece));

        piece.setPosition(Board.COLS - 3, 0);
        assertFalse(board.canPlace(piece));

        piece.setPosition(0, Board.ROWS);
        assertFalse(board.canPlace(piece));

        piece.setPosition(0, Board.ROWS - 1);
        board.lockPiece(piece);

        Piece colliding = new IPiece(30);
        colliding.setPosition(0, Board.ROWS - 1);
        assertFalse(board.canPlace(colliding));
    }

    @Test
    void fullRowsAreClearedAndRowsAboveShiftDown() {
        Board board = new Board(30);
        Color[][] grid = board.getGrid();

        for (int col = 0; col < Board.COLS; col++) {
            grid[Board.ROWS - 1][col] = Color.RED;
        }
        grid[Board.ROWS - 2][0] = Color.BLUE;

        assertEquals(1, board.clearLines());
        assertEquals(Color.BLUE, board.getCellColor(Board.ROWS - 1, 0));
        assertEquals(1, board.getBlockCount());
    }

    @Test
    void lockingIgnoresCellsAboveVisibleBoard() {
        Board board = new Board(30);
        Piece piece = new TPiece(30);
        piece.setPosition(3, -1);

        assertTrue(board.canPlace(piece));
        board.lockPiece(piece);

        assertEquals(3, board.getBlockCount());
    }
}
