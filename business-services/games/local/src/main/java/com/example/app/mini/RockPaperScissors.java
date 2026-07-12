package com.example.app.mini;

import java.util.Random;
import java.util.Scanner;

public class RockPaperScissors {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		Random random = new Random();

		String[] choices = { "Rock", "Paper", "Scissors" };

		System.out.println("=== Rock, Paper, Scissors Game ===");

		while (true) {
			System.out.println("\nChoose:");
			System.out.println("1. Rock");
			System.out.println("2. Paper");
			System.out.println("3. Scissors");
			System.out.println("4. Exit");

			System.out.print("Enter your choice (1-4): ");
			int userChoice = scanner.nextInt();

			if (userChoice == 4) {
				System.out.println("Thanks for playing!");
				break;
			}

			if (userChoice < 1 || userChoice > 3) {
				System.out.println("Invalid choice! Please try again.");
				continue;
			}

			int computerChoice = random.nextInt(3);

			System.out.println("\nYou chose: " + choices[userChoice - 1]);
			System.out.println("Computer chose: " + choices[computerChoice]);

			if (userChoice - 1 == computerChoice) {
				System.out.println("It's a Tie!");
			} else if ((userChoice == 1 && computerChoice == 2) || (userChoice == 2 && computerChoice == 0)
					|| (userChoice == 3 && computerChoice == 1)) {
				System.out.println("You Win!");
			} else {
				System.out.println("Computer Wins!");
			}
		}

		scanner.close();
	}
}