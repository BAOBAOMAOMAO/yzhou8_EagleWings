package yzhou8;

import java.awt.Dimension;

import heineman.Klondike;
import heineman.klondike.KlondikeDeckController;
import ks.common.controller.SolitaireMouseMotionAdapter;
import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.games.Solitaire;
import ks.common.games.SolitaireUndoAdapter;
import ks.common.model.BuildablePile;
import ks.common.model.Card;
import ks.common.model.Deck;
import ks.common.model.MutableInteger;
import ks.common.model.Pile;
import ks.common.view.BuildablePileView;
import ks.common.view.CardImages;
import ks.common.view.DeckView;
import ks.common.view.IntegerView;
import ks.common.view.PileView;
import ks.common.view.StringView;
import ks.launcher.Main;

public class EagleWings extends Solitaire {
	Deck stock;
	BuildablePile trunk;
	Pile[] wings = new Pile[9];
	Pile[] foundation = new Pile[5];
	Pile wastePile;
	
	int RankOfFoundation;
	MutableInteger NumRedealsRemaining;
	
	DeckView stockView;
	BuildablePileView trunkView;
	PileView[] foundationView = new PileView[5];
	PileView[] wingsView = new PileView[9];
	PileView wastePileView;
	
	IntegerView numRedealsRemainingView;
	StringView rankOfFoundationView;
	IntegerView scoreView;
	IntegerView numCardsLeftInStockView;
	IntegerView numCardsLeftInTrunkView;
		

	@Override
	public String getName() {
		return "Yihong-EagleWings";
	}

	@Override
	public boolean hasWon() {
		return getScore().getValue() == 52;
	}
	
	public boolean isWastePile(Pile p) {
		return p == wastePile;
	}
	
	public String getRankFoundation(){
		if(this.foundation[1].rank() <= 10){
			RankOfFoundation = this.foundation[1].rank();
			return ("" + this.foundation[1].rank());
		}
		else{
			switch(this.foundation[1].rank()){
			case 11:
				RankOfFoundation = 11;
				return "J";
			case 12:
				RankOfFoundation = 12;
				return "Q";
			case 13:
				RankOfFoundation = 13;
				return "K";
			default:
				return ("" + this.foundation[1].rank());				
			}
		}
	}

	@Override
	public void initialize() {
		// initialize model
		initializeModel(getSeed());
		
		// prepare trunks
		//may need to change
		for (int trunkNum = 1; trunkNum <= 13; trunkNum++) {
			Card d = stock.get();
			d.setFaceUp (false);
			trunk.add (d);
		}

		
		// prepare wings
		for (int wingsNum = 1; wingsNum <= 8; wingsNum ++){
			Card c = stock.get();
			wings[wingsNum].add(c);
		}
		
		// prepare foundation
		foundation[1].add(stock.get());

		updateNumberCardsLeft (-22);
		
		initializeView();
		initializeControllers();
		System.out.println(stock.peek());
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(999,999);
	}
	
