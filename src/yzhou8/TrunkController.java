package yzhou8;

import java.awt.event.MouseEvent;

import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.model.BuildablePile;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.view.BuildablePileView;
import ks.common.view.ColumnView;
import ks.common.view.Container;
import ks.common.view.Widget;

public class TrunkController extends SolitaireReleasedAdapter{
	/** The EagleWings Game. */
	protected EagleWings theGame;

	/** The specific Foundation pileView being controlled. */
	protected BuildablePileView src;
	/**
	 * TrunkController constructor comment.
	 */
	public TrunkController (EagleWings theGame, BuildablePileView src) {
		super(theGame);
		this.theGame = theGame;
		this.src = src;
	}
	public void mousePressed(MouseEvent me ) {
		// The container manages several critical pieces of information; namely, it
		// is responsible for the draggingObject; in our case, this would be a CardView
		// Widget managing the card we are trying to drag between two piles.
		Container c = theGame.getContainer();
		
		/** Return if there is no card to be chosen. */
		BuildablePile trunk = (BuildablePile) src.getModelElement();

		if (trunk.count() == 0||(trunk.count()!=1)) {
			return;
		}
		
		if(trunk.getNumFaceUp() == 0){
			trunk.flipCard();
		}
		// Get a card to move from BuildablePileView. Note: this returns a CardView.
		// Note that this method will alter the model for BuildablePileView if the condition is met.
		ColumnView cardView = src.getColumnView(me);
		
		// an invalid selection of some sort.
		if (cardView == null) {
			return;
		}
		// Check conditions
		Column col = (Column) cardView.getModelElement();
		if (col == null) {
			System.err.println ("BuildablePileController::mousePressed(): Unexpectedly encountered a ColumnView with no Column.");
			return; // sanity check, but should never happen.
		}

		// If we get here, then the user has indeed clicked on the top card in the PileView and
		// we are able to now move it on the screen at will. For smooth action, the bounds for the
		// cardView widget reflect the original card location on the screen.

		Widget w = c.getActiveDraggingObject();
		if (w != Container.getNothingBeingDragged()) {
			System.err.println ("BuildablePileController::mousePressed(): Unexpectedly encountered a Dragging Object during a Mouse press.");
			return;
		}
	
		// Tell container which object is being dragged, and where in that widget the user clicked.
		c.setActiveDraggingObject (cardView, me);
		
		// Tell container which source widget initiated the drag
		c.setDragSource (src);
	
		// The only widget that could have changed is ourselves. If we called refresh, there
		// would be a flicker, because the dragged widget would not be redrawn. We simply
		// force the WastePile's image to be updated, but nothing is refreshed on the screen.
		// This is patently OK because the card has not yet been dragged away to reveal the
		// card beneath it.  A bit tricky and I like it!
		src.redraw();
	}

}
