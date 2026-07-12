package com.example.chessAI;

import java.util.List;

/**
 * Validates chess moves.
 */
public class MoveValidator {

    private MoveGenerator moveGenerator;

    public MoveValidator() {
        moveGenerator = new MoveGenerator();
    }

    /**
     * Checks whether a move is legal.
     */
    public boolean isValidMove(ChessBoard board, Move move) {

        if (board == null || move == null) {
            return false;
        }

        // Check coordinates
        if (!insideBoard(move.getFromRow(), move.getFromCol())) {
            return false;
        }

        if (!insideBoard(move.getToRow(), move.getToCol())) {
            return false;
        }

        Piece piece = board.getPiece(
                move.getFromRow(),
                move.getFromCol());

        if (piece == null) {
            return false;
        }

        // Correct side to move?
        if (piece.getColor() != board.getSideToMove()) {
            return false;
        }

        Piece destination =
                board.getPiece(
                        move.getToRow(),
                        move.getToCol());

        // Cannot capture own piece
        if (destination != null &&
                destination.getColor() == piece.getColor()) {

            return false;
        }

        return getLegalMove(board, move) != null;
    }

    /**
     * Returns the generated legal move matching the requested coordinates,
     * preserving special-move flags such as castling, en passant, and promotion.
     */
    public Move getLegalMove(ChessBoard board, Move move) {

        if (board == null || move == null) {
            return null;
        }

        if (!insideBoard(move.getFromRow(), move.getFromCol())
                || !insideBoard(move.getToRow(), move.getToCol())) {
            return null;
        }

        Piece piece = board.getPiece(move.getFromRow(), move.getFromCol());

        if (piece == null || piece.getColor() != board.getSideToMove()) {
            return null;
        }

        Piece destination = board.getPiece(move.getToRow(), move.getToCol());

        if (destination != null && destination.getColor() == piece.getColor()) {
            return null;
        }

        List<Move> legalMoves = moveGenerator.generateLegalMoves(board, piece.getColor());

        for (Move legal : legalMoves) {
            if (sameMove(legal, move)) {
                if (move.getPromotionPiece() != null) {
                    legal.setPromotionPiece(move.getPromotionPiece());
                }

                return legal;
            }
        }

        return null;
    }

    /**
     * Returns true if coordinates are inside the board.
     */
    private boolean insideBoard(int row, int col) {

        return row >= 0 &&
               row < 8 &&
               col >= 0 &&
               col < 8;
    }

    /**
     * Compares two moves.
     */
    private boolean sameMove(
            Move a,
            Move b) {

        return a.getFromRow() == b.getFromRow()
                &&
                a.getFromCol() == b.getFromCol()
                &&
                a.getToRow() == b.getToRow()
                &&
                a.getToCol() == b.getToCol();
    }

    /**
     * Checks whether the current player is in check.
     */
    public boolean isCheck(
            ChessBoard board,
            Color color) {

        return board.isKingInCheck(color);
    }

    /**
     * Checks whether current player is checkmated.
     */
    public boolean isCheckmate(
            ChessBoard board,
            Color color) {

        if (!isCheck(board, color)) {
            return false;
        }

        List<Move> moves =
                moveGenerator.generateLegalMoves(
                        board,
                        color);

        return moves.isEmpty();
    }

    /**
     * Checks whether the game is stalemated.
     */
    public boolean isStalemate(
            ChessBoard board,
            Color color) {

        if (isCheck(board, color)) {
            return false;
        }

        List<Move> moves =
                moveGenerator.generateLegalMoves(
                        board,
                        color);

        return moves.isEmpty();
    }
}