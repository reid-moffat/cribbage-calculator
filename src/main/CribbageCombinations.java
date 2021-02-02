package main;

import java.util.HashSet;

import card.Card;

/**
 * An interface that includes the required methods for a class that can:
 * 
 * <ul>
 * <li>Store a cribbage hand</li>
 * <li>Add or remove cards from the hand</li>
 * <li>Clear and replace cards in the hand</li>
 * <li>Return the size of the hand</li>
 * <li>Return a copy of the hand</li>
 * <li>Caclulate the number of cribbage points in the hand with a starter
 * card</li>
 * </ul>
 * 
 * @author Reid Moffat
 */
public interface CribbageCombinations {
	/**
	 * Sets the cribbage hand to a copy of the specified hand
	 * 
	 * @param hand a {@code Set} of {@code Card} objects
	 */
	public void setHand(HashSet<Card> hand);

	/**
	 * Removes all {@code Card} objects from this hand
	 */
	public void clearHand();

	/**
	 * Adds a {@code Card} object to this hand
	 * 
	 * @param card a {@code Card} object
	 */
	public void add(Card card);

	/**
	 * Removes a {@code Card} object from this hand
	 * 
	 * @param card a {@code Card} object
	 */
	public void remove(Card card);

	/**
	 * Returns the number of {@code Card} objects in this hand
	 * 
	 * @return the number of {@code Card} objects in this hand
	 */
	public int size();

	/**
	 * Returns a copy of this hand
	 * 
	 * @return a copy of this hand
	 */
	public HashSet<Card> getCards();

	/**
	 * Calculates the sum of point combinations for a valid cribbage hand plus
	 * starter {@code Card}
	 * 
	 * @param starter the starter {@code Card}
	 * @return the total number of points in this cribbage hand with the given
	 *         starter {@code Card}
	 */
	public int totalPoints(Card starter);
}
