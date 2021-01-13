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
 * class {@code Calculators} to determine the optimal strategies for dropping
 * cards
 * 
 * @author Reid Moffat
 */
final class UserInterface {

	/**
	 * Stores a card object for all cards in a standard 52-card deck
	 */
	private HashSet<Card> cardPile;

	/**
	 * A set of cards that represents a cribbage card hand
	 * 
	 * <p>
	 * Can represent the five or six cards a player starts out with or the four
	 * cards that remain after dropping the chosen cards
	 */
	private HashSet<Card> hand;

	/**
	 * Used to get user input from the console
	 */
	private Scanner in;

	/**
	 * A set of valid card ranks that the user can input
	 * 
	 * <p>
	 * These are not part of the ranks in card.Rank, and a 10 is represented by 'T'
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

	/**
	 * A string that gives a short introduction to the program and asks the user to
	 * input the number of players in the cribbage game (for determining the number
	 * of starting cards)
	 */
	private static final String ENTER_PLAYERS = "Cribbage Calculator\n" + "Created by Reid Moffat\n\n"
			+ "How many players (2-4)?";

	/**
	 * A string that explains how to properly enter card values, gives some examples
	 * and prompts the user to enter their cards into the console
	 */
	private static final String ENTER_CARDS = "\nEach card must be its value (1-10, J, Q or K) plus the suit\n"
			+ "Examples:\n" + "'1D': Ace of diamonds\n" + "'4S': Four of spades\n" + "'10C': Ten of clubs\n"
			+ "'KH': King of hearts\n" + "Enter each of the cards in your hand one by one below:\n";

	/**
	 * Initializes the required fields
	 */
	public UserInterface() {
		this.cardPile = new HashSet<Card>();
		this.initialzeDeck();
		hand = new HashSet<>();
		this.in = new Scanner(System.in);
	}

	/**
	 * Initializes the cardPile with a card object for each card in a standard deck
	 */
	private void initialzeDeck() {
		for (Rank r : Card.RANKS) {
			for (Suit s : Card.SUITS) {
				this.cardPile.add(new Card(r, s));
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

	/**
	 * Introduces the program and prompts the user to enter the number of cribbage
	 * players in the console
	 * 
	 * <p>
	 * Loops until a valid number of players is inputed (2-4), then calculates and
	 * returns the number of starting cards associated with that number of players
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

		int numCards = numPlayers.equals("2") ? 6 : 5;
		System.out.println(numPlayers + " players: " + numCards + " cards to start");
		return numCards;
	}

	/**
	 * Prompts the user to enter the valid playing cards in their hand and stores
	 * their values
	 * 
	 * @param numCards the number of cards to be inputed
	 */
	private void getCards(int numCards) {
		System.out.println(UserInterface.ENTER_CARDS);

		boolean noDuplicates = false;
		while (!noDuplicates) {
			for (int i = 0; i < numCards; i++) {
				String input = in.nextLine();
				Card card = checkValidCard(input);
				while (card == null) {
					System.out.println("Invalid card, try again");
					input = in.nextLine();
					card = checkValidCard(input);
				}
				hand.add(card);
				System.out.println("Card " + (i + 1) + ": " + card.toString() + "\n");
			}

			noDuplicates = hand.size() == numCards;

			if (!noDuplicates) {
				System.out.println("Duplicate card in hand, input cards again:\n");
			}
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
		HashSet<Card> droppedHand= new HashSet<Card>(hand);

		if (this.hand.size() == 6) {
			HashSet<Card> remainingCards = new HashSet<Card>(hand);
			sb.append("Average points for each drop combination:");
			for (Card droppedCard1 : this.hand) {
				droppedHand.remove(droppedCard1);
				remainingCards.remove(droppedCard1);
				for (Card droppedCard2 : remainingCards) {
					droppedHand.remove(droppedCard2);
					double averagePoints = 0;
					for (Card starterCard : this.cardPile) {
						if (!this.containsCard(starterCard)) {
							averagePoints += Calculators.totalPoints(droppedHand, starterCard);
						}
					}
					averagePoints = Math.round(100 * (averagePoints / 46)) / 100.0;
					sb.append("\n" + droppedCard1.toString() + " and ");
					sb.append(droppedCard2.toString() + ": " + averagePoints);
					droppedHand.add(droppedCard2);
				}
				droppedHand.add(droppedCard1);
			}
		} else {
			sb.append("Average points for each drop combination:");
			for (Card droppedCard : this.hand) {
				droppedHand.remove(droppedCard);
				double averagePoints = 0;
				for (Card starterCard : this.cardPile) {
					if (!this.containsCard(starterCard)) {
						averagePoints += Calculators.totalPoints(droppedHand, starterCard);
					}
				}
				averagePoints = Math.round(100 * (averagePoints / 47)) / 100.0;
				sb.append("\n" + droppedCard.toString() + ": " + averagePoints);
				droppedHand.add(droppedCard);
			}
		}
		System.out.println(sb.toString());
	}

	/**
	 * 
	 * 
	 * @param card
	 * @return
	 */
	private static Card checkValidCard(String card) {
		/*
		 * If a card is a ten, change the "10" part of the string to "T" This makes it
		 * so all valid card strings have a length of 2 for easier use
		 */
		if (card.length() == 3) {
			if (card.charAt(0) == '1' && card.charAt(1) == '0') {
				StringBuilder sb = new StringBuilder();
				card = sb.append('T').append(card.charAt(2)).toString();
			} else {
				return null;
			}
		}

		card = card.toUpperCase();
		if (card.length() == 2) {
			Character rank = card.charAt(0);
			Character suit = card.charAt(1);
			if (!VALID_RANKS.contains(rank) || !VALID_SUITS.contains(suit)) {
				return null;
			}
		} else {
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
		for (Card c : this.hand) {
			if (c.equals(card)) {
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
