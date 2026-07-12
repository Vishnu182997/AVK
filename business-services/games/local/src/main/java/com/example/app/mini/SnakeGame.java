package com.example.app.mini;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    static class Point {
        int x, y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    final int TILE_SIZE = 20;
    final int WIDTH = 30;
    final int HEIGHT = 30;
    final int DELAY = 200;

    Timer timer;
    ArrayList<Point> snake;
    Point food;
    Random random = new Random();

    int dx = 1;
    int dy = 0;
    boolean running = true;
    int score = 0;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        startGame();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void startGame() {
        snake = new ArrayList<>();
        snake.add(new Point(WIDTH / 2, HEIGHT / 2));

        dx = 1;
        dy = 0;
        score = 0;
        running = true;

        spawnFood();
    }

    private void spawnFood() {
        while (true) {
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);

            boolean occupied = false;
            for (Point p : snake) {
                if (p.x == x && p.y == y) {
                    occupied = true;
                    break;
                }
            }

            if (!occupied) {
                food = new Point(x, y);
                return;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw food
        g.setColor(Color.RED);
        g.fillOval(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        // Draw snake
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Score: " + score, 10, 20);

        if (!running) {
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("GAME OVER", 150, 250);

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Press R to Restart", 180, 290);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
        }
        repaint();
    }

    private void move() {
        Point head = snake.get(0);
        Point newHead = new Point(head.x + dx, head.y + dy);

        // Wall collision
        if (newHead.x < 0 || newHead.x >= WIDTH ||
            newHead.y < 0 || newHead.y >= HEIGHT) {
            running = false;
            return;
        }

        // Self collision
        for (Point p : snake) {
            if (p.x == newHead.x && p.y == newHead.y) {
                running = false;
                return;
            }
        }

        snake.add(0, newHead);

        // Food
        if (newHead.x == food.x && newHead.y == food.y) {
            score++;
            spawnFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (!running && key == KeyEvent.VK_R) {
            startGame();
            return;
        }

        switch (key) {
            case KeyEvent.VK_UP:
                if (dy == 0) {
                    dx = 0;
                    dy = -1;
                }
                break;

            case KeyEvent.VK_DOWN:
                if (dy == 0) {
                    dx = 0;
                    dy = 1;
                }
                break;

            case KeyEvent.VK_LEFT:
                if (dx == 0) {
                    dx = -1;
                    dy = 0;
                }
                break;

            case KeyEvent.VK_RIGHT:
                if (dx == 0) {
                    dx = 1;
                    dy = 0;
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();

        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}