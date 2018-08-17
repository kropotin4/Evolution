package model;

public class Card {

    private static int idCounter = 0;
    private int id = idCounter;

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

    public void turnCard(){
        isUp = !isUp;
    }

    public int getId(){
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;

        if(!(obj instanceof Card)) return false;

        Card card = (Card) obj;

        if((card.up != up && card.down != down)
                || (card.up != down && card.down != up))
            return false;

        return true;
    }
}
