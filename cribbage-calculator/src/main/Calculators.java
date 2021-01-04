/**
 * 
 */
package main;

import java.util.Arrays;
import java.util.HashSet;

/**
 * A utility class that includes methods to calculate
 * points from a cribbage hand
 * 
 * @author Reid Moffat
 */
final class Calculators {

	/*
	 * Calculators is a utility class, a private
	 * constructor prevents instantiation 
	 */
	private Calculators() {}
	
	/**
	 * Adds all of the point combinations together for a
	 * cribbage 5-hand and returns the total number of points
	 * 
	 * <p>
	 * A cribbage 5-hand is the player's four cards plus the
	 * starter card at the end of the array. See {@code 
	 * isValidHand(String[])} for an explanation of valid
	 * cribbage 4-hands
	 * 
	 * @param hand an array of the cards in a cribbage hand
	 * @return the total number of points in this hand
	 * @throws IllegalArgumentException if the hand doesn't
	 * 			have five cards
	 */
	public static int totalPoints(String[] hand) {
		if (hand.length != 5) {
			throw new IllegalArgumentException("hand must five cards,"
					     + "four from the player and one starter card");
		}
		
		int points = 0;
		points += fifteens(hand);
		points += multiples(hand);
		points += straights(hand);
		points += flushes(hand);
		points += nobs(hand);
		return points;
	}
	
	/**
	 * Checks if a cribbage 4-hand is valid
	 * 
	 * <p>
	 * A cribbage 4-hand is defined as a 1-dimensional array
	 * of four unique strings, each representing a card in the 
	 * player's hand. Each card is the value of the card (ace = 1, 
	 * 2-10 = their respective value, jack = 11, queen = 12,
	 * king = 13 [all face cards are worth 10, but we need to
	 * distinguish between them for multiples and straights])
	 * plus the first letter of the suit (not case sensitive)
	 * 
	 * <p>
	 * For example, {@code ["3H", "10S", "5S", "13C"]}
	 * represents a hand with a 3 of hearts, 10 of spades,
	 * 5 of spades and King of clubs
	 * 
	 * @param hand a cribbage hand
	 * @return true is the hand is valid,
	 *         false if the hand is not valid
	 */
	public static boolean isValidHand(String[] hand) {
		boolean illegalHand = false;
		
		HashSet<String> uniqueCards = new HashSet<String>(Arrays.asList(hand));
		if (hand.length != 4 || uniqueCards.size() != 4) {
			return false;
		}
		
		try {
			for (String s : hand) {
				if (s.length() < 2 || s.length() > 3) {
					illegalHand = true;
					break;
				}
				char suit;
				s.toUpperCase();
				if (s.length() == 2) {
					if ((int) s.charAt(0) < 49 || (int) s.charAt(0) > 57) {
						illegalHand = true;
						break;
					}
					suit = s.charAt(1);
				} else {
					if ((int) s.charAt(0) != 49 || (int) s.charAt(1) < 48
							|| (int) s.charAt(1) > 51) {
						illegalHand = true;
						break;
					}
					suit = s.charAt(2);
				}
				if (suit != 'C' && suit != 'D' && suit != 'H' && suit != 'S') {
					illegalHand = true;
					break;
				}
			}
		} catch (Exception e) {
			illegalHand = true;
		}
		
		if (illegalHand) {
			return false;
		}
		return true;
	}
	
	
	private static int fifteens(String[] hand) {
		return -1;
	}
	
	
	private static int multiples(String[] hand) {
		return -1;
	}
	
	
	private static int straights(String[] hand) {
		return -1;
	}
	
	/**
	 * Returns the points received from a flush
	 * 
	 * @param hand a valid cribbage 5-hand
	 * @return 5 if all five cards are the same suit,
	 *         4 if the first four cards are the same suit
	 *         or 0 otherwise
	 */
	private static int flushes(String[] hand) {
		HashSet<Character> uniqueSuits = new HashSet<Character>();
		for (int i = 0; i < 4; i++) {
			uniqueSuits.add(hand[i].charAt(hand[i].length() - 1));
		}
		
		if (uniqueSuits.size() == 1) {
			uniqueSuits.add(hand[4].charAt(hand[4].length() - 1));
			if (uniqueSuits.size() == 1) {
				return 5;
			}
			return 4;
		}
		return 0;
	}
	
	/**
	 * Returns the number of points obtained from nobs
	 * 
	 * @param hand a valid cribbage 5-hand
	 * @return 1 if the hand has nobs,
	 * 		   0 otherwise
	 */
	private static int nobs(String[] hand) {
		/* If there are any jacks in the player's hand, store their suits */
		char[] suits = new char[4];
		for (int i = 0; i < suits.length; i++) {
			if (hand[i].charAt(0) == '1' && hand[i].charAt(1) == '1') {
				suits[i] = hand[i].charAt(2);
			}
		}
		
		/* Check if any of the jack's suits match the suit of the last card */
		char starterSuit = hand[4].charAt(hand[4].length() - 1);
		if (starterSuit == suits[0] || starterSuit == suits[1] ||
			starterSuit == suits[2] || starterSuit == suits[3]) {
			return 1;
		}
		return 0;
	}	
	
	/**
	 * Returns an array of just the values of each card. Used to simplify
	 * calculations where suits are unimportant (fifteens, flushes and multiples).
	 * Face cards are still distinguished between each other
	 * 
	 * @param hand a valid cribbage 5-hand
	 * @param trueValues pass in true if 
	 * @return an array of the five card's value
	 */
	private static int[] removeSuits(String[] hand, boolean trueValues) {
		int[] values = new int[5];
		for (int i = 0; i < hand.length; i++) {
			String card = hand[i];
			if (card.length() == 3) {
				values[i] = 10 + card.charAt(1) - '0';
			} else {
				values[i] = card.charAt(0) - '0';
			}
		}
		return values;
	}
	
	public static void main(String[] args) {
		String[] hand = {"3H", "11H", "5S", "13C", "5H"};
		
		int[] a = removeSuits(hand);
		for (int i : a) {
			System.out.println(i);
		}
	}
}