package server.message;

import model.Creature;
import model.Phase;
import model.Trait;

import java.util.ArrayList;

public class EatingMessage extends Message{

    Creature  eating;

    Trait trait;

    Creature attacker;
    Creature defending;

    int playerDegending;

    ArrayList<Trait> traits;

    //Питание:
    EatingMessage(Phase phase, Creature eating){ //Взятие еды из К.Б. (Существо)
        super(Phase.EATING);

        this.eating = eating;
    }
    EatingMessage(Phase phase, Creature eating, Trait trait){ //Взятие еды из К.Б. + Топотун
        super(Phase.EATING);

        this.eating = eating;
        this.trait = trait;
    }
    EatingMessage(Phase phase, Creature attacker, int playerDefending, Creature defending){ //Атака существа (Существо + Свойства, Существо) Пока без свойств
        super(Phase.EATING);

        this.attacker = attacker;
        this.defending = defending;
        this.playerDegending = playerDefending; // Атакует тот, кто ходит.
    }
    EatingMessage(Phase phase, Creature defending, ArrayList<Trait> traits){ //Защита от атаки (Существо + Свойства)
        super(Phase.EATING);

        this.defending = defending;
        this.traits = traits;
    }
}
