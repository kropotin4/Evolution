package model;

import java.io.Serializable;

public class SymbiosisPair implements Serializable {

    public final Creature crocodile;
    public final Creature bird;
    public final Card card;

    public SymbiosisPair(Creature crocodile, Creature bird, Card card){
        this.crocodile = crocodile;
        this.bird = bird;
        this.card = card;
    }

    public boolean haveCreature(Creature creature){
        return crocodile == creature || bird == creature;
    }
}
