package com.example.app.tetris;
import java.awt.Color;
import java.awt.Graphics;

import lombok.Data;

@Data
public class Piece {

    private final int tileSize;

    public int x = 3;
    public int y = 0;
    
    protected Color color;

    protected int[][][] shapes;
    protected int rotation = 0;

    private boolean vertical = false;

    public Piece(int tileSize) {
        this.tileSize = tileSize;
    }

    public void rotate() {
        vertical = !vertical;
    }

    public void moveLeft() {
        x--;
    }

    public void moveRight() {
        x++;
    }

    public void moveDown() {
        y++;
    }
    
    public Color getColor() {
        return color;
    }
    
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int[][] getCurrentShape() {
        return shapes[rotation];
    }

    public int[][] getCells() {

        int[][] cells = new int[4][2];

        if (!vertical) {

            for (int i = 0; i < 4; i++) {

                cells[i][0] = x + i;
                cells[i][1] = y;

            }

        } else {

            for (int i = 0; i < 4; i++) {

                cells[i][0] = x;
                cells[i][1] = y + i;

            }

        }

        return cells;
    }

    public void draw(Graphics g) {

        g.setColor(Color.CYAN);

        for (int[] c : getCells()) {

            g.fillRect(
                    c[0] * tileSize,
                    c[1] * tileSize,
                    tileSize,
                    tileSize);

            g.setColor(Color.BLACK);
            g.drawRect(
                    c[0] * tileSize,
                    c[1] * tileSize,
                    tileSize,
                    tileSize);

            g.setColor(Color.CYAN);
        }
    }
}