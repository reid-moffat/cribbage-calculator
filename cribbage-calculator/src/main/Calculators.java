package main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

import card.Card;
import card.Rank;
import card.Suit;

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
	public static int totalPoints(Card[] hand, Card starter) {
		if (!legalHand(hand, starter)) {
			throw new IllegalArgumentException("illegal hand and/or starter card");
		}
		Card[] fullHand = addStarter(hand, starter);

		int points = 0;
		points += fifteens(fullHand);
		points += multiples(fullHand);
		points += runs(fullHand);
		points += flushes(hand, starter);
		points += nobs(hand, starter);
		return points;
	}

	/**
	 * Returns true if the hand and starter are valid for a cribbage hand
	 * 
	 * <p>
	 * To be valid, hand and starter must not be null references and hand must have
	 * four card objects
	 * 
	 * @param hand    an array of card objects
	 * @param starter a card object
	 * @return true if the hand and starter are valid for a cribbage hand, false
	 *         otherwise
	 */
	private static boolean legalHand(Card[] hand, Card starter) {
		if (hand == null || starter == null) {
			return false;
		}
		if (hand.length != 4) {
			return false;
		}
		return true;
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
	public static int fifteens(Card[] cards) {
		int score = 0;

		/* Check if all five add up */
		score += isFifteen(cards);

		/* Check if four add up */
		for (int i = 0; i < 5; i++) {
			score += isFifteen(removeCards(cards, i));
		}

		/* Check if three add up */
		for (int i = 0; i < 4; i++) {
			for (int j = i + 1; j < 5; j++) {
				score += isFifteen(removeCards(cards, i, j));
			}
		}

		/* Check if two add up */
		for (int i = 0; i < 4; i++) {
			for (int j = i + 1; j < 5; j++) {
				Card[] vals = { cards[i], cards[j] };
				score += isFifteen(vals);
			}
		}

		return score;
	}

	/**
	 * Checks if all the values of the supplied card arguments add up to 15
	 * 
	 * @param cards an array card objects
	 * @return 2 if the card values add up to 15, 0 if not
	 * @throws IllegalArgumentException if the array of cards has 0, 1 or more than
	 *                                  5 cards
	 */
	private static int isFifteen(Card[] cards) {
		if (cards.length < 2) {
			throw new IllegalArgumentException("you must supply at least two card arguments");
		}
		if (cards.length > 5) {
			throw new IllegalArgumentException("you cannot have more than five cards");
		}

		int sum = 0;
		for (Card c : cards) {
			sum += c.getValue();
		}
		return sum == 15 ? 2 : 0;
	}

	/**
	 * Returns the number of points obtained from multiples in the given cribbage
	 * hand
	 * 
	 * <p>
	 * Multiples are a double (2 points), triple (6 points) or quadruple (12 points)
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
	public static int multiples(Card[] cards) {
		HashMap<Integer, Integer> values = countDuplicates(cards);

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
	public static int runs(Card[] cards) {
		/* The number of occurrences for each card rank number */
		HashMap<Integer, Integer> duplicates = countDuplicates(cards);

		/* All unique card rank numbers */
		Integer[] uniques = Arrays.copyOf(duplicates.values().toArray(), duplicates.size(), Integer[].class);
		Arrays.sort(uniques);

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
	 * Checks if the supplied cards are consecutive to form a run
	 * 
	 * @param values a list of card values
	 * @return 0 if the cards don't form a run, the length of the run (3-5) if the
	 *         cards do form a run
	 * @throws IllegalArgumentException if the array of cards has 0, 1 or more than
	 *                                  5 cards
	 */
	private static int isRun(Integer[] values) {
		if (values.length < 2) {
			throw new IllegalArgumentException("you must supply at least two card arguments");
		}
		if (values.length > 5) {
			throw new IllegalArgumentException("you cannot have more than five cards");
		}

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
	public static int flushes(Card[] hand, Card starter) {
		HashSet<Suit> uniqueSuits = new HashSet<Suit>(
				Arrays.asList(hand).stream().map(card -> card.getSuit()).collect(Collectors.toSet()));
		return uniqueSuits.size() == 1 ? 4 + (uniqueSuits.add(starter.getSuit()) == false ? 1 : 0) : 0;
	}

	/**
	 * Returns the number of points obtained from nobs in a given cribbage hand
	 * 
	 * <p>
	 * One point is obtained from nobs if the player's hand has a jack of the same
	 * suit as the starter card
	 * 
	 * @param hand    a valid cribbage hand
	 * @param starter the starter card
	 * @return the number of points obtained from nobs
	 */
	public static int nobs(Card[] hand, Card starter) {
		HashSet<Suit> jackSuits = new HashSet<Suit>(Arrays.asList(hand).stream().filter(c -> c.getRank() == Rank.JACK)
				.map(c -> c.getSuit()).collect(Collectors.toSet()));
		return jackSuits.contains(starter.getSuit()) ? 1 : 0;
	}

	/**
	 * 
	 * 
	 * @param hand
	 * @param starter
	 * @return
	 */
	private static Card[] addStarter(Card[] hand, Card starter) {
		Card[] arr = new Card[5];
		for (int i = 0; i < 4; i++) {
			arr[i] = hand[i];
		}
		arr[4] = starter;
		return arr;
	}

	/**
	 * Returns an array of cards without the specified card from the player hand
	 * 
	 * @param index the index of the card to be removed
	 * @return an array of cards without the specified card from the player hand
	 */
	public static Card[] removeCards(Card[] hand, int... index) {
		Card[] copy = new Card[hand.length - index.length];
		HashSet<Integer> indicies = new HashSet<Integer>();
		for (int i : index) {
			indicies.add(i);
		}

		for (int i = 0, j = 0; i < hand.length; i++) {
			if (!indicies.contains(i)) {
				copy[j] = hand[i];
				j++;
			}
		}

		return copy;
	}

	/**
	 * Maps each card rank number in cards to the number of occurrences of that rank
	 * number
	 * 
	 * <p>
	 * For example, three kings, an ace and four would have the key value pairs:
	 * 
	 * <ul>
	 * <li><code>1: 1</code></li>
	 * <li><code>4: 1</code></li>
	 * <li><code>13: 1</code></li>
	 * </ul>
	 * 
	 * @param cards an array of card objects
	 * @return a HashMap that maps each rank number to the number of occurrences
	 */
	private static HashMap<Integer, Integer> countDuplicates(Card[] cards) {
		HashMap<Integer, Integer> duplicates = new HashMap<Integer, Integer>();

		for (Card c : cards) {
			Integer cardRankNumber = c.getRankNumber();
			if (duplicates.containsKey(cardRankNumber)) {
				Integer oldAmount = duplicates.get(cardRankNumber);
				duplicates.replace(cardRankNumber, Integer.valueOf(oldAmount + 1));
			} else {
				duplicates.put(cardRankNumber, Integer.valueOf(1));
			}
		}

		return duplicates;
	}

}