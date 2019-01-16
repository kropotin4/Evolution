package model.decks;

import model.Card;
import model.CardsStack;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayerCardDeck implements Serializable {
    private ArrayList<CardsStack> deck = new ArrayList<>(12);


    public ArrayList<CardsStack> getCardDeck(){
        return deck;
    }

    public void addCard(Card card){
        for(CardsStack cardsStack : deck){
            if(cardsStack.getCard().equals(card)){
                cardsStack.add(card);
                return;
            }
        }
        deck.add(new CardsStack(card));
    }
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

    public Card getCard(){
        return deck.get(0).getCard();
    }

    public int getCardsNumber(){
        int res = 0;
        for(CardsStack cardsStack : deck){
            res += cardsStack.getCardsNumber();
        }
        return res;
    }
}
