/**
 * 
 */
package main;

import java.util.Arrays;
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
	
	private Scanner in;
	
	private static final String ENTER_PLAYERS = "Cribbage Calculator\n" +
												"Created by Reid Moffat\n\n" +  
			                                    "How many players (2-4)?";
	
	private static final String ENTER_CARDS = "\nEach card must be its value (from 1 to 13) plus the suit\n" +
											  "Examples:\n" +
											  "'1D': Ace of diamonds\n" +
											  "'4S': Four of spades\n" +
											  "'10C': Ten of clubs\n" +
											  "'13H': King of hearts\n" +
											  "Enter each of the cards in your hand one by one below:\n";

	public UserInterface() {
		this.cardPile = new HashSet<String>();
		this.initialzeDeck();
		this.in = new Scanner(System.in);
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

	/**
	 * Runs the program's user interface, calling the required methods in the
	 * correct order
	 */
	public void run() {
		int numCards = this.getNumCards();
		this.getCards(numCards);
		this.printAveragePoints();
	}

	private int getNumCards() {
		System.out.println(UserInterface.ENTER_PLAYERS);

		String numPlayers = in.nextLine();
		while (!numPlayers.equals("2") && !numPlayers.equals("3") && !numPlayers.equals("4")) {
			System.out.println("Invalid input. Try again:");
			numPlayers = in.nextLine();
		}

		int numCards = numPlayers.equals("2") ? 6 : 5;
		this.hand = new String[numCards];
		System.out.println(numPlayers + " players: " + numCards + " cards to start");
		return numCards;
	}

	private void getCards(int numCards) {
		System.out.println(UserInterface.ENTER_CARDS);

		boolean noDuplicates = false;
		while (!noDuplicates) {
			for (int i = 0; i < numCards; i++) {
				String card = in.nextLine();
				String cardValue = checkValidCard(card);
				while (cardValue == null) {
					System.out.println("Invalid card, try again");
					card = in.nextLine();
					cardValue = checkValidCard(card);
				}
				hand[i] = card.toUpperCase();
				System.out.println("Card " + (i + 1) + ": " + cardValue + "\n");
			}

			noDuplicates = new HashSet<String>(Arrays.asList(hand)).size() == numCards;

			if (!noDuplicates) {
				System.out.println("Duplicate card in hand, input cards again:\n");
			}
		}

		this.in.close();
	}

	private void printAveragePoints() {
		StringBuilder sb = new StringBuilder();
		String[] handCopy = new String[5];

		if (this.hand.length == 6) {
			sb.append("Average points for each drop combination:");
			for (int i = 0; i < hand.length - 1; i++) {
				for (int j = i + 1; j < hand.length; j++) {
					double averagePoints = 0;
					handCopy = removeCard(this.hand, j);
					String droppedCard1 = this.hand[i];
					String droppedCard2 = this.hand[j];
					for (String starterCard : this.cardPile) {
						if (!this.containsCard(starterCard)) {
							handCopy[i] = starterCard;
							averagePoints += Calculators.totalPoints(handCopy);
						}
					}
					averagePoints = Math.round(100 * (averagePoints / 47)) / 100.0;
					sb.append("\n" + valueToCard(droppedCard1) + " and ");
					sb.append(valueToCard(droppedCard2) + ": " + averagePoints);
				}
			}
		} else {
			sb.append("Average points for each drop combination:");
			for (int i = 0; i < hand.length; i++) {
				double averagePoints = 0;
				String droppedCard = this.hand[i];
				for (String starterCard : this.cardPile) {
					if (!this.containsCard(starterCard)) {
						handCopy = Arrays.copyOf(hand, hand.length);
						handCopy[i] = starterCard;
						averagePoints += Calculators.totalPoints(handCopy);
					}
				}
				averagePoints = Math.round(100 * (averagePoints / 47)) / 100.0;
				sb.append("\n" + valueToCard(droppedCard) + ": " + averagePoints);
			}
		}
		System.out.println(sb.toString());
	}
	
	private static String checkValidCard(String card) {
		if (card.length() < 2 || card.length() > 3) {
			return null;
		}

		try {
			char suit;
			card = card.toUpperCase();
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
		final String[] ranks = { "Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack",
				"Queen", "King" };
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
	
	private static String[] removeCard(String[] cards, int index) {
		String[] copy = new String[cards.length - 1];
		for (int i = 0, j = 0; i < cards.length; i++) {
			if (i != index) {
				copy[j] = cards[i];
				j++;
			}
		}
		return copy;
	}

	private boolean containsCard(String card) {
		for (String s : this.hand) {
			if (s.equals(card)) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		UserInterface UI = new UserInterface();
		UI.run();
	}

}
