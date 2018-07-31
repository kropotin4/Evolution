package server.message;

import model.Phase;

public class SpecialMessage extends  Message {

    public SpecialMessage(Phase phase){
        super(phase, MessageType.SPECIAL);
    }

}
