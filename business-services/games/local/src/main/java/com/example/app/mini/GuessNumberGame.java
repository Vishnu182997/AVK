package com.example.app.mini;

import java.util.Random;
import java.util.Scanner;

public class GuessNumberGame {

	public static void main(String[] args) {

		Random random = new Random();
		Scanner scanner = new Scanner(System.in);

		int secretNumber = random.nextInt(50) + 1; // Random number between 1 and 100
		int guess;
		int attempts = 0;

		System.out.println("=== Guess the Number Game ===");
		System.out.println("I have chosen a number between 1 and 50");
		System.out.println("Can you guess it?");

		do {
			System.out.print("Enter your guess: ");
			guess = scanner.nextInt();
			attempts++;

			if (guess < secretNumber) {
				System.out.println("This is low! Try again.");
			} else if (guess > secretNumber) {
				System.out.println("This is high! Try again.");
			} else {
				System.out.println("Congratulations!");
				System.out.println("You guessed the number in " + attempts + " attempts.");
			}

		} while (guess != secretNumber);

		scanner.close();
	}
}