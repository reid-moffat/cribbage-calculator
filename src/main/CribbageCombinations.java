package main;

import java.util.HashSet;

import card.Card;

/**
 * Includes abstract methods for each cribbage point combination in a hand:
 * 
 * <ul>
 * <li>Fifteens</li>
 * <li>Multiples</li>
 * <li>Runs</li>
 * <li>Flushes</li>
 * <li>Nobs</li>
 * </ul>
 * 
 * As well as a method to calculate the total points in a hand
 * 
 * @author Reid Moffat
 */
public interface CribbageCombinations {
	public void setHand(HashSet<Card> hand);
	
	public void add(Card card);
	
	public void clear();
	
	public void remove(Card card);
	
	public HashSet<Card> getCards();
	
	public int totalPoints(Card starter);
}
