package yzhou8;

import java.awt.event.MouseEvent;

import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.model.Card;
import ks.common.model.Deck;
import ks.launcher.Main;
import ks.tests.KSTestCase;

public class TestEagleWings extends KSTestCase {
	EagleWings eagleWings;
	GameWindow gw;
	protected void setUp(){
		eagleWings = new EagleWings();
		gw = Main.generateWindow(eagleWings, Deck.OrderByRank);
		System.out.println(eagleWings.stock.peek());

	}
	protected void tearDown(){
		gw.dispose();
	}

	public void testRevealStockMove(){
		Card topCard = eagleWings.stock.peek();
		RevealStockMove rsm = new RevealStockMove(eagleWings.stock,eagleWings.wastePile);		
		assertTrue(rsm.valid(eagleWings));
		
		rsm.doMove(eagleWings);		
		assertEquals(29,eagleWings.stock.count());
		assertEquals(topCard,eagleWings.wastePile.peek());
		
		int value = eagleWings.getNumLeft().getValue();
		assertEquals(29,value);
		
		rsm.undo(eagleWings);		
		assertEquals(30,eagleWings.stock.count());  
		
		//fix things so they stay broke
		eagleWings.stock.removeAll();
		assertFalse(rsm.valid(eagleWings));
		assertFalse(rsm.doMove(eagleWings));
		
	}
	public void testStockController(){
		//invoke normal move
		MouseEvent pr = createPressed(eagleWings,eagleWings.stockView,0,0);
		eagleWings.stockView.getMouseManager().handleMouseEvent(pr);
	
		assertEquals(new Card(Card.EIGHT,Card.DIAMONDS),eagleWings.wastePile.peek());
		assertTrue(eagleWings.undoMove());
		assertTrue(eagleWings.wastePile.empty());
		
		//invoke redeal move
		int Num = eagleWings.stock.count();
		while(eagleWings.stock.count() > 0){
			Card c = eagleWings.stock.get();
			eagleWings.wastePile.add(c);
		}
		MouseEvent pr1 = createPressed(eagleWings,eagleWings.stockView,0,0);
		eagleWings.stockView.getMouseManager().handleMouseEvent(pr1);
		
		assertEquals(Num,eagleWings.stock.count());
		assertEquals(0,eagleWings.wastePile.count());
	}
	
	public void testWingsController1(){
		int preScore2 = eagleWings.getScoreValue();
		Card g = eagleWings.trunk.peek();
		assertFalse(eagleWings.trunk.peek().isFaceUp());
		
		MouseEvent pr1 = createPressed(eagleWings,eagleWings.wingsView[8],5,5);
		eagleWings.wingsView[8].getMouseManager().handleMouseEvent(pr1);
		
		MouseEvent rel1 = createReleased(eagleWings,eagleWings.foundationView[2],5,5);
		eagleWings.foundationView[2].getMouseManager().handleMouseEvent(rel1);
		
		assertEquals(1,eagleWings.foundation[2].count());
		assertEquals(new Card(Card.EIGHT,Card.SPADES),eagleWings.foundation[2].peek());
		assertEquals(eagleWings.getScoreValue(),preScore2+1);
		
		//automaticFilling Move test
		assertEquals(1,eagleWings.wings[8].count());
		assertEquals(12,eagleWings.trunk.count());
		assertEquals(g,eagleWings.wings[8].peek());
		assertTrue(eagleWings.undoMove());
		assertEquals(13,eagleWings.trunk.count());
		assertTrue(eagleWings.wings[8].count() == 0);
		assertTrue(eagleWings.undoMove());
		assertEquals(eagleWings.getScoreValue(),preScore2);
		
		//trunk automatic Flip Move
		//wastePile to wings Move
		//ressambleStock Move
	}
	public void testWingscontroller2(){
		eagleWings.wings[1].get();
		MouseEvent pr = createPressed(eagleWings,eagleWings.wingsView[1],5,5);
		eagleWings.wingsView[1].getMouseManager().handleMouseEvent(pr);
		
		eagleWings.wastePile.add(new Card(Card.ACE,Card.DIAMONDS));
		MouseEvent press = createPressed(eagleWings,eagleWings.wastePileView,5,5);
		eagleWings.wastePileView.getMouseManager().handleMouseEvent(press);
		
		MouseEvent rel = createReleased(eagleWings,eagleWings.wingsView[1],5,5);
		eagleWings.wingsView[1].getMouseManager().handleMouseEvent(rel);
		
		assertEquals(1,eagleWings.wings[1].count());
		assertEquals(0,eagleWings.wastePile.count());
		
		assertTrue(eagleWings.undoMove());
		assertEquals(0,eagleWings.wings[1].count());
		assertEquals(1,eagleWings.wastePile.count());
		
		
		eagleWings.wings[2].get();
		System.out.println("here!!!!!!"+eagleWings.wings[2].count());
		while(eagleWings.trunk.count() > 1){
			eagleWings.trunk.get();
		}
		eagleWings.trunk.flipCard();
		System.out.println(eagleWings.trunk.peek().isFaceUp());
		eagleWings.refreshWidgets();
		MouseEvent press2 = createPressed(eagleWings,eagleWings.trunkView,0,0);
		eagleWings.trunkView.getMouseManager().handleMouseEvent(press2);
		
		MouseEvent rell = createReleased(eagleWings,eagleWings.wingsView[2],5,5);
		eagleWings.wingsView[2].getMouseManager().handleMouseEvent(rell);
		
	}
	
