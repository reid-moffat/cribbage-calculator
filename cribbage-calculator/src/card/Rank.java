package card;

/**
 * The thirteen card ranks in a standard deck of 52 playing cards. This
 * enumeration orders based on cribbage values and traditional ordering for face
 * cards:
 * 
 * <ul>
 * <li><code>ACE: 1</code>
 * <li><code>TWO: 2</code>
 * <li><code>THREE: 3</code>
 * <li><code>FOUR: 4</code>
 * <li><code>FIVE: 5</code>
 * <li><code>SIX: 6</code>
 * <li><code>SEVEN: 7</code>
 * <li><code>EIGHT: 8</code>
 * <li><code>NINE: 9</code>
 * <li><code>TEN: 10</code>
 * <li><code>JACK: 10</code>
 * <li><code>QUEEN: 10</code>
 * <li><code>KING: 10</code>
 * </ul>
 * 
 * Note that card values are only used during play and when calculating
 * fifteens, a ten and jack both have a value of 10 and will make a fifteen with
 * a five; but they cannot make a pair
 *
 */
enum Rank {

	/**
	 * A
	 */
	ACE,

	/**
	 * 
	 */
	TWO,

	/**
	 * 
	 */
	THREE,

	/**
	 * 
	 */
	FOUR,

	/**
	 * 
	 */
	FIVE,

	/**
	 * 
	 */
	SIX,

	/**
	 * 
	 */
	SEVEN,

	/**
	 * 
	 */
	EIGHT,

	/**
	 * 
	 */
	NINE,

	/**
	 * 
	 */
	TEN,

	/**
	 * 
	 */
	JACK,

	/**
	 * 
	 */
	QUEEN,

	/**
	 * 
	 */
	KING

}