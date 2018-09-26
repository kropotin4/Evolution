package control;

import model.Card;
import model.Creature;
import model.Phase;
import model.decks.PlayerCardDeck;
import view.gui.CardNode;
import view.gui.CreatureNode;
import view.gui.MainPane;

import java.util.ArrayList;

public class ControlerGUI {

    Controler controler;
    MainPane mainPane;

    int playerNumber;

    public ControlerGUI(Controler controler, MainPane mainPane, int playerNumber){
        this.controler = controler;
        this.mainPane = mainPane;
        this.playerNumber = playerNumber;
    }

    public void startGame(){
        mainPane.update(0);
    }

    public void doNextMove(){
        controler.doNextMove();
        mainPane.update(controler.getPlayerTurn());
    }

    public int getPlayersNumber(){
        return controler.getPlayersNumber();
    }
    public Phase getCurrentPhase(){
        return controler.getCurrentPhase();
    }

    public void addCreature(CardNode cardNode){
        controler.addCreature(playerNumber, cardNode.getCard());
        mainPane.setIsCreatureAdding(false);
        mainPane.setIsCardSelecting(false);
        mainPane.updateCurrentPlayer();
    }

    public void addTraitToCreature(CreatureNode creatureNode, CardNode cardNode){

    }
    public void addTraitToSelectedCreature(CardNode cardNode, boolean isUp){

    }
    public void addPairTraitToCreature(CreatureNode creatureNode1, CreatureNode creatureNode2, Card card){

    }

    public ArrayList<Card> getCreatureCards(CreatureNode creatureNode){
        return controler.getCreatureCards(creatureNode.getPlayerPane().getPlayerNumber(), creatureNode.getCreatureId());
    }
    public int getPlayerCardNumber(){
        return controler.getPlayerCardsNumber(playerNumber);
    }

    public void getFoodFromFodder(CreatureNode creatureNode){

    }

    public void attackCreature(CreatureNode attacker, CreatureNode defender){

    }
    public void defendCreature(){

    }

    public void selectCreature(CreatureNode creatureNode){
        mainPane.setSelectedCreature(creatureNode);
    }
    public ArrayList<Creature> getCreatures(int playerNumber){
        return controler.getCreatures(playerNumber);
    }

    public void selectCard(CardNode cardNode){
        mainPane.setSelectedCard(cardNode);
        mainPane.showSelectedCard(true);
    }
    public CardNode getSelectedCard(){
        return mainPane.getSelectedCard();
    }
    public PlayerCardDeck getPlayerCardDeck(){
        return controler.getPlayers().get(playerNumber).getPlayerCardDeck();
    }
    public boolean isCardSelecting(){
        return mainPane.isCardSelecting();
    }
    public boolean isCardSelected(){
        return mainPane.isCardSelected();
    }
    public boolean isCreatureAdding(){
        return mainPane.isCreatureAdding();
    }

    public void setDeckPaneTop(){
        mainPane.deckPane.setTop();
    }
}
