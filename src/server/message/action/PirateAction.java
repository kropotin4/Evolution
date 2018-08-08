package server.message.action;

import model.Trait;

import java.util.UUID;

public class PirateAction extends ActionMessage {

    final UUID pirateCreature;
    final UUID victimCreature;

    public PirateAction(UUID pirateCreature, UUID victimCreature){
        super(Trait.PIRACY);

        this.pirateCreature = pirateCreature;
        this.victimCreature = victimCreature;
    }

    public UUID getPirateCreature(){
        return pirateCreature;
    }

    public UUID getVictimCreature() {
        return victimCreature;
    }
}
