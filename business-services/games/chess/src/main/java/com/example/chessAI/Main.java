package com.example.chessAI;


import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.example.chessAI.gui.ChessGUI;

public final class Main {

    private Main() {
        // Prevent instantiation
    }

    public static void main(String[] args) {

        // Use the system look and feel for a native appearance.
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Unable to load system look and feel.");
        }

        // Start the application on the Swing Event Dispatch Thread.
        SwingUtilities.invokeLater(() -> {
            ChessGUI chessGUI = new ChessGUI();
            chessGUI.setLocationRelativeTo(null); // Center the window
            chessGUI.setVisible(true);
        });
    }
}