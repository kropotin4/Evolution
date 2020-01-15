package model.decks;

import model.Card;
import model.CardsStack;

import java.io.Serializable;
import java.util.ArrayList;

/******************************************
 * The deck with players card.
 *
 * @author akropotin
 *
 * @see Card
 ******************************************/
public class PlayerCardDeck implements Serializable {
    private ArrayList<CardsStack> deck = new ArrayList<>(12);

    /**********************
     * Get player card deck.
     * @return ArrayList with player cards.
     * @see Card
     **********************/
    public ArrayList<CardsStack> getCardDeck(){
        return deck;
    }

    /**********************
     * Add card to player deck
     * @param card new card from common deck.
     * @see Card
     **********************/
    public void addCard(Card card){
        for(CardsStack cardsStack : deck){
            if(cardsStack.getCard().equals(card)){
                cardsStack.add(card);
                return;
            }
        }
        deck.add(new CardsStack(card));
    }
    /**********************
     * Remove card from player deck.
     * @param card remove card.
     * @see Card
     **********************/
    public boolean removeCard(Card card){
        for(CardsStack cardsStack : deck){
            if(cardsStack.getCard() == card){
                if(cardsStack.getCardsNumber() == 1){
                    return deck.remove(cardsStack);
                }
                else{
                    cardsStack.reduce();
                    return true;
                }
            }
        }
        return false;
    }

    /**********************
     * Strange function.
     * @return card
     * @see Card
     **********************/
    public Card getCard(){
        return deck.get(0).getCard();
    }

    /**********************
     * Get number of cards in player deck.
     * @return cards number
     * @see Card
     **********************/
    public int getCardsNumber(){
        int res = 0;
        for(CardsStack cardsStack : deck){
            res += cardsStack.getCardsNumber();
        }
        return res;
    }
}
