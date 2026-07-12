package com.example.chessAI;

import com.example.chessAI.pieces.Bishop;
import com.example.chessAI.pieces.King;
import com.example.chessAI.pieces.Knight;
import com.example.chessAI.pieces.Pawn;
import com.example.chessAI.pieces.Queen;
import com.example.chessAI.pieces.Rook;

/**
 * Stores a snapshot of a chess board position.
 * Used for undoing moves during gameplay and AI search.
 */
public class BoardState {

    private Piece[][] board;

    private Color sideToMove;

    private boolean whiteKingSideCastle;
    private boolean whiteQueenSideCastle;
    private boolean blackKingSideCastle;
    private boolean blackQueenSideCastle;

    private Position enPassantSquare;

    private int halfMoveClock;
    private int fullMoveNumber;


    /**
     * Creates a copy of the current board state.
     */
    public BoardState(ChessBoard chessBoard) {

        Piece[][] original = chessBoard.getBoard();

        board = new Piece[ChessBoard.SIZE][ChessBoard.SIZE];


        // Deep copy pieces
        for (int row = 0; row < ChessBoard.SIZE; row++) {

            for (int col = 0; col < ChessBoard.SIZE; col++) {

                Piece piece = original[row][col];

                if (piece != null) {

                    board[row][col] = copyPiece(piece);
                }
            }
        }


        sideToMove = chessBoard.getSideToMove();


        whiteKingSideCastle =
                chessBoard.canWhiteCastleKingSide();

        whiteQueenSideCastle =
                chessBoard.canWhiteCastleQueenSide();

        blackKingSideCastle =
                chessBoard.canBlackCastleKingSide();

        blackQueenSideCastle =
                chessBoard.canBlackCastleQueenSide();


        if (chessBoard.getEnPassantSquare() != null) {

            enPassantSquare =
                    new Position(chessBoard.getEnPassantSquare());
        }


        halfMoveClock =
                chessBoard.getHalfMoveClock();

        fullMoveNumber =
                chessBoard.getFullMoveNumber();
    }


    /**
     * Restores this saved state into the board.
     */
    public void restore(ChessBoard chessBoard) {


        Piece[][] target = chessBoard.getBoard();


        for (int row = 0; row < ChessBoard.SIZE; row++) {

            for (int col = 0; col < ChessBoard.SIZE; col++) {

                target[row][col] = null;

                if (board[row][col] != null) {

                    target[row][col] =
                            copyPiece(board[row][col]);
                }
            }
        }


        chessBoard.setSideToMove(sideToMove);

        chessBoard.setEnPassantSquare(
                enPassantSquare == null
                        ? null
                        : new Position(enPassantSquare)
        );

        chessBoard.setCastlingRights(
                whiteKingSideCastle,
                whiteQueenSideCastle,
                blackKingSideCastle,
                blackQueenSideCastle
        );

        chessBoard.setMoveCounters(halfMoveClock, fullMoveNumber);
    }


    /**
     * Creates a copy of a chess piece.
     */
    private Piece copyPiece(Piece piece) {


        Piece copy = null;


        switch (piece.getType()) {


            case PAWN:
                copy = new Pawn(piece.getColor());
                break;


            case KNIGHT:
                copy = new Knight(piece.getColor());
                break;


            case BISHOP:
                copy = new Bishop(piece.getColor());
                break;


            case ROOK:
                copy = new Rook(piece.getColor());
                break;


            case QUEEN:
                copy = new Queen(piece.getColor());
                break;


            case KING:
                copy = new King(piece.getColor());
                break;
        }


        if (copy != null) {

            copy.setMoved(piece.hasMoved());
        }


        return copy;
    }
}