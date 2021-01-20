package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.stream.Collectors;

import card.Card;
import card.Rank;
import card.Suit;

/**
 * UI for a cribbage calculator
 * 
 * <p>
 * Used to get user input from the console for a cribbage hand, and use the
 * class {@code Calculators} to determine the optimal strategies for dropping
 * cards
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
	 * A set of cards that represents a cribbage card hand
	 * 
	 * <p>
	 * Can represent the five or six cards a player starts out with or the four
	 * cards that remain after dropping the chosen cards
	 */
	private final HashSet<Card> hand;

	/**
	 * Used to get user input from the console
	 */
	private final Scanner in;

	/**
	 * A set of valid card ranks used to check if a user input is valid
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
	 * A set of valid card suits that the user can input
	 * 
	 * <p>
	 * These are not part of the suits in card.Suit
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
		hand = new HashSet<Card>();
		this.in = new Scanner(System.in);
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
		System.out.println(UserInterface.ENTER_PLAYERS);

		String numPlayers = in.nextLine();
		while (!numPlayers.equals("2") && !numPlayers.equals("3") && !numPlayers.equals("4")) {
			System.out.println("Invalid input. Try again:");
			numPlayers = in.nextLine();
		}

		return numPlayers.equals("2") ? 6 : 5;
	}

	/**
	 * Prompts the user to enter the valid playing cards in their hand and stores
	 * their values
	 * 
	 * @param numCards the number of cards to be inputed
	 */
	private void getCards(int numCards) {
		System.out.println(numCards + " cards to start");
		System.out.println(UserInterface.ENTER_CARDS);

		for (int i = 0; i < numCards; i++) {
			Card card = checkValidCard(in.nextLine());
			while (card == null || containsCard(card)) {
				System.out.println("Invalid or duplicate card, input again:\n");
				card = checkValidCard(in.nextLine());
			}
			hand.add(card);
			System.out.println("Card " + (i + 1) + ": " + card.toString() + "\n");
		}

		this.in.close();
	}

	/**
	 * Calculates the average points obtained from a cribbage for each combination
	 * of cards to be dropped and prints out the value
	 * 
	 * <p>
	 * The average number of points takes into account the number of points gained
	 * from each possible starter card to be flipped up
	 */
	private void printAveragePoints() {
		StringBuilder sb = new StringBuilder();

		/* With 6 cards, 2 must be dropped */
		if (this.hand.size() == 6) {
			sb.append("Average points for each drop combination:");
			for (Card[] combination : subset2(this.hand)) {
				double averagePoints = 0;
				for (Card starterCard : cardPile) {
					averagePoints += this.containsCard(starterCard) ? 0
							: Calculators.totalPoints(removeCards(this.hand, combination), starterCard);
				}
				sb.append("\n" + combination[0].toString() + " and " + combination[1].toString() + ": "
						+ Math.round(100 * (averagePoints / 46)) / 100.0);
			}
		} else { /* With 5 cards, only one needs to be dropped */
			sb.append("Average points for each card dropped:");
			HashSet<Card> droppedHand = new HashSet<Card>(this.hand);
			for (Card droppedCard : this.hand) {
				droppedHand.remove(droppedCard);
				double averagePoints = 0;
				for (Card starterCard : cardPile) {
					averagePoints += this.containsCard(starterCard) ? 0
							: Calculators.totalPoints(droppedHand, starterCard);
				}
				sb.append("\n" + droppedCard.toString() + ": " + Math.round(100 * (averagePoints / 47)) / 100.0);
				droppedHand.add(droppedCard);
			}
		}
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
		card = card.toUpperCase(); // Not case sensitive
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
	 * @return true if the card is in the player's hand, false otherwise
	 */
	private boolean containsCard(Card card) {
		return this.hand.contains(card);
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

	/**
	 * Returns a new {@code HashSet} without the specified cards
	 * 
	 * <p>
	 * Cards that are not in the set are ignored, attempting to remove a
	 * {@code Card} that is not in the set will not throw an exception
	 * 
	 * @param hand  a {@code HashSet} of cards
	 * @param cards variable amount of cards to be removed
	 * @return a new HashSet without the specified cards
	 */
	private static HashSet<Card> removeCards(HashSet<Card> hand, Card... cards) {
		return new HashSet<Card>(hand.stream().filter(card -> !Arrays.asList(cards).stream().anyMatch(card::equals))
				.collect(Collectors.toSet()));
	}

	public static void main(String[] args) {
		UserInterface UI = new UserInterface();
		UI.run();
	}

}
