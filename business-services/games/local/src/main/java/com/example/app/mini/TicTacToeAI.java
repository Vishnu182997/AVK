package com.example.app.mini;
import java.util.Scanner;

public class TicTacToeAI {

    static char[][] board = {
            {'1', '2', '3'},
            {'4', '5', '6'},
            {'7', '8', '9'}
    };

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("=== Tic Tac Toe ===");
        System.out.println("You are X");
        System.out.println("Computer is O");

        while (true) {

            printBoard();

            playerMove();

            if (checkWinner('X')) {
                printBoard();
                System.out.println("🎉 You Win!");
                break;
            }

            if (isBoardFull()) {
                printBoard();
                System.out.println("Draw!");
                break;
            }

            computerMove();

            if (checkWinner('O')) {
                printBoard();
                System.out.println("🤖 Computer Wins!");
                break;
            }

            if (isBoardFull()) {
                printBoard();
                System.out.println("Draw!");
                break;
            }
        }
    }

    static void printBoard() {
        System.out.println();
        System.out.println(" " + board[0][0] + " | " + board[0][1] + " | " + board[0][2]);
        System.out.println("---+---+---");
        System.out.println(" " + board[1][0] + " | " + board[1][1] + " | " + board[1][2]);
        System.out.println("---+---+---");
        System.out.println(" " + board[2][0] + " | " + board[2][1] + " | " + board[2][2]);
        System.out.println();
    }

    static void playerMove() {

        while (true) {
            System.out.print("Enter position (1-9): ");
            int pos = sc.nextInt();

            if (pos < 1 || pos > 9) {
                System.out.println("Invalid!");
                continue;
            }

            int r = (pos - 1) / 3;
            int c = (pos - 1) % 3;

            if (board[r][c] != 'X' && board[r][c] != 'O') {
                board[r][c] = 'X';
                break;
            } else {
                System.out.println("Already occupied!");
            }
        }
    }

    static void computerMove() {

        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        for (int i = 1; i <= 9; i++) {

            int r = (i - 1) / 3;
            int c = (i - 1) % 3;

            if (board[r][c] != 'X' && board[r][c] != 'O') {

                char temp = board[r][c];
                board[r][c] = 'O';

                int score = minimax(false);

                board[r][c] = temp;

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }

        int r = (bestMove - 1) / 3;
        int c = (bestMove - 1) % 3;

        board[r][c] = 'O';

        System.out.println("Computer chooses: " + bestMove);
    }

    static int minimax(boolean isMax) {

        if (checkWinner('O'))
            return 1;

        if (checkWinner('X'))
            return -1;

        if (isBoardFull())
            return 0;

        if (isMax) {

            int best = Integer.MIN_VALUE;

            for (int i = 1; i <= 9; i++) {

                int r = (i - 1) / 3;
                int c = (i - 1) % 3;

                if (board[r][c] != 'X' && board[r][c] != 'O') {

                    char temp = board[r][c];
                    board[r][c] = 'O';

                    best = Math.max(best, minimax(false));

                    board[r][c] = temp;
                }
            }

            return best;

        } else {

            int best = Integer.MAX_VALUE;

            for (int i = 1; i <= 9; i++) {

                int r = (i - 1) / 3;
                int c = (i - 1) % 3;

                if (board[r][c] != 'X' && board[r][c] != 'O') {

                    char temp = board[r][c];
                    board[r][c] = 'X';

                    best = Math.min(best, minimax(true));

                    board[r][c] = temp;
                }
            }

            return best;
        }
    }

    static boolean checkWinner(char p) {

        for (int i = 0; i < 3; i++)
            if (board[i][0] == p && board[i][1] == p && board[i][2] == p)
                return true;

        for (int i = 0; i < 3; i++)
            if (board[0][i] == p && board[1][i] == p && board[2][i] == p)
                return true;

        if (board[0][0] == p && board[1][1] == p && board[2][2] == p)
            return true;

        if (board[0][2] == p && board[1][1] == p && board[2][0] == p)
            return true;

        return false;
    }

    static boolean isBoardFull() {

        for (char[] row : board)
            for (char cell : row)
                if (cell != 'X' && cell != 'O')
                    return false;

        return true;
    }
}