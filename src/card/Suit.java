package card;

/**
 * The four suits in a standard deck of 52 playing cards ordered alphabetically:
 * 
 * <ol>
 * <li><code>CLUBS</code></li>
 * <li><code>DIAMONDS</code></li>
 * <li><code>HEARTS</code></li>
 * <li><code>SPADES</code></li>
 * </ol>
 * 
 */
public enum Suit {
	/**
	 * The suit clubs
	 */
	CLUBS,

	/**
	 * The suit diamonds
	 */
	DIAMONDS,

	/**
	 * The suit hearts
	 */
	HEARTS,

	/**
	 * The suit spades
	 */
	SPADES;

	/**
	 * Returns the rank of this {@code Suit}
	 * 
	 * <P>
	 * Ranks are alphabetical as follows:
	 * 
	 * <ol>
	 * <li><code>SPADES</code></li>
	 * <li><code>HEARTS</code></li>
	 * <li><code>DIAMONDS</code></li>
	 * <li><code>CLUBS</code></li>
	 * </ol>
	 * 
	 * Note: 'Rank' has nothing to do with the card's rank (ACE, KING, THREE, SEVEN,
	 * etc.), this is only for games in which {@code Suits} have different values
	 * 
	 * @return the rank of this {@code Suit}
	 */
	int getSuitRank() {
		return 4 - this.ordinal();
	}

}