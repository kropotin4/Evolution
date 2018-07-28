package model.decks;

import model.Card;

import java.util.ArrayList;

public class PlayerCardDeck {
    private ArrayList<Card> deck = new ArrayList<>(12);

    public PlayerCardDeck(){}

    public ArrayList<Card> getCardDeck(){
        return deck;
    }

    public void addCard(Card card){
        deck.add(card);
    }

    public boolean removeCard(Card card){
        return deck.remove(card);
    }
}
