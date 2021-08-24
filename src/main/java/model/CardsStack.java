package model;

import java.io.Serializable;
import java.util.ArrayList;


/******************************************
 * This class used for stack the same cards in PlayerCardDeck.
 * Its reduce size of player deck window.
 *
 * @author akropotin
 *
 * @see Card
 * @see model.decks.PlayerCardDeck
 ******************************************/
public class CardsStack implements Serializable {

    ArrayList<Card> cards = new ArrayList<>();

    /**********************
     * CardsStack class constructor.
     * @param card first card of stack.
     * @see Card
     **********************/
    public CardsStack(Card card){
        cards.add(card);
    }


    /**********************
     * Add card to stack.
     * @param card card with the same type that stack cards.
     * @see Card
     **********************/
    public void add(Card card){
        cards.add(card);
    }

    /**********************
     * Reduce card number.
     * @return true -- if reduce was success
     * @see Card
     **********************/
    public boolean reduce(){
        if(cards.size() > 0) {
            cards.remove(0);
            return true;
        }
        return false;
    }


    /**********************
     * Get card.
     * @return card of stack
     * @see Card
     **********************/
    public Card getCard(){
        return cards.get(0);
    }

    /**********************
     * Get cards number.
     * @return number of cards
     * @see Card
     **********************/
    public int getCardsNumber(){
        return cards.size();
    }
}
