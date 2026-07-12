package com.example.chessAI;

import java.util.Stack;

import com.example.chessAI.pieces.Bishop;
import com.example.chessAI.pieces.King;
import com.example.chessAI.pieces.Knight;
import com.example.chessAI.pieces.Pawn;
import com.example.chessAI.pieces.Queen;
import com.example.chessAI.pieces.Rook;

public class ChessBoard {

    public static final int SIZE = 8;

    private Piece[][] board;

    private Color sideToMove;

    // Castling rights
    private boolean whiteKingSideCastle = true;
    private boolean whiteQueenSideCastle = true;
    private boolean blackKingSideCastle = true;
    private boolean blackQueenSideCastle = true;

    // En-passant target square
    private Position enPassantSquare;

    // Halfmove/fullmove counters (FEN compatible)
    private int halfMoveClock;
    private int fullMoveNumber;

    // Move history for undo
    private Stack<BoardState> history;

    public ChessBoard() {

        board = new Piece[SIZE][SIZE];
        history = new Stack<BoardState>();

        initializeBoard();
    }

    /**
     * Places all pieces in the starting position.
     */
    public final void initializeBoard() {

        clear();

        sideToMove = Color.WHITE;

        whiteKingSideCastle = true;
        whiteQueenSideCastle = true;
        blackKingSideCastle = true;
        blackQueenSideCastle = true;

        enPassantSquare = null;

        halfMoveClock = 0;
        fullMoveNumber = 1;

        // White pieces
        board[7][0] = new Rook(Color.WHITE);
        board[7][1] = new Knight(Color.WHITE);
        board[7][2] = new Bishop(Color.WHITE);
        board[7][3] = new Queen(Color.WHITE);
        board[7][4] = new King(Color.WHITE);
        board[7][5] = new Bishop(Color.WHITE);
        board[7][6] = new Knight(Color.WHITE);
        board[7][7] = new Rook(Color.WHITE);

        for (int c = 0; c < SIZE; c++) {
            board[6][c] = new Pawn(Color.WHITE);
        }

        // Black pieces
        board[0][0] = new Rook(Color.BLACK);
        board[0][1] = new Knight(Color.BLACK);
        board[0][2] = new Bishop(Color.BLACK);
        board[0][3] = new Queen(Color.BLACK);
        board[0][4] = new King(Color.BLACK);
        board[0][5] = new Bishop(Color.BLACK);
        board[0][6] = new Knight(Color.BLACK);
        board[0][7] = new Rook(Color.BLACK);

        for (int c = 0; c < SIZE; c++) {
            board[1][c] = new Pawn(Color.BLACK);
        }
    }

    /**
     * Clears the board.
     */
    public void clear() {

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                board[r][c] = null;
            }
        }
    }

    /**
     * Returns the piece at a square.
     */
    public Piece getPiece(int row, int col) {

        if (!isValidSquare(row, col)) {
            return null;
        }

        return board[row][col];
    }

    /**
     * Sets a piece on the board.
     */
    public void setPiece(int row, int col, Piece piece) {

        if (isValidSquare(row, col)) {
            board[row][col] = piece;
        }
    }

    /**
     * Executes a move.
     */
    public void makeMove(Move move) {

        history.push(new BoardState(this));

        Piece piece = getPiece(move.getFromRow(), move.getFromCol());

        setPiece(move.getToRow(), move.getToCol(), piece);
        setPiece(move.getFromRow(), move.getFromCol(), null);

        sideToMove = (sideToMove == Color.WHITE)
                ? Color.BLACK
                : Color.WHITE;
    }

    /**
     * Restores the previous position.
     */
    public void undoMove() {

        if (!history.isEmpty()) {

            BoardState state = history.pop();
            state.restore(this);
        }
    }

    /**
     * Checks board bounds.
     */
    public boolean isValidSquare(int row, int col) {

        return row >= 0 && row < SIZE &&
               col >= 0 && col < SIZE;
    }

    public Color getSideToMove() {
        return sideToMove;
    }

    public void setSideToMove(Color sideToMove) {
        this.sideToMove = sideToMove;
    }

    public Piece[][] getBoard() {
        return board;
    }

    public boolean canWhiteCastleKingSide() {
        return whiteKingSideCastle;
    }

    public boolean canWhiteCastleQueenSide() {
        return whiteQueenSideCastle;
    }

    public boolean canBlackCastleKingSide() {
        return blackKingSideCastle;
    }

    public boolean canBlackCastleQueenSide() {
        return blackQueenSideCastle;
    }

    public Position getEnPassantSquare() {
        return enPassantSquare;
    }

    public void setEnPassantSquare(Position position) {
        enPassantSquare = position;
    }

    public int getHalfMoveClock() {
        return halfMoveClock;
    }

    public int getFullMoveNumber() {
        return fullMoveNumber;
    }
    
    /**
     * Returns true if the specified king is in check.
     */
    public boolean isKingInCheck(Color color) {

        Position kingPosition = findKing(color);

        if (kingPosition == null) {
            return false;
        }

        Color opponent =
                (color == Color.WHITE)
                        ? Color.BLACK
                        : Color.WHITE;

        MoveGenerator generator = new MoveGenerator();

        java.util.List<Move> moves =
                generator.generateLegalMoves(this, opponent);

        for (Move move : moves) {

            if (move.getToRow() == kingPosition.getRow()
                    && move.getToCol() == kingPosition.getCol()) {

                return true;
            }
        }

        return false;
    }
    
    /**
     * Finds the king position.
     */
    private Position findKing(Color color) {

        for (int row = 0; row < 8; row++) {

            for (int col = 0; col < 8; col++) {

                Piece piece = board[row][col];

                if (piece != null
                        && piece.getColor() == color
                        && piece.getType() == PieceType.KING) {

                    return new Position(row, col);
                }
            }
        }

        return null;
    }
}