package model;

import java.io.Serializable;

public class CreaturesPair implements Serializable {

    public final Creature firstCreature;
    public final Creature secondCreature;

    public CreaturesPair(Creature firstCreature, Creature secondCreature){
        this.firstCreature = firstCreature;
        this.secondCreature = secondCreature;
    }

    public boolean haveCreature(Creature creature){
        return creature == firstCreature || creature == secondCreature;
    }
}