	private void initializeModel(int seed) {
		stock = new Deck("deck");
		stock.create(seed);
		model.addElement(stock);
		
		trunk = new BuildablePile("trunk");
		model.addElement(trunk);
		
		for(int i = 1;i <= 8; i++){
			wings[i] = new Pile("wing" + i);
			model.addElement(wings[i]);			
		}
		
		for(int i = 1; i <= 4; i++){
			foundation[i] = new Pile("foundation" + i);
			model.addElement(foundation[i]);
		}
		
		wastePile = new Pile("wastePile");
		model.addElement(wastePile);
		
		NumRedealsRemaining = new MutableInteger(2);
				
		updateNumberCardsLeft(52);
		updateScore(1);		
	}

	
	private void initializeView() {
		CardImages ci = getCardImages();
		
		//Stock View
		stockView = new DeckView (stock);
		stockView.setBounds (20,20, ci.getWidth(), ci.getHeight());
		container.addWidget (stockView);
		
		//trunk view
		trunkView = new BuildablePileView (trunk);
		trunkView.setBounds (90+((int)3.5*ci.getWidth()), ci.getHeight() + 80, ci.getWidth(), ci.getHeight()+80);
		container.addWidget (trunkView);

		//Foundations views
		for (int pileNum = 1; pileNum <=4; pileNum++) {
			foundationView[pileNum] = new PileView (foundation[pileNum]);
			foundationView[pileNum].setBounds (20*(pileNum+3) + ci.getWidth()*(pileNum+2), 20, ci.getWidth(), ci.getHeight());
			container.addWidget (foundationView[pileNum]);
		}
		
		//First 4 wings views
		for (int pileNum = 1; pileNum <=4; pileNum++) {
			wingsView[pileNum] = new PileView (wings[pileNum]);
			wingsView[pileNum].setBounds (20*(pileNum-2) + ci.getWidth()*pileNum, 200+45*(pileNum-1)+ci.getHeight(), ci.getWidth(), ci.getHeight());
			container.addWidget (wingsView[pileNum]);
		}
		
		//second 4 wings views
		for (int pileNum = 5; pileNum <=8; pileNum++) {
			wingsView[pileNum] = new PileView (wings[pileNum]);
			wingsView[pileNum].setBounds (20*(pileNum-2) + ci.getWidth()*pileNum, 515-45*(pileNum-1)+ci.getHeight(), ci.getWidth(), ci.getHeight());
			container.addWidget (wingsView[pileNum]);
		}
		
		//waste pile view
		wastePileView = new PileView (wastePile);
		wastePileView.setBounds (20*2 + ci.getWidth(), 20, ci.getWidth(), ci.getHeight());
		container.addWidget (wastePileView);
		
		//numCardsLeftInStockView
		numCardsLeftInStockView = new IntegerView (getNumLeft());
		numCardsLeftInStockView.setFontSize (14);
		numCardsLeftInStockView.setBounds (60 + ci.getWidth()*2, 20, ci.getWidth(), 20);
		container.addWidget (numCardsLeftInStockView);	
		
		//redealsView
		numRedealsRemainingView = new IntegerView (NumRedealsRemaining);
		numRedealsRemainingView.setFontSize (14);
		numRedealsRemainingView.setBounds (60 + ci.getWidth()*2, 40,ci.getWidth(), 60);
		container.addWidget (numRedealsRemainingView);	
	
		//score view
		scoreView = new IntegerView (getScore());
		scoreView.setBounds (160+7*ci.getWidth(), 20, ci.getWidth(), 60);
		container.addWidget (scoreView);
		
		//rankOfFoundationView
		rankOfFoundationView = new StringView (this.getRankFoundation());
		rankOfFoundationView.setFontSize (14);
		rankOfFoundationView.setBounds (160 + ci.getWidth()*7, 90, ci.getWidth(), 20);
		container.addWidget (rankOfFoundationView);
		
	}

	private void initializeControllers() {
		stockView.setMouseAdapter(new EagleWingsStockController (this,stock, wastePile,NumRedealsRemaining));
		stockView.setMouseMotionAdapter (new SolitaireMouseMotionAdapter(this));
		stockView.setUndoAdapter (new SolitaireUndoAdapter(this));
		
		wastePileView.setMouseAdapter(new WastePileController (this, wastePileView));
		wastePileView.setMouseMotionAdapter (new SolitaireMouseMotionAdapter(this));
		wastePileView.setUndoAdapter (new SolitaireUndoAdapter(this));

		trunkView.setMouseAdapter(new TrunkController (this, trunkView));
		trunkView.setMouseMotionAdapter (new SolitaireMouseMotionAdapter(this));
		trunkView.setUndoAdapter (new SolitaireUndoAdapter(this));

		for(int i = 1; i <=4; i++){
			foundationView[i].setMouseAdapter(new FoundationController (this, trunk,foundationView[i]));
			foundationView[i].setMouseMotionAdapter (new SolitaireMouseMotionAdapter(this));
			foundationView[i].setUndoAdapter (new SolitaireUndoAdapter(this));

		}
		for(int i =1; i <=8; i++){
			wingsView[i].setMouseAdapter(new WingsController (this, wingsView[i]));
			wingsView[i].setMouseMotionAdapter (new SolitaireMouseMotionAdapter(this));
			wingsView[i].setUndoAdapter (new SolitaireUndoAdapter(this));
		}
		
		// Ensure that any releases (and movement) are handled by the non-interactive widgets		
		numCardsLeftInStockView.setMouseMotionAdapter (new SolitaireMouseMotionAdapter(this));
		numCardsLeftInStockView.setMouseAdapter (new SolitaireReleasedAdapter(this));
		numCardsLeftInStockView.setUndoAdapter (new SolitaireUndoAdapter(this));

		// same for scoreView
		scoreView.setMouseMotionAdapter (new SolitaireMouseMotionAdapter(this));
		scoreView.setMouseAdapter (new SolitaireReleasedAdapter(this));
		scoreView.setUndoAdapter (new SolitaireUndoAdapter(this));

	}
	
	/** Code to launch solitaire variation. */
	public static void main (String []args) {
		// Seed is to ensure we get the same initial cards every time
		// Here the seed is to "order by suit."
		Main.generateWindow(new EagleWings(), Deck.OrderByRank);
	}

}
