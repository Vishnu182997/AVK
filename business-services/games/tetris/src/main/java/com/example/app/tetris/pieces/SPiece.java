package com.example.app.tetris.pieces;
import java.awt.Color;

import com.example.app.tetris.Piece;

public class SPiece extends Piece {

    private static final int[][][] SHAPES = {

        // Rotation 0
        {
            {0, 1, 1},
            {1, 1, 0}
        },

        // Rotation 1
        {
            {1, 0},
            {1, 1},
            {0, 1}
        },

        // Rotation 2 (same as Rotation 0)
        {
            {0, 1, 1},
            {1, 1, 0}
        },

        // Rotation 3 (same as Rotation 1)
        {
            {1, 0},
            {1, 1},
            {0, 1}
        }

    };

    public SPiece(int tileSize) {
        super(tileSize);

        this.color = Color.GREEN;
        this.shapes = SHAPES;
    }
}