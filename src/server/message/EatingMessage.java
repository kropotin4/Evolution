package server.message;

import model.*;

import java.util.ArrayList;
import java.util.UUID;

/***************************************
 * Сообщения о действиях в фазе питания
 **************************************/


public class EatingMessage extends Message{

    public EatingMessage(Table table){
        super(Phase.EATING, MessageType.EATING);
        setTable(table);
    }
    public EatingMessage(Table table, String message){
        super(Phase.EATING, MessageType.EATING);
        setTable(table);
        setMes(message);
    }

}
