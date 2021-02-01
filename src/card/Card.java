package card;

import java.util.Objects;

/**
 * A class that represents cards in the standard 52-card playing card deck
 * 
 * <p>
 * Each card has a {@code Rank} and {@code Suit}
 * 
 * <p>
 * A constant, ordered array of all card {@code Ranks} and {@code Suits} can be
 * obtained with {@code Card.RANKS} and {@code Card.SUITS} respectively
 * 
 * @author Reid Moffat
 */
public final class Card implements PlayingCard {

	/**
	 * An array of all card ranks in order
	 * 
	 * <p>
	 * Ranks are ordered with aces low
	 */
	public static final Rank[] RANKS = Rank.values();

	/**
	 * An array of all card suits in alphabetical order
	 */
	public static final Suit[] SUITS = Suit.values();

	/**
	 * This card's rank (ACE, TWO, THREE, ..., QUEEN or KING)
	 */
	private Rank rank;

	/**
	 * This card's suit (CLUBS, DIAMONDS, HEARTS or SPADES)
	 */
	private Suit suit;

	/**
	 * Initializes the card with a rank and suit
	 * 
	 * @param rank the card's {@code Rank}
	 * @param suit the card's {@code Suit}
	 */
	public Card(Rank rank, Suit suit) {
		this.rank = rank;
		this.suit = suit;
	}

	/**
	 * Sets the rank and suit of this card
	 * 
	 * @param rank the card's new {@code Rank}
	 * @param suit the card's new {@code Suit}
	 */
	@Override
	public void setState(Rank rank, Suit suit) {
		this.rank = rank;
		this.suit = suit;
	}

	/**
	 * Returns this card's rank
	 * 
	 * @return this card's rank
	 */
	@Override
	public Rank getRank() {
		return this.rank;
	}

	/**
	 * Returns this card's suit
	 * 
	 * @return this card's suit
	 */
	@Override
	public Suit getSuit() {
		return this.suit;
	}

	/**
	 * Returns the rank number of this card
	 * 
	 * <p>
	 * Rank numbers are as follows:
	 * 
	 * <ol>
	 * <li><code>ACE</code>
	 * <li><code>TWO</code>
	 * <li><code>THREE</code>
	 * <li><code>FOUR</code>
	 * <li><code>FIVE</code>
	 * <li><code>SIX</code>
	 * <li><code>SEVEN</code>
	 * <li><code>EIGHT</code>
	 * <li><code>NINE</code>
	 * <li><code>TEN</code>
	 * <li><code>JACK</code>
	 * <li><code>QUEEN</code>
	 * <li><code>KING</code>
	 * </ol>
	 * 
	 * @return the rank place of this card
	 */
	public int getRankNumber() {
		return this.rank.getRankNumber();
	}

	/**
	 * Returns the number of ranks the current card is above the other card
	 * 
	 * <p>
	 * Aces are low, and negative values mean the card rank is below
	 * 
	 * <p>
	 * Examples:
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
	 * Returns a hash code value for this {@code Card}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(rank, suit);
	}

	/**
	 * Returns true if both {@code Cards} have the same {@code Rank} and
	 * {@code Suit}
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
	 * Returns the English description this card
	 * 
	 * <p>
	 * The structure of the string is "<i>Rank</i> of <i>suit</i>"
	 * 
	 * <p>
	 * Examples:
	 * <ul>
	 * <li>"Ace of spades"</li>
	 * <li>"Three of clubs"</li>
	 * <li>"Eight of hearts"</li>
	 * <li>"Queen of diamonds"</li>
	 * </ul>
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String rankString = rank.toString();
		sb.append(rankString.substring(0, 1)); // Keep the first letter capitalized
		sb.append(rankString.substring(1, rankString.length()).toLowerCase());
		sb.append(" of " + suit.toString().toLowerCase());
		return sb.toString();
	}

}
