/**
 * 
 */
package main;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * A utility class that includes methods to calculate points from a cribbage
 * hand and a method to check if a given hand is valid
 * 
 * @author Reid Moffat
 */
final class Calculators {

	/*
	 * Calculators is a utility class, a private constructor prevents instantiation
	 */
	private Calculators() {
	}

	/**
	 * Adds all of the point combinations together for a cribbage 5-hand and returns
	 * the total number of points
	 * 
	 * <p>
	 * A cribbage 5-hand is the player's four cards plus the starter card at the end
	 * of the array. See {@code isValidHand(String[])} for an explanation of valid
	 * cribbage 4-hands
	 * 
	 * @param hand a valid cribbage 5-hand
	 * @return the total number of points in this hand
	 * @throws IllegalArgumentException if the hand doesn't have five cards
	 */
	public static int totalPoints(String[] hand) {
		if (hand.length != 5) {
			String errMsg = "hand must five cards,four from the player and one starter card";
			throw new IllegalArgumentException(errMsg);
		}

		int points = 0;
		points += fifteens(hand);
		points += multiples(hand);
		points += runs(hand);
		points += flushes(hand);
		points += nobs(hand);
		return points;
	}

	/**
	 * Checks if a cribbage 4-hand is valid
	 * 
	 * <p>
	 * A cribbage 4-hand is defined as a 1-dimensional array of four unique strings,
	 * each representing a card in the player's hand. Each card is the value of the
	 * card (ace = 1, 2-10 = their respective value, jack = 11, queen = 12, king =
	 * 13 [all face cards are worth 10, but we need to distinguish between them for
	 * multiples and straights]) plus the first letter of the suit (not case
	 * sensitive)
	 * 
	 * <p>
	 * For example, {@code ["3H", "10S", "5S", "13C"]} represents a hand with a 3 of
	 * hearts, 10 of spades, 5 of spades and King of clubs
	 * 
	 * @param hand a cribbage hand
	 * @return true is the hand is valid, false if the hand is not valid
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
					if ((int) s.charAt(0) != 49 || (int) s.charAt(1) < 48 || (int) s.charAt(1) > 51) {
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
		int[] values = removeSuits(hand, false);
		return -1;
	}

	private static int multiples(String[] hand) {
		// TODO just iterate over the card values, faster than what i have here
		HashMap<Integer, Integer> values = new HashMap<Integer, Integer>();

		int[] cardValues = removeSuits(hand, true);
		for (int i : cardValues) {
			Integer key = Integer.valueOf(i);
			if (values.containsKey(key)) {
				Integer oldAmount = values.get(key);
				values.replace(key, Integer.valueOf(oldAmount + 1));
			} else {
				values.put(key, Integer.valueOf(1));
			}
		}

		int score = 0;
		for (int i = 1; i < 14; i++) {
			Integer amount = values.getOrDefault(Integer.valueOf(i), Integer.valueOf(1));
			if (amount == Integer.valueOf(1)) {
				continue;
			} else if (amount == Integer.valueOf(2)) {
				score += 2;
			} else if (amount == Integer.valueOf(3)) {
				score += 6;
			} else if (amount == Integer.valueOf(4)) {
				score += 12;
			}
		}
		return score;
	}

	private static int runs(String[] hand) {
		/* An array of Integers which represent the values of each card in the hand */
		Integer[] values = Arrays.stream(removeSuits(hand, true)).boxed().toArray(Integer[]::new);

		/* The number of occurrences for each card value */
		HashMap<Integer, Integer> duplicates = new HashMap<Integer, Integer>();
		for (Integer i : values) {
			if (duplicates.containsKey(i)) {
				Integer oldAmount = duplicates.get(i);
				duplicates.replace(i, Integer.valueOf(oldAmount + 1));
			} else {
				duplicates.put(i, Integer.valueOf(1));
			}
		}

		/* All unique card values */
		Integer[] uniques = new HashSet<>(Arrays.asList(values)).toArray(new Integer[] {});

		/* Maximum length of a run */
		int maxLength = uniques.length;

		int score = 0;
		if (maxLength == 5) {
			score += checkRun(uniques);
			if (score == 0) {
				score += checkRun(Arrays.copyOfRange(uniques, 0, 4));
				score += checkRun(Arrays.copyOfRange(uniques, 1, 5));
			}
			if (score == 0) {
				score += checkRun(Arrays.copyOfRange(uniques, 0, 3));
				score += checkRun(Arrays.copyOfRange(uniques, 1, 4));
				score += checkRun(Arrays.copyOfRange(uniques, 2, 5));
			}
		} else if (maxLength == 4) {
			score += 2 * checkRun(uniques);
			if (score == 0) {
				score += checkRun(Arrays.copyOfRange(uniques, 0, 2));
			}
		} else if (maxLength == 3) {
			score += checkRun(uniques, duplicates);
		}

		return score;
	}

	private static int checkRun(Integer[] values) {
		int startValue = values[0];
		for (int i = 1; i < values.length; i++) {
			if (values[i] != startValue + i) {
				return 0;
			}
		}
		return values.length;
	}

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
		if (starterSuit == suits[0] || starterSuit == suits[1] || starterSuit == suits[2] || starterSuit == suits[3]) {
			return 1;
		}
		return 0;
	}

	/*
	 * Returns a sorted array of each card's value If trueValues is false, all face
	 * cards are given the value 10
	 */
	private static int[] removeSuits(String[] hand, boolean trueValues) {
		int[] values = new int[5];
		for (int i = 0; i < hand.length; i++) {
			String card = hand[i];
			if (card.length() == 3) {
				if (trueValues) {
					values[i] = 10 + card.charAt(1) - '0';
				} else {
					values[i] = 10;
				}
			} else {
				values[i] = card.charAt(0) - '0';
			}
		}
		Arrays.sort(values);
		return values;
	}

	public static void main(String[] args) {
		String[] hand = { "6H", "5H", "11S", "1C", "5H" };

		Integer[] a = { 1, 3, 4, 5, 6 };
		System.out.println(checkRun(a));
		/*
		 * for (int i = 0; i < a.length; i++) { System.out.println(a[i]); }
		 */
	}
}