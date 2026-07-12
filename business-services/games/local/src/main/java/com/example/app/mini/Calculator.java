package com.example.app.mini;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator extends JFrame implements ActionListener {

	JTextField txtFirst, txtSecond, txtResult;
	JButton btnAdd, btnSub, btnMul, btnDiv, btnMod, btnSqrt, btnLog, btnPowN;;

	public Calculator() {

		setTitle("Simple Calculator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel mainPanel = new JPanel(new GridLayout(4, 2, 1, 1));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

		JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 1, 1));

		// Row 1
		mainPanel.add(new JLabel("First Number:"));
		txtFirst = new JTextField(15);
		mainPanel.add(txtFirst);

		// Row 2
		mainPanel.add(new JLabel("Second Number:"));
		txtSecond = new JTextField(15);
		mainPanel.add(txtSecond);

		// Row 3
		mainPanel.add(new JLabel("Operation:"));

		btnAdd = new JButton("+");
		btnSub = new JButton("-");
		btnMul = new JButton("*");
		btnDiv = new JButton("/");
		btnMod = new JButton("%");
		btnSqrt = new JButton("√");
		btnLog = new JButton("log");
		btnPowN = new JButton("xⁿ");

		buttonPanel.add(btnAdd);
		buttonPanel.add(btnSub);
		buttonPanel.add(btnMul);
		buttonPanel.add(btnDiv);
		buttonPanel.add(btnMod);
		buttonPanel.add(btnSqrt);
		buttonPanel.add(btnLog);
		buttonPanel.add(btnPowN);

		mainPanel.add(buttonPanel);

		// Row 4
		mainPanel.add(new JLabel("Result:"));

		txtResult = new JTextField(15);
		txtResult.setEditable(false);
		mainPanel.add(txtResult);

		add(mainPanel);

		// Register listeners
		btnAdd.addActionListener(this);
		btnSub.addActionListener(this);
		btnMul.addActionListener(this);
		btnDiv.addActionListener(this);
		btnMod.addActionListener(this);
		btnSqrt.addActionListener(this);
		btnLog.addActionListener(this);
		btnPowN.addActionListener(this);

		pack(); // Window size based on content
		setLocationRelativeTo(null); // Center on screen
		setResizable(false); // Optional
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		try {
			double num1 = Double.parseDouble(txtFirst.getText());
			double result = 0;

			// Read second number only when required
			double num2 = 0;
			if (e.getSource() != btnSqrt && e.getSource() != btnLog) {

				num2 = Double.parseDouble(txtSecond.getText());
			}

			if (e.getSource() == btnAdd)
				result = num1 + num2;

			else if (e.getSource() == btnSub)
				result = num1 - num2;

			else if (e.getSource() == btnMul)
				result = num1 * num2;

			else if (e.getSource() == btnDiv) {
				if (num2 == 0) {
					txtResult.setText("Cannot divide by zero");
					return;
				}
				result = num1 / num2;
			}

			else if (e.getSource() == btnMod)
				result = num1 % num2;

			else if (e.getSource() == btnSqrt) {
				if (num1 < 0) {
					txtResult.setText("Invalid Input");
					return;
				}
				result = Math.sqrt(num1);
			}

			else if (e.getSource() == btnLog) {
				if (num1 <= 0) {
					txtResult.setText("Invalid Input");
					return;
				}
				result = Math.log10(num1);
			}

			else if (e.getSource() == btnPowN)
				result = Math.pow(num1, num2);

			txtResult.setText(String.valueOf(result));

		} catch (NumberFormatException ex) {
			txtResult.setText("Invalid Input");
		}
	}

	public static void main(String[] args) {
		new Calculator();
	}
}