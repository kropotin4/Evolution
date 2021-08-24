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

    private Player player;  // Owner of creature

    // if creature totalSatiety < totalHunger in the end of round -> dead
    private int totalHunger = 1;    // The need for food
    private int totalSatiety = 0;   // Current number of eating food

    // Each fat trait on the creature add fatCapacity. Player can transfer food to fat for future rounds.
    private int fatCapacity = 0;    // Max fat number
    private int fatQuantity = 0;    // Current fat number

    private boolean isHibernating = false;
    private int wasHibernating = 0;
    private boolean isAttacked = false;     // Was attack somebody in current move
    private boolean isPoisoned = false;     // if true -> die in the end of round
    private boolean isPirated = false;      // Did he stil the food
    private boolean isActiveScavenger = false;
    private boolean isMimicked = false;
    private boolean isLossTail = false;

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

    // Class for pair trait -> need save link for other creature
    private class PairTraitElement implements Serializable{
        final Creature otherCreature;
        final Card card;

        public PairTraitElement(Creature otherCreature, Card card){
            this.otherCreature = otherCreature;
            this.card = card;
        }
    }

    private ArrayList<PairTraitElement> communicationList = new ArrayList<>();
    private ArrayList<PairTraitElement> cooperationList = new ArrayList<>();

    // crocodiles: this creature can not eat if any symbiont is hungry;
    // this creature can not be eaten if any symbiont is alive
    private ArrayList<Creature> crocodileList = new ArrayList<>();

    //birds: can not eat if this creature is hungry; can not be eaten if this animal is alive
    private ArrayList<Creature> birdList = new ArrayList<>();


    //Creature`s traits list (in order of obtaining)
    private ArrayList<Card> cards = new ArrayList<>();

    ///endregion

    public Creature(Player player){
        this.player = player;
        id = commonID++;
    }

    /**********************
     * Adds new trait if possible; do some checks; changes constants
     * @return false if trait has not added
     **********************/
    public boolean addTrait(Card card){
        if (card.getTrait() == Trait.FAT_TISSUE){
            ++fatCapacity;
            cards.add(card);
            return true;
        }

        // According to rules you can not put two equal cards (except fat tissue) to the same creature
        for(Card c : cards) {
            if(c == card || c.getTrait() == card.getTrait())
                return false;
        }

        // According to rules a predator can not be a scavenger so
        // if smb puts a predator card to scavenger or scavenger card to predator
        // the last card should be removed from the animal
        if ((isScavenger && (card.getTrait() == Trait.PREDATOR))
                || (isPredator && (card.getTrait() == Trait.SCAVENGER)))
            return false; //actually has not added any traits

        if(card.getTrait() == Trait.SCAVENGER){
            if(!player.haveCreaturesWithActiveScavenger())
                isActiveScavenger = true;
        }

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
            case COMMUNICATION: return communicationList.size() > 0;
            case COOPERATION: return cooperationList.size() > 0;
            case SYMBIOSIS: return crocodileList.size() > 0 || birdList.size() > 0;
            default: return false;
        }
    }
    public Card findCard(Trait trait){
        if(!findTrait(trait)) return null;

        for(Card card : cards){
            if(card.getTrait() == trait)
                return card;
        }

        return null;
    }
    public boolean canAddTrait(Trait trait){
        if(trait == Trait.FAT_TISSUE)
            return true;
        else if(Trait.isPairTrait(trait)){
            switch (trait){
                case COOPERATION:
                    if (cooperationList.size() >= 2) return false;
                    return cooperationList.size() != (player.getCreatures().size() - 1);
                case COMMUNICATION:
                    if (communicationList.size() >= 2) return false;
                    return communicationList.size() != (player.getCreatures().size() - 1);
                case SYMBIOSIS:
                    if ((birdList.size() + crocodileList.size()) >= 2) return false;
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
                for(PairTraitElement pairTraitElement : cooperationList){
                    if(pairTraitElement.otherCreature == secondCreature)
                        return false;
                }//Старая версия
                break;
            case COMMUNICATION:
                /*for(CreaturesPair creaturesPair : player.communicationCreatures){
                    if(creaturesPair.haveCreature(this) && creaturesPair.haveCreature(secondCreature))
                        return false;
                }*/
                for(PairTraitElement pairTraitElement : communicationList){
                    if(pairTraitElement.otherCreature == secondCreature)
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
    private boolean switchTrait(Trait trait, boolean turnOn){
        switch (trait){
            case PREDATOR:
                isPredator = turnOn;
                if(!turnOn) isAttacked = false;
                break;
            case HIGH_BODY: isBig = turnOn;
                break;
            case RUNNING: isRunning = turnOn;
                break;
            case MIMICRY:
                isMimetic = turnOn;
                if(!turnOn) isMimicked = false;
                break;
            case GRAZING: isGrazing = turnOn;
                break;
            case POISONOUS: isPoisonous = turnOn;
                break;
            case TAIL_LOSS:
                isTailLossable = turnOn;
                if(!turnOn) isLossTail = false;
                break;
            case HIBERNATION: isHibernatable = turnOn;
                break;
            case SCAVENGER: isScavenger = turnOn;
                break;
            case PIRACY:
                isPirate = turnOn;
                if(!turnOn) isPirated = false;
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
    public boolean addPairTrait(Card card, Creature creature){
        switch (card.getTrait()) {
            case COOPERATION:
                for(PairTraitElement pairTraitElement : cooperationList){
                    if(pairTraitElement.otherCreature == creature)
                        return false;
                }
                cooperationList.add(new PairTraitElement(creature, card));
                return true;
            case COMMUNICATION:
                for(PairTraitElement pairTraitElement : communicationList){
                    if(pairTraitElement.otherCreature == creature)
                        return false;
                }
                communicationList.add(new PairTraitElement(creature, card));
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
                else return true;

        }
        return false;
    }

    // 0 - comm, 1 - coop
    public ArrayList[] getCommAndCoopLists(){
        ArrayList[] arrayLists = new ArrayList[2];

        arrayLists[0] = new ArrayList<>();
        arrayLists[1] = new ArrayList<>();

        //int[] links = new int[2];

        for(PairTraitElement pairTraitElement : communicationList){
            if(!pairTraitElement.card.isUsed()){
                arrayLists[0].add(pairTraitElement.otherCreature);
                //++links[0];
            }
        }
        for(PairTraitElement pairTraitElement : cooperationList){
            if(!pairTraitElement.card.isUsed()){
                arrayLists[1].add(pairTraitElement.otherCreature);
                //++links[1];
            }
        }

        return arrayLists;
    }

    public void addFood(){
        if(totalSatiety < totalHunger)
            ++totalSatiety;
        else if(fatQuantity < fatCapacity){
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
    public void reduceFood(){
        if(totalSatiety > 0)
            --totalSatiety;
    }

    // Use fat
    public void useFatTissue(int cardNumber){
        if (player.table.getPlayerTurn() != player.playerNumber) return;
        if (cards.get(cardNumber).getTrait() != Trait.FAT_TISSUE || !cards.get(cardNumber).isFat() || isFed())
            return;

        cards.get(cardNumber).setFat(false);
        --fatQuantity;
        addFood();
    }

    public void setHunger(){
        totalSatiety = 0;
    }
    public boolean isHungry(){
        return totalHunger > totalSatiety;
    }
    public int getTotalHunger(){
        return totalHunger;
    }
    public int getTotalSatiety(){
        return totalSatiety;
    }
    public int getFatQuantity() {
        return fatQuantity;
    }
    public boolean isSatisfied(){
        return ((fatCapacity == fatQuantity) && isFed()) || isHibernating;
    }
    public boolean isFed(){
        return totalSatiety == totalHunger;
    }
    public boolean canEat(){
        boolean birdFlag = false;
        for(Creature creature : crocodileList){
            if(creature.isHungry()){
                birdFlag = true;
                break;
            }
        }

        return !isSatisfied() && !birdFlag;
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
    public void setPirated(boolean isPirated){
        this.isPirated = isPirated;
    }
    public boolean isPirated(){
        return isPirated;
    }
    public void setActiveScavenger(boolean isActiveScavenger){
        this.isActiveScavenger = isActiveScavenger;
    }
    public boolean isActiveScavenger(){
        return isActiveScavenger;
    }
    public void setHibernating(boolean isHibernating){
        this.isHibernating = isHibernating;
        if(isHibernating)
            wasHibernating = 2;
    }
    public boolean isHibernating(){
        return isHibernating;
    }
    public int getHibernatingTime(){
        return wasHibernating;
    }
    public void reduceHibernatingTime(){
        --wasHibernating;
    }
    public boolean isPiratingPossible(Creature victim) {
        if (!findTrait(Trait.PIRACY) || isPirated()) return false;
        if ((victim.totalSatiety == 0) || victim.isFed()) return false;
        return true;
    }
    public boolean isMimetic() {
        return isMimetic;
    }
    public boolean isMimicked(){
        return  isMimicked;
    }
    public void setMimicked(boolean mimicked) {
        isMimicked = mimicked;
    }
    public void setAttacked(boolean attacked) {
        isAttacked = attacked;
    }
    public boolean isAttacked() {
        return isAttacked;
    }
    public boolean isRunning() {
        return isRunning;
    }
    public boolean isTailLossable() {
        return isTailLossable;
    }
    public boolean isLossTail() {
        return isLossTail;
    }
    public void setLossTail(boolean lossTail) {
        isLossTail = lossTail;
    }

    public boolean pirate(Creature victim){
        setPirated(true);
        addFood();
        victim.reduceFood();
        return true;
    }

    public void attack(Creature creature){

        if (creature.isPoisonous) isPoisoned = true;

        creature.player.killCreature(creature);

        for (int i = 0; i < 2; ++i){
            if (!isFed()) ++totalSatiety;
            else if (!isSatisfied()) ++fatQuantity;
        }

        isAttacked = true;
    }

    public boolean isAttackPossible(Creature victim){
        return this.isPredator
                && (!victim.isCamouflaged || this.isSharp)
                && !(victim.isBurrowing && victim.isFed())
                && (victim.crocodileList.isEmpty())
                && (this.isSwimming == victim.isSwimming)
                && (!victim.isBig || this.isBig)
                && !isAttacked;
    }
    // The check is necessary to identify those cases when the defender can do nothing
    // => No need to send other player messages
    public boolean isAbsoluteAttackPossible(Creature victim){
        if(!isAttackPossible(victim))
            return false;

        if((victim.isTailLossable && !victim.isLossTail)
        || (victim.isMimetic && victim.getPlayer().haveCreaturesToMimicry(this) && !victim.isMimicked)
        || victim.isRunning)
            return false;

        return true;
    }


    public ArrayList<Trait> getDefenseTraitList(Creature attacker){
        ArrayList<Trait> defenseTraits = new ArrayList<>();

        if(isTailLossable && !isLossTail)
            defenseTraits.add(Trait.TAIL_LOSS);
        if(isMimetic && player.haveCreaturesToMimicry(attacker) && !isMimicked)
            defenseTraits.add(Trait.MIMICRY);
        if(isRunning)
            defenseTraits.add(Trait.RUNNING);

        return defenseTraits;
    }

    public boolean getFood (){
        if (player.table.isFodderBaseEmpty()) return false;
        else if (this.isSatisfied()) return false;
        if (isFed()) {
            ++fatQuantity;
        } else {
            ++totalSatiety;
        }
        return true;
    }
    public boolean stealFood(Creature creature){
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
        return cards.contains(card) || player.findCardWithPairTrait(card);
    }
    public ArrayList<Card> getCards(){
        return cards;
    }

    public int getCommunicationListSize() {
        return communicationList.size();
    }
    public int getCooperationListSize() {
        return cooperationList.size();
    }
    public ArrayList<Creature> getCrocodileList() {
        return crocodileList;
    }
    public ArrayList<Creature> getBirdList() {
        return birdList;
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
            for (PairTraitElement pairTraitElement : cooperationList) {
                str.append(pairTraitElement.otherCreature.id + " ");
            }
            str.append("\n");
        }

        if (!communicationList.isEmpty()) {
            str.append("\tCommunicated with: ");
            for (PairTraitElement pairTraitElement : communicationList) {
                str.append(pairTraitElement.otherCreature.id + " ");
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