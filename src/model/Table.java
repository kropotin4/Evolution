package model;

import ai.aiThread;
import model.decks.CommonCardDeck;

import java.io.Serializable;
import java.util.ArrayList;

public class Table implements Serializable {

    Phase curPhase;
    public int step = 0;
    int playerTurn = 0;
    boolean gameOver = false;

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
        System.out.println("doNextMove: cards = " + commonDeck.getCardCount());

        boolean allPass = true;
        int i = 0, oldTurn = playerTurn;
        step++;
        switch (curPhase){
            case GROWTH:
                if(passNumber == players.size()){
                    curPhase = Phase.EATING;
                    setFodder();

                    playerTurn = (playerTurn + 1) % players.size();
                    for(Player player : players)
                        player.setPass(false);
                    break;
                }

                while(i++ <= players.size()){
                    playerTurn = (playerTurn + 1) % players.size();
                    if(!players.get(playerTurn).canMove()) continue;
                    else{
                        allPass = false;
                        break;
                    }
                }

                if(allPass){
                    curPhase = Phase.EATING;
                    setFodder();

                    playerTurn = (oldTurn + 1) % players.size();
                    for(Player player : players)
                        player.setPass(false);
                    break;
                }

                break;
            case EATING:
                if(passNumber == players.size()){
                    doExtinction();
                    curPhase = Phase.GROWTH;

                    playerTurn = (playerTurn + 1) % players.size();
                    for(Player player : players)
                        player.setPass(false);
                    break;
                }

                while(i++ <= players.size()){
                    playerTurn = (playerTurn + 1) % players.size();
                    if(!players.get(playerTurn).canMove()) continue;
                    else{
                        allPass = false;
                        break;
                    }
                }
                if(allPass){
                    doExtinction();
                    curPhase = Phase.GROWTH;

                    playerTurn = (oldTurn + 1) % players.size();
                    for(Player player : players)
                        player.setPass(false);
                    break;
                }

                break;
        }

        if(players.get(playerTurn).isAi){

            aiThread aiThread = new aiThread(this, playerTurn);
            aiThread.start();

            try {
                aiThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void doExtinction(){
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

        // До раздачи карт
        if(isEndMove()){
            gameOver = true;
        }

        ///region Раздача карт

        int[] player_cards = new int[players.size()];

        int i = 0;
        int max = 0;
        for(Player player : players){

            if(player.creatures.size() == 0 && player.getPlayerCardsNumber() == 0)
                player_cards[i] = initCardsNumber;
            else
                player_cards[i] = player.creatures.size() + 1;

            if(player_cards[i] > max)
                max = player_cards[i];

            player.setPass(false);
            ++i;
        }

        for(int g = 0; g < max; ++g){
            i = 0;
            for(Player player : players){
                if(player_cards[i] > g)
                    player.getCard();
                ++i;
            }
        }
        ///endregion
    }
    public boolean isEndMove(){
        return commonDeck.getCardCount() <= 0;
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

    /**
     * @return positive number if player has won; negative if has lost; zero if draw
     */
    public boolean isGameOver() {
        return gameOver;
    }

    public int hasPlayerWon(int player){
        int result = players.get(player).getScore();
        int best = 0;
        int cur;
        for (Player p : players){
            if (p.getPlayerNumber() == player) continue;
            cur = p.getScore();
            if (cur > best) best = cur;
        }
        return result - best;
    }

    public Phase getCurrentPhase(){
        return curPhase;
    }

    public void setPlayerScavenger(int playerNumber, int creatureID){
        for(Creature creature : findPlayer(playerNumber).getCreatures()){
            if(creature.getId() == creatureID)
                creature.setActiveScavenger(true);
            else
                creature.setActiveScavenger(false);
        }
    }
    public void useScavenger(int ownerAttackCreature){

        for(int i = ownerAttackCreature; i < players.size(); ++i) {
            for (Creature creature : players.get(i).getCreatures()) {
                if(creature.isActiveScavenger() && !creature.isSatisfied()){
                    creature.addFood();
                    return;
                }
            }
        }

        for(int i = 0; i < ownerAttackCreature; ++i){
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

    @Override
    public String toString() {
        return "Table{" +
                "curPhase=" + curPhase +
                ", step=" + step +
                ", playerTurn=" + playerTurn +
                ", fodder=" + fodder +
                ", commonDeck=" + commonDeck +
                ", dice=" + dice +
                ", initCardsNumber=" + initCardsNumber +
                ", players=" + players +
                ", passNumber=" + passNumber +
                '}';
    }
}
