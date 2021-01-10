/**
 * main includes a user interface and calculator class for getting user input
 * and calculating the optimal strategies
 */
package main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * The utility class {@code Calculators} contains methods for calculating the
 * score of a cribbage hand with a starter card
 * 
 * @author Reid Moffat
 */
final class Calculators {

	/**
	 * Calculators is a utility class, a private constructor prevents instantiation
	 */
	private Calculators() {
	}

	/**
	 * Adds all of the point combinations together for a valid cribbage hand and
	 * returns the total number of points
	 * 
	 * <p>
	 * A valid cribbage hand is defined as a 1-dimensional array of five unique
	 * strings, each representing a card in the player's hand and the last string
	 * being the starter card. Each card is the value of the card (ace = 1, 2-10 =
	 * their respective value, jack = 11, queen = 12, king = 13 [all face cards are
	 * worth 10, but we need to distinguish between them for multiples and
	 * straights]) plus the first letter of the suit (not case sensitive)
	 * 
	 * <p>
	 * For example, {@code ["3H", "10S", "5S", "13C", "2D"]} represents a hand with
	 * a 3 of hearts, 10 of spades, 5 of spades, King of clubs and a starter 2 of
	 * diamonds
	 * 
	 * @param hand a valid cribbage hand
	 * @return the total number of points in this hand
	 * @throws IllegalArgumentException if the hand doesn't have five cards
	 */
	public static int totalPoints(String[] hand) {
		if (hand.length != 5) {
			String errMsg = "hand must five cards, four from the player and one starter card";
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
	 * Returns the number of points obtained from fifteens in the given cribbage
	 * hand
	 * 
	 * <p>
	 * Each combination of cards that add up to 15 is worth two points. Any number
	 * of cards can be used, and cards may be used for multiple fifteens. All face
	 * cards are worth 10 points for fifteens
	 * 
	 * <p>
	 * For example, {@code ["4H", "5S", "6S", "13C", "1D"]} has two fifteens: 4 + 5
	 * + 6 and 5 + king
	 * 
	 * @param hand a valid cribbage hand
	 * @return the number of points obtained from fifteens
	 */
	public static int fifteens(String[] hand) {
		final int[] values = removeSuits(hand, false);
		int[] valuesCopy = Arrays.copyOf(values, 5);

		int score = 0;
		// Check if all five add up
		score += isFifteen(values);

		// Check if four add up
		for (int i = 0; i < 5; i++) {
			valuesCopy[i] = 0;
			score += isFifteen(values);
			valuesCopy = Arrays.copyOf(valuesCopy, 5);
		}

		// Check if three add up
		for (int i = 0; i < 3; i++) {
			for (int j = i + 1; j < 4; j++) {
				for (int k = j + 1; k < 5; k++) {
					int[] vals = { values[i], values[j], values[k] };
					score += isFifteen(vals);
				}
			}
		}

		// Check if two add up
		for (int i = 0; i < 4; i++) {
			for (int j = i + 1; j < 5; j++) {
				int[] vals = { values[i], values[j] };
				score += isFifteen(vals);
			}
		}

		return score;
	}

	/**
	 * Checks if the values in the array add up to 15; returning 2 if they do and 0
	 * if they do not
	 * 
	 * @param values an array of integers
	 * @return 2 if the sum of all values is 15, 0 if not
	 */
	private static int isFifteen(int[] values) {
		int sum = 0;
		for (int i : values) {
			sum += i;
		}
		return sum == 15 ? 2 : 0;
	}

	/**
	 * Returns the number of points obtained from multiples in the given cribbage
	 * hand
	 * 
	 * <p>
	 * Multiples are a double (2 points), triple (3 points) or quadruple (12 points)
	 * of one value of card. Face cards are not considered the same for multiples; a
	 * ten and a queen both have a value of 10 but they would not give points for a
	 * double
	 * 
	 * <p>
	 * For example, {@code ["13H", "10S", "11S", "13C", "12D"]} has two points from
	 * multiples (double kings)
	 * 
	 * @param hand a valid cribbage hand
	 * @return the number of points obtained from multiples
	 */
	public static int multiples(String[] hand) {
		HashMap<Integer, Integer> values = new HashMap<Integer, Integer>();

		int[] cardValues = removeSuits(hand, true);
		for (int i : cardValues) {
			Integer key = Integer.valueOf(i);
			if (values.containsKey(key)) {
				values.replace(key, Integer.valueOf(values.get(key) + 1));
			} else {
				values.put(key, Integer.valueOf(1));
			}
		}

		int score = 0;
		for (Integer amount : values.values()) {
			if (amount == Integer.valueOf(2)) {
				score += 2;
			} else if (amount == Integer.valueOf(3)) {
				score += 6;
			} else if (amount == Integer.valueOf(4)) {
				score += 12;
			}
		}
		return score;
	}

	/**
	 * Returns the number of points obtained from runs in a given cribbage hand
	 * 
	 * <p>
	 * A run is a sequence of three (3 points), four (4 points) or five (5 points)
	 * consecutive cards. Suit does not matter, and cards can be part of multiple
	 * runs
	 * 
	 * <p>
	 * For example, {@code ["3H", "4S", "5S", "4C", "4D"]} has twelve points from
	 * runs. This is because the run 3-4-5 can be made four times by swapping out
	 * the two fours and two fives
	 * 
	 * @param hand a valid cribbage hand
	 * @return the number of points obtained from runs
	 */
	public static int runs(String[] hand) {
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
			score += isRun(uniques);
			if (score == 0) {
				score += isRun(Arrays.copyOfRange(uniques, 0, 4));
				score += isRun(Arrays.copyOfRange(uniques, 1, 5));
			}
			if (score == 0) {
				score += isRun(Arrays.copyOfRange(uniques, 0, 3));
				score += isRun(Arrays.copyOfRange(uniques, 1, 4));
				score += isRun(Arrays.copyOfRange(uniques, 2, 5));
			}
			return score;
		} else if (maxLength == 4) {
			score += 2 * isRun(uniques);
			if (score == 0) {
				if (isRun(Arrays.copyOfRange(uniques, 0, 3)) == 3) {
					score = duplicates.get(uniques[3]) == 2 ? 3 : 6;
				}
				if (isRun(Arrays.copyOfRange(uniques, 1, 4)) == 3) {
					score = duplicates.get(uniques[0]) == 2 ? 3 : 6;
				}
			}
			return score;
		} else if (maxLength == 3) {
			if (isRun(uniques) == 0) {
				return 0;
			}

			/*
			 * Since there is a run and three unique cards, there is either a triple or two
			 * doubles. A triple run is three runs (9 points from runs) and a double double
			 * run is four runs (12 points from runs)
			 */
			for (Integer i : uniques) {
				if (duplicates.get(i) == 3) {
					return 9;
				}
			}
			return 12;
		}
		return 0; // A triple and double or quad and single won't have a run
	}

	/**
	 * Returns the number of points obtained from a flush for the given values
	 * 
	 * @param values an array of Integers
	 * @return 0 if it is not a run, 3-5 for a run of 3-5 respectively
	 */
	private static int isRun(Integer[] values) {
		int startValue = values[0];
		for (int i = 1; i < values.length; i++) {
			if (values[i] != startValue + i) {
				return 0;
			}
		}
		return values.length;
	}

	/**
	 * Returns the number of points obtained from flushes in a given cribbage hand
	 * 
	 * <p>
	 * To obtain a flush, the player's hand must have all four cards of the same
	 * suit (4 points). If the starter card is also the same suit, 5 points are
	 * obtained. Note that if only three cards in the player's hand plus the starter
	 * card have the same suit, this is not a flush
	 * 
	 * <p>
	 * For example, {@code ["3H", "10H", "5H", "13H", "2H"]} has 5 points from
	 * flushes
	 * 
	 * @param hand a valid cribbage hand
	 * @return the number of points obtained from flushes
	 */
	public static int flushes(String[] hand) {
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
	 * Returns the number of points obtained from nobs in a given cribbage hand
	 * 
	 * <p>
	 * One point is obtained from nobs if the player's hand has a jack of the same
	 * suit as the starter card
	 * 
	 * <p>
	 * For example, {@code ["3H", "11S", "5S", "13C", "2S"]} has one point from nobs
	 * because the jack in the player's hand has the same suit (spades) ad the
	 * starter card
	 * 
	 * @param hand a valid cribbage hand
	 * @return the number of points obtained from nobs
	 */
	public static int nobs(String[] hand) {
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

	/**
	 * Turns a cribbage hand into a sorted array of each card's int value
	 *
	 * @param hand a valid cribbage hand
	 * @param trueValues false if all face cards should be given the value 10 (for
	 *                   fifteens), true for face cards to keep their 'value' (11,
	 *                   12 and 13 for jack, queen, king respectively)
	 * @return a sorted array of the card values
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
}