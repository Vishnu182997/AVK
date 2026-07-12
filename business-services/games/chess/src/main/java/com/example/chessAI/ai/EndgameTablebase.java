package com.example.chessAI.ai;


import java.util.List;

import com.example.chessAI.ChessBoard;
import com.example.chessAI.Color;
import com.example.chessAI.Move;
import com.example.chessAI.MoveGenerator;
import com.example.chessAI.Piece;
import com.example.chessAI.PieceType;


/**
 * Simple endgame tablebase.
 *
 * Used when only a few pieces remain.
 */
public class EndgameTablebase {


    private MoveGenerator moveGenerator;



    public EndgameTablebase() {

        moveGenerator =
                new MoveGenerator();
    }





    /**
     * Checks whether this position
     * can be handled by tablebase.
     */
    public boolean supports(
            ChessBoard board) {


        int pieces =
                countPieces(board);



        /*
         * Basic tablebase support
         * up to 5 pieces.
         */
        return pieces <= 5;
    }






    /**
     * Returns best endgame move.
     *
     * Returns null if not supported.
     */
    public Move getBestMove(
            ChessBoard board,
            Color side) {


        if(!supports(board)) {

            return null;
        }



        /*
         * King + Queen vs King
         */
        Move move =
                findQueenMateMove(
                        board,
                        side
                );


        if(move != null) {

            return move;
        }



        /*
         * King + Rook vs King
         */
        move =
                findRookMove(
                        board,
                        side
                );



        return move;
    }






    /**
     * Detects queen endgame moves.
     */
    private Move findQueenMateMove(
            ChessBoard board,
            Color color) {



        Piece queen =
                findPiece(
                        board,
                        color,
                        PieceType.QUEEN
                );



        if(queen == null) {

            return null;
        }



        List<Move> moves =
                moveGenerator
                .generateLegalMoves(
                        board,
                        color
                );



        for(Move move : moves) {


            if(move.getMovedPiece()
                    != null
                    &&
               move.getMovedPiece()
                    .getType()
                    ==
               PieceType.QUEEN) {


                return move;
            }
        }


        return null;
    }







    /**
     * Detects rook endgame moves.
     */
    private Move findRookMove(
            ChessBoard board,
            Color color) {


        Piece rook =
                findPiece(
                        board,
                        color,
                        PieceType.ROOK
                );



        if(rook == null) {

            return null;
        }



        List<Move> moves =
                moveGenerator
                .generateLegalMoves(
                        board,
                        color
                );



        for(Move move : moves) {


            if(move.getMovedPiece()
                    != null
                    &&
               move.getMovedPiece()
                    .getType()
                    ==
               PieceType.ROOK) {


                return move;
            }
        }


        return null;
    }







    /**
     * Counts pieces on board.
     */
    private int countPieces(
            ChessBoard board) {


        int count = 0;


        Piece[][] pieces =
                board.getBoard();



        for(int r=0;r<8;r++) {


            for(int c=0;c<8;c++) {


                if(pieces[r][c]!=null) {

                    count++;
                }
            }
        }


        return count;
    }








    /**
     * Finds a specific piece.
     */
    private Piece findPiece(
            ChessBoard board,
            Color color,
            PieceType type) {



        Piece[][] pieces =
                board.getBoard();



        for(int r=0;r<8;r++) {


            for(int c=0;c<8;c++) {


                Piece p =
                        pieces[r][c];


                if(p!=null
                        &&
                   p.getColor()==color
                        &&
                   p.getType()==type) {


                    return p;
                }
            }
        }


        return null;
    }
}