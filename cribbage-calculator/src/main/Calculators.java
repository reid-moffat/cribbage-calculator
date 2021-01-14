package main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	 * Adds all of the point combinations together for a valid cribbage hand plus
	 * starter card (the card that is flipped up), then returns the total points
	 * 
	 * <p>
	 * A valid cribbage hand is defined as a {@code HashSet} containing four
	 * {@code Card} objects
	 * 
	 * @param hand    a valid cribbage hand
	 * @param starter the starter card
	 * @return the total number of points in this cribbage hand with the given
	 *         starter card
	 * @throws IllegalArgumentException if {@code hand} does not contain exactly
	 *                                  four {@code Card} objects or {@code starter}
	 *                                  is {@code null}
	 */
	public static int totalPoints(HashSet<Card> hand, Card starter) {
		if (!legalHand(hand, starter)) {
			throw new IllegalArgumentException("illegal hand and/or starter card");
		}
		HashSet<Card> handWithStarter = new HashSet<Card>(hand);
		handWithStarter.add(starter);

		int points = 0;
		points += fifteens(handWithStarter);
		points += multiples(handWithStarter);
		points += runs(handWithStarter);
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
	private static boolean legalHand(HashSet<Card> hand, Card starter) {
		return !(hand == null || starter == null || hand.size() != 4);
	}

	/**
	 * Returns the number of points obtained from fifteens in the given cribbage
	 * hand (with the starter card included)
	 * 
	 * <p>
	 * Each combination of cards that add up to 15 is worth two points. Any number
	 * of cards can be used, and cards may be used for multiple fifteens. All face
	 * cards are worth 10 points when calculating fifteens
	 * 
	 * @param hand a valid cribbage hand
	 * @return the number of points obtained from fifteens
	 */
	public static int fifteens(HashSet<Card> cards) {
		int score = 0;

		/* Check if all five add up */
		score += isFifteen(cards);

		/* Check if four add up */
		for (Card c : cards) {
			score += isFifteen(removeCards(cards, c));
		}

		/* Check if three or two add up */
		HashSet<Card[]> combinationsOf2 = subset2(cards);
		for (Card[] combination : combinationsOf2) {
			score += isFifteen(removeCards(cards, combination)); // 3 cards
			score += isFifteen(new HashSet<Card>(Arrays.asList(combination))); // 2 cards
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
	private static int isFifteen(HashSet<Card> cards) {
		checkNumCards(cards, 2, 5);
		return cards.stream().mapToInt(Card::getValue).sum() == 15 ? 2 : 0;
	}

	/**
	 * Returns the number of points obtained from multiples in the given cribbage
	 * hand (with the starter card included)
	 * 
	 * <p>
	 * Multiples are a double (2 points), triple (6 points) or quadruple (12 points)
	 * of one value of card. Face cards are not considered the same for multiples; a
	 * ten and a queen both have a value of 10 but they would not give points for a
	 * double
	 * 
	 * @param hand a valid cribbage hand
	 * @return the number of points obtained from multiples
	 */
	public static int multiples(HashSet<Card> cards) {
		return countDuplicates(cards).values().stream().mapToInt(v -> v * v - v).sum();
	}

	/**
	 * Returns the number of points obtained from runs in a given cribbage hand
	 * (with the starter card included)
	 * 
	 * <p>
	 * A run is a sequence of three (3 points), four (4 points) or five (5 points)
	 * consecutive cards. Suit does not matter, and cards can be part of multiple
	 * runs
	 * 
	 * @param hand a valid cribbage hand
	 * @return the number of points obtained from runs
	 */
	public static int runs(HashSet<Card> cards) {
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
	 * and starter card
	 * 
	 * <p>
	 * To obtain a flush, the player's hand must have all four cards of the same
	 * suit (4 points). If the starter card is also the same suit, 5 points are
	 * obtained. Note that if only three cards in the player's hand plus the starter
	 * card have the same suit, this is not a flush
	 * 
	 * @param hand    a valid cribbage hand
	 * @param starter the starter card
	 * @return the number of points obtained from flushes
	 */
	public static int flushes(HashSet<Card> hand, Card starter) {
		HashSet<Suit> uniqueSuits = new HashSet<Suit>(
				hand.stream().map(card -> card.getSuit()).collect(Collectors.toSet()));
		return uniqueSuits.size() == 1 ? 4 + (uniqueSuits.add(starter.getSuit()) == false ? 1 : 0) : 0;
	}

	/**
	 * Returns the number of points obtained from nobs in a given cribbage hand and
	 * starter card
	 * 
	 * <p>
	 * One point is obtained from nobs if the player's hand has a jack of the same
	 * suit as the starter card
	 * 
	 * @param hand    a valid cribbage hand
	 * @param starter the starter card
	 * @return the number of points obtained from nobs
	 */
	public static int nobs(HashSet<Card> hand, Card starter) {
		return hand.stream().filter(c -> c.getRank() == Rank.JACK).map(c -> c.getSuit())
				.anyMatch(s -> s.equals(starter.getSuit())) ? 1 : 0;
	}

	/**
	 * Returns a new {@code HashSet} without the specified cards
	 * 
	 * <p>
	 * Cards that are not in the set are ignored, attempting to remove a
	 * {@code Card} that is not in the set will not throw an exception
	 * 
	 * @param hand  a HashSet of cards
	 * @param cards variable amount of cards to be removed
	 * @return a new HashSet without the specified cards
	 */
	public static HashSet<Card> removeCards(HashSet<Card> hand, Card... cards) {
		Stream<Card> cardStream = Arrays.asList(cards).stream();
		return new HashSet<Card>(
				hand.stream().filter(card -> !cardStream.anyMatch(c -> c.equals(card))).collect(Collectors.toSet()));
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
	private static HashMap<Integer, Integer> countDuplicates(HashSet<Card> cards) {
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

	/**
	 * Throws an exception if {@code cards} does not contain the required number of
	 * cards
	 * 
	 * @param cards a {@code HashSet} of {@code Card} objects
	 * @param min   the minimum number of {@code Card} objects that should be in
	 *              {@code cards}
	 * @param max   the maximum number of {@code Card} objects that should be in
	 *              {@code cards}
	 */
	private static void checkNumCards(HashSet<Card> cards, int min, int max) {
		if (cards.size() < min || cards.size() > max) {
			String errMsg = "between " + min + " and " + max + " cards must be supplied";
			throw new IllegalArgumentException(errMsg);
		}
	}

	/**
	 * Generates all 2-element subsets for {@code cards}
	 * 
	 * @param cards a {@code HashSet} of {@code Card} objects
	 * @return a {@code HashSet} of 2-element subsets (stored as {@code Card[]})
	 */
	public static HashSet<Card[]> subset2(HashSet<Card> cards) {
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

}