package model;

import java.util.ArrayList;

/****************************
 * responsible for the creature
 * **************************/

public class Creature {

    int totalHunger = 1;
    int totalSatiety = 0;
    int fatCapacity = 0;
    int fatQuantity = 0;

    boolean isHibernating = false;
    boolean wasHibernating = false;

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
        return switchTrait(trait, true);
    }
    boolean removeTrait(Trait trait) {
        traits.remove(trait);
        return cancelTrait(trait); //TODO: index deletion for fat tissue
    }
    boolean cancelTrait(Trait trait){
        if (trait == trait.FAT_TISSUE){
            --fatCapacity;
            if (trait.FAT_TISSUE.isFatPlaced()){
                --fatQuantity;
            }
            return true;
        }

        totalHunger -= trait.getHunger();
        return switchTrait(trait, false);
    }
    boolean switchTrait(Trait trait, boolean turnOn){
        switch (trait){
            case PREDATOR: isPredator = turnOn;
                break;
            case HIGH_BODY: isBig = turnOn;
                break;
            case RUNNING: isRunning = turnOn;
                break;
            case MIMICRY: isMimetic = turnOn;
                break;
            case GRAZING: isGrazing = turnOn;
                break;
            case POISONOUS: isPoisonous = turnOn;
                break;
            case TAIL_LOSS: isTailLossable = turnOn;
                break;
            case HIBERNATION: isHibernatable = turnOn;
                break;
            case SCAVENGER: isScavenger = turnOn;
                break;
            case PIRACY: isPirate = turnOn;
                break;
            case BURROWING: isBurrowing = turnOn;
                break;
            case CAMOUFLAGE: isCamouflaged = turnOn;
                break;
            case SHARP_VISION: isSharp = turnOn;
                break;
            case PARASITE: isInfected = turnOn;
                break;
            case SWIMMING: isSwimming = turnOn;
                break;
        }
        return true;
    }

}