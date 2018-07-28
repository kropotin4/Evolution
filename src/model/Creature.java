package model;

import java.util.ArrayList;

/****************************
 * responsible for the creature
 * **************************/

public class Creature {

    int totalHunger = 1;
    int fatCapacity = 0;
    int fatSize = 0;

    boolean isPredator = false;
    boolean isBig = false;
    boolean isRunning = false;
    boolean isMimetic = false;
    boolean isGrazing = false;
    boolean isPoisonous = false;
    boolean isTailLossable = false;
    boolean isHibernatable = false;
    boolean isScavenger = false;
    boolean isPirate = false;
    boolean isBurrowing = false;
    boolean isCamouflaged = false;
    boolean isSharp = false;
    boolean isInfected = false;
    boolean isSwimming = false;

    ArrayList<Creature> communicationList = new ArrayList<>();
    ArrayList<Creature> cooperationList = new ArrayList<>();

    //crocodile: this creature can not eat if any symbiont is hungry; this creature can not be eaten if any symbiont is alive
    ArrayList<Creature> symbiontList = new ArrayList<>();

    //birds: can not eat if this creature is hungry; can not be eaten if this animal is alive
    ArrayList<Creature> otherAnimalList = new ArrayList<>();


    //Creature`s traits list (in order of obtaining)
    ArrayList<Trait> traits = new ArrayList<>();

    Creature(){}

    /**Adds new trait if possible; do some checks; changes constants
     * @returns false if trait has not added
     * @returns true if trait is added
     */
    boolean addTrait(Trait trait){
        if (trait == trait.PREDATOR){
            ++fatCapacity;
            traits.add(trait);
            return true;
        }

        /*according to rules you can not put two equal cards (except fat tissue) to the same creature*/
        for(Trait t : traits) {
            if(t == trait)
                return false;
        }

        /*according to rules a predator can not be a scavenger so
        if smb puts a predator card to scavenger or scavenger card to predator
        the last card should be removed from the animal*/
        if (((trait == trait.PREDATOR) && (isScavenger)) || ((trait == trait.SCAVENGER) && (isPredator)))
            return true; //actually has not added any traits

        traits.add(trait);
        totalHunger += trait.getHunger();
        switch (trait){
            case PREDATOR: isPredator = true;
                break;
            case HIGH_BODY: isBig = true;
                break;
            case RUNNING: isRunning = true;
                break;
            case MIMICRY: isMimetic = true;
                break;
            case GRAZING: isGrazing = true;
                break;
            case POISONOUS: isPoisonous = true;
                break;
            case TAIL_LOSS: isTailLossable = true;
                break;
            case HIBERNATION: isHibernatable = true;
                break;
            case SCAVENGER: isScavenger = true;
                break;
            case PIRACY: isPirate = true;
                break;
            case BURROWING: isBurrowing = true;
                break;
            case CAMOUFLAGE: isCamouflaged = true;
                break;
            case SHARP_VISION: isSharp = true;
                break;
            case PARASITE: isInfected = true;
                break;
            case SWIMMING: isSwimming = true;
                break;
        }
        return true; 
    }
    //Удалить свойство
    void deleteTrait(Trait trait){
        traits.remove(trait);
    }
}
