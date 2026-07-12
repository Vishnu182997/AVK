package com.example.app.tetris;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

	// ==========================
	// Board Settings
	// ==========================
	public static final int TILE_SIZE = 30;
	public static final int COLS = 10;
	public static final int ROWS = 20;

	public static final int WIDTH = COLS * TILE_SIZE;
	public static final int HEIGHT = ROWS * TILE_SIZE;

	// ==========================
	// Game Objects
	// ==========================
	private volatile Thread gameThread;

	private final KeyHandler keyHandler;
	private final Board board;

	private Piece currentPiece;
	private Piece nextPiece;

	// ==========================
	// Game State
	// ==========================
	private boolean gameOver = false;
	private boolean paused = false;

	private int score = 0;
	private int level = 1;
	private int totalLines = 0;

	// Falling speed
	private int fallCounter = 0;
	private int fallSpeed = 30;

	public GamePanel() {

		setPreferredSize(new Dimension(WIDTH + 180, HEIGHT));
		setBackground(Color.BLACK);
		setFocusable(true);

		keyHandler = new KeyHandler();
		addKeyListener(keyHandler);

		board = new Board(TILE_SIZE);

		currentPiece = PieceFactory.getRandomPiece(TILE_SIZE);
		nextPiece = PieceFactory.getRandomPiece(TILE_SIZE);
	}

	// ==========================
	// Start Game Thread
	// ==========================
	public void startGameThread() {

		if (gameThread != null) {
			return;
		}

		gameThread = new Thread(this, "TetrisGameLoop");
		gameThread.start();
	}

	public void stopGameThread() {

		gameThread = null;
	}

	@Override
	public void removeNotify() {

		stopGameThread();
		super.removeNotify();
	}

	// ==========================
	// Game Loop
	// ==========================
	@Override
	public void run() {

		final double drawInterval = 1000000000.0 / 60.0;

		double delta = 0;

		long lastTime = System.nanoTime();

		while (gameThread != null) {

			long currentTime = System.nanoTime();

			delta += (currentTime - lastTime) / drawInterval;

			lastTime = currentTime;

			while (delta >= 1) {

				update();

				repaint();

				delta--;
			}

			try {

				Thread.sleep(2);

			} catch (InterruptedException e) {

				Thread.currentThread().interrupt();
			}
		}
	}

	// ==========================
	// Update Game
	// ==========================
	private void update() {

		handleGameControls();

		if (gameOver || paused) {
			return;
		}

		handleInput();

		fallCounter++;

		if (fallCounter >= fallSpeed) {

			movePieceDown();

			fallCounter = 0;
		}
	}

	private void handleGameControls() {

		if (keyHandler.restartPressed) {
			restartGame();
			keyHandler.restartPressed = false;
		}

		if (keyHandler.pausePressed && !gameOver) {
			paused = !paused;
			keyHandler.clearGameplayInput();
			keyHandler.pausePressed = false;
		}
	}

	private void restartGame() {

		board.reset();
		currentPiece = PieceFactory.getRandomPiece(TILE_SIZE);
		nextPiece = PieceFactory.getRandomPiece(TILE_SIZE);
		gameOver = false;
		paused = false;
		score = 0;
		level = 1;
		totalLines = 0;
		fallCounter = 0;
		fallSpeed = 30;
		keyHandler.clearGameplayInput();
	}

	private void handleInput() {

		// Move Left
		if (keyHandler.leftPressed) {

			currentPiece.moveLeft();

			if (!board.canPlace(currentPiece)) {
				currentPiece.moveRight();
			}

			keyHandler.leftPressed = false;
		}

		// Move Right
		if (keyHandler.rightPressed) {

			currentPiece.moveRight();

			if (!board.canPlace(currentPiece)) {
				currentPiece.moveLeft();
			}

			keyHandler.rightPressed = false;
		}

		// Rotate
		if (keyHandler.rotatePressed) {

			currentPiece.rotate();

			if (!board.canPlace(currentPiece)) {
				currentPiece.rotateBack();
			}

			keyHandler.rotatePressed = false;
		}

		// Soft Drop
		if (keyHandler.downPressed) {

			movePieceDown();

			keyHandler.downPressed = false;
		}
	}

	private void movePieceDown() {

		currentPiece.moveDown();

		if (!board.canPlace(currentPiece)) {

			// Undo movement
			currentPiece.y--;

			// Lock piece
			board.lockPiece(currentPiece);

			// Clear completed rows
			int cleared = board.clearLines();

			if (cleared > 0) {

				totalLines += cleared;

				score += cleared * 100 * level;

				level = (totalLines / 10) + 1;

				// Increase speed gradually
				fallSpeed = Math.max(5, 30 - (level - 1) * 2);
			}

			// Spawn next piece
			currentPiece = nextPiece;
			nextPiece = PieceFactory.getRandomPiece(TILE_SIZE);

			currentPiece.x = 3;
			currentPiece.y = 0;

			// Check Game Over
			if (!board.canPlace(currentPiece)) {

				gameOver = true;
			}
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g.create();

		board.draw(g2);

		if (currentPiece != null) {
			currentPiece.draw(g2);
		}

		drawSidePanel(g2);

		if (paused && !gameOver) {
			drawCenteredMessage(g2, "PAUSED", Color.WHITE);
		}

		if (gameOver) {
			drawGameOver(g2);
		}

		g2.dispose();
	}
	
	private void drawSidePanel(Graphics2D g) {

	    int x = WIDTH + 20;

	    g.setColor(Color.WHITE);

	    g.setFont(new Font("Arial", Font.BOLD, 18));

	    g.drawString("Score", x, 40);
	    g.drawString(String.valueOf(score), x, 65);

	    g.drawString("Level", x, 120);
	    g.drawString(String.valueOf(level), x, 145);

	    g.drawString("Lines", x, 200);
	    g.drawString(String.valueOf(totalLines), x, 225);

	    g.drawString("Next", x, 290);

	    drawNextPiece(g, x, 320);

	    g.setFont(new Font("Arial", Font.PLAIN, 13));
	    g.setColor(Color.LIGHT_GRAY);
	    g.drawString("←/→ Move", x, 455);
	    g.drawString("↓ Soft drop", x, 475);
	    g.drawString("↑/Space Rotate", x, 495);
	    g.drawString("P Pause", x, 515);
	    g.drawString("R Restart", x, 535);
	}
	
	private void drawNextPiece(Graphics2D g, int startX, int startY) {

	    if (nextPiece == null)
	        return;

	    int[][] shape = nextPiece.getCurrentShape();

	    g.setColor(nextPiece.color);

	    for (int row = 0; row < shape.length; row++) {

	        for (int col = 0; col < shape[row].length; col++) {

	            if (shape[row][col] == 1) {

	                int px = startX + col * TILE_SIZE;
	                int py = startY + row * TILE_SIZE;

	                g.fillRect(px, py, TILE_SIZE, TILE_SIZE);

	                g.setColor(Color.BLACK);

	                g.drawRect(px, py, TILE_SIZE, TILE_SIZE);

	                g.setColor(nextPiece.color);
	            }
	        }
	    }
	}
	
	private void drawCenteredMessage(Graphics2D g, String text, Color color) {

		g.setColor(new Color(0, 0, 0, 180));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(color);
		g.setFont(new Font("Arial", Font.BOLD, 42));
		FontMetrics fm = g.getFontMetrics();
		int x = (WIDTH - fm.stringWidth(text)) / 2;
		g.drawString(text, x, HEIGHT / 2);
	}

	private void drawGameOver(Graphics2D g) {

	    g.setColor(new Color(0, 0, 0, 180));

	    g.fillRect(0, 0, WIDTH, HEIGHT);

	    g.setColor(Color.RED);

	    g.setFont(new Font("Arial", Font.BOLD, 42));

	    FontMetrics fm = g.getFontMetrics();

	    String text = "GAME OVER";

	    int x = (WIDTH - fm.stringWidth(text)) / 2;

	    int y = HEIGHT / 2;

	    g.drawString(text, x, y);

	    g.setFont(new Font("Arial", Font.PLAIN, 20));

	    String scoreText = "Score : " + score;

	    int sx = (WIDTH - g.getFontMetrics().stringWidth(scoreText)) / 2;

	    g.drawString(scoreText, sx, y + 40);
	}
}