package model;

import java.io.Serializable;

public class SymbiosisPair implements Serializable {

    public final Creature crocodile;
    public final Creature bird;

    public SymbiosisPair(Creature crocodile, Creature bird){
        this.crocodile = crocodile;
        this.bird = bird;
    }

    public boolean haveCreature(Creature creature){
        return crocodile == creature || bird == creature;
    }
}
