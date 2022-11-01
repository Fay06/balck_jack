package blackjack;
import java.util.*;

public class Blackjack implements BlackjackEngine {
	
	/**
	 * Constructor you must provide.  Initializes the player's account 
	 * to 200 and the initial bet to 5.  Feel free to initialize any other
	 * fields. Keep in mind that the constructor does not define the 
	 * deck(s) of cards.
	 * @param randomGenerator
	 * @param numberOfDecks
	 */
	
	private Random therandomGenerator;
	private int thenumberOfDecks;
	private static int account = 200;
	private static int bet= 5;
	private int status;
	ArrayList<Card> list = new ArrayList<Card>();
	ArrayList<Card> player = new ArrayList<Card>();
	ArrayList<Card> dealer = new ArrayList<Card>();
	
	
	public Blackjack(Random randomGenerator, int numberOfDecks) {
		therandomGenerator = randomGenerator;
		thenumberOfDecks = numberOfDecks;
	}
	
	public int getNumberOfDecks() {
		return thenumberOfDecks;
	}
	
	public void createAndShuffleGameDeck() {
		for(int i = 0; i<thenumberOfDecks; i++) {
			for(CardSuit suit : CardSuit.values()) {
				for(CardValue value : CardValue.values()) {
					list.add(new Card(value,suit));
				}
			}
		}
		Collections.shuffle(list,therandomGenerator);
		
	}
	
	public Card[] getGameDeck() {
		return list.toArray(new Card[list.size()]);
	}
	
	public void deal() {	
		player.clear();
		dealer.clear();
		list.clear();
		createAndShuffleGameDeck();
		
		list.get(1).setFaceDown();
		
		player.add(list.remove(0));
		dealer.add(list.remove(0));
		player.add(list.remove(0));
		dealer.add(list.remove(0));
		
		status = GAME_IN_PROGRESS;
		
		account = account - bet;
	}
		
	public Card[] getDealerCards() {
		return dealer.toArray(new Card[dealer.size()]);
	}

	public int[] getDealerCardsTotal() {
		int[] result = null;
		int total = 0;
		int totalA = 0;
		int a = 0;
		
		for(int i = 0; i<dealer.size(); i++){
			total = total + (dealer.get(i)).getValue().getIntValue();
		}
			
		for(int j = 0; j<dealer.size();j++) {
			if((dealer.get(j)).getValue().getIntValue()==1) {
				a++;
			}
		}
			
		if(a==0) {
			if(total<=21) {
				result = new int[] {total};
			}
		}else {
			totalA = total+10;
			if(totalA<=21) {
				result = new int[] {total,totalA};
			}else if(total<=21) {
				result = new int[] {total};
			}
		}
		return result;
	}
		
	public int getDealerCardsEvaluation() {
		int a = 0;
		if(choosenumD()==0) {
			a=BUST;
		}else if(choosenumD()<21) {
			a=LESS_THAN_21;
		}else if(choosenumD() == 21) {
				if(dealer.size()==2) {
					a=BLACKJACK;
				}else {
					a=HAS_21;
				}
		}
		return a;
	}
	
	public Card[] getPlayerCards() {
		return player.toArray(new Card[player.size()]);
	}
	
	public int[] getPlayerCardsTotal() {
		int[] result = null;
		int total = 0;
		int totalA = 0;
		int a = 0;
		
		for(int i = 0; i<player.size(); i++){
			total = total + (player.get(i)).getValue().getIntValue();
		}
			
		for(int j = 0; j<player.size();j++) {
			if((player.get(j)).getValue().getIntValue()==1) {
				a++;
			}
		}
		
		if(a==0) {
			if(total<=21) {
				result = new int[] {total};
			}
		}else {
			for(int b = 0; b<a; b++) {
				totalA = total+10;
			}
			if(totalA<=21) {
				result = new int[] {total,totalA};
			}else if(total<=21) {
				result = new int[] {total};
			}
		}
		return result;
	}
		
	public int getPlayerCardsEvaluation() {
		int a = 0;
		if(choosenumP()==0) {
			a=BUST;
		}else if(choosenumP()<21) {
			a=LESS_THAN_21;
		}else if(choosenumP() == 21) {
			if(player.size()==2) {
				a=BLACKJACK;
			}else {
				a=HAS_21;
			}
		}		
		return a;
	}
	
	public void playerHit() {
		player.add(list.remove(0));
		
		int num = getPlayerCardsEvaluation();
		if(num == BUST) {
			status = DEALER_WON;
		}
	}
	
	public int choosenumD() {
		int[] deckdealer = getDealerCardsTotal();

		if (deckdealer != null) {
			return deckdealer[deckdealer.length-1];
		}else {
			return 0;
		}
		
	}
	
	public int choosenumP() {
		int[] deckplayer = getPlayerCardsTotal();

		if (deckplayer != null) {
			return deckplayer[deckplayer.length-1];
		}else {
			return 0;
		}
		
	}
	
	public void playerStand() {
		dealer.get(0).setFaceUp();
		
		while(getDealerCardsEvaluation() == LESS_THAN_21 && choosenumD()<16) {
			dealer.add(list.remove(0));
		}
		
		if(getPlayerCardsEvaluation() == BLACKJACK){
			status = PLAYER_WON;
			account = account+bet+bet;
		}else if(getDealerCardsEvaluation() == BLACKJACK){
			status = DEALER_WON;
		}else if(getPlayerCardsEvaluation() == BUST){
			status = DEALER_WON;
		}else if(getDealerCardsEvaluation() == BUST) {
			status = PLAYER_WON;
			account = account+bet+bet;
		}else if(getPlayerCardsEvaluation() == HAS_21){
			status = PLAYER_WON;
			account = account+bet+bet;
		}else if(getDealerCardsEvaluation() == HAS_21){
			status = DEALER_WON;
		}else if(getDealerCardsEvaluation() == getPlayerCardsEvaluation()) {
			if(getDealerCardsEvaluation() == LESS_THAN_21){
				if(choosenumD()==choosenumP()) {
					status = DRAW;
					account = account+bet;
				}else if(choosenumD()>choosenumP()) {
					status = DEALER_WON;
				}else if(choosenumD()<choosenumP()) {
					status = PLAYER_WON;
					account = account+bet+bet;
				}
			}else {
				status = DRAW;
			}
		}
	}
			
	public int getGameStatus() {
		return status;
	}
		
	public void setBetAmount(int amount) {
		bet = amount;
	}
	
	public int getBetAmount() {
		return bet;
	}
	
	public void setAccountAmount(int amount) {	
		account = amount;
	}
	
	public int getAccountAmount() {
		return account;
	}
	
	/* Feel Free to add any private methods you might need */
}