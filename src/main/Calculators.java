package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import card.Card;
import card.Rank;
import card.Suit;

/**
 * The utility class {@code Calculators} contains methods for calculating the
 * score of a cribbage hand with a starter card
 * 
 * <p>
 * Passing a HashSet of four cards and a starter card in {@code totalPoints()}
 * will return the total number of cribbage points in the given hand
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
	 * Calculates the sum of point combinations for a valid cribbage hand plus
	 * starter card
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
		if (hand == null || starter == null || hand.size() != 4) {
			throw new IllegalArgumentException("illegal hand and/or starter card");
		}
		HashSet<Card> handWithStarter = new HashSet<Card>(hand);
		handWithStarter.add(starter);

		/*
		 * Although calculating a power set is O(2^n), there are always only five cards
		 * so this method is still efficient
		 * 
		 * Using a power set significantly reduces the amount of test cases, allowing
		 * methods to be implemented much more concisely using only a few lines in most
		 * cases
		 */
		HashSet<HashSet<Card>> combinations = powerSet(handWithStarter);

		// @formatter:off
		int points = fifteens(combinations)
		           + multiples(handWithStarter)
		           + runs(combinations)
		           + flushes(hand, starter)
		           + nobs(hand, starter);
		// @formatter:on
		return points;
	}

	/**
	 * Returns the number of points obtained from fifteens
	 * 
	 * <p>
	 * Each combination of cards that add up to 15 is worth two points. Any number
	 * of cards can be used, and cards may be used for multiple fifteens. All face
	 * cards are worth 10 when calculating fifteens
	 * 
	 * @param cardCombinations the power set of a set containing a cribbage hand and
	 *                         starter card
	 * @return the number of points obtained from fifteens
	 */
	private static int fifteens(HashSet<HashSet<Card>> cardCombinations) {
		return cardCombinations.stream().filter(c -> c.size() >= 2).mapToInt(c -> isFifteen(c)).sum();
	}

	/**
	 * Checks if all the rank values of the {@code Card} objects add up to 15
	 * 
	 * @param cards a {@code HashSet} of {@code Card} objects
	 * @return 2 if the card values add up to 15, 0 if not
	 */
	private static int isFifteen(HashSet<Card> cards) {
		return cards.stream().mapToInt(Card::getValue).sum() == 15 ? 2 : 0;
	}

	/**
	 * Returns the number of points obtained from multiples in the given cribbage
	 * hand (with the starter card included)
	 * 
	 * <p>
	 * Multiples are a double (2 points), triple (6 points) or quadruple (12 points)
	 * of one rank of card. Face cards are not considered the same for multiples; a
	 * ten and a queen both have a value of 10 but they would not give points for a
	 * double
	 * 
	 * @param cards a {@code HashSet} of {@code Card} objects
	 * @return the number of points obtained from multiples
	 */
	private static int multiples(HashSet<Card> cards) {
		// @formatter:off
		/**
		 * Counting points from each multiple is simple because a multiple of n cards is
		 * worth n*n - n points:
		 * 
		 * Single: 1*1-1 = 0 points
		 * Double: 2*2-2 = 2 points
		 * Triple: 3*3 - 3 = 3 points
		 * Quadruple: 4*4 - 4 = 12 points
		 */
		// @formatter:on
		return countDuplicates(cards).values().stream().mapToInt(v -> v * v - v).sum();
	}

	/**
	 * Returns the number of points obtained from runs
	 * 
	 * <p>
	 * A run is a sequence of three (3 points), four (4 points) or five (5 points)
	 * cards with consecutive ranks. Suit does not matter, and cards can be part of
	 * multiple runs
	 * 
	 * @param cardCombinations the power set of a set containing a cribbage hand and
	 *                         starter card
	 * @return the number of points obtained from runs
	 */
	private static int runs(HashSet<HashSet<Card>> cardCombinations) {
		// @formatter:off
		/*
		 * Each card can be part of multiple runs of the same length, but not not
		 * multiple runs of different lengths. The longest run always trumps lower length runs
		 * 
		 * Take a hand containing cards with the ranks 2-3-3-4-5 for example:
		 * -There are eight points from runs: 2-3-4-5 and 2-3-4-5 (with the other 3)
		 * -The combination 2-3-4 is not counted as a run because 2-3-4-5 trumps it
		 */
		// @formatter:on
		int score = cardCombinations.stream().filter(c -> c.size() == 5).mapToInt(Calculators::isRun).sum();
		if (score == 0) {
			score += cardCombinations.stream().filter(c -> c.size() == 4).mapToInt(Calculators::isRun).sum();
			if (score == 0) {
				score += cardCombinations.stream().filter(c -> c.size() == 3).mapToInt(Calculators::isRun).sum();
			}
		}
		return score;
	}

	/**
	 * Checks if the supplied cards are consecutive to form a run
	 * 
	 * @param cards a {@code HashSet} of {@code Card} objects
	 * @return 0 if the cards don't form a run, the length of the run (3-5) if the
	 *         cards do form a run
	 */
	private static int isRun(HashSet<Card> cards) {
		// Creates a sorted list of card rank numbers (ex: [2, 5, 5, 11, 13])
		ArrayList<Integer> values = new ArrayList<Integer>(
				cards.stream().mapToInt(card -> card.getRankNumber()).sorted().boxed().collect(Collectors.toList()));
		// If all of the rank numbers are consecutive, the length of the run (# of
		// points) is returned
		return IntStream.range(0, values.size() - 1).anyMatch(i -> values.get(i + 1) != values.get(i) + 1) ? 0
				: values.size();
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
	private static int flushes(HashSet<Card> hand, Card starter) {
		HashSet<Suit> uniqueSuits = new HashSet<Suit>(hand.stream().map(Card::getSuit).collect(Collectors.toSet()));
		return uniqueSuits.size() == 1 ? 4 + (uniqueSuits.add(starter.getSuit()) ? 0 : 1) : 0;
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
	private static int nobs(HashSet<Card> hand, Card starter) {
		return hand.stream().filter(c -> c.getRank() == Rank.JACK).map(Card::getSuit)
				.anyMatch(starter.getSuit()::equals) ? 1 : 0;
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
	 * <li><code>13: 3</code></li>
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
	 * Returns the power set of a given set
	 * 
	 * <p>
	 * Adapted from <a href=
	 * "https://stackoverflow.com/questions/1670862/obtaining-a-powerset-of-a-set-in-java">stack
	 * overflow</a>
	 * 
	 * @param originalSet a {@code HashSet} of objects
	 * @param <T>         the type of objects in {@code originalSet}
	 * @return a {@code HashSet} containing all subsets of {@code originalSet}
	 */
	private static <T> HashSet<HashSet<T>> powerSet(HashSet<T> originalSet) {
		HashSet<HashSet<T>> sets = new HashSet<HashSet<T>>();
		if (originalSet.isEmpty()) {
			sets.add(new HashSet<T>());
			return sets;
		}
		List<T> list = new ArrayList<T>(originalSet);
		T head = list.get(0);
		HashSet<T> rest = new HashSet<T>(list.subList(1, list.size()));
		for (HashSet<T> set : powerSet(rest)) {
			HashSet<T> newSet = new HashSet<T>();
			newSet.add(head);
			newSet.addAll(set);
			sets.add(newSet);
			sets.add(set);
		}
		return sets;
	}

}