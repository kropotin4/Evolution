package model;

import model.decks.CommonCardDeck;

import java.util.ArrayList;

public class Table {

    Phase curPhase;
    int step = 0;
    int playerTurn = 0;

    static int fodder = 0;
    int initialCardCount = 6;

    static CommonCardDeck commonDeck;
    Dice dice;

    ArrayList<Player> players;

    public Table(int quarterCardCount, int playerCount){
        commonDeck = new CommonCardDeck(quarterCardCount);
        setDice(playerCount);


        players = new ArrayList<>(playerCount);

        for(int i = 0; i < initialCardCount; ++i){
            for(Player player : players){

            }
        }

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

    public static boolean isFodderBaseEmpty(){
        if (fodder == 0)
            return true;
        return false;
    }
}
