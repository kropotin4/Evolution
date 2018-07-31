package server.message;

import model.Creature;
import model.Phase;
import model.Trait;

import java.util.ArrayList;

public class EatingMessage extends Message{

    int type;

    Creature  eating;

    Trait trait;

    Creature attacker;
    Creature defending;

    int playerDegending;

    ArrayList<Trait> traits;

    //Питание:
    public EatingMessage(Phase phase, Creature eating){ //Взятие еды из К.Б. (Существо)
        super(Phase.EATING, MessageType.EATING);
        type = 0;

        this.eating = eating;
    }
    public EatingMessage(Phase phase, Creature eating, Trait trait){ //Взятие еды из К.Б. + Топотун
        super(Phase.EATING, MessageType.EATING);
        type = 1;

        this.eating = eating;
        this.trait = trait;
    }
    public EatingMessage(Phase phase, Creature attacker, int playerDefending, Creature defending){ //Атака существа (Существо + Свойства, Существо) Пока без свойств
        super(Phase.EATING, MessageType.EATING);
        type = 2;

        this.attacker = attacker;
        this.defending = defending;
        this.playerDegending = playerDefending; // Атакует тот, кто ходит.
    }
    public EatingMessage(Phase phase, Creature defending, ArrayList<Trait> traits){ //Защита от атаки (Существо + Свойства)
        super(Phase.EATING, MessageType.EATING);
        type = 3;

        this.defending = defending;
        this.traits = traits;
    }
}
