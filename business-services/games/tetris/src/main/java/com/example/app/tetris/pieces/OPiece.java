package com.example.app.tetris.pieces;
import java.awt.Color;

import com.example.app.tetris.Piece;

public class OPiece extends Piece {

    // O-piece (square) looks the same in every rotation
    private static final int[][][] SHAPES = {

        {
            {1, 1},
            {1, 1}
        },

        {
            {1, 1},
            {1, 1}
        },

        {
            {1, 1},
            {1, 1}
        },

        {
            {1, 1},
            {1, 1}
        }

    };

    public OPiece(int tileSize) {
        super(tileSize);

        this.color = Color.YELLOW;
        this.shapes = SHAPES;
    }

    @Override
    public void rotate() {
        // O-piece does not change when rotated.
    }
}