package server.message;

import model.Phase;

public class CFBMessage extends Message{

    //Определение К.Б.
    CFBMessage(Phase phase){
        super(Phase.CALC_FODDER_BASE);
    }

}
