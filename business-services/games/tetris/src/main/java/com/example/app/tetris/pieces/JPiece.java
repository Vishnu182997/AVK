package com.example.app.tetris.pieces;
import java.awt.Color;

import com.example.app.tetris.Piece;

public class JPiece extends Piece {

    private static final int[][][] SHAPES = {

        // Rotation 0
        {
            {1, 0, 0},
            {1, 1, 1}
        },

        // Rotation 1
        {
            {1, 1},
            {1, 0},
            {1, 0}
        },

        // Rotation 2
        {
            {1, 1, 1},
            {0, 0, 1}
        },

        // Rotation 3
        {
            {0, 1},
            {0, 1},
            {1, 1}
        }

    };

    public JPiece(int tileSize) {
        super(tileSize);

        this.color = Color.BLUE;
        this.shapes = SHAPES;
    }
}