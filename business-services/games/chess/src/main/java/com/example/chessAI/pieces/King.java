package com.example.chessAI.pieces;

import java.util.ArrayList;
import java.util.List;

import com.example.chessAI.ChessBoard;
import com.example.chessAI.Color;
import com.example.chessAI.Move;
import com.example.chessAI.Piece;
import com.example.chessAI.PieceType;

public class King extends Piece {


    public King(Color color){
        super(color,PieceType.KING);
    }


    @Override
    public int getValue(){
        return 20000;
    }


    @Override
    public List<Move> getLegalMoves(
            ChessBoard board,int row,int col){

        List<Move> moves=new ArrayList<Move>();

        for(int r=-1;r<=1;r++){
            for(int c=-1;c<=1;c++){
                if(r==0 && c==0)
                    continue;

                int nr=row+r;
                int nc=col+c;

                if(board.isValidSquare(nr,nc)){
                    Piece p=board.getPiece(nr,nc);
                    if(p==null || p.getColor()!=color)
                        moves.add(new Move(row,col,nr,nc));
                }
            }
        }

        addCastlingMoves(board,row,col,moves);

        return moves;
    }

    private void addCastlingMoves(ChessBoard board, int row, int col,
                                  List<Move> moves) {
        if(hasMoved || col != 4 || board.isKingInCheck(color)) {
            return;
        }

        boolean kingSide = color == Color.WHITE
                ? board.canWhiteCastleKingSide()
                : board.canBlackCastleKingSide();
        boolean queenSide = color == Color.WHITE
                ? board.canWhiteCastleQueenSide()
                : board.canBlackCastleQueenSide();

        if(kingSide && canCastle(board,row,5,6,7)) {
            Move move = new Move(row,col,row,6);
            move.setCastleKingSide(true);
            moves.add(move);
        }

        if(queenSide && canCastle(board,row,3,2,0)
                && board.getPiece(row,1) == null) {
            Move move = new Move(row,col,row,2);
            move.setCastleQueenSide(true);
            moves.add(move);
        }
    }

    private boolean canCastle(ChessBoard board, int row, int throughCol,
                              int destinationCol, int rookCol) {
        Piece rook = board.getPiece(row,rookCol);
        if(rook == null || rook.getColor() != color
                || rook.getType() != PieceType.ROOK || rook.hasMoved()) {
            return false;
        }

        if(board.getPiece(row,throughCol) != null
                || board.getPiece(row,destinationCol) != null) {
            return false;
        }

        Color originalTurn = board.getSideToMove();
        board.setSideToMove(color);

        Move through = new Move(row,4,row,throughCol);
        board.makeMove(through);
        boolean throughSafe = !board.isKingInCheck(color);
        board.undoMove();

        Move destination = new Move(row,4,row,destinationCol);
        board.makeMove(destination);
        boolean destinationSafe = !board.isKingInCheck(color);
        board.undoMove();

        board.setSideToMove(originalTurn);

        return throughSafe && destinationSafe;
    }


    @Override
    public char getFenSymbol(){
        return color==Color.WHITE?'K':'k';
    }
}
