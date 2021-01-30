package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import card.Card;
import card.Rank;
import card.Suit;

/**
 * The class {@code CribbageHand} represents a player's hand in the game of
 * cribbage
 * 
 * <p>
 * Initializing this class requires a {@code HashSet} of {@code Card} objects.
 * The hand's cards can be modified using the methods {@code setHand},
 * {@code add}, {@code clear} and {@code remove}
 * 
 * <p>
 * The total points obtained from this cribbage hand can be determined by
 * passing a starter {@code Card} object through {@code totalPoints}
 * 
 * @author Reid Moffat
 */
final class CribbageHand implements CribbageCombinations {

	/**
	 * A set of unique playing {@code Cards}. Must include 4 cards for points to be
	 * calculated
	 * 
	 * <p>
	 * Does not include the starter {@code Card}
	 */
	private HashSet<Card> hand;

	/**
	 * The starter {@code Card}
	 * 
	 * <p>
	 * Kept separate from the {@code Card} hand due to points from runs and nobs
	 */
	private Card starter;

	/**
	 * A set that includes the hand of four {@code Cards} and the starter card
	 */
	private HashSet<Card> handWithStarter;

	/**
	 * Every combination of {@code Card} objects in the hand and starter
	 * {@code Card}
	 * 
	 * <p>
	 * More formally, the power set of {@code handWithStarter}
	 */
	private HashSet<HashSet<Card>> cardCombinations;

	/**
	 * Initializes this {@code CribbageHand} with a set of {@code Cards}
	 * 
	 * @param hand a {@code Set} of {@code Card} objects (not including the starter
	 *             card)
	 */
	public CribbageHand(HashSet<Card> hand) {
		this.hand = hand;
	}

	/**
	 * Sets the cribbage hand to a copy of the specified hand
	 * 
	 * @param hand a {@code Set} of {@code Card} objects
	 */
	@Override
	public void setHand(HashSet<Card> hand) {
		this.hand = new HashSet<Card>(hand);
	}

	/**
	 * Adds a {@code Card} object to this hand
	 * 
	 * @param card a {@code Card} object
	 */
	@Override
	public void add(Card card) {
		this.hand.add(card);
	}

	/**
	 * Removes all {@code Card} objects from this hand
	 */
	@Override
	public void clear() {
		this.hand.clear();
	}

	/**
	 * Removes a {@code Card} object from this hand
	 * 
	 * @param card a {@code Card} object
	 */
	@Override
	public void remove(Card card) {
		if (!this.hand.remove(card)) {
			throw new IllegalArgumentException("card not present in hand");
		}
	}

	/**
	 * Returns the number of {@code Card} objects in this hand
	 * 
	 * @return the number of {@code Card} objects in this hand
	 */
	@Override
	public int size() {
		return this.hand.size();
	}

	/**
	 * Returns a copy of this hand
	 * 
	 * @return a copy of this hand
	 */
	@Override
	public HashSet<Card> getCards() {
		return new HashSet<Card>(hand);
	}

	/**
	 * Some fields needs to be refreshed if the hand changes before performing a
	 * total point calculation
	 * 
	 * <p>
	 * This method must always be called in totalPoints()
	 * 
	 * @param starter
	 */
	private void refreshHand(Card starter) {
		this.starter = starter;

		this.handWithStarter = new HashSet<Card>(hand);
		this.handWithStarter.add(this.starter);

		/*
		 * Although calculating a power set is O(2^n), there are always only five cards
		 * so this method is still efficient
		 * 
		 * Using a power set significantly reduces the amount of test cases, allowing
		 * methods to be implemented much more concisely using only a few lines in most
		 * cases
		 */
		this.cardCombinations = powerSet(handWithStarter);
	}

	/**
	 * Calculates the sum of point combinations for a valid cribbage hand plus
	 * starter card
	 * 
	 * <p>
	 * A valid cribbage hand is defined as a {@code HashSet} containing four
	 * {@code Card} objects
	 * 
	 * @param starter the starter card
	 * @return the total number of points in this cribbage hand with the given
	 *         starter card
	 * @throws IllegalArgumentException if {@code hand} does not contain exactly
	 *                                  four {@code Card} objects or {@code starter}
	 *                                  is {@code null}
	 */
	@Override
	public int totalPoints(Card starter) {
		if (hand == null || starter == null || hand.size() != 4) {
			throw new IllegalArgumentException("illegal hand and/or starter card");
		}
		this.refreshHand(starter);

		return fifteens() + multiples() + runs() + flushes() + nobs();
	}

