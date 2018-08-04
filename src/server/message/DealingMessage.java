package server.message;

import model.Card;
import model.Creature;
import model.Phase;
import model.Player;

public class DealingMessage extends Message{

    Player player;

    //Сдача карт
    public DealingMessage(Player player){
        super(Phase.DEALING, MessageType.DEALING);

        this.player = player;

    }

}
