package com.example.app.tetris.pieces;
import java.awt.Color;

import com.example.app.tetris.Piece;

public class LPiece extends Piece {

    private static final int[][][] SHAPES = {

        // Rotation 0
        {
            {0, 0, 1},
            {1, 1, 1}
        },

        // Rotation 1
        {
            {1, 0},
            {1, 0},
            {1, 1}
        },

        // Rotation 2
        {
            {1, 1, 1},
            {1, 0, 0}
        },

        // Rotation 3
        {
            {1, 1},
            {0, 1},
            {0, 1}
        }

    };

    public LPiece(int tileSize) {
        super(tileSize);

        this.color = Color.ORANGE;
        this.shapes = SHAPES;
    }
}