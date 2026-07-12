package com.example.chessAI.utils;


/**
 * Application-wide constants.
 */
public final class Constants {


    private Constants() {
        // Prevent object creation
    }



    /*
     * Chess board constants
     */

    public static final int BOARD_SIZE = 8;

    public static final int TOTAL_SQUARES = 64;



    /*
     * GUI constants
     */

    public static final int DEFAULT_WINDOW_WIDTH = 640;

    public static final int DEFAULT_WINDOW_HEIGHT = 640;


    public static final int SQUARE_SIZE = 80;



    /*
     * Colors
     */

    public static final String WHITE_SQUARE_COLOR =
            "#F0D9B5";


    public static final String BLACK_SQUARE_COLOR =
            "#B58863";




    /*
     * Resource paths
     */

    public static final String RESOURCE_PATH =
            "/resources/";


    public static final String PIECE_IMAGE_PATH =
            "/piece_images/";



    public static final String SOUND_PATH =
            "/sounds/";



    public static final String OPENING_BOOK =
            "/opening_book.bin";




    /*
     * AI constants
     */

    public static final int MAX_SEARCH_DEPTH = 8;


    public static final long DEFAULT_THINK_TIME =
            5000; // milliseconds



    public static final int CHECKMATE_SCORE =
            200000;


    public static final int DRAW_SCORE =
            0;




    /*
     * Piece values
     */

    public static final int PAWN_VALUE =
            100;


    public static final int KNIGHT_VALUE =
            320;


    public static final int BISHOP_VALUE =
            330;


    public static final int ROOK_VALUE =
            500;


    public static final int QUEEN_VALUE =
            900;


    public static final int KING_VALUE =
            20000;





    /*
     * FEN constants
     */

    public static final String START_FEN =
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";




    /*
     * Game constants
     */

    public static final int MAX_PLAYERS =
            2;


    public static final String WHITE_PLAYER =
            "White";


    public static final String BLACK_PLAYER =
            "Black";
}