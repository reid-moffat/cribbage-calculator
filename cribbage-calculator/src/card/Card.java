package card;

import java.util.Objects;

/**
 * A class that represents cards in the standard 52-card playing card deck
 * 
 * <p>
 * This class is made specifically for cribbage, and as such each card has
 * cribbage values:
 * 
 * <ul>
 * <li><code>Ace: 1</code>
 * <li><code>Two to Nine: Their respective values</code>
 * <li><code>Ten and face cards: 10</code>
 * </ul>
 * 
 * @author Reid Moffat
 */
public final class Card implements Comparable<Card> {

	/**
	 * An array of all card ranks in order
	 */
	public static final Rank[] RANKS = Rank.values();

	/**
	 * An array of all card suits in alphabetical order
	 */
	public static final Suit[] SUITS = Suit.values();

	/**
	 * This card's rank (ACE, TWO, THREE, ..., QUEEN, KING)
	 */
	private Rank rank;

	/**
	 * This card's suit (CLUBS, DIAMONDS, HEARTS or SPADES)
	 */
	private Suit suit;

	/**
	 * Creates an 'empty' card object, without a rank or suit
	 */
	public Card() {
	}

	/**
	 * Initializes the card with a rank and suit
	 * 
	 * @param rank the card's rank
	 * @param suit the card's suit
	 */
	public Card(Rank rank, Suit suit) {
		this.rank = rank;
		this.suit = suit;
	}

	/**
	 * Sets the rank and suit of this card
	 * 
	 * @param rank the card's new rank
	 * @param suit the card's new suit
	 */
	public void setState(Rank rank, Suit suit) {
		this.rank = rank;
		this.suit = suit;
	}

	/**
	 * Returns the number of ranks the current card is above the other card
	 * 
	 * <p>
	 * For example,
	 * 
	 * <ul>
	 * <li>Comparing a jack to a five returns 6</li>
	 * <li>Comparing an ace to a six returns -5</li>
	 * <li>Comparing a nine to a king returns -4</li>
	 * </ul>
	 * 
	 * @return the number of ranks the current card is above the other card
	 */
	@Override
	public int compareTo(Card other) {
		return this.getRankNumber() - other.getRankNumber();
	}

	/**
	 * Returns this card's rank
	 * 
	 * <p>
	 * Rank is not the same as value, a Jack of hearts has the rank jack and a value
	 * of 10 for example
	 * 
	 * @return this card's rank
	 */
	public Rank getRank() {
		return this.rank;
	}

	/**
	 * Returns this card's suit
	 * 
	 * @return this card's suit
	 */
	public Suit getSuit() {
		return this.suit;
	}

	/**
	 * Returns the cribbage value of this card
	 * 
	 * <p>
	 * Used to calculate fifteens
	 * 
	 * @return the cribbage value of this card
	 */
	public int getValue() {
		return this.rank.getValue();
	}

	/**
	 * Returns the rank place of this card
	 * 
	 * <p>
	 * Rank places are as follows:
	 * 
	 * <ul>
	 * <li>Ace: 1</li>
	 * <li>Two to ten: Their respective values</li>
	 * <li>Jack: 11</li>
	 * <li>Queen: 12</li>
	 * <li>King: 13</li>
	 * </ul>
	 * 
	 * <p>
	 * Used to check for runs and multiples
	 * 
	 * @return the rank place of this card
	 */
	public int getRankNumber() {
		return this.rank.getRankNumber();
	}

	/**
	 * 
	 */
	@Override
	public int hashCode() {
		return Objects.hash(rank, suit);
	}

	/**
	 * Returns true if both cards have the same rank and suit
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Card)) {
			return false;
		}
		Card other = (Card) obj;
		return rank == other.rank && suit == other.suit;
	}

	/**
	 * Returns the a string with the English meaning of a card
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String rankString = rank.toString();
		sb.append(rankString.substring(0, 1));
		sb.append(rankString.substring(1, rankString.length()).toLowerCase());
		sb.append(" of " + suit.toString().toLowerCase());
		return sb.toString();
	}

}
