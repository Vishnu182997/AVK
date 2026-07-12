package com.example.app.tetris.pieces;
import java.awt.Color;

import com.example.app.tetris.Piece;

public class IPiece extends Piece {

    // Four rotation states
    private static final int[][][] SHAPES = {

        // Rotation 0 (Horizontal)
        {
            {1, 1, 1, 1}
        },

        // Rotation 1 (Vertical)
        {
            {1},
            {1},
            {1},
            {1}
        },

        // Rotation 2 (Horizontal)
        {
            {1, 1, 1, 1}
        },

        // Rotation 3 (Vertical)
        {
            {1},
            {1},
            {1},
            {1}
        }

    };

    public IPiece(int tileSize) {
        super(tileSize);

        this.color = Color.CYAN;
        this.shapes = SHAPES;
    }
}