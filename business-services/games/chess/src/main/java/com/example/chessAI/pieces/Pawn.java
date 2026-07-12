package com.example.chessAI.pieces;

import java.util.ArrayList;
import java.util.List;

import com.example.chessAI.ChessBoard;
import com.example.chessAI.Color;
import com.example.chessAI.Move;
import com.example.chessAI.Piece;
import com.example.chessAI.PieceType;
import com.example.chessAI.Position;

public class Pawn extends Piece {


    public Pawn(Color color){
        super(color,PieceType.PAWN);
    }


    @Override
    public int getValue(){
        return 100;
    }


    @Override
    public List<Move> getLegalMoves(
            ChessBoard board,int row,int col){

        List<Move> moves=new ArrayList<Move>();
        int direction = color==Color.WHITE ? -1 : 1;
        int next=row+direction;

        if(board.isValidSquare(next,col)
                && board.getPiece(next,col)==null){
            addPawnMove(row,col,next,col,moves);

            int startRow = color==Color.WHITE ? 6 : 1;
            int doubleRow = row + (2 * direction);
            if(row == startRow
                    && board.isValidSquare(doubleRow,col)
                    && board.getPiece(doubleRow,col)==null){
                moves.add(new Move(row,col,doubleRow,col));
            }
        }

        addCapture(board,row,col,next,col-1,moves);
        addCapture(board,row,col,next,col+1,moves);

        return moves;
    }

    private void addCapture(ChessBoard board, int row, int col,
                            int targetRow, int targetCol, List<Move> moves) {
        if(!board.isValidSquare(targetRow,targetCol)) {
            return;
        }

        Piece target=board.getPiece(targetRow,targetCol);
        if(target!=null && target.getColor()!=color) {
            addPawnMove(row,col,targetRow,targetCol,moves);
            return;
        }

        Position enPassantSquare = board.getEnPassantSquare();
        if(enPassantSquare != null
                && enPassantSquare.getRow() == targetRow
                && enPassantSquare.getCol() == targetCol) {
            Move move = new Move(row,col,targetRow,targetCol);
            move.setEnPassant(true);
            moves.add(move);
        }
    }

    private void addPawnMove(int fromRow, int fromCol, int toRow, int toCol,
                             List<Move> moves) {
        Move move = new Move(fromRow,fromCol,toRow,toCol);
        if(toRow == 0 || toRow == 7) {
            move.setPromotion(true);
            move.setPromotionPiece(PieceType.QUEEN);
        }
        moves.add(move);
    }


    @Override
    public char getFenSymbol(){
        return color==Color.WHITE?'P':'p';
    }
}
