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

        Piece capturedPiece = getPiece(move.getToRow(), move.getToCol());
        boolean pawnMove = piece != null && piece.getType() == PieceType.PAWN;

        if (move.isEnPassant() && piece != null) {
            int capturedPawnRow = move.getFromRow();
            setPiece(capturedPawnRow, move.getToCol(), null);
            capturedPiece = new Pawn(piece.getColor().opposite());
        }

        setPiece(move.getToRow(), move.getToCol(), piece);
        setPiece(move.getFromRow(), move.getFromCol(), null);

        if (piece != null) {
            piece.setMoved(true);
        }

        if (move.isCastleKingSide()) {
            moveRook(move.getToRow(), 7, move.getToRow(), 5);
        } else if (move.isCastleQueenSide()) {
            moveRook(move.getToRow(), 0, move.getToRow(), 3);
        }

        if (move.isPromotion() && piece != null) {
            PieceType promotionType = move.getPromotionPiece() == null
                    ? PieceType.QUEEN
                    : move.getPromotionPiece();
            setPiece(move.getToRow(), move.getToCol(), createPiece(promotionType, piece.getColor()));
        }

        updateCastlingRights(move, piece, capturedPiece);
        updateEnPassantSquare(move, piece);

        halfMoveClock = pawnMove || capturedPiece != null
                ? 0
                : halfMoveClock + 1;

        if (sideToMove == Color.BLACK) {
            fullMoveNumber++;
        }

        sideToMove = sideToMove.opposite();
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

    private void moveRook(int fromRow, int fromCol, int toRow, int toCol) {
        Piece rook = getPiece(fromRow, fromCol);
        setPiece(toRow, toCol, rook);
        setPiece(fromRow, fromCol, null);
        if (rook != null) {
            rook.setMoved(true);
        }
    }

    private void updateEnPassantSquare(Move move, Piece piece) {
        enPassantSquare = null;

        if (piece != null
                && piece.getType() == PieceType.PAWN
                && Math.abs(move.getToRow() - move.getFromRow()) == 2) {

            enPassantSquare = new Position(
                    (move.getFromRow() + move.getToRow()) / 2,
                    move.getFromCol());
        }
    }

    private void updateCastlingRights(Move move, Piece piece, Piece capturedPiece) {
        if (piece != null && piece.getType() == PieceType.KING) {
            if (piece.getColor() == Color.WHITE) {
                whiteKingSideCastle = false;
                whiteQueenSideCastle = false;
            } else {
                blackKingSideCastle = false;
                blackQueenSideCastle = false;
            }
        }

        if (piece != null && piece.getType() == PieceType.ROOK) {
            disableRookCastlingRight(move.getFromRow(), move.getFromCol());
        }

        if (capturedPiece != null && capturedPiece.getType() == PieceType.ROOK) {
            disableRookCastlingRight(move.getToRow(), move.getToCol());
        }
    }

    private void disableRookCastlingRight(int row, int col) {
        if (row == 7 && col == 0) {
            whiteQueenSideCastle = false;
        } else if (row == 7 && col == 7) {
            whiteKingSideCastle = false;
        } else if (row == 0 && col == 0) {
            blackQueenSideCastle = false;
        } else if (row == 0 && col == 7) {
            blackKingSideCastle = false;
        }
    }

    private Piece createPiece(PieceType type, Color color) {
        switch (type) {
            case KNIGHT:
                return new Knight(color);
            case BISHOP:
                return new Bishop(color);
            case ROOK:
                return new Rook(color);
            case QUEEN:
                return new Queen(color);
            case KING:
                return new King(color);
            case PAWN:
            default:
                return new Pawn(color);
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

    public void setCastlingRights(boolean whiteKingSideCastle,
                                  boolean whiteQueenSideCastle,
                                  boolean blackKingSideCastle,
                                  boolean blackQueenSideCastle) {
        this.whiteKingSideCastle = whiteKingSideCastle;
        this.whiteQueenSideCastle = whiteQueenSideCastle;
        this.blackKingSideCastle = blackKingSideCastle;
        this.blackQueenSideCastle = blackQueenSideCastle;
    }

    public void setMoveCounters(int halfMoveClock, int fullMoveNumber) {
        this.halfMoveClock = halfMoveClock;
        this.fullMoveNumber = fullMoveNumber;
    }
    
    /**
     * Returns true if the specified king is in check.
     */
    public boolean isKingInCheck(Color color) {

        Position kingPosition = findKing(color);

        if (kingPosition == null) {
            return false;
        }

        return isSquareAttacked(kingPosition.getRow(),
                kingPosition.getCol(),
                color.opposite());
    }

    /**
     * Returns true when a square is attacked by the supplied color.
     */
    public boolean isSquareAttacked(int row, int col, Color attackingColor) {

        int pawnDirection = attackingColor == Color.WHITE ? -1 : 1;
        int pawnRow = row - pawnDirection;
        if (isAttackingPiece(pawnRow, col - 1, attackingColor, PieceType.PAWN)
                || isAttackingPiece(pawnRow, col + 1, attackingColor, PieceType.PAWN)) {
            return true;
        }

        int[][] knightOffsets = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };
        for (int[] offset : knightOffsets) {
            if (isAttackingPiece(row + offset[0], col + offset[1],
                    attackingColor, PieceType.KNIGHT)) {
                return true;
            }
        }

        int[][] diagonalDirections = {
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
        };
        if (isAttackedAlongDirections(row, col, attackingColor,
                diagonalDirections, PieceType.BISHOP, PieceType.QUEEN)) {
            return true;
        }

        int[][] straightDirections = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1}
        };
        if (isAttackedAlongDirections(row, col, attackingColor,
                straightDirections, PieceType.ROOK, PieceType.QUEEN)) {
            return true;
        }

        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for (int colOffset = -1; colOffset <= 1; colOffset++) {
                if (rowOffset == 0 && colOffset == 0) {
                    continue;
                }

                if (isAttackingPiece(row + rowOffset, col + colOffset,
                        attackingColor, PieceType.KING)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isAttackingPiece(int row, int col, Color attackingColor,
                                     PieceType type) {
        Piece piece = getPiece(row, col);
        return piece != null
                && piece.getColor() == attackingColor
                && piece.getType() == type;
    }

    private boolean isAttackedAlongDirections(int row, int col,
                                              Color attackingColor,
                                              int[][] directions,
                                              PieceType firstType,
                                              PieceType secondType) {
        for (int[] direction : directions) {
            int currentRow = row + direction[0];
            int currentCol = col + direction[1];

            while (isValidSquare(currentRow, currentCol)) {
                Piece piece = getPiece(currentRow, currentCol);
                if (piece != null) {
                    if (piece.getColor() == attackingColor
                            && (piece.getType() == firstType
                            || piece.getType() == secondType)) {
                        return true;
                    }
                    break;
                }

                currentRow += direction[0];
                currentCol += direction[1];
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