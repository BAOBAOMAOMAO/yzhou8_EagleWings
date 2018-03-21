package yzhou8;

import ks.common.games.Solitaire;
import ks.common.model.BuildablePile;
import ks.common.model.Card;
import ks.common.model.Move;
import ks.common.model.Pile;

public class AutomaticFillingMove extends Move {
	BuildablePile trunk;
	Pile wing;
	
	AutomaticFillingMove(BuildablePile from,Pile to){
		this.trunk = from;
		this.wing = to;
	}
	
	@Override
	public boolean doMove(Solitaire game) {
		if(!valid(game)){
			return false;
		}
		Card c = trunk.get();
		c.setFaceUp(true);
		wing.add(c);
		if(trunk.count() == 1){
			if(!trunk.peek().isFaceUp()){
				trunk.flipCard();
			}
		}

		return true;				
	}

	@Override
	public boolean undo(Solitaire game) {
		if(trunk.count() == 1){
			if(trunk.peek().isFaceUp()){
				trunk.flipCard();
			}
		}
		Card c = wing.get();
		c.setFaceUp(false);
		trunk.add(c);
		return true;				

	}

	@Override
	public boolean valid(Solitaire game) {
		if(wing.count()==0&&(trunk.count() > 1)){
			return true;
		}
		return false;
	}

}
