package com.example.chessAI.gui;

import com.example.chessAI.Color;
import com.example.chessAI.GameMode;
import com.example.chessAI.ai.AiDifficulty;

import javax.swing.*;
import java.awt.*;

public class ChessGUI extends JFrame {

    private BoardPanel boardPanel;
    private JLabel statusLabel;
    private JComboBox<GameMode> modeSelector;
    private JComboBox<AiDifficulty> difficultySelector;
    private JComboBox<Color> humanColorSelector;
    private JLabel sideLabel;

    public ChessGUI() {
        initializeFrame();
        initializeComponents();
        initializeMenu();
    }

    private void initializeFrame() {
        setTitle("Chess AI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(900, 760);
        setMinimumSize(new Dimension(800, 700));
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        boardPanel = new BoardPanel();

        statusLabel = new JLabel("White to Move");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        boardPanel.setStatusLabel(statusLabel);

        add(createControlsPanel(), BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
        updateModeControls();
    }

    private JPanel createControlsPanel() {
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));

        modeSelector = new JComboBox<GameMode>(GameMode.values());
        modeSelector.setToolTipText("Choose game mode");
        modeSelector.addActionListener(e -> changeMode((GameMode) modeSelector.getSelectedItem()));

        difficultySelector = new JComboBox<AiDifficulty>(AiDifficulty.values());
        difficultySelector.setSelectedItem(AiDifficulty.HARD);
        difficultySelector.setToolTipText("Choose computer difficulty");
        difficultySelector.addActionListener(e -> boardPanel.setAiDifficulty((AiDifficulty) difficultySelector.getSelectedItem()));

        humanColorSelector = new JComboBox<Color>(new Color[] {Color.WHITE, Color.BLACK});
        humanColorSelector.setToolTipText("Choose your side");
        humanColorSelector.addActionListener(e -> boardPanel.setHumanColor((Color) humanColorSelector.getSelectedItem()));

        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> startNewGame());

        JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> boardPanel.undoMove());

        sideLabel = new JLabel();

        controls.add(new JLabel("Mode:"));
        controls.add(modeSelector);
        controls.add(new JLabel("Difficulty:"));
        controls.add(difficultySelector);
        controls.add(new JLabel("Your side:"));
        controls.add(humanColorSelector);
        controls.add(newGameButton);
        controls.add(undoButton);
        controls.add(sideLabel);
        return controls;
    }

    private void initializeMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");

        JMenuItem newGame = new JMenuItem("New Game");
        JMenuItem undoMove = new JMenuItem("Undo");
        JMenuItem exit = new JMenuItem("Exit");

        newGame.addActionListener(e -> startNewGame());
        undoMove.addActionListener(e -> boardPanel.undoMove());
        exit.addActionListener(e -> {
            boardPanel.disposeEngine();
            System.exit(0);
        });

        gameMenu.add(newGame);
        gameMenu.add(undoMove);
        gameMenu.addSeparator();
        gameMenu.add(exit);

        JMenu aiMenu = new JMenu("Computer");
        JMenuItem humanVsHuman = new JMenuItem("Human vs Human");
        JMenuItem humanVsComputer = new JMenuItem("Human vs Computer");
        humanVsHuman.addActionListener(e -> modeSelector.setSelectedItem(GameMode.HUMAN_VS_HUMAN));
        humanVsComputer.addActionListener(e -> modeSelector.setSelectedItem(GameMode.HUMAN_VS_AI));
        aiMenu.add(humanVsHuman);
        aiMenu.add(humanVsComputer);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(e -> JOptionPane.showMessageDialog(this, "Java Chess AI\nVersion 1.0", "About", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(about);

        menuBar.add(gameMenu);
        menuBar.add(aiMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    private void changeMode(GameMode mode) {
        if (mode == null) { return; }
        boardPanel.setGameMode(mode);
        updateModeControls();
    }

    private void updateModeControls() {
        boolean aiMode = boardPanel.getGameMode() == GameMode.HUMAN_VS_AI;
        difficultySelector.setVisible(aiMode);
        humanColorSelector.setVisible(aiMode);
        sideLabel.setText(aiMode
                ? "You: " + boardPanel.getHumanColor() + "  Computer: " + boardPanel.getHumanColor().opposite()
                : "Both White and Black are human-controlled");
        revalidate();
        repaint();
    }

    public void startNewGame() {
        boardPanel.resetBoard();
        updateModeControls();
    }

    public void setStatus(String text) { statusLabel.setText(text); }
    public BoardPanel getBoardPanel() { return boardPanel; }
}
