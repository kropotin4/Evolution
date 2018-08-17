package control;

import model.*;

import java.util.ArrayList;
import java.util.UUID;

public class Controler {

    private Table table;

    public void initialize(int quarterCardCount, int playerCount){
        table = new Table(quarterCardCount, playerCount);
    }

    // +? проверку на return
    public void addTraitToCreature(Player player, UUID creature, Card card, boolean isUp){
        player.addTraitToCreature(
                player.findCreature(creature),
                card,
                isUp
        );
    }

    public void setFodder(){
        table.setFodder();
    }
    public Phase getCurrentPhase(){
        return table.getCurrentPhase();
    }

    public int addPlayer(String login){

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

    public boolean attackCreature(int attackerPlayer, int defenderPlayer, UUID attackerCreature, UUID defenderCreature){
        Player aPlayer = findPlayer(attackerPlayer);
        Player dPlayer = findPlayer(defenderPlayer);

        Creature attacker = aPlayer.findCreature(attackerCreature);
        Creature defender = dPlayer.findCreature(defenderCreature);

        if(attacker.isAbsoluteAttackPossible(defender)){
            aPlayer.attackCreature(attacker, defender);
            return true;
        }

        return false;
    }
}
