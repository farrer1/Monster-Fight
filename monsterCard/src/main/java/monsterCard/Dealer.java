package monsterCard;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.ArrayList;

public class Dealer {
	List<Card> store;
	List<Integer> dealt;
	
	//Default constructor, populates the Dealer with blank Card objects
	public Dealer() {
		store = new ArrayList<Card>();
		for(int i=0;i<9;i++) {
			store.add(new Card());
		}
		dealt = new ArrayList<Integer>();
	}
	
	//May want another constructor that populates the Dealer with Cards taken in from the database
	
	//Adds all cards from a list of cards to storage
	public void addCards(List<Card> new_cards) {
		for(int i = 0; i < new_cards.size(); i++) {
			store.add(new_cards.get(i));
		}
	}
	
	//Adds new blank Cards to 'store'
	public void addCards(int new_cards) {
		for(int i = 0; i < new_cards; i++) {
			store.add(new Card());
		}
	}
	
	//Provides a List containing random Card objects of length 'num'
	//When cards are dealt to players, their index in 'store' is added to 'dealt' so that we know that Card is taken
	//Stops dealing cards if all Cards have been dealt, and deals the rest as blank cards
	public List<Card> getCards(int num) {
		List<Card> tmp = new ArrayList<Card>();
		Random rand = new Random();
		int index = 0;
		for(int i = 0; i < num; i++) {
			//Adds 10 new blank Cards to deal out
			if(dealt.size()==store.size()) {
				this.addCards(10);
			}
			index = rand.nextInt(store.size());
			if(!dealt.contains(index)) {
				tmp.add(store.get(index));
				dealt.add(index);
			}else {
				i--;
			}
		}
		return tmp;
	}
	
	//Returns Cards that were previously dealt out by getCards()
	public void returnCard(List<Card> hand) throws NoSuchElementException{
		for(int i = 0; i< hand.size(); i++) {

			//First get the index of the card in the store
			int index_of_card = store.indexOf(hand.get(i));

			if(index_of_card == -1)  {
				throw new NoSuchElementException("Trying to return card that wasn't originally in deck");
			}else{
				//Remove the card. .Remove() only removes object if it exists
				dealt.remove(index_of_card);
			}
		}
	}
}