package com.example.app.tetris.pieces;
import java.awt.Color;

import com.example.app.tetris.Piece;

public class ZPiece extends Piece {

    private static final int[][][] SHAPES = {

        // Rotation 0
        {
            {1, 1, 0},
            {0, 1, 1}
        },

        // Rotation 1
        {
            {0, 1},
            {1, 1},
            {1, 0}
        },

        // Rotation 2 (same as Rotation 0)
        {
            {1, 1, 0},
            {0, 1, 1}
        },

        // Rotation 3 (same as Rotation 1)
        {
            {0, 1},
            {1, 1},
            {1, 0}
        }

    };

    public ZPiece(int tileSize) {
        super(tileSize);

        this.color = Color.RED;
        this.shapes = SHAPES;
    }
}