package server.message;

import model.Card;
import model.Creature;
import model.Phase;

import java.util.UUID;

/***************************************
 * Сообщения о действиях в фазе роста
 *
 *      type = 0 : Положить свойство на существо
 *      type = 1 : Положить парное свойство на 2 существ
 *
 **************************************/

public class GrowthMessage extends Message{

    private final int creature1;
    private final int creature2;
    private final Card card;
    private final boolean isUp;

    private final int type;

    //Развитие
    public GrowthMessage(int creature, Card card, boolean isUp){
        super(Phase.GROWTH, MessageType.GROWTH);

        this.creature1 = creature;
        this.creature2 = creature;
        this.card = card;
        this.isUp = isUp;

        type = 0;
    }

    public GrowthMessage(int creature1,int creature2, Card card, boolean isUp){
        super(Phase.GROWTH, MessageType.GROWTH);

        this.creature1 = creature1;
        this.creature2 = creature2;
        this.card = card;
        this.isUp = isUp;

        type = 1;
    }

    public int getFirstCreatureId(){
        return  creature1;
    }
    public int getSecondCreatureId(){
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
