package com.example.chessAI.pieces;

import java.util.ArrayList;
import java.util.List;

import com.example.chessAI.ChessBoard;
import com.example.chessAI.Color;
import com.example.chessAI.Move;
import com.example.chessAI.Piece;
import com.example.chessAI.PieceType;

public class Knight extends Piece {


    public Knight(Color color){
        super(color,PieceType.KNIGHT);
    }


    @Override
    public int getValue(){
        return 320;
    }


    @Override
    public List<Move> getLegalMoves(
            ChessBoard board,int row,int col){


        List<Move> moves=new ArrayList<Move>();

        int[][] jumps={
                {2,1},{2,-1},
                {-2,1},{-2,-1},
                {1,2},{1,-2},
                {-1,2},{-1,-2}
        };


        for(int[] j:jumps){

            int r=row+j[0];
            int c=col+j[1];


            if(board.isValidSquare(r,c)){

                Piece p=board.getPiece(r,c);

                if(p==null || p.getColor()!=color)
                    moves.add(new Move(row,col,r,c));
            }
        }

        return moves;
    }


    @Override
    public char getFenSymbol(){
        return color==Color.WHITE?'N':'n';
    }
}