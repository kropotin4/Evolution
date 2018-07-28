package model.decks;

import model.Card;

import java.util.ArrayList;

public class DropCardDeck {
    private ArrayList<Card> deck = new ArrayList<>(12);

    DropCardDeck(){}

    ArrayList<Card> getCardDeck(){
        return deck;
    }

    public void addCard(Card card){
        deck.add(card);
    }

}
