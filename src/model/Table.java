package model;

import model.decks.CommonCardDeck;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Table implements Serializable {

    Phase curPhase;
    public int step = 0;
    int playerTurn = 0;

    int fodder = 0;

    CommonCardDeck commonDeck;

    Dice dice;
    int initCardsNumber;

    ArrayList<Player> players;
    int passNumber;

    //Создание общей колоды + создание игроков + раздача им карт + определение кубика
    public Table(int quarterCardCount, int playerCount){
        commonDeck = new CommonCardDeck(quarterCardCount);
        setDice(playerCount);
        initCardsNumber = 8;

        //TODO: передавать в стол готовую колоду, чтобы можно было выбирать карты поштучно
        players = new ArrayList<>(playerCount);
        for(int i = 0; i < playerCount; ++i){
            addPlayer("");
        }

        if(commonDeck.getCardCount() > 0)
            for(int i = 0; i < initCardsNumber; ++i){
                for(Player player : players) {
                    if(commonDeck.getCardCount() <= 0) break;
                    player.getCard();
                }
            }

        curPhase = Phase.GROWTH;

    }

    public void doNextMove(){
        boolean allPass = true;
        int i = 0, oldTurn = playerTurn;
        step++;
        switch (curPhase){
            case CALC_FODDER_BASE:
                setFodder();
                for(Player player : players){
                    player.setPass(false);
                }
                curPhase = Phase.EATING;  /// !!!!!!!!!!!!!
                break;
            case GROWTH:
                if(passNumber == players.size()){
                    curPhase = Phase.CALC_FODDER_BASE;
                    playerTurn = (playerTurn + 1) % players.size();
                    for(Player player : players)
                        player.setPass(false);
                    break;
                }

                while(i++ <= players.size()){
                    playerTurn = (playerTurn + 1) % players.size();
                    if(players.get(playerTurn).isPass) continue;
                    else{
                        allPass = false;
                        break;
                    }

                }
                if(allPass){
                    curPhase = Phase.CALC_FODDER_BASE;
                    playerTurn = (oldTurn + 1) % players.size();
                    for(Player player : players)
                        player.setPass(false);
                    break;
                }
                break;
            case EATING:
                if(passNumber == players.size()){
                    curPhase = Phase.EXTINCTION;
                    playerTurn = (playerTurn + 1) % players.size();
                    for(Player player : players)
                        player.setPass(false);
                    break;
                }

                while(i++ <= players.size()){
                    playerTurn = (playerTurn + 1) % players.size();
                    if(players.get(playerTurn).isPass) continue;
                    else{
                        allPass = false;
                        break;
                    }
                }
                if(allPass){
                    curPhase = Phase.EXTINCTION;
                    playerTurn = (oldTurn + 1) % players.size();
                    for(Player player : players)
                        player.setPass(false);
                    break;
                }
                break;
            case EXTINCTION: {
                Creature creature = null;
                for (Player player : players) {
                    for (int g = 0; g < player.getCreatures().size(); ++g) {
                        creature = player.getCreatures().get(g);

                        //Сначала ставь убивающее!
                        if(creature.isPoisoned()){
                            player.killCreature(creature);
                            g--;
                        }

                        if(creature.isPirated())
                            creature.setPirated(false);

                        if(creature.isHibernating()){
                            creature.reduceHibernatingTime();
                            creature.setHibernating(false);
                            creature.setHunger();
                        }
                        else if (creature.isFed()) {
                            creature.setHunger();
                        }
                        else {
                            player.killCreature(creature);
                            g--;
                        }
                    }
                }

                ///region Раздача карт
                for(Player player : players){
                    player.getCard();
                    player.setPass(false);
                }

                int max = 0;
                for(Player player : players)
                    if(max < player.getCreatures().size())
                        max = player.getCreatures().size();

                 for(int g = 0; g < max; ++g){
                     for(Player player : players){
                         if(player.getCreatures().size() > g)
                             player.getCard();
                     }
                 }
                 ///endregion

                curPhase = Phase.GROWTH;
                break;
            }
            //case DEALING:
            //   //TODO: Раздача карт
            //    curPhase = Phase.GROWTH;
            //    break;
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

    public void setPlayerScavanger(int playerNumber, int creatureID){
        for(Creature creature : findPlayer(playerNumber).getCreatures()){
            if(creature.getId() == creatureID)
                creature.setActiveScavenger(true);
            else
                creature.setActiveScavenger(false);
        }
    }
    public void useScavenger(int ownerAttatckCreature){

        for(int i = ownerAttatckCreature; i < players.size(); ++i) {
            for (Creature creature : players.get(i).getCreatures()) {
                if(creature.isActiveScavenger() && !creature.isSatisfied()){
                    creature.addFood();
                    return;
                }
            }
        }

        for(int i = 0; i < ownerAttatckCreature; ++i){
            for (Creature creature : players.get(i).getCreatures()) {
                if(creature.isActiveScavenger() && !creature.isSatisfied()){
                    creature.addFood();
                    return;
                }
            }
        }

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
        Player player = new Player(this, login, players.size());

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
    public Player findPlayer(int playerNumber){
        for(Player player : players){
            if(player.getPlayerNumber() == playerNumber) return player;
        }

        return null;
    }
}
