package yzhou8;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Deck;
import ks.common.model.Move;
import ks.common.model.Pile;

/*
 * Move card from top of wastePile to top of foundationPile
 */
public class PileToFoundationMove extends Move {
	Pile pile;
	Card cardBeingDragged;
	Pile foundation;
	
	PileToFoundationMove(Pile from,Card cardBeingDragged,Pile to){
		this.pile = from;
		this.cardBeingDragged = cardBeingDragged;
		this.foundation = to;
	}
	
	@Override
	public boolean doMove(Solitaire game) {
		if(!valid(game)){
			return false;
		}
		foundation.add(cardBeingDragged);
		game.updateScore(+1);
		
		return true;				
	}

	@Override
	public boolean undo(Solitaire game) {
		pile.add(foundation.get());
		
		game.updateScore(-1);
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		if(foundation.empty()){
			if(cardBeingDragged.getRank() == ((EagleWings)game).RankOfFoundation){
				return true;
			}
			else{
				return false;
			}
		}
		if(((cardBeingDragged.getRank() == foundation.peek().getRank()+1)
				||((cardBeingDragged.getRank() == 1)&&(foundation.peek().getRank() == 13)))
				&&(cardBeingDragged.getSuit() == foundation.peek().getSuit())){
			return true;
		}
		return false;
	}

}
