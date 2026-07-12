package com.example.chessAI;

public class Move {

    // Source square
    private final int fromRow;
    private final int fromCol;

    // Destination square
    private final int toRow;
    private final int toCol;

    // Piece information
    private Piece movedPiece;
    private Piece capturedPiece;

    // Special moves
    private boolean capture;
    private boolean castleKingSide;
    private boolean castleQueenSide;
    private boolean enPassant;
    private boolean promotion;

    // Promotion piece
    private PieceType promotionPiece;

    /**
     * Normal move constructor.
     */
    public Move(int fromRow, int fromCol,
                int toRow, int toCol) {

        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
    }

    /**
     * Full constructor.
     */
    public Move(int fromRow, int fromCol,
                int toRow, int toCol,
                Piece movedPiece,
                Piece capturedPiece) {

        this(fromRow, fromCol, toRow, toCol);

        this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece;
        this.capture = capturedPiece != null;
    }

    public int getFromRow() {
        return fromRow;
    }

    public int getFromCol() {
        return fromCol;
    }

    public int getToRow() {
        return toRow;
    }

    public int getToCol() {
        return toCol;
    }

    public Piece getMovedPiece() {
        return movedPiece;
    }

    public void setMovedPiece(Piece movedPiece) {
        this.movedPiece = movedPiece;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public void setCapturedPiece(Piece capturedPiece) {
        this.capturedPiece = capturedPiece;
        this.capture = (capturedPiece != null);
    }

    public boolean isCapture() {
        return capture;
    }

    public boolean isPromotion() {
        return promotion;
    }

    public void setPromotion(boolean promotion) {
        this.promotion = promotion;
    }

    public PieceType getPromotionPiece() {
        return promotionPiece;
    }

    public void setPromotionPiece(PieceType promotionPiece) {
        this.promotionPiece = promotionPiece;
    }

    public boolean isCastleKingSide() {
        return castleKingSide;
    }

    public void setCastleKingSide(boolean castleKingSide) {
        this.castleKingSide = castleKingSide;
    }

    public boolean isCastleQueenSide() {
        return castleQueenSide;
    }

    public void setCastleQueenSide(boolean castleQueenSide) {
        this.castleQueenSide = castleQueenSide;
    }

    public boolean isEnPassant() {
        return enPassant;
    }

    public void setEnPassant(boolean enPassant) {
        this.enPassant = enPassant;
    }

    @Override
    public String toString() {

        char fromFile = (char) ('a' + fromCol);
        int fromRank = 8 - fromRow;

        char toFile = (char) ('a' + toCol);
        int toRank = 8 - toRow;

        String move = "" + fromFile + fromRank + toFile + toRank;

        if (promotion) {
            move += "=" + promotionPiece.name().charAt(0);
        }

        return move;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Move)) {
            return false;
        }

        Move other = (Move) obj;

        return fromRow == other.fromRow &&
               fromCol == other.fromCol &&
               toRow == other.toRow &&
               toCol == other.toCol;
    }

    @Override
    public int hashCode() {

        int result = fromRow;
        result = 31 * result + fromCol;
        result = 31 * result + toRow;
        result = 31 * result + toCol;

        return result;
    }
}