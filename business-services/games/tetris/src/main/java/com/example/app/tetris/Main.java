package com.example.app.tetris;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame window = new JFrame("Java Tetris");

                GamePanel panel = new GamePanel();

                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                window.setResizable(false);

                window.add(panel);
                window.pack();

                window.setLocationRelativeTo(null);
                window.setVisible(true);

                panel.requestFocusInWindow();
                panel.startGameThread();
            }
        });
    }
}
