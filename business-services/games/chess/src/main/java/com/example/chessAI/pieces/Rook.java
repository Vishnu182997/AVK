package com.example.chessAI.pieces;

import java.util.ArrayList;
import java.util.List;

import com.example.chessAI.ChessBoard;
import com.example.chessAI.Color;
import com.example.chessAI.Move;
import com.example.chessAI.Piece;
import com.example.chessAI.PieceType;

public class Rook extends Piece {

    public Rook(Color color) {
        super(color, PieceType.ROOK);
    }

    @Override
    public int getValue() {
        return 500;
    }

    @Override
    public List<Move> getLegalMoves(
            ChessBoard board, int row, int col) {

        List<Move> moves = new ArrayList<Move>();

        addDirectionMoves(board, row, col, 1, 0, moves);
        addDirectionMoves(board, row, col, -1, 0, moves);
        addDirectionMoves(board, row, col, 0, 1, moves);
        addDirectionMoves(board, row, col, 0, -1, moves);

        return moves;
    }


    private void addDirectionMoves(
            ChessBoard board,
            int row,
            int col,
            int rowDir,
            int colDir,
            List<Move> moves) {


        int r = row + rowDir;
        int c = col + colDir;


        while (board.isValidSquare(r, c)) {

            Piece target = board.getPiece(r, c);


            if (target == null) {

                moves.add(new Move(row, col, r, c));

            } else {

                if (target.getColor() != color) {
                    moves.add(new Move(row, col, r, c));
                }

                break;
            }

            r += rowDir;
            c += colDir;
        }
    }


    @Override
    public char getFenSymbol() {
        return color == Color.WHITE ? 'R' : 'r';
    }
}