package model.decks;

import model.Card;
import model.CardPair;

import java.util.ArrayList;

public class PlayerCardDeck {
    private ArrayList<CardPair> deck = new ArrayList<>(12);


    public ArrayList<CardPair> getCardDeck(){
        return deck;
    }

    public void addCard(Card card){
        for(CardPair cardPair : deck){
            if(cardPair.getCard() == card){
                cardPair.add();
                return;
            }
        }
        deck.add(new CardPair(card));
    }

    public boolean removeCard(Card card){
        for(CardPair cardPair : deck){
            if(cardPair.getCard() == card){
                if(cardPair.getNumber() == 1){
                    return deck.remove(cardPair);
                }
                else{
                    cardPair.reduce();
                    return true;
                }
            }
        }
        return false;
    }

    public int getCardsNumber(){
        return deck.size();
    }
}
