package model;

import java.io.Serializable;
import java.util.ArrayList;

/****************************
 * responsible for the creature
 * **************************/

public class Creature implements Serializable {

    ///region fields
    private static int commonID = 0;
    private final int id;

    private Player player;

    private int totalHunger = 1;
    private int totalSatiety = 0;
    private int fatCapacity = 0;
    private int fatQuantity = 0;

    private boolean isHibernating = false;
    private boolean wasHibernating = false;
    private boolean isAttacked = false;
    private boolean isPoisoned = false;
    private boolean isPirated = false;

    private boolean isPredator = false;
    private boolean isBig = false;
    private boolean isRunning = false;
    private boolean isMimetic = false;
    private boolean isGrazing = false;
    private boolean isGrazingActive = false;
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
    ArrayList<Creature> crocodileList = new ArrayList<>();

    //birds: can not eat if this creature is hungry; can not be eaten if this animal is alive
    ArrayList<Creature> birdList = new ArrayList<>();


    //Creature`s traits list (in order of obtaining)
    ArrayList<Card> cards = new ArrayList<>();

    ///endregion

    public Creature(Player player){
        this.player = player;
        id = commonID++;
    }

    /**Adds new trait if possible; do some checks; changes constants
     * @return false if trait has not added
     */
    public boolean addTrait(Card card){
        if (card.getTrait() == Trait.FAT_TISSUE){
            ++fatCapacity;
            cards.add(card);
            return true;
        }

        /*according to rules you can not put two equal cards (except fat tissue) to the same creature*/
        for(Card c : cards) {
            if(c == card)
                return false;
        }

        /*according to rules a predator can not be a scavenger so
        if smb puts a predator card to scavenger or scavenger card to predator
        the last card should be removed from the animal*/
        if ((isScavenger && (card.getTrait() == Trait.PREDATOR)) || (isPredator && (card.getTrait() == Trait.SCAVENGER)))
            return true; //actually has not added any traits

        cards.add(card);
        totalHunger += card.getTrait().getHunger();
        return switchTrait(card.getTrait(), true);
    }
    public boolean findTrait(Trait trait){
        switch (trait){
            case PREDATOR: return isPredator;
            case HIGH_BODY: return isBig;
            case RUNNING: return isRunning;
            case MIMICRY: return isMimetic;
            case GRAZING: return isGrazing;
            case POISONOUS: return isPoisonous;
            case TAIL_LOSS: return isTailLossable;
            case HIBERNATION: return isHibernatable;
            case SCAVENGER: return isScavenger;
            case PIRACY: return isPirate;
            case BURROWING: return isBurrowing;
            case CAMOUFLAGE: return isCamouflaged;
            case SHARP_VISION: return isSharp;
            case PARASITE: return isInfected;
            case SWIMMING: return isSwimming;
            case FAT_TISSUE: return fatCapacity > 0;
        }
        return false;
    }
    public boolean canAddTrait(Trait trait){
        if(trait == Trait.FAT_TISSUE)
            return true;
        else if(isPairTrait(trait)){
            switch (trait){
                case COOPERATION:
                    if(cooperationList.size() >= 2) return false;
                    return cooperationList.size() != (player.getCreatures().size() - 1);
                case COMMUNICATION:
                    if(communicationList.size() >= 2) return false;
                    return communicationList.size() != (player.getCreatures().size() - 1);
                case SYMBIOSIS:
                    if((birdList.size() + crocodileList.size()) >= 2) return false;
                    return (birdList.size() + crocodileList.size()) != (player.getCreatures().size() - 1);
            }
        }

        return !findTrait(trait);
    }
    public boolean canAddPairTrait(Trait trait, Creature secondCreature){
        switch (trait){
            case COOPERATION:
                /*for(CreaturesPair creaturesPair : player.cooperationCreatures){
                    if(creaturesPair.haveCreature(this) && creaturesPair.haveCreature(secondCreature))
                        return false;
                }*/
                for(Creature creature : cooperationList){
                    if(creature == secondCreature)
                        return false;
                }//Старая версия
                break;
            case COMMUNICATION:
                /*for(CreaturesPair creaturesPair : player.communicationCreatures){
                    if(creaturesPair.haveCreature(this) && creaturesPair.haveCreature(secondCreature))
                        return false;
                }*/
                for(Creature creature : communicationList){
                    if(creature == secondCreature)
                        return false;
                }//Старая версия
                break;
            case SYMBIOSIS:
                for(Creature creature : crocodileList){
                    if(creature == secondCreature)
                        return false;
                }
                for(Creature creature : birdList){
                    if(creature == secondCreature)
                        return false;
                }
                break;
        }

        return true;
    }
    boolean removeTrait(Card card) {
        cards.remove(card);
        if(card.isFat()) --fatQuantity;

        if (card.getTrait() == Trait.FAT_TISSUE){
            --fatCapacity;
            return true;
        }

        totalHunger -= card.getTrait().getHunger();
        return switchTrait(card.getTrait(), false);
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
                return true;
            case COMMUNICATION:
                if (communicationList.contains(creature)) return false;
                communicationList.add(creature);
                return true;
        }
        return false;
    }
    boolean addSymbiosisTrait(Creature creature, boolean isCrocodile){
        if(isCrocodile){
            if(crocodileList.contains(creature)) return false;
            crocodileList.add(creature);
            return true;
        }
        else{
            if(birdList.contains(creature)) return false;
            birdList.add(creature);
            return true;
        }
    }
    boolean removePairTrait(Trait trait, Creature creature){
        switch (trait) {
            case COOPERATION:

                return cooperationList.remove(creature);

            case COMMUNICATION:

                return communicationList.remove(creature);

            case SYMBIOSIS:

                if (!crocodileList.remove(creature))
                    return birdList.remove(creature);

                return true;
        }
        return false;
    }
    public static boolean isPairTrait(Trait trait){
        if(trait == Trait.COMMUNICATION
        || trait == Trait.COOPERATION
        || trait == Trait.SYMBIOSIS)
            return true;

        return false;
    }

    public void addFood(){
        if(totalSatiety < totalHunger)
            ++totalSatiety;
    }
    public void addFat(){
        if(fatQuantity < fatCapacity){
            ++fatQuantity;
           for(Card card : cards){
               if(card.getTrait() == Trait.FAT_TISSUE){
                   if(!card.isFat()) {
                       card.setFat(true);
                       break;
                   }
               }
           }
        }
    }
    public boolean isFed(){
        return totalSatiety == totalHunger;
    }
    public void setHunger(){
        totalSatiety = 0;
    }
    public int getTotalHunger(){
        return totalHunger;
    }
    public int getTotalSatiety(){
        return totalSatiety;
    }
    public boolean isSatisfied(){
        return (fatCapacity == fatQuantity) && isFed();
    }

    public boolean setGrazingActive(boolean isActive){
        if(!isGrazing) return false;
        isGrazingActive = isActive;
        return true;
    }
    public boolean isGrazingActive(){
        return isGrazingActive;
    }

    public boolean isPoisoned(){
        return isPoisoned;
    }

    void attack(Creature creature){

        if (creature.isPoisonous) isPoisoned = true;

        creature.player.killCreature(creature);

        for (int i = 0; i < 2; ++i){
            if (!isFed()) ++totalSatiety;
            else if (!isSatisfied()) ++fatQuantity;
        }

        isAttacked = true;
    }

    public boolean isAttackPossible(Creature creature){
        if ((creature.isCamouflaged && !this.isSharp)
        || (creature.isBurrowing && creature.isFed())
        || (!creature.crocodileList.isEmpty())
        || (this.isSwimming != creature.isSwimming)
        || (creature.isBig && !this.isBig))
            return false;

        return true;
    }
    // Проверка необходима, чтобы выявить те случаи, когда защищающийся ничего не может сделать
    // ==>> Не нужно присылать ему сообщения
    public boolean isAbsoluteAttackPossible(Creature creature){
        if(!isAttackPossible(creature))
            return false;

        if(creature.isTailLossable
        || creature.isMimetic)
            return false;

        //TODO: Дописать все свойства

        return true;
    }

    boolean defend(Trait trait, Card card){
        //TODO: И так понятно

        if(trait == Trait.RUNNING){
            if(Dice.rollOneDice() <= 3){
                return false;
            }
        }

        return true;
    }

    boolean getFood (){
        if (player.table.isFodderBaseEmpty()) return false;
        else if (this.isSatisfied()) return false;
        if (isFed()) {
            ++fatQuantity;
        } else {
            ++totalSatiety;
        }
        return true;
    }
    boolean stealFood(Creature creature){
        if (this.isSatisfied()) return false;
        if (creature.isFed() || creature.totalSatiety == 0) return false;
        --creature.totalSatiety;
        ++this.totalSatiety;
        return true;
    }

    public Player getPlayer(){
        return player;
    }
    public int getId(){
        return id;
    }

    public boolean findCard(Card card){
        return cards.contains(card);
    }
    public ArrayList<Card> getCards(){
        return cards;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Creature " + id + ":\n\tTraits:\n");
        for (Card card : cards){
            str.append("\t\t>" + card.getTrait() + "\n");
        }
        str.append("\tSatisfaction: " + totalSatiety + "\\" + totalHunger + "\n");
        str.append("\tFatness: " + fatQuantity + "\\" + fatCapacity + "\n");
        if (!cooperationList.isEmpty()) {
            str.append("\tCooperated with: ");
            for (Creature creature : cooperationList) {
                str.append(creature.id + " ");
            }
            str.append("\n");
        }

        if (!communicationList.isEmpty()) {
            str.append("\tCommunicated with: ");
            for (Creature creature : communicationList) {
                str.append(creature.id + " ");
            }
            str.append("\n");
        }

        if (!crocodileList.isEmpty()) {
            str.append("\tSymbionts: ");
            for (Creature creature : crocodileList) {
                str.append(creature.id + " ");
            }
            str.append("\n");
        }

        if (!birdList.isEmpty()) {
            str.append("\tIs symbiont for: ");
            for (Creature creature : birdList) {
                str.append(creature.id + " ");
            }
            str.append("\n");
        }
        return str.toString();
    }
}