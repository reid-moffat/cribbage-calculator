package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

import card.Card;
import card.Rank;
import card.Suit;

/**
 * UI for a cribbage calculator
 * 
 * <p>
 * Used to get user input from the console for a cribbage hand, and use the
 * class {@code CribbageCombinations} to determine the optimal strategies for
 * dropping cards
 * 
 * @author Reid Moffat
 */
final class UserInterface {

	/**
	 * A set the contains a {@code Card} object for each card in a standard 52-card
	 * deck
	 */
	private static final HashSet<Card> cardPile = initialzeDeck();

	/**
	 * A set of 5 or 6 cards the player is dealt at the beginning of the round
	 * 
	 * <p>
	 * 5 Cards is for 3 or 4 players, 6 cards is for 2 players
	 */
	private final HashSet<Card> dealthHand;

	/**
	 * Used to get user input from the console
	 */
	private final Scanner input;

	/**
	 * A list of valid card ranks used to check if a user input is valid
	 * 
	 * <p>
	 * IMPORTANT: 10 is represented by 'T', although the user must input '10' for a
	 * card with a value of 10. See the method {@code checkValidCard} for more info
	 * 
	 * <ul>
	 * <li><code>1</code></li>
	 * <li><code>2</code></li>
	 * <li><code>3</code></li>
	 * <li><code>4</code></li>
	 * <li><code>5</code></li>
	 * <li><code>6</code></li>
	 * <li><code>7</code></li>
	 * <li><code>8</code></li>
	 * <li><code>9</code></li>
	 * <li><code>T</code></li>
	 * <li><code>J</code></li>
	 * <li><code>Q</code></li>
	 * <li><code>K</code></li>
	 * </ul>
	 */
	private static final ArrayList<Character> VALID_RANKS = new ArrayList<Character>(
			Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K'));

	/**
	 * A list of valid card suits that the user can input
	 * 
	 * <p>
	 * These are not part of the suits in Card.Suit
	 */
	private static final ArrayList<Character> VALID_SUITS = new ArrayList<Character>(Arrays.asList('C', 'D', 'H', 'S'));

	// @formatter:off
	/**
	 * A string that gives a short introduction to the program and asks the user to
	 * input the number of players in the cribbage game (for determining the number
	 * of starting cards)
	 */
	private static final String ENTER_PLAYERS = "Cribbage Calculator\n"
											  + "Created by Reid Moffat\n\n"
											  + "How many players (2-4)?";

	/**
	 * A string that explains how to properly enter card values, gives some examples
	 * and prompts the user to enter their cards into the console
	 */
	private static final String ENTER_CARDS = "\nEach card must be its value (1-10, J, Q or K) plus the suit\n"
											+ "Examples:\n"
											+ "'1D': Ace of diamonds\n"
											+ "'4S': Four of spades\n"
											+ "'10C': Ten of clubs\n"
											+ "'KH': King of hearts\n"
											+ "Enter each of the cards in your hand one by one below and press enter:\n";
	// @formatter:on

	/**
	 * Initializes a cribbage calculator {@code UserInterface} object
	 * 
	 * <p>
	 * Use the method {@code .run()} to run the UI
	 */
	public UserInterface() {
		this.dealthHand = new HashSet<Card>();
		this.input = new Scanner(System.in);
	}

	/**
	 * Returns a {@code HashSet} that includes each of the cards (as a {@code Card}
	 * object) in a standard 52-card deck
	 * 
	 * @return a {@code HashSet} with all standard playing cards
	 */
	private static HashSet<Card> initialzeDeck() {
		HashSet<Card> deck = new HashSet<Card>();
		for (Rank r : Card.RANKS) {
			for (Suit s : Card.SUITS) {
				deck.add(new Card(r, s));
			}
		}
		return deck;
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

	/**
	 * Introduces the program and prompts the user to enter the number of cribbage
	 * players in the console
	 * 
	 * <p>
	 * Loops until a valid number of players is inputed (2-4), then calculates and
	 * returns the number of cards each player starts the game with:
	 * 
	 * <ul>
	 * <li>2 Players: 6 cards</li>
	 * <li>3 Players: 5 cards</li>
	 * <li>4 Players: 5 cards</li>
	 * </ul>
	 * 
	 * @return the number of starting cards for the given number of players
	 */
	private int getNumCards() {
		/* Prompts the user to enter the number of players */
		System.out.println(UserInterface.ENTER_PLAYERS);

		/* Loops until a valid number of players is inputted */
		String numPlayers = input.nextLine();
		while (!(numPlayers.equals("2") || numPlayers.equals("3") || numPlayers.equals("4"))) {
			System.out.println("Invalid input. Try again:");
			numPlayers = input.nextLine();
		}

		/* Returns the number of cards dealt to each player */
		return numPlayers.equals("2") ? 6 : 5;
	}

	/**
	 * Prompts the user to enter the playing cards in their hand and stores their
	 * values
	 * 
	 * @param numCards the number of cards to be inputed
	 */
	private void getCards(int numCards) {
		/* Prompts the user to enter their cards with a guide to valid inputs */
		System.out.println(numCards + " cards to start");
		System.out.println(UserInterface.ENTER_CARDS);

		/* Gets and stores each valid card the user inputs */
		for (int i = 1; i <= numCards; i++) {
			Card card = checkValidCard(input.nextLine());
			while (card == null || !notInHand(card)) {
				System.out.println("Invalid or duplicate card, input again:\n");
				card = checkValidCard(input.nextLine());
			}
			this.dealthHand.add(card);
			System.out.println("Card " + i + ": " + card.toString() + "\n");
		}

		/* Close the scanner to prevent resource leak */
		this.input.close();
	}

	/**
	 * Calculates the average cribbage points obtained for each combination of cards
	 * to be dropped and prints out the value
	 * 
	 * <p>
	 * The average number of points takes into account the number of points gained
	 * from each possible starter card to be flipped up
	 */
	private void printAveragePoints() {
		StringBuilder sb = new StringBuilder();
		CribbageHand hand = new CribbageHand(new HashSet<Card>(this.dealthHand));

		/*
		 * The player has seen either 5 or 6 cards so far (from their hand), implying
		 * that the remaining 47 or 48 cards respectively could all possibly be the
		 * starter
		 */
		final int unknownCards = 52 - hand.size();

		/* With 6 cards, 2 must be dropped */
		if (hand.size() == 6) {
			sb.append("Average points for each drop combination:");

			/*
			 * Each combination of cards in the hand is temporarily removed and the average
			 * number of points (from all starter card possibilities) from the remaining
			 * hand is calculated
			 */
			for (Card[] combination : subset2(hand.getCards())) {
				/* The two cards to be dropped */
				hand.remove(combination[0]);
				hand.remove(combination[1]);

				/* Calculate the total number of points (all starters) for this combination */
				double totalPoints = cardPile.stream().filter(this::notInHand).mapToInt(hand::totalPoints).sum();

				/* The combination and its average number of points to 2 decimals */
				sb.append("\n" + combination[0].toString() + " and " + combination[1].toString() + ": "
						+ Math.round(100 * (totalPoints / unknownCards)) / 100.0);

				/* Put the dropped cards back in the hand */
				hand.add(combination[0]);
				hand.add(combination[1]);
			}

			/* With 5 cards, only one needs to be dropped */
		} else {
			sb.append("Average points for each card dropped:");

			/*
			 * Each card in the hand is temporarily removed and the average number of points
			 * (from all starter card possibilities) from the remaining hand is calculated
			 */
			for (Card droppedCard : hand.getCards()) {
				/* The card to be dropped */
				hand.remove(droppedCard);

				/* Calculate the total number of points (all starters) for this combination */
				double totalPoints = cardPile.stream().filter(this::notInHand).mapToInt(hand::totalPoints).sum();

				/* The combination and its average number of points to 2 decimals */
				sb.append(
						"\n" + droppedCard.toString() + ": " + Math.round(100 * (totalPoints / unknownCards)) / 100.0);

				/* Put the dropped card back in the hand */
				hand.add(droppedCard);
			}
		}

		/* Prints out the average points for each drop combination */
		System.out.println(sb.toString());
	}

	/**
	 * Checks if a String represents a valid card, and returns the {@code Card}
	 * object that it represents if it does
	 * 
	 * <p>
	 * A valid card string is the rank (1-10, j, q or k) of the card followed by the
	 * first letter of the suit (neither are case sensitive). Examples:
	 * 
	 * <ul>
	 * <li>"3d": Three of diamonds</li>
	 * <li>"jS": Jack of spades</li>
	 * <li>"10c": Ten of clubs</li>
	 * <li>"1H": Ace of hearts</li>
	 * </ul>
	 * 
	 * @param card a string
	 * @return the card object the String represents if the string is valid, null
	 *         otherwise
	 */
	private static Card checkValidCard(String card) {
		card = card.trim().toUpperCase(); // Not case sensitive
		/*
		 * If a card is a ten, change the "10" part of the string to "T" This makes it
		 * so all valid card strings have a length of 2 for easier use
		 */
		if (card.length() == 3 && card.charAt(0) == '1' && card.charAt(1) == '0') {
			card = "T" + card.charAt(2);
		} else if (card.length() != 2 || !VALID_RANKS.contains(card.charAt(0))
				|| !VALID_SUITS.contains(card.charAt(1))) {
			return null;
		}

		Rank rank = Card.RANKS[VALID_RANKS.indexOf(card.charAt(0))];
		Suit suit = Card.SUITS[VALID_SUITS.indexOf(card.charAt(1))];

		return new Card(rank, suit);
	}

	/**
	 * Checks if the player's hand (before dropping cards) contains the specified
	 * card
	 * 
	 * @param card the card to check
	 * @return true if the card is not in the player's hand, false otherwise
	 */
	private boolean notInHand(Card card) {
		return !this.dealthHand.contains(card);
	}

	/**
	 * Generates a {@code HashSet} of all 2-{@code Card} combinations in
	 * {@code cards}
	 * 
	 * @param cards a {@code HashSet} of {@code Card} objects
	 * @return a {@code HashSet} of 2-element subsets (stored as {@code Card[]})
	 */
	private static HashSet<Card[]> subset2(HashSet<Card> cards) {
		HashSet<Card[]> subsets = new HashSet<Card[]>();
		HashSet<Card> remaining = new HashSet<Card>(cards);
		for (Card card1 : cards) {
			remaining.remove(card1);
			for (Card card2 : remaining) {
				Card[] temp = { card1, card2 };
				subsets.add(temp);
			}
		}
		return subsets;
	}

	public static void main(String[] args) {
		new UserInterface().run();
	}

}
