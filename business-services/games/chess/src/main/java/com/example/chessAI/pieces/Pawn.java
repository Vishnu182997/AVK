package com.example.chessAI.pieces;

import java.util.ArrayList;
import java.util.List;

import com.example.chessAI.ChessBoard;
import com.example.chessAI.Color;
import com.example.chessAI.Move;
import com.example.chessAI.Piece;
import com.example.chessAI.PieceType;

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


        int direction =
                color==Color.WHITE ? -1 : 1;


        int next=row+direction;


        if(board.isValidSquare(next,col)
                && board.getPiece(next,col)==null){

            moves.add(new Move(row,col,next,col));
        }


        return moves;
    }


    @Override
    public char getFenSymbol(){
        return color==Color.WHITE?'P':'p';
    }
}