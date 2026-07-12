package com.example.app.mini;
import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MemoryGame extends JFrame implements ActionListener {

    private JButton[] buttons = new JButton[16];
    private String[] values = {
            "A", "B", "C", "D",
            "E", "F", "G", "H",
            "I", "J", "K", "L",
            "M", "N", "O", "P"
    };

    private int firstIndex = -1;
    private int secondIndex = -1;
    private boolean waiting = false;
    private int matchedPairs = 0;

    public MemoryGame() {
        setTitle("Memory Game");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 4));

        // Shuffle card values
        java.util.List<String> list = Arrays.asList(values);
        Collections.shuffle(list);

        // Create buttons
        for (int i = 0; i < 16; i++) {
            buttons[i] = new JButton("?");
            buttons[i].setFont(new Font("Arial", Font.BOLD, 24));
            buttons[i].addActionListener(this);
            add(buttons[i]);
        }

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (waiting)
            return;

        JButton clicked = (JButton) e.getSource();

        int index = -1;
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] == clicked) {
                index = i;
                break;
            }
        }

        if (clicked.getText().equals(values[index]) || clicked.getText().equals(""))
            return;

        clicked.setText(values[index]);

        if (firstIndex == -1) {
            firstIndex = index;
        } else {
            secondIndex = index;
            waiting = true;

            Timer timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {

                    if (values[firstIndex].equals(values[secondIndex])) {

                        buttons[firstIndex].setEnabled(false);
                        buttons[secondIndex].setEnabled(false);

                        matchedPairs++;

                        if (matchedPairs == 8) {
                            JOptionPane.showMessageDialog(null,
                                    "Congratulations! You Win!");
                        }

                    } else {

                        buttons[firstIndex].setText("?");
                        buttons[secondIndex].setText("?");

                    }

                    firstIndex = -1;
                    secondIndex = -1;
                    waiting = false;

                    ((Timer) evt.getSource()).stop();
                }
            });

            timer.setRepeats(false);
            timer.start();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MemoryGame());
    }
}