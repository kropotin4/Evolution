package model;

import java.io.Serializable;

public class CreaturesPair implements Serializable {

    public final Creature firstCreature;
    public final Creature secondCreature;
    public final Card card;

    public CreaturesPair(Creature firstCreature, Creature secondCreature, Card card){
        this.firstCreature = firstCreature;
        this.secondCreature = secondCreature;
        this.card = card;
    }

    public boolean haveCreature(Creature creature){
        return creature == firstCreature || creature == secondCreature;
    }
    public boolean haveCreatures(Creature creature1, Creature creature2){
        return haveCreature(creature1) && haveCreature(creature2);
    }
    public Creature getAnotherCreature(Creature creature){
        if(creature == firstCreature){
            return secondCreature;
        }
        else if(creature == secondCreature){
            return firstCreature;
        }
        return null;
    }
}
