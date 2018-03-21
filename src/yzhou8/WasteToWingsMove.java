package yzhou8;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Deck;
import ks.common.model.Move;
import ks.common.model.Pile;

public class WasteToWingsMove extends Move {
	Pile wastePile;
	Card cardBeingDragged;
	Pile wing;
	
	WasteToWingsMove(Pile from,Card cardBeingDragged,Pile to){
		this.wastePile = from;
		this.cardBeingDragged = cardBeingDragged;
		this.wing = to;
	}
	
	@Override
	public boolean doMove(Solitaire game) {
		if(!valid(game)){
			return false;
		}
		wing.add(cardBeingDragged);
		return true;				
	}

	@Override
	public boolean undo(Solitaire game) {
		wastePile.add(wing.get());		
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		if(!wing.empty()){
			return false;
		}
		return true;
	}

}
