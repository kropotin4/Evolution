package control;

import model.*;
import storage.Loader;
import storage.Saver;

import java.util.ArrayList;

public class Controller {

    private Table table;
    private Player player; // временная переменная
    private Creature creature;

    Saver saver = new Saver();
    Loader loader = new Loader();

    public Controller(int quarterCardCount, int playerCount){
        table = new Table(quarterCardCount, playerCount);
    }
    public Controller(Table table){
        this.table = table;
    }


    public int doNextMove(){
        table.doNextMove();

//        try {
//            saver.saveTable(table, "dump" + table.step);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        return table.getPlayerTurn();
    }

    // Опасность!!!!!!
    public Table getTable() {
        return table;
    }
    public void setTable(Table table){
        this.table = table;
    }

    public void addCreature(int playerNumber, Card card){
        System.out.println("Controller: addCreature: " + playerNumber  + " " + card);
        player = findPlayer(playerNumber);
        player.addCreature(card);
    }

    public void addTraitToCreature(int playerNumber, int creatureID, Card card, boolean isUp){
        System.out.println("Controller: addTraitToCreature: " + playerNumber + " " + creatureID + " " + card + " " + isUp);

        player = findPlayer(playerNumber);
        player.addTraitToCreature(
                player,
                player.findCreature(creatureID),
                card,
                isUp
        );
    }
    public void addTraitToCreature(int puttingPlayerNumber, int ownerPlayerNumber, int creatureID, Card card, boolean isUp){
        System.out.println("Controller: addTraitToCreature: " + puttingPlayerNumber + " " + ownerPlayerNumber + " " + creatureID + " " + card + " " + isUp);

        player.addTraitToCreature(
                findPlayer(puttingPlayerNumber),
                findPlayer(ownerPlayerNumber).findCreature(creatureID),
                card,
                isUp
        );
    }
    public void addPairTraitToCreature(int playerNumber, int creature1ID, int creature2ID, Card card, boolean isUp){
        player = findPlayer(playerNumber);
        player.addPairTraitToCreature(
                player.findCreature(creature1ID),
                player.findCreature(creature2ID),
                card,
                isUp
        );
    }
    public void addSymbiosisTraitToCreature(int playerNumber, int crocodile1ID, int birdID, Card card, boolean isUp){
        player = findPlayer(playerNumber);
        player.addSymbiosisTraitToCreature(
                player.findCreature(crocodile1ID),
                player.findCreature(birdID),
                card,
                isUp
        );
    }

    public ArrayList<Card> getCreatureCards(int playerNumber, int creatureID){
        player = findPlayer(playerNumber);
        return (ArrayList<Card>)player.findCreature(creatureID).getCards().clone();
    }
    public boolean findTrait(int playerNumber, int creatureID, Card card, boolean isUp){
        System.out.println("Controller: findCard: " + playerNumber + " " + creatureID + " " + card + " " + isUp);
        player = findPlayer(playerNumber);

        return player.findCreature(creatureID).findTrait(card.getTrait(isUp));
    }
    public boolean findTrait(int playerNumber, int creatureID, Trait trait){
        System.out.println("Controller: findCard: " + playerNumber + " " + creatureID + " " + trait);
        player = findPlayer(playerNumber);

        return player.findCreature(creatureID).findTrait(trait);
    }
    public boolean canAddTrait(int playerNumber, int creatureID, Trait trait){
        System.out.println("Controller: canAddTrait: " + playerNumber + " " + creatureID + " " + trait);
        player = findPlayer(playerNumber);

        return player.findCreature(creatureID).canAddTrait(trait);
    }
    public boolean canAddPairTrait(int playerNumber, int firstCreatureID, int secondCreatureID, Trait trait){
        System.out.println("Controller: canAddTrait: " + playerNumber + " " + firstCreatureID + " " + secondCreatureID + " " + trait);
        player = findPlayer(playerNumber);

        return player.findCreature(firstCreatureID).canAddPairTrait(trait, player.findCreature(secondCreatureID));
    }

    public boolean havePlayerPredator(int playerNumber){
        player = findPlayer(playerNumber);

        for(Creature creature : player.getCreatures()){
            if(!creature.isFed() && findTrait(playerNumber, creature.getId(), Trait.PREDATOR)){
                return true;
            }
        }

        return false;
    }

