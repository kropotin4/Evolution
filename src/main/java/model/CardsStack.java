package model;

import java.io.Serializable;
import java.util.ArrayList;

public class CardsStack implements Serializable {

    ArrayList<Card> cards = new ArrayList<>();


    public CardsStack(Card card){
        cards.add(card);
    }

    public void add(Card card){
        cards.add(card);
    }
    public boolean reduce(){
        if(cards.size() > 0) {
            cards.remove(0);
            return true;
        }
        return false;
    }

    public Card getCard(){
        return cards.get(0);
    }
    public int getNumber(){
        return cards.size();
    }
}
