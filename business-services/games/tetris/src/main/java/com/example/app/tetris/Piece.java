package com.example.app.tetris;
import java.awt.Color;
import java.awt.Graphics;

public class Piece {

    private final int tileSize;

    public int x = 3;
    public int y = 0;

    protected Color color;

    protected int[][][] shapes;
    protected int rotation = 0;

    public Piece(int tileSize) {
        this.tileSize = tileSize;
    }

    public void rotate() {
        if (shapes != null && shapes.length > 0) {
            rotation = (rotation + 1) % shapes.length;
        }
    }

    public void rotateBack() {
        if (shapes != null && shapes.length > 0) {
            rotation = (rotation + shapes.length - 1) % shapes.length;
        }
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

    public int getRotation() {
        return rotation;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int[][] getCurrentShape() {
        return shapes[rotation];
    }

    public int[][] getCells() {
        int[][] shape = getCurrentShape();
        int count = 0;

        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] == 1) {
                    count++;
                }
            }
        }

        int[][] cells = new int[count][2];
        int index = 0;

        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] == 1) {
                    cells[index][0] = x + col;
                    cells[index][1] = y + row;
                    index++;
                }
            }
        }

        return cells;
    }

    public void draw(Graphics g) {
        g.setColor(color);

        for (int[] c : getCells()) {
            g.fillRect(c[0] * tileSize, c[1] * tileSize, tileSize, tileSize);

            g.setColor(Color.BLACK);
            g.drawRect(c[0] * tileSize, c[1] * tileSize, tileSize, tileSize);

            g.setColor(color);
        }
    }
}
