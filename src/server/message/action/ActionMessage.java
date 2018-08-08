package server.message.action;

import model.Trait;
import server.message.Message;

public class ActionMessage {

    Trait trait;

    ActionMessage(Trait trait){
        this.trait = trait;
    }

    public Trait getTrait() {
        return trait;
    }
}
