package yzhou8;

import heineman.Klondike;
import heineman.klondike.MoveCardToFoundationMove;

import java.awt.event.MouseEvent;

import ks.common.model.BuildablePile;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.common.view.BuildablePileView;
import ks.common.view.CardView;
import ks.common.view.ColumnView;
import ks.common.view.Container;
import ks.common.view.PileView;
import ks.common.view.Widget;

public class FoundationController extends java.awt.event.MouseAdapter {
	/** The EagleWings Game. */
	protected EagleWings theGame;
	protected BuildablePile trunk;

	/** The specific Foundation pileView being controlled. */
	protected PileView src;
	/**
	 * FoundationController constructor comment.
	 */
	public FoundationController(EagleWings theGame,BuildablePile trunk, PileView foundation) {
		this.theGame = theGame;
		this.trunk = trunk;
		this.src = foundation;
	}
	/**
	 * Coordinate reaction to the completion of a Drag Event.
	 * <p>
	 * A bit of a challenge to construct the appropriate move, because cards
	 * can be dragged both from the WastePile (as a CardView object) and the 
	 * BuildablePileView (as a ColumnView).
	 * @param me java.awt.event.MouseEvent
	 */
	public void mouseReleased(MouseEvent me) {
		Container c = theGame.getContainer();

		/** Return if there is no card being dragged chosen. */
		Widget draggingWidget = c.getActiveDraggingObject();
		if (draggingWidget == Container.getNothingBeingDragged()) {
			System.err.println ("FoundationController::mouseReleased() unexpectedly found nothing being dragged.");
			c.releaseDraggingObject();		
			return;
		}

		/** Recover the from BuildablePile OR waste Pile */
		Widget fromWidget = c.getDragSource();
		if (fromWidget == null) {
			System.err.println ("FoundationController::mouseReleased(): somehow no dragSource in container.");
			c.releaseDraggingObject();
			return;
		}

		// Determine the Pile
		Pile foundation = (Pile) src.getModelElement();		
		if (fromWidget instanceof BuildablePileView) {
			// coming from a buildable pile [user may be trying to move multiple cards]
			BuildablePile fromPile = (BuildablePile) fromWidget.getModelElement();
			/** Must be the CardView widget being dragged. */
			ColumnView columnView = (ColumnView) draggingWidget;
			Column col = (Column) columnView.getModelElement();
			if (col == null) {
				System.err.println ("FoundationController::mouseReleased(): somehow CardView model element is null.");
				c.releaseDraggingObject();			
				return;
			}

			// must use peek() so we don't modify fromPile prematurely. Here is a HACK! Presumably
			// we only want the Move object to know things about the move, but we have to put
			// in a check to verify that Column is of size one. NO good solution that I can
			// see right now.
			if (fromPile.count() != 0) {
				fromWidget.returnWidget (draggingWidget);  // return home
			} 
			else {
				Move m = new TrunkToFoundationMove (fromPile, col.peek(), foundation);

				if (m.doMove (theGame)) {
					// Success
					theGame.pushMove (m);
				} 
				else {
					fromWidget.returnWidget (draggingWidget);
				}
			}
		}
		// Determine the Pile
		else{
			Pile pile = (Pile)fromWidget.getModelElement();
			
			boolean needAutoMove = true;
			EagleWings wings = (EagleWings) theGame;
			if (wings.isWastePile(pile)) {
				needAutoMove = false;
			}
			
			CardView cardView = (CardView) draggingWidget;
			Card theCard = (Card) cardView.getModelElement();
		
			Move move1 = new PileToFoundationMove(pile,theCard,foundation);
			if(move1.doMove(theGame)){
				theGame.pushMove (move1);     // Successful Move has been Move
			}
			else{
				fromWidget.returnWidget(draggingWidget);
			}
///////////////////////automatic move check
			if(needAutoMove && pile.count() == 0&&(trunk.count() > 1)){
				Move move2 = new AutomaticFillingMove(trunk,pile);
				if(move2.doMove(theGame)){
					theGame.pushMove (move2);     // Successful Move has been Move
				}
				else{
					fromWidget.returnWidget(draggingWidget);
				}
			}
		}
			
		// release the dragging object, (this will reset dragSource)
		c.releaseDraggingObject();
		
		// finally repaint
		c.repaint();

	}
}

