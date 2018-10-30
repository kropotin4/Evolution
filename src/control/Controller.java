package control;

import model.*;
import storage.Loader;
import storage.Saver;
import view.gui.CreatureNode;

import java.io.IOException;
import java.util.ArrayList;

public class Controller {

    private Table table;
    private Player player;
    private Creature creature;

    Saver saver = new Saver();
    Loader loader = new Loader();

    public Controller(int quarterCardCount, int playerCount){
        table = new Table(quarterCardCount, playerCount);
    }


    public int doNextMove(){
        table.doNextMove();

        try {
            saver.saveTable(table, "dump" + table.step);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        if(table.step > 15){
            try {
                table = loader.loadTable("dump" + (table.step - 10));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

        return table.getPlayerTurn();
    }


    public void addCreature(int playerNumber, Card card){
        System.out.println("Controller: addCreature: " + playerNumber  + " " + card);
        player = findPlayer(playerNumber);
        player.addCreature(card);
    }
    // +? проверку на return
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
    public void addTraitToCreature(int puttingPlayerNumber, int OwnerPlayerNumber, int creatureID, Card card, boolean isUp){
        System.out.println("Controller: addTraitToCreature: " + puttingPlayerNumber + " " + OwnerPlayerNumber + " " + creatureID + " " + card + " " + isUp);

        player.addTraitToCreature(
                findPlayer(puttingPlayerNumber),
                findPlayer(OwnerPlayerNumber).findCreature(creatureID),
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
    public void getFoodFromFodderToFat(int playerNumber, int creatureID){
        player = findPlayer(playerNumber);
        player.getFoodFromFodderToFat(creatureID);
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
        player = findPlayer(playerNumber);
        player.setPass(true);
    }
    public int addPlayer(String login){

        //TODO: Нужна очередность ходов -> венуть номер в массиве.
        //
        return table.addPlayer(login);
    }
    public int getPlayerCardsNumber(int playerNumber){
        player = findPlayer(playerNumber);
        return player.getPlayerCardsNumber();
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
            throw new RuntimeException("Controller: playerNumber = " + playerNumber);
        }

        return table.getPlayers().get(playerNumber);
    }
    public ArrayList<Player> getPlayers(){
        return table.getPlayers();
    }
    public int getPlayersNumber(){
        return table.getPlayerNumber();
    }

    public boolean isAttackPossible(int attackerPlayer, int defenderPlayer, int attackerCreatureID, int defenderCreatureID){
        Player aPlayer = findPlayer(attackerPlayer);
        Player dPlayer = findPlayer(defenderPlayer);

        Creature attacker = aPlayer.findCreature(attackerCreatureID);
        Creature defender = dPlayer.findCreature(defenderCreatureID);

        if(attacker.isAttackPossible(defender))
            return true;

        return false;
    }
    public boolean attackCreature(int attackerPlayer, int defenderPlayer, int attackerCreatureID, int defenderCreatureID){
        Player aPlayer = findPlayer(attackerPlayer);
        Player dPlayer = findPlayer(defenderPlayer);

        Creature attacker = aPlayer.findCreature(attackerCreatureID);
        Creature defender = dPlayer.findCreature(defenderCreatureID);

        System.out.println("Controller: attackCreature: " + aPlayer  + " (" + attacker + ") " + dPlayer + " (" + defender + ")");

        if(attacker.isAbsoluteAttackPossible(defender)){
            aPlayer.attackCreature(attacker, defender, null);
            return true;
        }

        return false;
    }
    public void defendCreature(int attackerPlayer, int defenderPlayer, int attackerCreature, int defenderCreature, Card[] defendTraits){
        Player aPlayer = findPlayer(attackerPlayer);
        Player dPlayer = findPlayer(defenderPlayer);

        Card[] cards = null;

        aPlayer.attackCreature(
                aPlayer.findCreature(attackerCreature),
                dPlayer.findCreature(defenderCreature),
                null,
                cards
        );
    }

    public ArrayList<Creature> getCreatures(int playerNumber){
        Player player = findPlayer(playerNumber);

        return player.getCreatures();
    }
}
