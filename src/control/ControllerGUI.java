package control;

import javafx.scene.Node;
import model.*;
import model.decks.PlayerCardDeck;
import view.gui.CardNode;
import view.gui.CreatureNode;
import view.gui.MainPane;
import view.gui.PlayerPane;

import java.util.ArrayList;

public class ControllerGUI {

    Controller controller;
    ControllerClient controllerClient;
    ControllerServer controllerServer;
    MainPane mainPane;

    int type;

    int playerNumber; // Меняется в doNextMove()

    public ControllerGUI(Controller controller, MainPane mainPane, int playerNumber){
        this.controller = controller;
        this.mainPane = mainPane;
        this.playerNumber = playerNumber;
        type = 0;
    }
    public ControllerGUI(ControllerClient controllerClient, MainPane mainPane, int playerNumber){
        this.controllerClient = controllerClient;
        this.mainPane = mainPane;
        this.playerNumber = playerNumber;
        type = 1;
    }
    public ControllerGUI(ControllerServer controllerServer, MainPane mainPane, int playerNumber){
        this.controllerServer = controllerServer;
        this.mainPane = mainPane;
        this.playerNumber = playerNumber;
        type = 2;
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
    public int getPlayerTurn(){
        return controller.getPlayerTurn();
    }
    public Phase getCurrentPhase(){
        return controller.getCurrentPhase();
    }
    public int getFoodNumber(){
        return controller.getFoodNumber();
    }

    public ArrayList<CreaturesPair> getCommunicationCreatures(int playerNumber){
        return controller.getCommunicationCreatures(playerNumber);
    }
    public ArrayList<CreaturesPair> getCooperationCreatures(int playerNumber){
        return controller.getCooperationCreatures(playerNumber);
    }
    public ArrayList<SymbiosisPair> getSymbiosisCreatures(int playerNumber){
        return controller.getSymbiosisCreatures(playerNumber);
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
    public boolean isCreatureSatisfied(CreatureNode creatureNode){
        return controller.isCreatureSatisfied(creatureNode.getPlayerPane().getPlayerNumber(), creatureNode.getCreatureId());
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
    public boolean canAddTrait(CreatureNode creatureNode, Trait trait){
        return controller.canAddTrait(
                creatureNode.getPlayerPane().getPlayerNumber(),
                creatureNode.getCreatureId(),
                trait
        );
    }
    public boolean canAddPairTrait(CreatureNode firstCreature, CreatureNode secondCreature, Trait trait){
        return controller.canAddPairTrait(
                firstCreature.getPlayerPane().getPlayerNumber(),
                firstCreature.getCreatureId(),
                secondCreature.getCreatureId(),
                trait
        );
    }
    public boolean isUpTrait(){
        return mainPane.isUpTrait();
    }
    public boolean isPairTraitSelected(){
        return mainPane.isPairTraitSelected();
    }
    public void showAddTraitPane(CreatureNode selectedCreature, double X, double Y){
        mainPane.showAddTraitPane(selectedCreature, X, Y);
    }
    public void addTraitToCreature(CreatureNode creatureNode, CardNode cardNode, boolean isUp){
        if(controller.canAddTrait(creatureNode.getPlayerPane().getPlayerNumber(), creatureNode.getCreatureId(), cardNode.getCard().getTrait(isUp))) {
            controller.addTraitToCreature(
                    creatureNode.getPlayerPane().getPlayerNumber(),
                    creatureNode.getCreatureId(),
                    cardNode.getCard(),
                    isUp
            );
            mainPane.showSelectedCard(false);
            mainPane.setIsCreatureAdding(false);
            mainPane.setIsCardSelecting(false);
            mainPane.updateCurrentPlayer();
        }
    }
    public void addTraitToCreature(int playerNumber, CreatureNode creatureNode, CardNode cardNode, boolean isUp){
        if(controller.canAddTrait(creatureNode.getPlayerPane().getPlayerNumber(), creatureNode.getCreatureId(), cardNode.getCard().getTrait(isUp))) {
            controller.addTraitToCreature(
                    playerNumber,
                    creatureNode.getPlayerPane().getPlayerNumber(),
                    creatureNode.getCreatureId(),
                    cardNode.getCard(),
                    isUp
            );
            mainPane.showSelectedCard(false);
            mainPane.setIsCreatureAdding(false);
            mainPane.setIsCardSelecting(false);
            mainPane.update(this.playerNumber);
        }
    }

    public void addPairTraitToCreature(CreatureNode creatureNode1, CreatureNode creatureNode2, CardNode cardNode, boolean isUp){

        controller.addPairTraitToCreature(
                playerNumber,
                creatureNode1.getCreatureId(),
                creatureNode2.getCreatureId(),
                cardNode.getCard(),
                isUp
        );

        mainPane.showSelectedCard(false);
        mainPane.setIsCreatureAdding(false);
        mainPane.setIsCardSelecting(false);
        mainPane.updateCurrentPlayer();

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
    public void getFoodFromFodderToFat(CreatureNode creatureNode){
        mainPane.setIsFoodGetting(false);
        controller.getFoodFromFodderToFat(playerNumber, creatureNode.getCreatureId());
        mainPane.updateCurrentPlayer();
    }
    public boolean isFoodGetting(){
        return mainPane.isFoodGetting();
    }


    public void showDefenderSelecting(CreatureNode creatureNode) {
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

                    creatureNode1.setStyleType(1);
                }

            }
        }

        for(CreatureNode creatureNode1 : mainPane.getCurrentPlayerPane().getCreatureNodes()){
            if(creatureNode == creatureNode1) continue;

            if (controller.isAttackPossible(
                    attackerPlayer,
                    mainPane.getCurrentPlayerPane().getPlayerNumber(),
                    attackerCreature,
                    creatureNode1.getCreatureId())) {

                creatureNode1.setStyleType(1);
            }
        }

    }
    public void setAttackerCreature(CreatureNode creatureNode){
        System.out.println("ControllerGUI: setAttackerCreature: " + creatureNode);
        setIsDefenderSelecting(true);
        setIsAttackerSelecting(false);
        showDefenderSelecting(creatureNode);
        mainPane.setAttackerCreature(creatureNode);
    }
    /// Не передает ход
    public void attackCreature(CreatureNode defender){
        controller.attackCreature(
                mainPane.getAttackerCreature().getPlayerPane().getPlayerNumber(),
                defender.getPlayerPane().getPlayerNumber(),
                mainPane.getAttackerCreature().getCreatureId(),
                defender.getCreatureId()
        );
        mainPane.getAttackerCreature().setStyleType(0);
        mainPane.update(playerNumber);
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
    public void setIsCreatureAdding(boolean isCreatureAdding){
        mainPane.setIsCreatureAdding(isCreatureAdding);
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
    public boolean isDefenderSelecting(){
        return mainPane.isDefenderSelecting();
    }
    public void setIsDefenderSelecting(boolean isAttackedSelecting){
        mainPane.setIsDefenderSelecting(isAttackedSelecting);
    }

    public void setDeckPaneTop(){
        mainPane.deckPane.setTop();
    }
}
