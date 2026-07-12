package com.example.chessAI;


import com.example.chessAI.ai.ChessEngine;

/**
 * Controls chess game flow.
 */
public class Game {


    private ChessBoard board;

    private ChessEngine engine;


    private Color humanColor;

    private Color computerColor;


    private boolean gameOver;





    /**
     * Creates a new game.
     *
     * @param humanColor player color
     */
    public Game(Color humanColor) {


        board =
                new ChessBoard();


        engine =
                new ChessEngine();



        this.humanColor =
                humanColor;


        this.computerColor =
                humanColor.opposite();



        gameOver = false;
    }







    /**
     * Returns current board.
     */
    public ChessBoard getBoard() {

        return board;
    }






    /**
     * Returns true if game ended.
     */
    public boolean isGameOver() {

        return gameOver;
    }







    /**
     * Returns current player turn.
     */
    public Color getCurrentTurn() {

        return board.getSideToMove();
    }







    /**
     * Plays a human move.
     */
    public boolean playHumanMove(
            Move move) {



        if(gameOver) {

            return false;
        }



        if(board.getSideToMove()
                != humanColor) {


            return false;
        }




        if(move == null) {

            return false;
        }



        MoveValidator validator = new MoveValidator();
        if(!validator.isValidMove(board, move)) {

            return false;
        }


        board.makeMove(move);



        checkGameEnd();



        return true;
    }








    /**
     * Lets computer make a move.
     */
    public Move playComputerMove() {



        if(gameOver) {

            return null;
        }



        if(board.getSideToMove()
                != computerColor) {


            return null;
        }



        Move move =
                engine.getBestMove(
                        board,
                        computerColor
                );



        if(move != null) {


            board.makeMove(move);

            checkGameEnd();
        }



        return move;
    }








    /**
     * Checks checkmate/stalemate.
     */
    private void checkGameEnd() {


        MoveGenerator generator =
                new MoveGenerator();



        boolean hasMoves =
                generator.hasLegalMoves(
                        board,
                        board.getSideToMove()
                );



        if(!hasMoves) {


            gameOver = true;
        }
    }








    /**
     * Restarts the game.
     */
    public void restart() {


        board =
                new ChessBoard();


        gameOver = false;
    }








    /**
     * Returns the winner.
     *
     * Null means draw or unfinished.
     */
    public Color getWinner() {


        if(!gameOver) {

            return null;
        }


        MoveValidator validator = new MoveValidator();


        if(!validator.isCheck(board, board.getSideToMove())) {

            return null;
        }


        return board.getSideToMove()
                .opposite();
    }
}