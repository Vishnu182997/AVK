package com.example.chessAI.utils;


import com.example.chessAI.ChessBoard;
import com.example.chessAI.Color;
import com.example.chessAI.Piece;
import com.example.chessAI.pieces.Bishop;
import com.example.chessAI.pieces.King;
import com.example.chessAI.pieces.Knight;
import com.example.chessAI.pieces.Pawn;
import com.example.chessAI.pieces.Queen;
import com.example.chessAI.pieces.Rook;

/**
 * Handles FEN conversion.
 */
public class FenParser {


    /**
     * Loads a FEN string into ChessBoard.
     */
    public static void loadFEN(
            ChessBoard board,
            String fen) {


        String[] parts =
                fen.split(" ");


        if(parts.length < 4) {

            throw new IllegalArgumentException(
                    "Invalid FEN"
            );
        }



        loadPieces(
                board,
                parts[0]
        );



        // Side to move

        if(parts[1].equals("w")) {

            board.setSideToMove(
            		Color.WHITE
            );

        } else {

            board.setSideToMove(
            		Color.BLACK
            );
        }



        /*
         * Castling rights,
         * en passant,
         * move counters
         *
         * can be added in ChessBoard.
         */
    }






    /**
     * Loads pieces from FEN board section.
     */
    private static void loadPieces(
            ChessBoard board,
            String position) {


        Piece[][] squares =
                board.getBoard();



        String[] rows =
                position.split("/");



        for(int row = 0;
            row < 8;
            row++) {


            int col = 0;



            for(int i = 0;
                i < rows[row].length();
                i++) {


                char symbol =
                        rows[row].charAt(i);



                if(Character.isDigit(symbol)) {


                    int empty =
                            Character
                            .getNumericValue(
                            symbol
                            );


                    col += empty;



                } else {


                    squares[row][col] =
                            createPiece(symbol);


                    col++;
                }
            }
        }
    }







    /**
     * Creates piece from FEN character.
     */
    private static Piece createPiece(
            char symbol) {



    	Color color =
                Character.isUpperCase(symbol)
                ? Color.WHITE
                : Color.BLACK;



        switch(Character.toUpperCase(symbol)) {


            case 'P':
                return new Pawn(color);


            case 'N':
                return new Knight(color);


            case 'B':
                return new Bishop(color);


            case 'R':
                return new Rook(color);


            case 'Q':
                return new Queen(color);


            case 'K':
                return new King(color);



            default:

                throw new IllegalArgumentException(
                        "Invalid piece: "
                        + symbol
                );
        }
    }








    /**
     * Converts current board to FEN.
     */
    public static String toFEN(
            ChessBoard board) {


        StringBuilder fen =
                new StringBuilder();



        Piece[][] squares =
                board.getBoard();



        for(int row = 0;
            row < 8;
            row++) {


            int empty = 0;



            for(int col = 0;
                col < 8;
                col++) {



                Piece piece =
                        squares[row][col];



                if(piece == null) {


                    empty++;


                } else {


                    if(empty > 0) {

                        fen.append(empty);

                        empty = 0;
                    }


                    fen.append(
                            piece.getFenSymbol()
                    );
                }
            }



            if(empty > 0) {

                fen.append(empty);
            }



            if(row != 7) {

                fen.append("/");
            }
        }



        /*
         * Side to move
         */

        fen.append(" ");


        fen.append(
                board.getSideToMove()
                == Color.WHITE
                ? "w"
                : "b"
        );



        /*
         * Simplified castling/en-passant
         */

        fen.append(" - - 0 1");



        return fen.toString();
    }
}