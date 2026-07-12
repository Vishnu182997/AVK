package com.example.chessAI;

import com.example.chessAI.gui.BoardPanel;
import com.example.chessAI.pieces.Bishop;
import com.example.chessAI.pieces.King;
import com.example.chessAI.pieces.Knight;
import com.example.chessAI.pieces.Pawn;
import com.example.chessAI.pieces.Rook;

public final class ChessRulesTest {

    private ChessRulesTest() {
    }

    public static void main(String[] args) {
        testInitialPlacement();
        testPieceMovementAndBlockedPaths();
        testCapturesAndTurns();
        testKingSafetyAndCheck();
        testBoardPanelUsesInitializedModel();
    }

    private static void testInitialPlacement() {
        ChessBoard board = new ChessBoard();

        assertPiece(board, 7, 0, Color.WHITE, PieceType.ROOK);
        assertPiece(board, 7, 1, Color.WHITE, PieceType.KNIGHT);
        assertPiece(board, 7, 2, Color.WHITE, PieceType.BISHOP);
        assertPiece(board, 7, 3, Color.WHITE, PieceType.QUEEN);
        assertPiece(board, 7, 4, Color.WHITE, PieceType.KING);
        assertPiece(board, 7, 5, Color.WHITE, PieceType.BISHOP);
        assertPiece(board, 7, 6, Color.WHITE, PieceType.KNIGHT);
        assertPiece(board, 7, 7, Color.WHITE, PieceType.ROOK);

        assertPiece(board, 0, 0, Color.BLACK, PieceType.ROOK);
        assertPiece(board, 0, 1, Color.BLACK, PieceType.KNIGHT);
        assertPiece(board, 0, 2, Color.BLACK, PieceType.BISHOP);
        assertPiece(board, 0, 3, Color.BLACK, PieceType.QUEEN);
        assertPiece(board, 0, 4, Color.BLACK, PieceType.KING);
        assertPiece(board, 0, 5, Color.BLACK, PieceType.BISHOP);
        assertPiece(board, 0, 6, Color.BLACK, PieceType.KNIGHT);
        assertPiece(board, 0, 7, Color.BLACK, PieceType.ROOK);

        for (int col = 0; col < ChessBoard.SIZE; col++) {
            assertPiece(board, 6, col, Color.WHITE, PieceType.PAWN);
            assertPiece(board, 1, col, Color.BLACK, PieceType.PAWN);
        }
    }

    private static void testPieceMovementAndBlockedPaths() {
        MoveValidator validator = new MoveValidator();
        ChessBoard board = new ChessBoard();

        assertTrue(validator.isValidMove(board, new Move(6, 4, 4, 4)),
                "White pawn should move two squares from its starting rank.");
        assertFalse(validator.isValidMove(board, new Move(7, 2, 5, 4)),
                "Bishop should not move through its own pawn.");
        assertTrue(validator.isValidMove(board, new Move(7, 6, 5, 5)),
                "Knight should jump over occupied squares.");

        ChessBoard emptyBoard = emptyBoard(Color.WHITE);
        emptyBoard.setPiece(7, 4, new King(Color.WHITE));
        emptyBoard.setPiece(0, 4, new King(Color.BLACK));
        emptyBoard.setPiece(4, 4, new Rook(Color.WHITE));
        emptyBoard.setPiece(4, 5, new Knight(Color.WHITE));
        assertFalse(validator.isValidMove(emptyBoard, new Move(4, 4, 4, 7)),
                "Rook should not move through another piece.");

        emptyBoard.setPiece(4, 5, null);
        assertTrue(validator.isValidMove(emptyBoard, new Move(4, 4, 4, 7)),
                "Rook should move horizontally on an open rank.");

        emptyBoard.setPiece(3, 3, new Bishop(Color.WHITE));
        assertTrue(validator.isValidMove(emptyBoard, new Move(3, 3, 1, 1)),
                "Bishop should move diagonally on an open diagonal.");
    }

    private static void testCapturesAndTurns() {
        MoveValidator validator = new MoveValidator();
        ChessBoard board = new ChessBoard();

        Move whiteDouble = matchingMove(board, new Move(6, 4, 4, 4));
        board.makeMove(whiteDouble);
        assertEquals(Color.BLACK, board.getSideToMove(), "Black should move after White.");

        Move blackDouble = matchingMove(board, new Move(1, 3, 3, 3));
        board.makeMove(blackDouble);
        assertEquals(Color.WHITE, board.getSideToMove(), "White should move after Black.");

        assertTrue(validator.isValidMove(board, new Move(4, 4, 3, 3)),
                "Pawn should capture diagonally.");
        Move capture = matchingMove(board, new Move(4, 4, 3, 3));
        board.makeMove(capture);
        assertPiece(board, 3, 3, Color.WHITE, PieceType.PAWN);
        assertEquals(null, board.getPiece(4, 4), "Source square should be empty after capture.");
    }

    private static void testKingSafetyAndCheck() {
        MoveValidator validator = new MoveValidator();
        ChessBoard board = emptyBoard(Color.WHITE);

        board.setPiece(7, 4, new King(Color.WHITE));
        board.setPiece(0, 0, new King(Color.BLACK));
        board.setPiece(0, 4, new Rook(Color.BLACK));
        board.setPiece(6, 4, new Rook(Color.WHITE));

        assertFalse(validator.isValidMove(board, new Move(6, 4, 6, 5)),
                "Pinned rook should not expose the king to check.");
        assertFalse(board.isKingInCheck(Color.WHITE),
                "Blocking rook should prevent check on the white king.");

        board.setPiece(6, 4, null);
        assertTrue(board.isKingInCheck(Color.WHITE),
                "Open file rook attack should check the king.");
    }

    private static void testBoardPanelUsesInitializedModel() {
        BoardPanel panel = new BoardPanel();
        ChessBoard board = panel.getBoard();

        assertPiece(board, 7, 4, Color.WHITE, PieceType.KING);
        assertPiece(board, 0, 4, Color.BLACK, PieceType.KING);
        assertPiece(board, 6, 0, Color.WHITE, PieceType.PAWN);
        assertPiece(board, 1, 0, Color.BLACK, PieceType.PAWN);
    }

    private static ChessBoard emptyBoard(Color sideToMove) {
        ChessBoard board = new ChessBoard();
        board.clear();
        board.setSideToMove(sideToMove);
        board.setCastlingRights(false, false, false, false);
        return board;
    }

    private static Move matchingMove(ChessBoard board, Move requestedMove) {
        for (Move legalMove : new MoveGenerator().generateLegalMoves(board, board.getSideToMove())) {
            if (legalMove.equals(requestedMove)) {
                return legalMove;
            }
        }

        throw new AssertionError("Expected legal move: " + requestedMove);
    }

    private static void assertPiece(ChessBoard board, int row, int col,
                                    Color color, PieceType type) {
        Piece piece = board.getPiece(row, col);
        assertTrue(piece != null, "Expected piece at " + row + "," + col);
        assertEquals(color, piece.getColor(), "Unexpected piece color.");
        assertEquals(type, piece.getType(), "Unexpected piece type.");
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void assertFalse(boolean condition, String message) {
        assertTrue(!condition, message);
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(message + " Expected: " + expected + " actual: " + actual);
        }
    }
}
