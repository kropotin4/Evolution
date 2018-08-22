package control;

import model.*;

import java.util.ArrayList;
import java.util.UUID;

public class Controler {

    private Table table;
    private boolean haveInit = false;

    public Controler(){}
    public Controler(int quarterCardCount, int playerCount){
        initialize(quarterCardCount, playerCount);
    }

    public void initialize(int quarterCardCount, int playerCount){
        if(haveInit) throw new RuntimeException("Retry Contoler init");
        haveInit = true;

        table = new Table(quarterCardCount, playerCount);
    }
    public void initialize(Table table){
        if(haveInit) throw new RuntimeException("Retry Contoler init");
        haveInit = true;

        this.table = table;
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
