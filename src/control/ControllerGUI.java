package control;

import javafx.scene.Node;
import model.Card;
import model.Creature;
import model.Phase;
import model.Trait;
import model.decks.PlayerCardDeck;
import view.gui.CardNode;
import view.gui.CreatureNode;
import view.gui.MainPane;
import view.gui.PlayerPane;

import java.util.ArrayList;

public class ControllerGUI {

    Controller controller;
    MainPane mainPane;

    int playerNumber; // Меняется в doNextMove()

    public ControllerGUI(Controller controller, MainPane mainPane, int playerNumber){
        this.controller = controller;
        this.mainPane = mainPane;
        this.playerNumber = playerNumber;
    }

    public void startGame(){
        mainPane.update(0);
    }

    public void doNextMove(){
        playerNumber = controller.doNextMove();
        mainPane.update(controller.getPlayerTurn());
    }

    public void passPlayer(){
        controller.setPlayerPass(playerNumber);
        doNextMove();
    }
    public int getPlayersNumber(){
        return controller.getPlayersNumber();
    }
    public Phase getCurrentPhase(){
        return controller.getCurrentPhase();
    }
    public int getFoodNumber(){
        return controller.getFoodNumber();
    }

    public boolean havePlayerPredator(){
        return controller.havePlayerPredator(playerNumber);
    }

    public boolean haveHungryCreature(){
        return controller.haveHungryCreature(playerNumber);
    }
    public int getCreauterHunger(CreatureNode creatureNode){
        return controller.getCreatureHunger(creatureNode.getPlayerPane().getPlayerNumber(), creatureNode.getCreatureId());
    }
    public int getCreauterSatiety(CreatureNode creatureNode){
        return controller.getCreatureSatiety(creatureNode.getPlayerPane().getPlayerNumber(), creatureNode.getCreatureId());
    }
    public boolean isCreatureFed(CreatureNode creatureNode){
        return controller.isCreatureFed(creatureNode.getPlayerPane().getPlayerNumber(), creatureNode.getCreatureId());
    }

    public void addCreature(CardNode cardNode){
        controller.addCreature(playerNumber, cardNode.getCard());
        mainPane.showSelectedCard(false);
        mainPane.setIsCreatureAdding(false);
        mainPane.setIsCardSelecting(false);
        mainPane.updateCurrentPlayer();
    }

    public boolean findTrait(CreatureNode creatureNode, Trait trait){
        return controller.findTrait(creatureNode.getPlayerPane().getPlayerNumber(), creatureNode.getCreatureId(), trait);
    }

    public void showAddTraitPane(){
        mainPane.showAddTraitPane();
    }
    public void addTraitToCreature(CreatureNode creatureNode, CardNode cardNode){

    }
    public void addTraitToSelectedCreature(CardNode cardNode, boolean isUp){
        if(!controller.findTrait(playerNumber, mainPane.getSelectedCreature().getCreatureId(), cardNode.getCard(), isUp)
        && cardNode.getCard().getTrait(isUp) != Trait.PARASITE) {
            controller.addTraitToCreature(playerNumber, mainPane.getSelectedCreature().getCreatureId(), cardNode.getCard(), isUp);
            mainPane.showSelectedCard(false);
            mainPane.setIsCreatureAdding(false);
            mainPane.setIsCardSelecting(false);
            mainPane.updateCurrentPlayer();
        }
    }
    public void addPairTraitToCreature(CreatureNode creatureNode1, CreatureNode creatureNode2, Card card){

    }

    public ArrayList<Card> getCreatureCards(CreatureNode creatureNode){
        return controller.getCreatureCards(creatureNode.getPlayerPane().getPlayerNumber(), creatureNode.getCreatureId());
    }
    public int getPlayerCardNumber(){
        return controller.getPlayerCardsNumber(playerNumber);
    }

    public void getFoodFromFodder(CreatureNode creatureNode){
        mainPane.setIsFoodGetting(false);
        controller.getFoodFromFodder(playerNumber, creatureNode.getCreatureId());
        mainPane.updateCurrentPlayer();
    }
    public boolean isFoodGetting(){
        return mainPane.isFoodGetting();
    }

    public void showAttackedCreatures(CreatureNode creatureNode) {
        int attackerPlayer = creatureNode.getPlayerPane().getPlayerNumber();
        int attackerCreature = creatureNode.getCreatureId();
        for (Node node : mainPane.getPlayersPane()) {
            PlayerPane playerPane = (PlayerPane) node;

            for (CreatureNode creatureNode1 : playerPane.getCreatureNodes()) {
                if (controller.isAttackPossible(
                        attackerPlayer,
                        playerPane.getPlayerNumber(),
                        attackerCreature,
                        creatureNode1.getCreatureId())) {

                    playerPane.setAttackStyle(creatureNode1); // Замена на setStyleType
                }

            }
        }
    }
    public void attackCreature(CreatureNode attacker, CreatureNode defender){

    }


    public void selectCreature(CreatureNode creatureNode){
        mainPane.setSelectedCreature(creatureNode);
    }
    public ArrayList<Creature> getCreatures(int playerNumber){
        return controller.getCreatures(playerNumber);
    }

    //public boolean findCard(CreatureNode creatureNode, CardNode cardNode){
    //   return controller.findCard(playerNumber, creatureNode.getCreatureId(), cardNode.getCard());
    //}
    public void selectCard(CardNode cardNode){
        mainPane.setSelectedCard(cardNode);
        mainPane.showSelectedCard(true);
    }
    public CardNode getSelectedCard(){
        return mainPane.getSelectedCard();
    }
    public PlayerCardDeck getPlayerCardDeck(){
        return controller.getPlayers().get(playerNumber).getPlayerCardDeck();
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

    public boolean isAttackerSelecting(){
        return mainPane.isAttackerSelecting();
    }
    public void setIsAttackerSelecting(boolean isAttackerSelecting){
        mainPane.setIsAttackerSelecting(isAttackerSelecting);
    }
    public boolean isAttackedSelecting(){
        return mainPane.isAttackedSelecting();
    }
    public void setIsAttackedSelecting(boolean isAttackedSelecting){
        mainPane.setIsAttackedSelecting(isAttackedSelecting);
    }

    public void setDeckPaneTop(){
        mainPane.deckPane.setTop();
    }
}