    public void setFodder(){
        table.setFodder();
    }
    public int getFoodNumber(){
        return table.getFodder();
    }
    public void getFoodFromFodder(int playerNumber, int creatureID){
        player = findPlayer(playerNumber);
        player.getFoodFromFodder(creatureID);
    }

    public void useFatTissue(int playerNumber, int creatureID, int cardNumber){
        findPlayer(playerNumber).findCreature(creatureID).useFatTissue(cardNumber);
    }
    public void setPlayerScavenger(int playerNumber, int creatureID){
        System.out.println("Controller: setPlayerScavenger: " + playerNumber + " " + creatureID);
        table.setPlayerScavenger(playerNumber, creatureID);
    }
    public boolean isActiveScavenger(int playerNumber, int creatureID){
        return findPlayer(playerNumber).findCreature(creatureID).isActiveScavenger();
    }
    public int getScavengerNumber(int playerNumber){
        return findPlayer(playerNumber).getScavengerNumber();
    }

    public void setCreatureHibernating(int playerNumber, int creatureID, boolean isHibernating){
        findPlayer(playerNumber).findCreature(creatureID).setHibernating(isHibernating);
    }
    public boolean isHibernating(int playerNumber, int creatureID){
        return findPlayer(playerNumber).findCreature(creatureID).isHibernating();
    }
    public int getHibernatingTime(int playerNumber, int creatureID){
        return findPlayer(playerNumber).findCreature(creatureID).getHibernatingTime();
    }

    public boolean haveHungryCreature(int playerNumber){
        player = findPlayer(playerNumber);
        for(Creature creature : player.getCreatures()){
            if(!creature.isSatisfied())
                return true;
        }
        return false;
    }
    public int getCreatureHunger(int playerNumber, int creatureID){
        player = findPlayer(playerNumber);
        return player.findCreature(creatureID).getTotalHunger();
    }
    public int getCreatureSatiety(int playerNumber, int creatureID){
        player = findPlayer(playerNumber);
        return player.findCreature(creatureID).getTotalSatiety();
    }
    public boolean isCreatureFed(int playerNumber, int creatureID){
        player = findPlayer(playerNumber);
        return player.findCreature(creatureID).isFed();
    }
    public boolean isCreatureSatisfied(int playerNumber, int creatureID){
        player = findPlayer(playerNumber);
        return player.findCreature(creatureID).isSatisfied();
    }
    public boolean isCreatureCanEat(int playerNumber, int creatureID){
        player = findPlayer(playerNumber);

        return player.findCreature(creatureID).canEat();
    }

    public void setGrazingActive(int playerNumber, int creatureID, boolean isActive){
        System.out.println("Controller: setGrazingActive: " + creatureID + " " +isActive);
        player = findPlayer(playerNumber);
        player.findCreature(creatureID).setGrazingActive(isActive);
    }
    public boolean isPoisoned(int playerNumber, int creatureID){
        player = findPlayer(playerNumber);
        return player.findCreature(creatureID).isPoisoned();
    }
    public boolean havePiracyCreatures(int playerNumber){
        return findPlayer(playerNumber).haveCreaturesWithTrait(Trait.PIRACY);
    }
    public boolean haveCanPiracyCreatures(int playerNumber){
        player = findPlayer(playerNumber);
        for(Creature creature : player.getCreatures()){
            if(creature.findTrait(Trait.PIRACY) && !creature.isPirated() && !creature.isSatisfied())
                return true;
        }
        return false;
    }

    public boolean isGameOver(){
        return table.isGameOver();
    }
    public Phase getCurrentPhase(){
        return table.getCurrentPhase();
    }

    public ArrayList<CreaturesPair> getCommunicationCreatures(int playerNumber){
        player = findPlayer(playerNumber);
        return player.getCommunicationCreatures();
    }
    public ArrayList<CreaturesPair> getCooperationCreatures(int playerNumber){
        player = findPlayer(playerNumber);
        return player.getCooperationCreatures();
    }
    public ArrayList<SymbiosisPair> getSymbiosisCreatures(int playerNumber){
        player = findPlayer(playerNumber);
        return player.getSymbiosisCreatures();
    }

