package model;

public class Card {

    private Trait up;
    private Trait down;

    public Card(Trait trait){
        up = trait;
        down = trait;
    }
    public Card(Trait upTrait, Trait downTrait){
        up = upTrait;
        down = downTrait;
    }

    public Trait getTrait(boolean isUp){
        if(isUp) return up;
        return down;
    }
}
