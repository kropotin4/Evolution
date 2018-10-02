package model;

import java.io.Serializable;

public class CardPair implements Serializable {

    Card card;
    int number;

    public CardPair(Card card){
        this.card = card;
        number = 1;
    }

    public void add(){
        number++;
    }
    public boolean reduce(){
        number--;
        if(number <= 0) return false;
        return true;
    }

    public Card getCard(){
        return card;
    }
    public int getNumber(){
        return number;
    }
}
