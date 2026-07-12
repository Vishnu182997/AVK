package com.example.chessAI.pieces;

import java.util.ArrayList;
import java.util.List;

import com.example.chessAI.ChessBoard;
import com.example.chessAI.Color;
import com.example.chessAI.Move;
import com.example.chessAI.Piece;
import com.example.chessAI.PieceType;

public class Queen extends Piece {


    public Queen(Color color){
        super(color,PieceType.QUEEN);
    }


    @Override
    public int getValue(){
        return 900;
    }


    @Override
    public List<Move> getLegalMoves(
            ChessBoard board,int row,int col){

        List<Move> moves=new ArrayList<Move>();

        Rook rook=new Rook(color);
        Bishop bishop=new Bishop(color);


        moves.addAll(
            rook.getLegalMoves(board,row,col)
        );

        moves.addAll(
            bishop.getLegalMoves(board,row,col)
        );


        return moves;
    }


    @Override
    public char getFenSymbol(){
        return color==Color.WHITE?'Q':'q';
    }
}