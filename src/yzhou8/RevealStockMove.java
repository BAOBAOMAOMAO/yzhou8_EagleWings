package yzhou8;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Deck;
import ks.common.model.Move;
/*
 * Move card from the top of the stock to the top of the wastePile.
 */
import ks.common.model.Pile;

public class RevealStockMove extends Move {
	Deck stock;
	Pile wastePile;
	
	RevealStockMove(Deck stock,Pile wastePile){
		this.stock = stock;
		this.wastePile = wastePile;
	}
	
	@Override
	public boolean doMove(Solitaire game) {
		if(!valid(game)){
			return false;
		}
		Card card = stock.get();
		wastePile.add(card);
		game.updateNumberCardsLeft(-1);
		return true;				
	}

	@Override
	public boolean undo(Solitaire game) {
		Card c = wastePile.get();
		stock.add(c);
		game.updateNumberCardsLeft(+1);
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		return !stock.empty();
	}

}
