package com.example.chessAI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.chessAI.pieces.Bishop;
import com.example.chessAI.pieces.King;
import com.example.chessAI.pieces.Knight;
import com.example.chessAI.pieces.Pawn;
import com.example.chessAI.pieces.Queen;
import com.example.chessAI.pieces.Rook;

class ChessBoardTest {

    private final MoveValidator validator = new MoveValidator();
    private final MoveGenerator generator = new MoveGenerator();

    @Test
    void initialPositionContainsAllPieces() {
        ChessBoard board = new ChessBoard();
        assertPiece(board, 7, 0, Color.WHITE, PieceType.ROOK);
        assertPiece(board, 7, 1, Color.WHITE, PieceType.KNIGHT);
        assertPiece(board, 7, 2, Color.WHITE, PieceType.BISHOP);
        assertPiece(board, 7, 3, Color.WHITE, PieceType.QUEEN);
        assertPiece(board, 7, 4, Color.WHITE, PieceType.KING);
        assertPiece(board, 0, 3, Color.BLACK, PieceType.QUEEN);
        assertPiece(board, 0, 4, Color.BLACK, PieceType.KING);
        for (int col = 0; col < ChessBoard.SIZE; col++) {
            assertPiece(board, 6, col, Color.WHITE, PieceType.PAWN);
            assertPiece(board, 1, col, Color.BLACK, PieceType.PAWN);
        }
        assertEquals(Color.WHITE, board.getSideToMove());
    }

    @Test
    void validatesBasicMovementForEveryPieceType() {
        ChessBoard board = emptyKingsBoard();
        board.setPiece(6, 4, new Pawn(Color.WHITE));
        board.setPiece(4, 0, new Rook(Color.WHITE));
        board.setPiece(4, 1, new Knight(Color.WHITE));
        board.setPiece(4, 2, new Bishop(Color.WHITE));
        board.setPiece(4, 3, new Queen(Color.WHITE));
        assertTrue(validator.isValidMove(board, new Move(6, 4, 4, 4)));
        assertTrue(validator.isValidMove(board, new Move(4, 0, 4, 7)));
        assertTrue(validator.isValidMove(board, new Move(4, 1, 2, 2)));
        assertTrue(validator.isValidMove(board, new Move(4, 2, 1, 5)));
        assertTrue(validator.isValidMove(board, new Move(4, 3, 1, 3)));
        assertTrue(validator.isValidMove(board, new Move(7, 4, 6, 3)));
    }

    @Test
    void rejectsIllegalAndBlockedMoves() {
        ChessBoard board = new ChessBoard();
        assertFalse(validator.isValidMove(board, new Move(7, 0, 5, 0)));
        assertFalse(validator.isValidMove(board, new Move(7, 2, 5, 2)));
        assertFalse(validator.isValidMove(board, new Move(6, 0, 5, 1)));
    }

    @Test
    void capturesAndAlternatesTurns() {
        ChessBoard board = emptyKingsBoard();
        board.setPiece(4, 4, new Rook(Color.WHITE));
        board.setPiece(4, 7, new Pawn(Color.BLACK));
        Move capture = new Move(4, 4, 4, 7);
        assertTrue(validator.isValidMove(board, capture));
        board.makeMove(capture);
        assertNull(board.getPiece(4, 4));
        assertPiece(board, 4, 7, Color.WHITE, PieceType.ROOK);
        assertEquals(Color.BLACK, board.getSideToMove());
        assertFalse(validator.isValidMove(board, new Move(4, 7, 4, 6)));
    }

    @Test
    void rejectsMovesThatLeaveKingInCheckAndDetectsCheck() {
        ChessBoard board = emptyKingsBoard();
        board.setPiece(5, 4, new Rook(Color.WHITE));
        board.setPiece(0, 4, new Rook(Color.BLACK));
        assertFalse(board.isKingInCheck(Color.WHITE));
        assertFalse(validator.isValidMove(board, new Move(5, 4, 5, 5)));
        assertTrue(validator.isValidMove(board, new Move(5, 4, 0, 4)));
        board.makeMove(new Move(5, 4, 0, 4));
        assertTrue(board.isKingInCheck(Color.BLACK));
    }

    @Test
    void detectsCheckmate() {
        ChessBoard board = new ChessBoard();
        assertTrue(validator.isValidMove(board, new Move(6, 5, 5, 5)));
        board.makeMove(new Move(6, 5, 5, 5));
        board.makeMove(new Move(1, 4, 3, 4));
        board.makeMove(new Move(6, 6, 4, 6));
        board.makeMove(new Move(0, 3, 4, 7));
        assertTrue(board.isKingInCheck(Color.WHITE));
        List<Move> whiteMoves = generator.generateLegalMoves(board, Color.WHITE);
        assertTrue(whiteMoves.isEmpty());
    }

    private ChessBoard emptyKingsBoard() {
        ChessBoard board = new ChessBoard();
        board.clear();
        board.setCastlingRights(false, false, false, false);
        board.setEnPassantSquare(null);
        board.setSideToMove(Color.WHITE);
        board.setPiece(7, 4, new King(Color.WHITE));
        board.setPiece(0, 7, new King(Color.BLACK));
        return board;
    }

    private void assertPiece(ChessBoard board, int row, int col, Color color, PieceType type) {
        Piece piece = board.getPiece(row, col);
        assertNotNull(piece);
        assertEquals(color, piece.getColor());
        assertEquals(type, piece.getType());
    }
}
