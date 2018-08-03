package model;

import model.decks.CommonCardDeck;

import java.util.ArrayList;
import java.util.UUID;

public class Table {

    Phase curPhase;
    int step = 0;
    int playerTurn = 0;

    static int fodder = 0;
    int initialCardCount = 6;

    static CommonCardDeck commonDeck;
    Dice dice;

    ArrayList<Player> players;

    public static int getFodder() {
        return fodder;
    }

    public Table(int quarterCardCount, int playerCount){
        commonDeck = new CommonCardDeck(quarterCardCount);
        setDice(playerCount);

        //TODO: передавать в стол готовую колоду, чтобы можно было выбирать карты поштучно
        players = new ArrayList<>(playerCount);

        /*for(int i = 0; i < initialCardCount; ++i){
            for(Player player : players){

            }
        }*/
        //игра начинается с фазы раздачи карт. В этот момент игроки и получат свои карты

    }

    public static Card getCard(){
        return commonDeck.getCard();
    }

    public int rollDice(){
        return dice.roll();
    }
    private void setDice(int playerCount){
        if(playerCount % 2 == 0)
             dice = new Dice((playerCount + 1) / 2, 2);
        else dice = new Dice((playerCount + 1) / 2, 0);
    }

    public void setFodder(){
        fodder = rollDice();
    }

    public Phase getCurrentPhase(){
        return curPhase;
    }

    public ArrayList<Player> getPlayers(){
        return players;
    }

    public int getPlayerCount(){
        return players.size();
    }

    public int getPlayerTurn(){
        return playerTurn;
    }

    public int getFood(int count){
        if(fodder >= count){
            fodder -= count;
        }
        else{
            count = fodder;
            fodder = 0;
        }
        return count;
    }
    public static boolean isFodderBaseEmpty(){
        return fodder == 0;
    }

    public Player findPlayer(UUID id){
        for(Player player : players){
            if(player.getId() == id) return player;
        }

        return null;
    }
}
