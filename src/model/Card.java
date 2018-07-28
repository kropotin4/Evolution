package model;

public class Card {

    final boolean isDouble;

    private Trait up;
    private Trait down;

    Card(Trait trait){
        isDouble = false;
        up = trait;
    }
    Card(Trait upTrait, Trait downTrait){
        isDouble = true;

        up = upTrait;
        down = downTrait;
    }

    Trait getTrait(boolean isUp){
        if(!isDouble) return up;

        if(isUp) return up;
        return down;
    }

    Trait getTrait(){
        return up;
    }
}
