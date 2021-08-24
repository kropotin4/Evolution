package model.decks;

import model.Card;

import java.io.Serializable;
import java.util.ArrayList;

/******************************************
 * The deck with dropped card.
 *
 * @author akropotin
 *
 * @see Card
 ******************************************/
public class DropCardDeck implements Serializable {
    private ArrayList<Card> deck = new ArrayList<>(12);


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
