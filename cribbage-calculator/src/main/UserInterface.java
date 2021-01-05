/**
 * 
 */
package main;

import java.util.HashSet;

/**
 * @author Reid Moffat
 *
 */
final class UserInterface {
	
	/* 
	 * Stores a string representation of all cards in a standard
	 * 52-card deck
	 * 
	 * Each card is represented by its value (ace = 1, 
	 * 2-10 = their respective value, jack = 11, queen = 12,
	 * king = 13) followed by the suit (C, D, H or S). For
	 * example, '13S' is the king of spades and '5H' is the king
	 * of hearts
	 */
	private HashSet<String> cardPile;
	
	public UserInterface() {
		this.cardPile = new HashSet<String>();
		this.initialzeDeck();
	}
	
	private void initialzeDeck() {
		final char[] suits = {'C', 'D', 'H', 'S'};
		for (int value = 1; value < 14; value++) {
			for (int suit = 0; suit < suits.length; suit++) {
				String card = String.valueOf(value) + String.valueOf(suits[suit]);
				this.cardPile.add(card);
			}
		}
	}
	
	public static void main(String[] args) {
		UserInterface UI = new UserInterface();
		
		// When checking for points, make sure the starter card isn't the same as
		// any of the four cards in the hand
	}

}