    public void setPlayerPass(int playerNumber){
        findPlayer(playerNumber).setPass(true);
    }
    public int addPlayer(String login){
        //TODO: Нужна очередность ходов -> венуть номер в массиве.
        //
        return table.addPlayer(login);
    }
    public void setLogin(String login, int playerNumber){
        findPlayer(playerNumber).setLogin(login);
    }
    public int getPlayerCardsNumber(int playerNumber){
        return findPlayer(playerNumber).getPlayerCardsNumber();
    }
    public boolean isPlayersTurn(int playerNumber){
        return playerNumber == table.getPlayerTurn();
    }
    public Player getPlayPlayer(){
        return table.getPlayers().get(table.getPlayerTurn());
    }
    public int getPlayerTurn(){
        return table.getPlayerTurn();
    }
    public Player findPlayer(int playerNumber){
        if(playerNumber < 0 || playerNumber >= table.getPlayers().size()){
            throw new RuntimeException("Controller: playerTurn = " + playerNumber);
        }

        return table.getPlayers().get(playerNumber);
    }
    public ArrayList<Player> getPlayers(){
        return table.getPlayers();
    }
    public int getPlayersNumber(){
        return table.getPlayerNumber();
    }

    public void pirateCreature(int piratePlayer, int victimPlayer, int pirateCreatureID, int victimCreatureID){
        Player pPlayer = findPlayer(piratePlayer);
        Player vPlayer = findPlayer(victimPlayer);

        Creature pirate = pPlayer.findCreature(pirateCreatureID);
        Creature victim = vPlayer.findCreature(victimCreatureID);

        System.out.println("Controller: pirateCreature: " + pPlayer  + " (" + pirate + ") " + vPlayer + " (" + victim + ")");

        pPlayer.pirateCreature(pirate, victim);
    }

    public Player hasIntention(){
        return table.hasIntention();
    }
    public boolean isAttackPossible(int attackerPlayer, int defenderPlayer, int attackerCreatureID, int defenderCreatureID){
        Creature attacker = findPlayer(attackerPlayer).findCreature(attackerCreatureID);
        Creature defender = findPlayer(defenderPlayer).findCreature(defenderCreatureID);

        if(attacker == null){
            System.err.println("Controller: isAttackPossible: null attacker");
        }
        if(defender == null){
            System.err.println("Controller: isAttackPossible: null defender");
        }

        return attacker.isAttackPossible(defender);
    }
    public boolean attackCreature(int attackerPlayer, int defenderPlayer, int attackerCreatureID, int defenderCreatureID){
        Player aPlayer = findPlayer(attackerPlayer);
        Player dPlayer = findPlayer(defenderPlayer);

        Creature attacker = aPlayer.findCreature(attackerCreatureID);
        Creature defender = dPlayer.findCreature(defenderCreatureID);

        System.out.println("Controller: attackCreature: " + aPlayer  + " (" + attacker + ") " + dPlayer + " (" + defender + ")");

        //if(attacker.isAbsoluteAttackPossible(defender)){
        return aPlayer.attackCreature(attacker, defender);
        //    return true;
        //}

        //return false;
    }
//    public ArrayList<Trait> getDefendTrait(Creature attacker, Creature defender){
//
//    }

    public boolean doTailLoss(Card lostCard, Creature attacker, Creature victim, Creature ... creatures){
        return victim.getPlayer().doTailLoss(
                lostCard,
                attacker,
                victim,
                creatures
        );
    }
    public boolean doMimicry(int defenderPlayer, int victimCreatureID){
        Player dPlayer = table.findPlayer(defenderPlayer);

        Creature victim = dPlayer.findCreature(victimCreatureID);

        if(victim == null){
            System.err.println("Controller: doMimicry: null victim");
        }

        return victim.getPlayer().doMimicry(victim);
    }

    public boolean doRunning(Creature attacker, Creature victim){
        return victim.getPlayer().doRunning(
                attacker,
                victim
        );
    }

    public ArrayList<Creature> getCreatures(int playerNumber){
        Player player = findPlayer(playerNumber);

        return player.getCreatures();
    }
}
