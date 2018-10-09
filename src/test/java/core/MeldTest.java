package core;

import junit.framework.TestCase;

public class MeldTest extends TestCase {
	public void testAddTile() {
		Deck deck = new Deck();
		Meld meld = new Meld();
		
		meld.add(deck.drawTile());
		meld.add(deck.drawTile());
		meld.add(deck.drawTile());
		meld.add(deck.drawTile());
		
		assertEquals(4, meld.size());
	}
	
	public void testRemoveTile() {
		Deck deck = new Deck();
		Meld meld = new Meld();
		Tile t1 = new Tile('R', 4);
		Tile t2 = new Tile('R', 3);
		
		meld.add(t1);
		meld.add(t2);
		meld.remove(t1);
		meld.remove(t2);
		
		assertEquals(0, meld.size());
	}
	
	public void testGetTile() {
		Deck deck = new Deck();
		Meld meld = new Meld();
		Tile t1 = new Tile('R', 4);
		Tile t2 = new Tile('R', 3);
		
		meld.add(t1);
		meld.add(t2);
		
		assertEquals(3, meld.getTile(1).getRank());
	}
}