	/**
	 * Returns the number of points obtained from fifteens
	 * 
	 * <p>
	 * Each unique combination of cards that add up to 15 is worth two points. Any
	 * number of cards can be used for each combination, and cards may be used for
	 * multiple fifteens. All face cards are worth 10 when calculating fifteens
	 * 
	 * @return the number of points obtained from fifteens
	 */
	private int fifteens() {
		return this.cardCombinations.stream().mapToInt(this::isFifteen).sum();
	}

	/**
	 * Checks if all the rank values of the {@code Card} objects add up to 15
	 * 
	 * @param cards a {@code HashSet} of {@code Card} objects
	 * @return 2 if the card values add up to 15, 0 if not
	 */
	private int isFifteen(HashSet<Card> cards) {
		return cards.stream().mapToInt(Card::getValue).sum() == 15 ? 2 : 0;
	}

	/**
	 * Returns the number of points obtained from multiples in the given cribbage
	 * hand (with the starter card included)
	 * 
	 * <p>
	 * Multiples are a double (2 points), triple (6 points) or quadruple (12 points)
	 * of one rank of card. Face cards are not considered the same for multiples; a
	 * ten and a queen both have a rank value of 10 but they would not give points
	 * for a double
	 * 
	 * @return the number of points obtained from multiples
	 */
	private int multiples() {
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
		return countDuplicates(this.handWithStarter).values().stream().mapToInt(v -> v * v - v).sum();
	}

	/**
	 * Returns the number of points obtained from runs
	 * 
	 * <p>
	 * A run is a sequence of three (3 points), four (4 points) or five (5 points)
	 * cards with consecutive ranks. Suit does not matter, and cards can be part of
	 * multiple runs (but only the higest run is counted; a run of four is only four
	 * points, not two runs of three)
	 * 
	 * @return the number of points obtained from runs
	 */
	private int runs() {
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
		int score = this.cardCombinations.stream().filter(c -> c.size() == 5).mapToInt(this::isRun).sum();
		if (score == 0) {
			score += this.cardCombinations.stream().filter(c -> c.size() == 4).mapToInt(this::isRun).sum();
			if (score == 0) {
				score += this.cardCombinations.stream().filter(c -> c.size() == 3).mapToInt(this::isRun).sum();
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
	private int isRun(HashSet<Card> cards) {
		// Creates a sorted list of card rank numbers (ex: [2, 5, 5, 11, 13])
		ArrayList<Integer> values = new ArrayList<Integer>(
				cards.stream().mapToInt(Card::getRankNumber).sorted().boxed().collect(Collectors.toList()));
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
	 * @return the number of points obtained from flushes
	 */
	private int flushes() {
		HashSet<Suit> uniqueSuits = new HashSet<Suit>(
				this.hand.stream().map(Card::getSuit).collect(Collectors.toSet()));
		return uniqueSuits.size() == 1 ? 4 + (uniqueSuits.add(this.starter.getSuit()) ? 0 : 1) : 0;
	}

	/**
	 * Returns the number of points obtained from nobs in a given cribbage hand and
	 * starter card
	 * 
	 * <p>
	 * One point is obtained from nobs if the player's hand has a jack of the same
	 * suit as the starter card
	 * 
	 * @return the number of points obtained from nobs
	 */
	private int nobs() {
		return this.hand.stream().filter(c -> c.getRank() == Rank.JACK).map(Card::getSuit)
				.anyMatch(this.starter.getSuit()::equals) ? 1 : 0;
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
	private Map<Integer, Integer> countDuplicates(HashSet<Card> cards) {
		return cards.stream().collect(Collectors.groupingBy(Card::getRankNumber, Collectors.summingInt(x -> 1)));
	}

	/**
	 * Returns the power set of a given {@code HashSet}
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