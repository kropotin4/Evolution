package control;

import model.*;
import storage.Loader;
import storage.Saver;

import java.io.IOException;
import java.util.ArrayList;

public class Controler {

    private Table table;
    private Player player;
    private Creature creature;


    public Controler(int quarterCardCount, int playerCount){
        table = new Table(quarterCardCount, playerCount);
    }


    public int doNextMove(){
        table.doNextMove();
        Saver saver = new Saver();
        try {
            saver.saveTable(table, "dump" + table.step);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(table.step > 15){
            Loader loader = new Loader();
            try {
                table = loader.loadTable("dump" + (table.step - 10));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return table.getPlayerTurn();
    }


    public void addCreature(int playerNumber, Card card){
        player = findPlayer(playerNumber);
        player.addCreature(card);
    }
    // +? проверку на return
    public void addTraitToCreature(int playerNumber, int creatureID, Card card, boolean isUp){
        System.out.println("Controler: addTraitToCreature: " + playerNumber + " " + creatureID + " " + card + " " + isUp);
        player = findPlayer(playerNumber);
        player.addTraitToCreature(
                player.findCreature(creatureID),
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
        System.out.println("Controler: findCard: " + playerNumber + " " + creatureID + " " + card + " " + isUp);
        player = findPlayer(playerNumber);

        return player.findCreature(creatureID).findTrait(card.getTrait(isUp));
    }

    public void setFodder(){
        table.setFodder();
    }
    public int getFoodNumber(){
        return table.getFodder();
    }
    public void getFoodFromFodder(int creatureID){

    }

    public int getCreauterHunger(int playerNumber, int creatureID){
        player = findPlayer(playerNumber);
        return player.findCreature(creatureID).getTotalHunger();
    }
    public int getCreauterSatiety(int playerNumber, int creatureID){
        player = findPlayer(playerNumber);
        return player.findCreature(creatureID).getTotalSatiety();
    }
    public boolean isCreatureFed(int playerNumber, int creatureID){
        player = findPlayer(playerNumber);
        return player.findCreature(creatureID).isFed();
    }

    public Phase getCurrentPhase(){
        return table.getCurrentPhase();
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
            throw new RuntimeException("Controler: playerNumber = " + playerNumber);
        }

        return table.getPlayers().get(playerNumber);
    }
    public ArrayList<Player> getPlayers(){
        return table.getPlayers();
    }
    public int getPlayersNumber(){
        return table.getPlayerNumber();
    }

    public boolean attackCreature(int attackerPlayer, int defenderPlayer, int attackerCreature, int defenderCreature){
        Player aPlayer = findPlayer(attackerPlayer);
        Player dPlayer = findPlayer(defenderPlayer);

        Creature attacker = aPlayer.findCreature(attackerCreature);
        Creature defender = dPlayer.findCreature(defenderCreature);

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
