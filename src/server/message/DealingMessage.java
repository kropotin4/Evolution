package server.message;

import model.Card;
import model.Creature;
import model.Phase;
import model.Player;

public class DealingMessage extends Message{

    Player player;

    //Развитие
    public DealingMessage(Phase phase, Player player){
        super(Phase.DEALING, MessageType.DEALING);

        this.player = player;

    }

}
