package com.example.chessAI.gui;

import javax.swing.*;
import java.awt.*;

public class ChessGUI extends JFrame {

    private BoardPanel boardPanel;
    private JLabel statusLabel;

    public ChessGUI() {
        initializeFrame();
        initializeComponents();
        initializeMenu();
    }

    /**
     * Configure the main application window.
     */
    private void initializeFrame() {
        setTitle("Chess AI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setSize(900, 700);
        setMinimumSize(new Dimension(800, 650));
        setLocationRelativeTo(null);
    }

    /**
     * Create all GUI components.
     */
    private void initializeComponents() {

        boardPanel = new BoardPanel();

        statusLabel = new JLabel("White to Move");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        boardPanel.setStatusLabel(statusLabel);

        add(boardPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }

    /**
     * Creates the menu bar.
     */
    private void initializeMenu() {

        JMenuBar menuBar = new JMenuBar();

        // Game Menu
        JMenu gameMenu = new JMenu("Game");

        JMenuItem newGame = new JMenuItem("New Game");
        JMenuItem undoMove = new JMenuItem("Undo");
        JMenuItem exit = new JMenuItem("Exit");

        newGame.addActionListener(e -> startNewGame());

        undoMove.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Undo feature will be implemented later."));

        exit.addActionListener(e -> System.exit(0));

        gameMenu.add(newGame);
        gameMenu.add(undoMove);
        gameMenu.addSeparator();
        gameMenu.add(exit);

        // AI Menu
        JMenu aiMenu = new JMenu("Computer");

        JMenuItem playWhite = new JMenuItem("Computer Plays White");
        JMenuItem playBlack = new JMenuItem("Computer Plays Black");

        playWhite.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Computer White mode coming soon."));

        playBlack.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Computer Black mode coming soon."));

        aiMenu.add(playWhite);
        aiMenu.add(playBlack);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");

        JMenuItem about = new JMenuItem("About");

        about.addActionListener(e ->
                JOptionPane.showMessageDialog(
                        this,
                        "Java Chess AI\nVersion 1.0",
                        "About",
                        JOptionPane.INFORMATION_MESSAGE));

        helpMenu.add(about);

        menuBar.add(gameMenu);
        menuBar.add(aiMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    /**
     * Starts a new chess game.
     */
    public void startNewGame() {

        boardPanel.resetBoard();
        statusLabel.setText("White to Move");
    }

    /**
     * Updates the status text.
     */
    public void setStatus(String text) {
        statusLabel.setText(text);
    }

    /**
     * Returns the chess board panel.
     */
    public BoardPanel getBoardPanel() {
        return boardPanel;
    }
}