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

        return moves;
    }


    @Override
    public char getFenSymbol(){
        return color==Color.WHITE?'K':'k';
    }
}