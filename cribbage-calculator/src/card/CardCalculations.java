package card;

/**
 * Contains static methods for cribbage card calculations, such as checking if
 * cards are consecutive of add up to a set value
 * 
 * @author Reid Moffat
 */
public final class CardCalculations {

	/**
	 * Private constructor to prevent instantiation
	 */
	private CardCalculations() {
	}

	/**
	 * Checks if all the values of the supplied card arguments add up to 15
	 * 
	 * @param cards an array card objects
	 * @return true if the card values add up to 15, false if not
	 * @throws IllegalArgumentException if the array of cards has 0, 1 or more than
	 *                                  5 cards
	 */
	public static boolean isFifteen(Card[] cards) {
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
	 * Checks if the supplied cards are consecutive to form a run
	 * 
	 * @param cards 3-5 cards
	 * @return 0 if the cards don't form a run, the length of the run (3-5) if the
	 *         cards do form a run
	 * @throws IllegalArgumentException if the array of cards has 0, 1 or more than
	 *                                  5 cards
	 */
	public static int isRun(Card[] cards) {
		if (cards.length < 2) {
			throw new IllegalArgumentException("you must supply at least two card arguments");
		}
		if (cards.length > 5) {
			throw new IllegalArgumentException("you cannot have more than five cards");
		}

		return -1;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
