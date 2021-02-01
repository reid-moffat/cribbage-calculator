package card;

/**
 * The thirteen card ranks in a standard deck of 52 playing cards
 * 
 * <p>
 * Card ranks are ordered as aces low:
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
 */
public enum Rank {

	/**
	 * The rank ace (lowest)
	 */
	ACE,

	/**
	 * The rank two
	 */
	TWO,

	/**
	 * The rank three
	 */
	THREE,

	/**
	 * The rank four
	 */
	FOUR,

	/**
	 * The rank five
	 */
	FIVE,

	/**
	 * The rank six
	 */
	SIX,

	/**
	 * The rank seven
	 */
	SEVEN,

	/**
	 * The rank eight
	 */
	EIGHT,

	/**
	 * The rank nine
	 */
	NINE,

	/**
	 * The rank ten
	 */
	TEN,

	/**
	 * The rank jack
	 */
	JACK,

	/**
	 * The rank queen
	 */
	QUEEN,

	/**
	 * The rank king (highest)
	 */
	KING;

	/**
	 * Returns the rank place (starting at 1) of this rank with aces low:
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
	 * @return the rank place of the rank
	 */
	int getRankNumber() {
		return this.ordinal() + 1;
	}

}