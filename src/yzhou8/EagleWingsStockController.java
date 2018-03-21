package yzhou8;

import heineman.Klondike;
import heineman.klondike.DealCardMove;
import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.model.Deck;
import ks.common.model.Move;
import ks.common.model.MutableInteger;
import ks.common.model.Pile;

public class EagleWingsStockController extends SolitaireReleasedAdapter {
	/** The game. */
	protected EagleWings theGame;

	/** The WastePile of interest. */
	protected Pile wastePile;

	/** The Deck of interest. */
	protected Deck deck;
	
	MutableInteger NumRedealsRemaining;
	/**
	 * KlondikeDeckController constructor comment.
	 */
	public EagleWingsStockController(EagleWings theGame,Deck d, Pile wastePile, MutableInteger NumRedealsRemaining) {
		super(theGame);

		this.theGame = theGame;
		this.wastePile = wastePile;
		this.deck = d;
		this.NumRedealsRemaining = NumRedealsRemaining;
	}

	/**
	 * Coordinate reaction to the beginning of a Drag Event. In this case,
	 * no drag is ever achieved, and we simply deal upon the pres.
	 */
	public void mousePressed (java.awt.event.MouseEvent me) {

		// Attempting a DealFourCardMove
		if(deck.empty() ){
			Move m = new ReassembleStockMove (deck, wastePile,NumRedealsRemaining);
			if (m.doMove(theGame)) {
				theGame.pushMove (m);     // Successful Move has been Move
				theGame.refreshWidgets(); // refresh updated widgets.
			}
		}
		else{
			Move m = new RevealStockMove (deck, wastePile);
			if (m.doMove(theGame)) {
				theGame.pushMove (m);     // Successful Move has been Move
				theGame.refreshWidgets(); // refresh updated widgets.
			}
		}
	}

}
