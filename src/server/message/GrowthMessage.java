package server.message;

import model.Card;
import model.Creature;
import model.Phase;

public class GrowthMessage extends Message{

    Creature creature;
    Card card;
    boolean isUp;

    //Развитие
    GrowthMessage(Phase phase, Creature creature, Card card, boolean isUp){
        super(Phase.GROWTH);

        this.creature = creature;
        this.card = card;
        this.isUp = isUp;

    }

}
