package model;

public class Card {

    private Trait up;
    private Trait down;

    Card(Trait trait){
        up = trait;
        down = trait;
    }
    Card(Trait upTrait, Trait downTrait){
        up = upTrait;
        down = downTrait;
    }

    Trait getTrait(boolean isUp){
        if(isUp) return up;
        return down;
    }

}
