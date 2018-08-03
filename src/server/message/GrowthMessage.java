package server.message;

import model.Card;
import model.Creature;
import model.Phase;

import java.util.UUID;

public class GrowthMessage extends Message{

    private final UUID creature1;
    private final UUID creature2;
    private final Card card;
    private final boolean isUp;

    private final int type;

    //Развитие
    public GrowthMessage(UUID creature, Card card, boolean isUp){
        super(Phase.GROWTH, MessageType.GROWTH);

        this.creature1 = creature;
        this.creature2 = creature;
        this.card = card;
        this.isUp = isUp;

        type = 0;
    }

    public GrowthMessage(UUID creature1,UUID creature2, Card card, boolean isUp){
        super(Phase.GROWTH, MessageType.GROWTH);

        this.creature1 = creature1;
        this.creature2 = creature2;
        this.card = card;
        this.isUp = isUp;

        type = 1;
    }

    public UUID getFirstCreatureId(){
        return  creature1;
    }
    public UUID getSecondCreatureId(){
        return  creature2;
    }

    public Card getCard(){
        return card;
    }
    public boolean isUp(){
        return isUp;
    }

    public int getType(){
        return type;
    }
}
