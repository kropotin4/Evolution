package model;

import java.io.Serializable;

/******************************************
 * Class for link pair of creations with symbiosis trait.
 *
 * @author akropotin
 *
 * @see Card
 ******************************************/
public class SymbiosisPair implements Serializable {

    public final Creature crocodile;
    public final Creature bird;
    public final Card card;

    /**********************
     * SymbiosisPair class constructor.
     * @param crocodile main creation in link.
     * @param bird defend creation in link.
     * @param card card with pait trait.
     * @see Card
     **********************/
    public SymbiosisPair(Creature crocodile, Creature bird, Card card){
        this.crocodile = crocodile;
        this.bird = bird;
        this.card = card;
    }

    /**********************
     * Function that checks for the presence of a creature in the link.
     * @param creature checking creation.
     * @see Card
     **********************/
    public boolean haveCreature(Creature creature){
        return crocodile == creature || bird == creature;
    }
}
