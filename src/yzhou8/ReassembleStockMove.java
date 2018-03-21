package yzhou8;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Deck;
import ks.common.model.Move;
import ks.common.model.MutableInteger;
import ks.common.model.Pile;

public class ReassembleStockMove extends Move {
	Deck stock;
	Pile wastePile;
	MutableInteger NumRedealsRemaining;
	
	ReassembleStockMove(Deck stock,Pile wastePile,MutableInteger NumRedealsRemaining){
		this.stock = stock;
		this.wastePile = wastePile;
		this.NumRedealsRemaining = NumRedealsRemaining;
	}
	
	@Override
	public boolean doMove(Solitaire game) {
		if(!valid(game)){
			return false;
		}
		while(!wastePile.empty()){
			Card card = wastePile.get();
			stock.add(card);
			game.updateNumberCardsLeft(+1);
		}
		int val = NumRedealsRemaining.getValue();
		NumRedealsRemaining.setValue(val-1);
		return true;				
	}

	@Override
	public boolean undo(Solitaire game) {
		while(!stock.empty()){
			Card card = stock.get();
			wastePile.add(card);
			game.updateNumberCardsLeft(-1);
		}
		int val = NumRedealsRemaining.getValue();
		NumRedealsRemaining.setValue(val+1);
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		return stock.empty()&&(!wastePile.empty())&&(NumRedealsRemaining.getValue()>0);
	}

}

