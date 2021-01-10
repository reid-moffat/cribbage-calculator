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

	public Card() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	@Override
	public int compareTo(Card o) {
		// TODO Auto-generated method stub
		return this.RANK;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
