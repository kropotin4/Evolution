package model;

public class Card {

    private Trait up;
    private Trait down;
    private boolean isUp;

    public Card(Trait trait){
        up = trait;
        down = trait;
    }
    public Card(Trait upTrait, Trait downTrait){
        up = upTrait;
        down = downTrait;
    }

    public Trait getTrait(){
        return isUp ? up : down;
    }

    public void turnCard(){
        isUp = !isUp;
    }
}
