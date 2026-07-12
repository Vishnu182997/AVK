package com.example.chessAI.pieces;


import java.util.ArrayList;
import java.util.List;

import com.example.chessAI.ChessBoard;
import com.example.chessAI.Color;
import com.example.chessAI.Move;
import com.example.chessAI.Piece;
import com.example.chessAI.PieceType;

public class Bishop extends Piece {

    public Bishop(Color color) {
        super(color, PieceType.BISHOP);
    }


    @Override
    public int getValue() {
        return 330;
    }


    @Override
    public List<Move> getLegalMoves(
            ChessBoard board, int row, int col) {


        List<Move> moves = new ArrayList<Move>();

        addDiagonal(board,row,col,1,1,moves);
        addDiagonal(board,row,col,1,-1,moves);
        addDiagonal(board,row,col,-1,1,moves);
        addDiagonal(board,row,col,-1,-1,moves);


        return moves;
    }


    private void addDiagonal(
            ChessBoard board,
            int row,
            int col,
            int rDir,
            int cDir,
            List<Move> moves) {


        int r=row+rDir;
        int c=col+cDir;


        while(board.isValidSquare(r,c)) {


            Piece target=board.getPiece(r,c);


            if(target==null){

                moves.add(new Move(row,col,r,c));

            }else{

                if(target.getColor()!=color)
                    moves.add(new Move(row,col,r,c));

                break;
            }

            r+=rDir;
            c+=cDir;
        }
    }


    @Override
    public char getFenSymbol() {
        return color==Color.WHITE?'B':'b';
    }
}