	public void testTrunkController(){
		//empty card test
		int preScore3 = eagleWings.getScoreValue();
		while(eagleWings.trunk.count()>0){
			eagleWings.trunk.get();
		}
		
		MouseEvent pr3 = createPressed(eagleWings,eagleWings.trunkView,5,5);
		eagleWings.trunkView.getMouseManager().handleMouseEvent(pr3);
		
		//face down test
		Card card = new Card(Card.EIGHT,Card.DIAMONDS);
		card.setFaceUp(false);
		eagleWings.trunk.add(card);
		
		MouseEvent pr4 = createPressed(eagleWings,eagleWings.trunkView,5,5);
		eagleWings.trunkView.getMouseManager().handleMouseEvent(pr4);
		
		MouseEvent rel3 = createReleased(eagleWings,eagleWings.foundationView[4],5,5);
		eagleWings.foundationView[4].getMouseManager().handleMouseEvent(rel3);
		
		assertTrue(eagleWings.undoMove());
		
		//face up test
		card.setFaceUp(true);

		MouseEvent pr5 = createPressed(eagleWings,eagleWings.trunkView,5,5);
		eagleWings.trunkView.getMouseManager().handleMouseEvent(pr5);
		
		MouseEvent rel4 = createReleased(eagleWings,eagleWings.foundationView[4],5,5);
		eagleWings.foundationView[4].getMouseManager().handleMouseEvent(rel3);
		
		assertTrue(eagleWings.trunk.count() == 0);
		assertEquals(1,eagleWings.foundation[4].count());
		assertEquals(eagleWings.getScoreValue(),preScore3+1);
		assertTrue(eagleWings.undoMove());
		assertTrue(eagleWings.trunk.peek().isFaceUp());
		assertTrue(eagleWings.trunk.count() == 1);
		assertEquals(0,eagleWings.foundation[4].count());
		assertEquals(eagleWings.getScoreValue(),preScore3);
		
		
		//trunkToFoundation Move
		Card topCard = eagleWings.trunk.get();
		TrunkToFoundationMove ttf = new TrunkToFoundationMove(eagleWings.trunk,topCard,eagleWings.foundation[4]);		
		assertTrue(ttf.valid(eagleWings));
		
		ttf.doMove(eagleWings);		
		assertEquals(0,eagleWings.trunk.count());
		assertEquals(topCard,eagleWings.foundation[4].peek());
		
		ttf.undo(eagleWings);		
		assertEquals(1,eagleWings.trunk.count());  
		assertTrue(eagleWings.trunk.peek().isFaceUp());
	}
	
	public void testWastePileCountroller(){
		//wastePile to foundation Move
		MouseEvent pr1 = createPressed(eagleWings,eagleWings.wastePileView,5,5);
		eagleWings.wastePileView.getMouseManager().handleMouseEvent(pr1);
		
		eagleWings.wastePile.add(eagleWings.stock.get());
		int preScore1 = eagleWings.getScoreValue();
		MouseEvent pr2 = createPressed(eagleWings,eagleWings.wastePileView,5,5);
		eagleWings.wastePileView.getMouseManager().handleMouseEvent(pr2);
		
		MouseEvent rel2 = createReleased(eagleWings,eagleWings.foundationView[3],5,5);
		eagleWings.foundationView[3].getMouseManager().handleMouseEvent(rel2);		
		
		assertEquals(1,eagleWings.foundation[3].count());
		assertEquals(0,eagleWings.wastePile.count());
		assertEquals(eagleWings.getScoreValue(),preScore1+1);
		assertTrue(eagleWings.undoMove());
		assertEquals(eagleWings.getScoreValue(),preScore1);
	}
}
