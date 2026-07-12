package com.example.chessAI.utils;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.example.chessAI.ChessBoard;
import com.example.chessAI.Move;
import com.example.chessAI.Piece;
import com.example.chessAI.PieceType;



/**
 * PGN Parser
 *
 * Reads and writes Portable Game Notation files.
 */
public class PGNParser {



    /**
     * Loads PGN file.
     *
     * Returns PGNGame object.
     */
    public static PGNGame load(
            String fileName)
            throws IOException {


        BufferedReader reader =
                new BufferedReader(
                        new FileReader(fileName)
                );



        PGNGame game =
                new PGNGame();



        String line;

        boolean readingMoves = false;


        StringBuilder moves =
                new StringBuilder();



        while((line = reader.readLine()) != null) {



            line = line.trim();



            if(line.length() == 0) {

                continue;
            }



            /*
             * Header section
             */
            if(line.startsWith("[")) {


                parseHeader(
                        game,
                        line
                );


            } else {


                readingMoves = true;
            }



            if(readingMoves
                    && !line.startsWith("[")) {


                moves.append(line)
                     .append(" ");
            }
        }


        reader.close();



        game.setMoves(
                cleanMoves(
                        moves.toString()
                )
        );


        return game;
    }







    /**
     * Parses PGN header.
     *
     * Example:
     *
     * [White "Kasparov"]
     */
    private static void parseHeader(
            PGNGame game,
            String line) {


        int space =
                line.indexOf(" ");



        if(space == -1) {

            return;
        }



        String key =
                line.substring(
                        1,
                        space
                );



        String value =
                line.substring(
                        space + 1
                )
                .replace("\"","")
                .replace("]","");



        game.addHeader(
                key,
                value
        );
    }







    /**
     * Removes:
     *
     * 1.
     * 2.
     *
     * comments
     * results
     */
    private static String cleanMoves(
            String moves) {


        moves =
                moves.replaceAll(
                        "\\{.*?\\}",
                        ""
                );


        moves =
                moves.replaceAll(
                        "\\d+\\.",
                        ""
                );


        moves =
                moves.replace(
                        "1-0",
                        ""
                );


        moves =
                moves.replace(
                        "0-1",
                        ""
                );


        moves =
                moves.replace(
                        "1/2-1/2",
                        ""
                );



        return moves.trim();
    }








    /**
     * Saves PGN file.
     */
    public static void save(
            PGNGame game,
            String fileName)
            throws IOException {



        BufferedWriter writer =
                new BufferedWriter(
                        new FileWriter(fileName)
                );



        for(String key :
                game.getHeaders().keySet()) {



            writer.write(
                    "[" +
                    key +
                    " \"" +
                    game.getHeaders()
                    .get(key)
                    +
                    "\"]"
            );


            writer.newLine();
        }



        writer.newLine();



        writer.write(
                game.getMoves()
        );


        writer.close();
    }









    /**
     * Converts Move object to PGN.
     */
    public static String moveToPGN(
            Move move,
            ChessBoard board) {



        if(move == null) {

            return "";
        }



        Piece piece =
                move.getMovedPiece();



        StringBuilder notation =
                new StringBuilder();




        if(piece != null) {


            PieceType type =
                    piece.getType();



            if(type != PieceType.PAWN) {


                notation.append(
                        pieceLetter(type)
                );
            }
        }




        if(move.isCapture()) {


            if(piece != null
                    &&
               piece.getType()
                    == PieceType.PAWN) {


                notation.append(
                        (char)
                        ('a'
                        +
                        move.getFromCol())
                );
            }


            notation.append("x");
        }





        notation.append(
                squareName(
                        move.getToRow(),
                        move.getToCol()
                )
        );



        return notation.toString();
    }








    private static String pieceLetter(
            PieceType type) {


        switch(type) {


            case KING:
                return "K";


            case QUEEN:
                return "Q";


            case ROOK:
                return "R";


            case BISHOP:
                return "B";


            case KNIGHT:
                return "N";


            default:
                return "";
        }
    }







    private static String squareName(
            int row,
            int col) {


        char file =
                (char)
                ('a' + col);



        int rank =
                8 - row;



        return "" +
                file +
                rank;
    }







    /**
     * Stores PGN game information.
     */
    public static class PGNGame {


        private Map<String,String> headers;


        private String moves;



        public PGNGame() {


            headers =
                    new LinkedHashMap<String,String>();


            moves = "";
        }




        public void addHeader(
                String key,
                String value) {


            headers.put(
                    key,
                    value
            );
        }



        public Map<String,String> getHeaders() {

            return headers;
        }



        public String getMoves() {

            return moves;
        }



        public void setMoves(
                String moves) {

            this.moves = moves;
        }
    }
}