package com.example.app.tetris;
import java.util.Random;

import com.example.app.tetris.pieces.IPiece;
import com.example.app.tetris.pieces.JPiece;
import com.example.app.tetris.pieces.LPiece;
import com.example.app.tetris.pieces.OPiece;
import com.example.app.tetris.pieces.SPiece;
import com.example.app.tetris.pieces.TPiece;
import com.example.app.tetris.pieces.ZPiece;

public class PieceFactory {

    private static final Random random = new Random();

    public static Piece getRandomPiece(int tileSize) {

        TetrominoType type =
                TetrominoType.values()[random.nextInt(TetrominoType.values().length)];

        switch (type) {

            case I:
                return new IPiece(tileSize);   // Temporary

            case O:
                return new OPiece(tileSize);   // Replace with OPiece later

            case T:
                return new TPiece(tileSize);   // Replace with TPiece later

            case S:
                return new SPiece(tileSize);   // Replace with SPiece later

            case Z:
                return new ZPiece(tileSize);   // Replace with ZPiece later

            case J:
                return new JPiece(tileSize);   // Replace with JPiece later

            case L:
                return new LPiece(tileSize);   // Replace with LPiece later

            default:
                return new Piece(tileSize);
        }
    }
}