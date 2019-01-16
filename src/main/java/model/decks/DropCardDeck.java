package model.decks;

import model.Card;

import java.io.Serializable;
import java.util.ArrayList;

public class DropCardDeck implements Serializable {
    private ArrayList<Card> deck = new ArrayList<>(12);

    public DropCardDeck(){}

    public ArrayList<Card> getCardDeck(){
        return deck;
    }
    public int getCardsNumber(){
        return deck.size();
    }

    public void addCard(Card card){
        deck.add(card);
    }

}
