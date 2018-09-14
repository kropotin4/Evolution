package control;

import model.*;

import java.util.ArrayList;
import java.util.UUID;

public class Controler {

    private Table table;

    public Controler(int quarterCardCount, int playerCount){
        table = new Table(quarterCardCount, playerCount);
    }


    public void doNextMove(){
        table.doNextMove();
    }

    // +? проверку на return
    public void addTraitToCreature(int playerNumber, int creatureID, Card card, boolean isUp){
        Player player = findPlayer(playerNumber);
        player.addTraitToCreature(
                player.findCreature(creatureID),
                card,
                isUp
        );
    }
    public void addPairTraitToCreature(int playerNumber, int creature1ID, int creature2ID, Card card, boolean isUp){
        Player player = findPlayer(playerNumber);
        player.addPairTraitToCreature(
                player.findCreature(creature1ID),
                player.findCreature(creature2ID),
                card,
                isUp
        );
    }
    public ArrayList<Card> getCreatureCards(int playerNumber, int creatureID){
        Player player = findPlayer(playerNumber);
        return (ArrayList<Card>)player.findCreature(creatureID).getCards().clone();
    }

    public void setFodder(){
        table.setFodder();
    }
    public void getFoodFromFodder(int creatureID){

    }

    public Phase getCurrentPhase(){
        return table.getCurrentPhase();
    }

    public int addPlayer(String login){

        //TODO: Нужна очередность ходов -> венуть номер в массиве.
        //
        return table.addPlayer(login);
    }
    public int getPlayerCardsNumber(int playerNumber){
        Player player = findPlayer(playerNumber);
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
