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
	 * The rank ace
	 * 
	 * <p>
	 * Aces have a value of 1 in cribbage
	 */
	ACE,

	/**
	 * The rank two
	 * 
	 * <p>
	 * Twos have a value of 2 in cribbage
	 */
	TWO,

	/**
	 * The rank three
	 * 
	 * <p>
	 * Threes have a value of 3 in cribbage
	 */
	THREE,

	/**
	 * The rank four
	 * 
	 * <p>
	 * Fours have a value of 4 in cribbage
	 */
	FOUR,

	/**
	 * The rank five
	 * 
	 * <p>
	 * Fives have a value of 5 in cribbage
	 */
	FIVE,

	/**
	 * The rank six
	 * 
	 * <p>
	 * Sixes have a value of 6 in cribbage
	 */
	SIX,

	/**
	 * The rank seven
	 * 
	 * <p>
	 * Sevens have a value of 7 in cribbage
	 */
	SEVEN,

	/**
	 * The rank eight
	 * 
	 * <p>
	 * Eights have a value of 8 in cribbage
	 */
	EIGHT,

	/**
	 * The rank nine
	 * 
	 * <p>
	 * Nines have a value of 9 in cribbage
	 */
	NINE,

	/**
	 * The rank ten
	 * 
	 * <p>
	 * Tens have a value of 10 in cribbage
	 */
	TEN,

	/**
	 * The rank jack
	 * 
	 * <p>
	 * Jacks have a value of 10 in cribbage
	 */
	JACK,

	/**
	 * The rank queen
	 * 
	 * <p>
	 * Queens have a value of 10 in cribbage
	 */
	QUEEN,

	/**
	 * The rank king
	 * 
	 * <p>
	 * Kings have a value of 10 in cribbage
	 */
	KING;

	/**
	 * Returns the cribbage value of the rank (aces = 1, face cards = 10)
	 * 
	 * @return the cribbage value of the rank
	 */
	int getValue() {
		int value = this.ordinal() + 1;
		return value > 10 ? 10 : value;
	}

	/**
	 * Returns the rank place (starting at 1) of the rank
	 * 
	 * <p>
	 * Unlike getValue(), face cards are given distinct values instead of 10 in
	 * order to differentiate between them for runs
	 * 
	 * <ul>
	 * <li><code>Jack: 11</code></li>
	 * <li><code>Queen: 12</code></li>
	 * <li><code>King: 13</code></li>
	 * </ul>
	 * 
	 * @return the rank place of the rank
	 */
	int getRankValue() {
		return this.ordinal() + 1;
	}

}