package model;

import org.jetbrains.annotations.Contract;

import java.io.Serializable;

public class Card implements Serializable {

    private static int idCounter = 0;
    private int id = idCounter;

    private boolean fat = false;

    private Trait up;
    private Trait down;
    private boolean isUp;

    public Card(Trait trait){
        idCounter++;
        up = trait;
        down = trait;
    }
    public Card(Trait upTrait, Trait downTrait){
        idCounter++;
        up = upTrait;
        down = downTrait;
    }

    public Trait getTrait(){
        return isUp ? up : down;
    }
    public Trait getTrait(boolean isUp){
        return isUp ? up : down;
    }
    public boolean isUp(){
        return isUp;
    }

    public void turnCard(){
        isUp = !isUp;
    }

    public void setFat(boolean fat) {
        this.fat = fat;
    }
    public boolean isFat() {
        return fat;
    }

    public int getId(){
        return id;
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;

        if(!(obj instanceof Card)) return false;

        Card card = (Card) obj;

        //if(card.getId() != id) return false; // !!!!!!!!!!!!!

        if((card.up == up && card.down == down)
                || (card.up == down && card.down == up))
            return true;

        return false;
    }

    @Override
    public String toString() {
        if(up == down)
            return "Card{" +
                    "id=" + id +
                    ", trait=" + up +
                    '}';
        else
            return "Card{" +
                    "id=" + id +
                    ", up=" + up +
                    ", down=" + down +
                    '}';
    }
}