package model;

import model.decks.CommonCardDeck;

import java.util.ArrayList;
import java.util.UUID;

public class Table {

    Phase curPhase;
    int step = 0;
    int playerTurn = 0;

    static int fodder = 0;

    CommonCardDeck commonDeck;

    Dice dice;

    ArrayList<Player> players;
    int passNumber;


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

    public void doNextMove(){
        boolean allPass = true;
        int i = 0, oldTurn = playerTurn;

        switch (curPhase){
            case CALC_FODDER_BASE:
                setFodder();
                curPhase = Phase.GROWTH;
                break;
            case GROWTH:
                if(passNumber == players.size()){
                    curPhase = Phase.CALC_FODDER_BASE;
                    playerTurn = (playerTurn + 1) % players.size();
                    break;
                }

                while(i++ <= players.size()){
                    playerTurn = (playerTurn + 1) % players.size();
                    if(players.get(playerTurn).isPass) continue;
                    allPass = false;
                }
                if(allPass){
                    curPhase = Phase.CALC_FODDER_BASE;
                    playerTurn = (oldTurn + 1) % players.size();
                    break;
                }
                break;
            case EATING:
                if(passNumber == players.size()){
                    curPhase = Phase.EXTINCTION;
                    playerTurn = (playerTurn + 1) % players.size();
                    break;
                }

                while(i++ <= players.size()){
                    playerTurn = (playerTurn + 1) % players.size();
                    if(players.get(playerTurn).isPass) continue;
                    allPass = false;
                }
                if(allPass){
                    curPhase = Phase.EXTINCTION;
                    playerTurn = (oldTurn + 1) % players.size();
                    break;
                }
                break;
            case EXTINCTION:
                //TODO: Написать смерть
                curPhase = Phase.DEALING;
                break;
            case DEALING:
                //TODO: Раздача карт
                curPhase = Phase.GROWTH;
                break;
        }
    }

    public CommonCardDeck getCommonDeck() {
        return commonDeck;
    }
    public Card getCard(){
        return commonDeck.getCard();
    }

    public int rollDice(){
        return dice.roll();
    }
    private void setDice(int playerCount){
        dice = new Dice((playerCount + 1) / 2, playerCount % 2 == 0 ? 2 : 0);
    }

    public Phase getCurrentPhase(){
        return curPhase;
    }

    public void setFodder(){
        fodder = rollDice();
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
    public boolean isFodderBaseEmpty(){
        return fodder == 0;
    }
    public int getFodder() {
        return fodder;
    }

    public int addPlayer(String login){
        Player player = new Player(this, login);

        players.add(player);

        return players.size() - 1;
    }
    public ArrayList<Player> getPlayers(){
        return players;
    }
    public int getPlayerNumber(){
        return players.size();
    }
    public int getPlayerTurn(){
        return playerTurn;
    }
    public Player findPlayer(UUID id){
        for(Player player : players){
            if(player.getId() == id) return player;
        }

        return null;
    }
}
