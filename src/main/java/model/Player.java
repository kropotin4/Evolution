package model;

import model.decks.DropCardDeck;
import model.decks.PlayerCardDeck;

import java.io.Serializable;
import java.util.ArrayList;

/*********************
 * Что может игрок:
 *
 *      Фаза роста:
 *          *Положить карту на существо (addTrait в разных варациях)
 *      Фаза определения К.Б.
 *          *Бросить кубик (все больше хочется, чтобы это делал сервер)
 *      Фаза питания:
 *          *Атаковать (a)
 *          *Спец умения (это в ответ на соответсвующее сообщение сервера)
 *          *Защита (в ответ на атаку -> у самого существа)
 *          *Взять еду из К.Б.
 *
 *
 *
 *********************/


public class Player implements Serializable {

    ///region fields
    Table table;

    String login;
    int playerNumber;

    boolean isAi = false;
    String color;
    boolean isPass = false;

    boolean defendIntention = false;
    Creature attackCreature = null;
    Creature defendCreature = null;

    PlayerCardDeck playerDeck = new PlayerCardDeck();
    DropCardDeck dropDeck = new DropCardDeck();

    ArrayList<Creature> creatures = new ArrayList<>();

    ArrayList<CreaturesPair> communicationCreatures = new ArrayList<>();
    ArrayList<CreaturesPair> cooperationCreatures = new ArrayList<>();
    ArrayList<SymbiosisPair> symbiosisCreatures = new ArrayList<>();
    ///endregion

    public Player(Table table, String login, int playerNumber){
        this.table = table;
        this.login = login;
        this.playerNumber = playerNumber;
        color = playerNumber == 0? "#ff0000" :
                playerNumber == 1? "#ffff00" :
                playerNumber == 2? "#00ff00" :
                playerNumber == 3? "#0000ff" : "#ffffff";
    }

    public ArrayList<Creature> getCreatures() {
        return creatures;
    }

