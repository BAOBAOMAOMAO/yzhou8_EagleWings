package yzhou8;

import ks.common.games.Solitaire;
import ks.common.model.BuildablePile;
import ks.common.model.Card;
import ks.common.model.Move;
import ks.common.model.Pile;


public class TrunkToFoundationMove  extends Move {
	BuildablePile trunk;
	Card cardBeingDragged;
	Pile foundation;
	
	TrunkToFoundationMove(BuildablePile from,Card cardBeingDragged,Pile to){
		this.trunk = from;
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
		if(foundation.empty()){
			return false;
		}
		trunk.add(foundation.get());
		game.updateScore(-1);
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		if(trunk.empty()){
			return true;
		}
		if(foundation.empty()){
			if(cardBeingDragged.getRank() == ((EagleWings)game).RankOfFoundation){
				return true;
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
