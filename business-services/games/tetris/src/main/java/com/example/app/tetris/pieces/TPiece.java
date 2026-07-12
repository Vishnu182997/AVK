package com.example.app.tetris.pieces;
import java.awt.Color;

import com.example.app.tetris.Piece;

public class TPiece extends Piece {

    private static final int[][][] SHAPES = {

        // Rotation 0
        {
            {0, 1, 0},
            {1, 1, 1}
        },

        // Rotation 1
        {
            {1, 0},
            {1, 1},
            {1, 0}
        },

        // Rotation 2
        {
            {1, 1, 1},
            {0, 1, 0}
        },

        // Rotation 3
        {
            {0, 1},
            {1, 1},
            {0, 1}
        }

    };

    public TPiece(int tileSize) {
        super(tileSize);

        this.color = new Color(128, 0, 128); // Purple
        this.shapes = SHAPES;
    }
}