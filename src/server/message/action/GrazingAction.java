package server.message.action;

import model.Trait;

public class GrazingAction extends ActionMessage {

    final int number;

    GrazingAction(int number){
        super(Trait.GRAZING);

        this.number = number;
    }

    public int getGrazingNumber() {
        return number;
    }
}
