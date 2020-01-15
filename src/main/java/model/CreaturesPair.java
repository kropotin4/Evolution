package model;

import java.io.Serializable;

/******************************************
 * Class for link pair of creations with pair trait (not symbiosis).
 *
 * @author akropotin
 *
 * @see Card
 ******************************************/
public class CreaturesPair implements Serializable {

    public final Creature firstCreature;
    public final Creature secondCreature;
    public final Card card;

    /**********************
     * CreaturesPair class constructor.
     * @param firstCreature first creation in link.
     * @param secondCreature second creation in link.
     * @param card card with pait trait.
     * @see Card
     **********************/
    public CreaturesPair(Creature firstCreature, Creature secondCreature, Card card){
        this.firstCreature = firstCreature;
        this.secondCreature = secondCreature;
        this.card = card;
    }

    /**********************
     * Function that checks for the presence of a creature in the link.
     * @param creature checking creation.
     * @see Card
     **********************/
    public boolean haveCreature(Creature creature){
        return creature == firstCreature || creature == secondCreature;
    }
    /**********************
     * Function that checks for the presence of a creatures in the link.
     * @param creature1 first checking creation.
     * @param creature2 second checking creation.
     * @see Card
     **********************/
    public boolean haveCreatures(Creature creature1, Creature creature2){
        return haveCreature(creature1) && haveCreature(creature2);
    }
}
