package com.example.chessAI.ai;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.example.chessAI.Move;


/**
 * Chess opening book.
 *
 * Provides predefined strong opening moves.
 */
public class OpeningBook {


    private HashMap<String, List<Move>> book;

    private Random random;



    public OpeningBook() {

        book = new HashMap<String, List<Move>>();

        random = new Random();

        initializeBook();
    }




    /**
     * Loads opening positions.
     */
    private void initializeBook() {


        /*
         * Starting position FEN
         */
        String startPosition =
                "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";



        List<Move> moves =
                new ArrayList<Move>();


        /*
         * e2-e4
         */
        moves.add(
                new Move(
                        6,
                        4,
                        4,
                        4
                )
        );



        /*
         * d2-d4
         */
        moves.add(
                new Move(
                        6,
                        3,
                        4,
                        3
                )
        );



        /*
         * c2-c4
         */
        moves.add(
                new Move(
                        6,
                        2,
                        4,
                        2
                )
        );



        book.put(
                startPosition,
                moves
        );
    }





    /**
     * Returns a book move.
     *
     * @param fen current board FEN
     */
    public Move getBookMove(String fen) {


        List<Move> moves =
                book.get(fen);



        if(moves == null
                || moves.isEmpty()) {


            return null;
        }



        return moves.get(
                random.nextInt(
                        moves.size()
                )
        );
    }





    /**
     * Adds a new opening line.
     */
    public void addOpening(
            String fen,
            Move move) {


        List<Move> moves =
                book.get(fen);



        if(moves == null) {


            moves =
                    new ArrayList<Move>();


            book.put(
                    fen,
                    moves
            );
        }



        moves.add(move);
    }





    /**
     * Checks if position exists.
     */
    public boolean contains(
            String fen) {


        return book.containsKey(fen);
    }




    /**
     * Clears opening book.
     */
    public void clear() {


        book.clear();
    }




    /**
     * Number of positions stored.
     */
    public int size() {


        return book.size();
    }
}