/**
 * 
 */
package main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

/**
 * @author Reid Moffat
 *
 */
final class UserInterface {

	/*
	 * Stores a string representation of all cards in a standard 52-card deck
	 * 
	 * Each card is represented by its value (ace = 1, 2-10 = their respective
	 * value, jack = 11, queen = 12, king = 13) followed by the suit (C, D, H or S).
	 * For example, '13S' is the king of spades and '5H' is the king of hearts
	 */
	private HashSet<String> cardPile;

	private String[] hand;

	public UserInterface() {
		this.cardPile = new HashSet<String>();
		this.initialzeDeck();
	}

	private void initialzeDeck() {
		final char[] suits = { 'C', 'D', 'H', 'S' };
		for (int value = 1; value < 14; value++) {
			for (int suit = 0; suit < suits.length; suit++) {
				String card = String.valueOf(value) + String.valueOf(suits[suit]);
				this.cardPile.add(card);
			}
		}
	}

	private void getUserInput() {
		Scanner in = new Scanner(System.in);

		System.out.println("Cribbage Calculator");
		System.out.println("Created by Reid Moffat");

		System.out.println("\nHow many players (2-4)?");
		String numPlayers = in.next();
		while (!numPlayers.equals("2") && !numPlayers.equals("3") && !numPlayers.equals("4")) {
			System.out.println("Invalid input. Try again:");
			numPlayers = in.next();
		}
		int numCards = numPlayers.equals("2") ? 6 : 5;

		System.out.println("\nEach card must be its value (from 1 to 13) plus the suit");
		System.out.println("Examples:");
		System.out.println("'1D': Ace of diamonds");
		System.out.println("'4S': Four of spades");
		System.out.println("'10C': Ten of clubs");
		System.out.println("'13H': King of hearts");
		System.out.println("Enter each of the " + numCards + " cards in your hand one by one below:\n");

		for (int i = 1; i < numCards + 1; i++) {
			String card = in.nextLine();
			System.out.println("Card " + i + ": " + checkValidCard(card));
		}
		
		in.close();
	}

	private static String checkValidCard(String card) {
		if (card.length() < 2 || card.length() > 3) {
			return null;
		}
		
		try {
			char suit;
			card.toUpperCase();
			if (card.length() == 2) {
				if (card.charAt(0) < '1' || (int) card.charAt(0) > '9') {
					return null;
				}
				suit = card.charAt(1);
			} else {
				if (card.charAt(0) != '1' || card.charAt(1) < '0' || card.charAt(1) > '3') {
					return null;
				}
				suit = card.charAt(2);
			}
			if (suit != 'C' && suit != 'D' && suit != 'H' && suit != 'S') {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
		
		return valueToCard(card);
	}
	
	private static String valueToCard(String card) {
		final String[] ranks = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven",
						  "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
		final HashMap<Character, String> suits = new HashMap<Character, String>();
		suits.put('C', "clubs");
		suits.put('D', "diamonds");
		suits.put('H', "hearts");
		suits.put('S', "spades");
		
		int value = card.length() == 3 ? 10 + card.charAt(1) - '0' : card.charAt(0) - '0';
		String rank = ranks[value - 1];
		String suit = suits.get(card.charAt(card.length() - 1));
		return rank + " of " + suit;
	}

	public static void main(String[] args) {
		UserInterface UI = new UserInterface();
		UI.getUserInput();

		// When checking for points, make sure the starter card isn't the same as
		// any of the four cards in the hand
	}

}
