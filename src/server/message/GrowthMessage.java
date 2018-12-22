package server.message;

import model.Card;
import model.Creature;
import model.Phase;
import model.Table;

import java.util.UUID;

/***************************************
 * Сообщения о действиях в фазе роста
 *
 **************************************/

public class GrowthMessage extends Message{

    //Развитие
    public GrowthMessage(Table table){
        super(Phase.GROWTH, MessageType.GROWTH);

        setTable(table);
    }

}
