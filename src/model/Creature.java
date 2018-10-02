package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

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
    ArrayList<Card> cards = new ArrayList<>();

    ///endregion

    public Creature(Player player){
        this.player = player;
        id = commonID++;
    }

    public Player getPlayer(){
        return player;
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
            case FAT_TISSUE: return false; // Всегда false
        }
        return false;
    }
    boolean removeTrait(Card card) {
        cards.remove(card);

        return cancelTrait(card.getTrait()); //TODO: index deletion for fat tissue
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
    static boolean isPairTrait(Trait trait){
        if(trait == Trait.COMMUNICATION
        || trait == Trait.COOPERATION
        || trait == Trait.SYMBIOSYS)
            return true;

        return false;
    }

    public void addFood(){
        if(totalSatiety < totalHunger)
            ++totalSatiety;
    }
    public boolean isFed(){
        return totalSatiety == totalHunger;
    }
    public int getTotalHunger(){
        return totalHunger;
    }
    public int getTotalSatiety(){
        return totalSatiety;
    }
    boolean isSatisfied(){
        return (fatCapacity == fatQuantity) && isFed();
    }

    void attack (Creature creature){

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
        || (!creature.symbiontList.isEmpty())
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
        str.append("Creature " + id + ":\nTraits:\n");
        for (Card card : cards){
            System.out.print(">" + card.getTrait() + "\n");
        }
        str.append("Satisfaction: " + totalSatiety + "\\" + totalHunger + "\n");
        str.append("Fatness: " + fatQuantity + "\\" + fatCapacity + "\n");
        if (!cooperationList.isEmpty()) {
            str.append("Cooperated with: ");
            for (Creature creature : cooperationList) {
                str.append(creature.id + " ");
            }
            str.append("\n");
        }

        if (!communicationList.isEmpty()) {
            str.append("Communicated with: ");
            for (Creature creature : communicationList) {
                str.append(creature.id + " ");
            }
            str.append("\n");
        }

        if (!symbiontList.isEmpty()) {
            str.append("Symbionts: ");
            for (Creature creature : symbiontList) {
                str.append(creature.id + " ");
            }
            str.append("\n");
        }

        if (!otherAnimalList.isEmpty()) {
            str.append("Is symbiont for: ");
            for (Creature creature : otherAnimalList) {
                str.append(creature.id + " ");
            }
            str.append("\n");
        }
        return str.toString();
    }
}