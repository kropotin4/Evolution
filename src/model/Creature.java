package model;

import java.util.ArrayList;

/****************************
 * responsible for the creature
 * **************************/

public class Creature {

    ///region fields

    private Player player;

    private int totalHunger = 1;
    private int totalSatiety = 0;
    private int fatCapacity = 0;
    private int fatQuantity = 0;

    private boolean isHibernating = false;
    private boolean wasHibernating = false;
    private boolean isAttacked = false;
    private boolean isPoisoned = false;

    private boolean isPredator = false;
    private boolean isBig = false;
    private boolean isRunning = false;
    private boolean isMimetic = false;
    private boolean isGrazing = false;
    private boolean isPoisonous = false;
    private boolean isTailLossable = false;
    private boolean isHibernatable = false;
    private boolean isScavenger = false;
    private boolean isPirate = false;
    private boolean isBurrowing = false;
    private boolean isCamouflaged = false;
    private boolean isSharp = false;
    private boolean isInfected = false;
    private boolean isSwimming = false;

    ArrayList<Creature> communicationList = new ArrayList<>();
    ArrayList<Creature> cooperationList = new ArrayList<>();

    //crocodiles: this creature can not eat if any symbiont is hungry; this creature can not be eaten if any symbiont is alive
    ArrayList<Creature> symbiontList = new ArrayList<>();

    //birds: can not eat if this creature is hungry; can not be eaten if this animal is alive
    ArrayList<Creature> otherAnimalList = new ArrayList<>();


    //Creature`s traits list (in order of obtaining)
    ArrayList<Trait> traits = new ArrayList<>();

    ///endregion

    Creature(Player player){
        this.player = player;
    }

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

    boolean addPairTrait(Trait trait, Creature creature){
        switch (trait) {
            case COOPERATION:
                if (cooperationList.contains(creature)) return false;
                cooperationList.add(creature);
                //creature.cooperationList.add(this);
                return true;
            case COMMUNICATION:
                if (communicationList.contains(creature)) return false;
                communicationList.add(creature);
                //creature.communicationList.add(this);
                return true;
            case SYMBIOSYS:
                if (true) {
                    if (symbiontList.contains(creature)) return false;
                    symbiontList.add(creature);
                    //creature.symbiontList.add(this);
                    return true;
                } else {
                    if (otherAnimalList.contains(creature)) return false;
                    otherAnimalList.add(creature);
                    //creature.otherAnimalList.add(this);
                    return true;
            }
        }
        return false;
    }
    boolean removePairTrait(Trait trait, Creature creature){
        switch (trait) {
            case COOPERATION:
                if (cooperationList.contains(creature)) {
                    cooperationList.remove(creature);
                    //creature.cooperationList.remove(this);
                    return true;
                }
                break;
            case COMMUNICATION:
                if (communicationList.contains(creature)) {
                    communicationList.remove(creature);
                    //creature.communicationList.remove(this);
                    return true;
                }
                break;
            case SYMBIOSYS:
                if (true) {
                    if (symbiontList.contains(creature)) {
                        symbiontList.remove(creature);
                        //creature.symbiontList.remove(this);
                        return true;
                    }
                } else {
                    if (otherAnimalList.contains(creature)) {
                        otherAnimalList.remove(creature);
                        //creature.otherAnimalList.remove(this);
                        return true;
                    }
                } break;
        }
        return false;
    }

    boolean isFed(){
        return (totalSatiety == totalHunger);
    }

    boolean isSatisfied(){
        return (isFed() && (fatCapacity == fatQuantity));
    }

    boolean attack (Creature creature){
        if ((creature.isCamouflaged && !this.isSharp)
        || (creature.isBurrowing && creature.isFed())
        || (!creature.symbiontList.isEmpty())
        || (this.isSwimming != creature.isSwimming)
        || (creature.isBig && !this.isBig))
            return false;
        //Todo: tail loss
        //Todo: mimicry
        if (creature.isPoisonous) isPoisoned = true;

        creature.player.killCreature(creature);
        for (int i = 0; i < 2; ++i){
            if (!isFed()) ++totalSatiety;
            else if (!isSatisfied()) ++fatQuantity;
        }
        isAttacked = true;
        return true;
    }

    boolean getFood (){
        if (Table.isFodderBaseEmpty()) return false;
        else if (this.isSatisfied()) return false;
        if (!isFed()) ++totalSatiety; else ++fatQuantity;
        return true;
    }

    boolean stealFood(Creature creature){
        if (this.isSatisfied()) return false;
        if (creature.isFed() || creature.totalSatiety == 0) return false;
        --creature.totalSatiety;
        ++this.totalSatiety;
        return true;
    }
}