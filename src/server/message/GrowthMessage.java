package server.message;

import model.Card;
import model.Creature;
import model.Phase;

public class GrowthMessage extends Message{

    Creature creature;
    Card card;

    //Развитие
    public GrowthMessage(Creature creature, Card card){
        super(Phase.GROWTH, MessageType.GROWTH);

        this.creature = creature;
        this.card = card;

    }
}
