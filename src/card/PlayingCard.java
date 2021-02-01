package card;

/**
 * An interface for a playing card class. Includes methods to compare cards,
 * modify the card as well as get the card's rank, suit and rank number
 * 
 * @author Reid Moffat
 */
public interface PlayingCard extends Comparable<Card> {

	/**
	 * Sets the rank and suit of this card
	 * 
	 * @param rank the card's new rank
	 * @param suit the card's new suit
	 */
	public void setState(Rank rank, Suit suit);

	/**
	 * Returns this card's rank
	 * 
	 * @return this card's rank
	 */
	public Rank getRank();

	/**
	 * Returns this card's suit
	 * 
	 * @return this card's suit
	 */
	public Suit getSuit();

	/**
	 * Returns the rank place of this card
	 * 
	 * @return the rank place of this card
	 */
	public int getRankNumber();
}