    public boolean getFoodFromFodder(int creatureID){
        Creature creature = findCreature(creatureID);
        if(!table.isFodderBaseEmpty() && !creature.isSatisfied()) {
            creature.addFood();

            table.getFood(1 + getGrazingActiveNumber());

            return true;
        }
        return false;
    }
    public boolean getFoodFromCooperation(Creature creature, Creature otherCreature){
        //Creature creature = findCreature(creatureID);
        //Creature otherCreature = findCreature(otherCreatureID);
        if(!creature.isSatisfied()){

            for(CreaturesPair creaturesPair : cooperationCreatures){
                if(!creaturesPair.card.isUsed() && creaturesPair.haveCreatures(creature, otherCreature)){
                    creaturesPair.card.setUsed(true);
                    creature.addFood();
                    return true;
                }
            }

        }
        return false;
    }
    public boolean getFoodFromCommunication(Creature creature, Creature otherCreature){
        //Creature creature = findCreature(creatureID);
        //Creature otherCreature = findCreature(otherCreatureID);
        if(!table.isFodderBaseEmpty() && !creature.isSatisfied()){
            for(CreaturesPair creaturesPair : communicationCreatures){
                if(!creaturesPair.card.isUsed() && creaturesPair.haveCreatures(creature, otherCreature)){
                    creaturesPair.card.setUsed(true);
                    creature.addFood();
                    table.getFood(1);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean attackCreature(Creature attacker, Creature defender){
        if(!attacker.isAttackPossible(defender))
            return false;

        if(attacker.isAbsoluteAttackPossible(defender)) {
            attacker.attack(defender);
            table.useScavenger(attacker.getPlayer().playerNumber);
            return true;
        }

        defender.getPlayer().defendIntention = true;
        defender.getPlayer().attackCreature = attacker;
        defender.getPlayer().defendCreature = defender;

        return false;
    }
    public boolean pirateCreature(Creature pirate, Creature victim){
        if(pirate.isPirated() || victim.getTotalSatiety() == 0 || victim.isFed()) return false;
        return pirate.pirate(victim);
    }

    public boolean doTailLoss(Card lostCard, Creature attacker, Creature victim, Creature ... creatures){
        if(!victim.findCard(lostCard)) return false;

        if(!Trait.isPairTrait(lostCard.getTrait())){
            if(victim.removeTrait(lostCard)){
                attacker.addFood();
                attacker.setAttacked(true);
                victim.setLossTail(true);
                victim.getPlayer().defendIntention = false;
                victim.getPlayer().defendCreature = null;
                victim.getPlayer().attackCreature = null;
                //table.doNextMove();
                return true;
            }
        }
        else if(creatures.length == 1){
            if(removePairTraitFromCreatures(victim, creatures[0], lostCard.getTrait())){
                attacker.addFood();
                attacker.setAttacked(true);
                victim.setLossTail(true);
                victim.getPlayer().defendIntention = false;
                victim.getPlayer().defendCreature = null;
                victim.getPlayer().attackCreature = null;
                //table.doNextMove();
                return true;
            }
        }
        return false;
    }

    public boolean doMimicry(Creature victim){
        return doMimicry(attackCreature, defendCreature, victim);
    }
    public boolean doMimicry(Creature attacker, Creature mimetic, Creature victim){
        if(mimetic.getPlayer() != victim.getPlayer() || !mimetic.isMimetic() || mimetic.isMimicked()) return false;

        mimetic.setMimicked(true);
        if(attackCreature(attacker, victim)){

            mimetic.getPlayer().defendIntention = false;
            mimetic.getPlayer().defendCreature = null;
            mimetic.getPlayer().attackCreature = null;
            return true;
        }

        return true;
    }
    public boolean doRunning(Creature attacker, Creature victim){
        if(!victim.isRunning()) return false;

        if(Dice.rollOneDice() > 3){
            attacker.setAttacked(true);
            victim.getPlayer().defendIntention = false;
            victim.getPlayer().defendCreature = null;
            victim.getPlayer().attackCreature = null;
            return true;
        }

        if((victim.isMimetic() == victim.isMimicked())
                && victim.isTailLossable() == victim.isLossTail())
        {
            attacker.attack(victim);
            victim.getPlayer().defendIntention = false;
            victim.getPlayer().defendCreature = null;
            victim.getPlayer().attackCreature = null;
        }

        return false;
    }

    public boolean killCreature(Creature creature){
        if(creatures.remove(creature)){

            for(int i = 0; i < communicationCreatures.size(); ++i){
                if(communicationCreatures.get(i).haveCreature(creature)){

                    communicationCreatures.get(i).firstCreature.removePairTrait(
                            Trait.COMMUNICATION,
                            communicationCreatures.get(i).secondCreature
                    );

                    communicationCreatures.get(i).secondCreature.removePairTrait(
                            Trait.COMMUNICATION,
                            communicationCreatures.get(i).firstCreature
                    );

                    communicationCreatures.remove(i);
                    --i;
                }
            }

            for(int i = 0; i < cooperationCreatures.size(); ++i){
                if(cooperationCreatures.get(i).haveCreature(creature)){

                    cooperationCreatures.get(i).firstCreature.removePairTrait(
                            Trait.COMMUNICATION,
                            cooperationCreatures.get(i).secondCreature
                    );

                    cooperationCreatures.get(i).secondCreature.removePairTrait(
                            Trait.COMMUNICATION,
                            cooperationCreatures.get(i).firstCreature
                    );

                    cooperationCreatures.remove(i);
                    --i;
                }
            }

            for(int i = 0; i < symbiosisCreatures.size(); ++i){
                if(symbiosisCreatures.get(i).haveCreature(creature)){

                    symbiosisCreatures.get(i).crocodile.removePairTrait(
                            Trait.COMMUNICATION,
                            symbiosisCreatures.get(i).bird
                    );

                    symbiosisCreatures.get(i).bird.removePairTrait(
                            Trait.COMMUNICATION,
                            symbiosisCreatures.get(i).crocodile
                    );

                    symbiosisCreatures.remove(i);
                    --i;
                }
            }

            return true;
        }
        return false;
    }
    public boolean addCreature(Card card){
        if(playerDeck.removeCard(card)){
            creatures.add(new Creature(this));
            return true;
        }
        return false;
    }

    public boolean addTraitToCreature(Player player, Creature creature, Card card, boolean isUp){
        if(player.playerDeck.removeCard(card)){
            if(isUp != card.isUp())
                card.turnCard();

            return creature.addTrait(card);
        }
        return false;
    }
    public boolean addPairTraitToCreature(Creature creature1, Creature creature2, Card card, boolean isUp){
        if(playerDeck.removeCard(card)){
            if(isUp != card.isUp())
                card.turnCard();

            if(card.getTrait(isUp) == Trait.COOPERATION)
                cooperationCreatures.add(new CreaturesPair(creature1, creature2, card));
            else if(card.getTrait(isUp) == Trait.COMMUNICATION)
                communicationCreatures.add(new CreaturesPair(creature1, creature2, card));

            creature1.addPairTrait(card, creature2);
            creature2.addPairTrait(card, creature1);
            return true;
        }
        return false;
    }
    public boolean addSymbiosisTraitToCreature(Creature crocodile, Creature bird, Card card, boolean isUp){
        if(playerDeck.removeCard(card)){
            if(card.getTrait(isUp) != Trait.SYMBIOSIS)
                return false;

            symbiosisCreatures.add(new SymbiosisPair(crocodile, bird, card));

            crocodile.addSymbiosisTrait(bird, false);
            bird.addSymbiosisTrait(crocodile, true);
            return true;
        }
        return false;
    }
    public boolean removePairTraitFromCreatures(Creature creature1, Creature creature2, Trait trait){

        switch (trait){
            case COOPERATION:
                for (CreaturesPair creaturesPair : cooperationCreatures){
                    if(creaturesPair.haveCreature(creature1) && creaturesPair.haveCreature(creature2)){
                        creature1.removePairTrait(trait, creature2);
                        creature2.removePairTrait(trait, creature1);
                        return cooperationCreatures.remove(creaturesPair);
                    }
                }
                break;
            case COMMUNICATION:
                for (CreaturesPair creaturesPair : communicationCreatures){
                    if(creaturesPair.haveCreature(creature1) && creaturesPair.haveCreature(creature2)){
                        creature1.removePairTrait(trait, creature2);
                        creature2.removePairTrait(trait, creature1);
                        return communicationCreatures.remove(creaturesPair);
                    }
                }
                break;
            case SYMBIOSIS:
                for (SymbiosisPair symbiosisPair : symbiosisCreatures){
                    if(symbiosisPair.haveCreature(creature1) && symbiosisPair.haveCreature(creature2)){
                        creature1.removePairTrait(trait, creature2);
                        creature2.removePairTrait(trait, creature1);
                        return symbiosisCreatures.remove(symbiosisPair);
                    }
                }
                break;
        }

        return false;
    }


    public boolean haveCreaturesWithTrait(Trait trait){
        for(Creature creature : creatures){
            if(creature.findTrait(trait))
                return true;
        }
        return false;
    }
    public boolean haveCreaturesWithActiveScavenger(){
        for(Creature creature : creatures){
            if(creature.isActiveScavenger())
                return true;
        }
        return false;
    }
    public boolean haveCreaturesToMimicry(Creature attacker){
        int res = 0;
        for(Creature creature : creatures){
            if(attacker.isAttackPossible(creature))
                ++res;
        }
        return res > 1;
    }

    public boolean haveActiveCreatures() {
        /*for (Creature creature : creatures){
            if (creature.findTrait(Trait.GRAZING)) return true;
        }*/
        for (Creature creature : creatures){
            if (!creature.isSatisfied()){
                if (table.getFodder() > 0) return true;
                if (creature.getFatQuantity() > 0) return true;
                for (Player player : table.getPlayers()) {
                    for (Creature victim : player.getCreatures()) {
                        if (creature.isAttackPossible(victim) ||
                            creature.isPiratingPossible(victim)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    public boolean isDefendIntention(){
        return defendIntention;
    }
    public Creature getAttackCreature(){
        return attackCreature;
    }
    public Creature getDefendCreature(){
        return defendCreature;
    }
    public int getGrazingActiveNumber(){
        int res = 0;
        for(Creature creature : creatures){
            if(creature.isGrazingActive())
                ++res;
        }
        return res;
    }
    public int getScavengerNumber(){
        int res = 0;
        for(Creature creature : creatures){
            if(creature.findTrait(Trait.SCAVENGER))
                ++res;
        }
        return res;
    }

    public void getCardFromCommonDeck(){
        if(table.commonDeck.getCardCount() > 0)
            playerDeck.addCard(table.getCard());
    }
    public ArrayList<CreaturesPair> getCommunicationCreatures(){
        return communicationCreatures;
    }
    public ArrayList<CreaturesPair> getCooperationCreatures(){
        return cooperationCreatures;
    }
    public ArrayList<SymbiosisPair> getSymbiosisCreatures(){
        return symbiosisCreatures;
    }
    public int getPlayerCardsNumber(){
        return playerDeck.getCardsNumber();
    }
    public PlayerCardDeck getPlayerCardDeck(){
        return playerDeck;
    }
    public boolean findCardWithPairTrait(Card card){
        Trait trait = Trait.isPairTrait(card.getTrait(true)) ? card.getTrait(true) : card.getTrait(false);
        if(!Trait.isPairTrait(trait)) return false;

        switch (trait){
            case COOPERATION:
                for (CreaturesPair creaturesPair : cooperationCreatures){
                    if(creaturesPair.card == card){
                        return true;
                    }
                }
                break;
            case COMMUNICATION:
                for (CreaturesPair creaturesPair : communicationCreatures){
                    if(creaturesPair.card == card){
                        return true;
                    }
                }
                break;
            case SYMBIOSIS:
                for (SymbiosisPair symbiosisPair : symbiosisCreatures){
                    if(symbiosisPair.card == card){
                        return true;
                    }
                }
                break;
        }

        return false;
    }

    public boolean canMove(){
        return !(isPass ||
                ((table.getCurrentPhase() == Phase.GROWTH && playerDeck.getCardsNumber() == 0) ||
                        (table.getCurrentPhase() == Phase.EATING && !haveActiveCreatures())));
    }

    public void setAI(boolean isAi){
        this.isAi = isAi;
    }
    public void setPass(boolean isPass){
        this.isPass = isPass;
        if(isPass) table.passNumber++;
        else table.passNumber--;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getLogin(){
        return login;
    }
    public void setPlayerNumber(int playerNumber){
        this.playerNumber = playerNumber;
    }

    public Creature findCreature(int CreatureId){
        for(Creature creature : creatures){
            if(creature.getId() == CreatureId) return creature;
        }

        return null;
    }
    public int getPlayerNumber(){
        return playerNumber;
    }

    public int getScore(){
        int res = 0;
        for (Creature creature : this.creatures){
            res += 1 + creature.getCards().size() + creature.getTotalHunger();
        }
        return res;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "Player{" +
                ", login='" + login + '\'' +
                ", playerNumber=" + playerNumber +
                ", isPass=" + isPass +
                ", playerDeck=" + playerDeck +
                ", dropDeck=" + dropDeck +
                ", creatures=" + creatures +
                ", communicationCreatures=" + communicationCreatures +
                ", cooperationCreatures=" + cooperationCreatures +
                ", symbiosisCreatures=" + symbiosisCreatures +
                '}';
    }
}
