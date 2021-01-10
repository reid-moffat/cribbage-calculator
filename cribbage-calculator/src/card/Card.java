package card;

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
	 * This card's rank (ACE, TWO, THREE, ..., QUEEN, KING)
	 */
	private Rank rank;

	/**
	 * This card's suit (CLUBS, DIAMONDS, HEARTS or SPADES)
	 */
	private Suit suit;

	/**
	 * Initializes the card wit a rank and suit
	 * 
	 * @param rank the card's rank
	 * @param suit the card's suit
	 */
	public Card(Rank rank, Suit suit) {
		this.rank = rank;
		this.suit = suit;
	}

	/**
	 * Checks if all the values of the supplied card arguments add up to 15
	 * 
	 * @param cards a variable amount of card objects
	 * @return true if the card values add up to 15, false if not
	 * @throws IllegalArgumentException if 0, 1 or more than 5 card arguments are
	 *                                  supplied
	 */
	public static boolean isFifteen(Card... cards) {
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
		return sum == 15;
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
		return this.getRankValue() - other.getRankValue();
	}

	/**
	 * Returns this card's rank (not the same as value)
	 * 
	 * @return this card's rank
	 */
	private Rank getRank() {
		return this.rank;
	}

	/**
	 * Returns this card's suit
	 * 
	 * @return this card's suit
	 */
	private Suit getSuit() {
		return this.suit;
	}

	/**
	 * Returns the cribbage value of this card
	 * 
	 * @return the cribbage value of this card
	 */
	private int getValue() {
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
	 * @return the rank place of this card
	 */
	public int getRankValue() {
		return this.rank.getRankValue();
	}

}
