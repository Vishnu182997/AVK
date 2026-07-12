package com.example.chessAI.ai;


import java.util.Random;

import com.example.chessAI.ChessBoard;
import com.example.chessAI.Color;
import com.example.chessAI.Piece;
import com.example.chessAI.PieceType;


/**
 * Generates unique hashes for chess positions.
 */
public class ZobristHash {


    private static final long[][][] PIECE_KEYS =
            new long[2][6][64];


    private static final long SIDE_KEY;


    private static final long[] CASTLE_KEYS =
            new long[4];


    private static final long[] EN_PASSANT_KEYS =
            new long[64];


    static {


        Random random =
                new Random(123456789);



        // Piece keys
        for(int color = 0; color < 2; color++) {


            for(int piece = 0; piece < 6; piece++) {


                for(int square = 0; square < 64; square++) {


                    PIECE_KEYS[color][piece][square]
                            = random.nextLong();
                }
            }
        }



        // Side to move
        SIDE_KEY =
                random.nextLong();



        // Castling rights
        for(int i = 0; i < 4; i++) {

            CASTLE_KEYS[i]
                    = random.nextLong();
        }



        // En passant
        for(int i = 0; i < 64; i++) {

            EN_PASSANT_KEYS[i]
                    = random.nextLong();
        }

    }




    /**
     * Generates hash for current board.
     */
    public static long generateHash(
            ChessBoard board) {


        long hash = 0;



        Piece[][] pieces =
                board.getBoard();



        for(int row = 0;
            row < ChessBoard.SIZE;
            row++) {


            for(int col = 0;
                col < ChessBoard.SIZE;
                col++) {


                Piece piece =
                        pieces[row][col];



                if(piece == null) {
                    continue;
                }



                int colorIndex =
                        piece.getColor()
                        == Color.WHITE
                        ? 0
                        : 1;



                int pieceIndex =
                        getPieceIndex(
                                piece.getType()
                        );



                int square =
                        row * 8 + col;



                hash ^=
                    PIECE_KEYS
                    [colorIndex]
                    [pieceIndex]
                    [square];

            }
        }



        // Side to move
        if(board.getSideToMove()
                == Color.BLACK) {


            hash ^= SIDE_KEY;
        }



        /*
         * Castling and en-passant
         * can be added when the ChessBoard
         * getter methods are completed.
         */


        return hash;
    }





    /**
     * Converts PieceType to array index.
     */
    private static int getPieceIndex(
            PieceType type) {


        switch(type) {


            case PAWN:
                return 0;


            case KNIGHT:
                return 1;


            case BISHOP:
                return 2;


            case ROOK:
                return 3;


            case QUEEN:
                return 4;


            case KING:
                return 5;


            default:
                return 0;
        }
    }
}