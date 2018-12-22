package control;

import javafx.scene.Node;
import javafx.stage.Stage;
import model.*;
import model.decks.PlayerCardDeck;
import server.message.ChatMessage;
import server.message.EatingMessage;
import server.message.GrowthMessage;
import view.gui.*;

import java.util.ArrayList;

public class ControllerGUI {

    Controller controller;
    ControllerClient controllerClient;
    ControllerServer controllerServer;
    MainPane mainPane;

    GameType type;

    boolean blockActions = false;

    int playerNumber; // doNextMove()

    public ControllerGUI(Stage primaryStage, Controller controller, int playerNumber){
        this.controller = controller;
        this.mainPane = new MainPane(primaryStage, this);
        this.playerNumber = playerNumber;
        type = GameType.ALONE;

        //mainPane.setPhaseElement(Phase.GROWTH);
        //startGame();
    }
    public ControllerGUI(Stage primaryStage, Controller controller, ControllerClient controllerClient, int playerNumber){
        this.controller = controller;
        this.controllerClient = controllerClient;
        this.mainPane = new MainPane(primaryStage, this);
        this.playerNumber = playerNumber;
        type = GameType.CLIENT;

        if(playerNumber != controller.getPlayerTurn())
            blockActions = true;
    }
    /*public ControllerGUI(Controller controller, ControllerServer controllerServer, MainPane mainPane, int playerNumber){
        this.controllerServer = controllerServer;
        this.mainPane = mainPane;
        this.playerNumber = playerNumber;
        type = 2;
    }*/


    public void startGame(){
        System.out.println("ControllerGUI: start game (player number = " + playerNumber + ")");
        mainPane.show();
        mainPane.update(playerNumber);
    }

    public void doNextMove(){
        if(type == GameType.ALONE)
            playerNumber = controller.doNextMove();
        else
            controller.doNextMove();

        blockActions = playerNumber != controller.getPlayerTurn();

        mainPane.update(playerNumber);

        if(type == GameType.CLIENT){
            switch (controller.getCurrentPhase()){
                case EATING:
                    controllerClient.sendMessage(new EatingMessage(controller.getTable()));
                    break;
                case GROWTH:
                    controllerClient.sendMessage(new GrowthMessage(controller.getTable()));
                    break;
                default:
                    break;
            }
        }
    }

    public void update(){
        blockActions = playerNumber != controller.getPlayerTurn();

        mainPane.update(playerNumber);
    }
    public boolean isBlockActions(){
        return blockActions;
    }

    public void sendChatMessage(String message){
        if(type == GameType.CLIENT){
            controllerClient.sendMessage(new ChatMessage(controllerClient.getLogin(), message));
        }
    }
    public void addMessageToChat(String message){
        mainPane.getChat().addMessage(message);
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

    // сделана блокировка
    public void useFatTissue(CreatureNode creatureNode, int cardNumber){
        if(!blockActions) {
            controller.useFatTissue(
                    creatureNode.getPlayerPane().getPlayerNumber(),
                    creatureNode.getCreatureId(),
                    cardNumber
            );
            mainPane.updateCurrentPlayer();

            if(type == GameType.CLIENT){
                doNextMove();
            }
        }
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

    public void setGrazingActive(CreatureNode creatureNode, boolean isActive){
        controller.setGrazingActive(creatureNode.getPlayerPane().getPlayerNumber(), creatureNode.getCreatureId(), isActive);
    }
    public boolean isPoisoned(CreatureNode creatureNode){
        return controller.isPoisoned(creatureNode.getPlayerPane().getPlayerNumber(), creatureNode.getCreatureId());
    }
    public boolean havePiracyCreatures(){
        return controller.havePiracyCreatures(playerNumber);
    }
    public boolean haveCanPiracyCreatures(){
        return controller.haveCanPiracyCreatures(playerNumber);
    }

    //Сделана блокировка
    public void addCreature(CardNode cardNode){
        if(!blockActions) {
            controller.addCreature(playerNumber, cardNode.getCard());
            mainPane.showSelectedCard(false);
            mainPane.setIsCreatureAdding(false);
            mainPane.setIsCardSelecting(false);
            mainPane.updateCurrentPlayer();

            if(type == GameType.CLIENT){
                doNextMove();
            }
        }
    }

    ////////////////////
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

            if(type == GameType.CLIENT){
                doNextMove();
            }
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

            if(type == GameType.CLIENT){
                doNextMove();
            }
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

        if(type == GameType.CLIENT){
            doNextMove();
        }

    }
    /////////////////////

    public ArrayList<Card> getCreatureCards(CreatureNode creatureNode){
        return controller.getCreatureCards(creatureNode.getPlayerPane().getPlayerNumber(), creatureNode.getCreatureId());
    }

    public int getPlayerCardsNumber(){
        return controller.getPlayerCardsNumber(playerNumber);
    }

    public void getFoodFromFodder(CreatureNode creatureNode){
        mainPane.setIsFoodGetting(false);
        controller.getFoodFromFodder(playerNumber, creatureNode.getCreatureId());
        mainPane.updateCurrentPlayer();

        if(type == GameType.CLIENT){
            doNextMove();
        }
    }
    public boolean isFoodGetting(){
        return mainPane.isFoodGetting();
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

    public void setPlayerScavanger(CreatureNode scavanger){
        controller.setPlayerScavanger(
                scavanger.getPlayerPane().getPlayerNumber(),
                scavanger.getCreatureId()
        );
    }
    public boolean isActiveScavanger(CreatureNode scavenger){
        return controller.isActiveScavanger(
                scavenger.getPlayerPane().getPlayerNumber(),
                scavenger.getCreatureId()
        );
    }
    public int getScavengerNumber(CreatureNode creatureNode){
        return controller.getScavengerNumber(creatureNode.getPlayerPane().getPlayerNumber());
    }

    public void setCreatureHibernating(CreatureNode creatureHibernating){
        if(!blockActions) {
            controller.setCreatureHibernating(
                    creatureHibernating.getPlayerPane().getPlayerNumber(),
                    creatureHibernating.getCreatureId(),
                    true
            );
            mainPane.updateCurrentPlayer();
        }
    }
    public boolean isHibernating(CreatureNode creatureNode){
        return controller.isHibernating(
                creatureNode.getPlayerPane().getPlayerNumber(),
                creatureNode.getCreatureId()
        );
    }
    public int getHibernatingTime(CreatureNode creatureNode){
        return controller.getHibernatingTime(
                creatureNode.getPlayerPane().getPlayerNumber(),
                creatureNode.getCreatureId()
        );
    }

    ///region pirate
    public boolean isPirateSelecting() {
        return mainPane.isPirateSelecting();
    }
    public void setPirateSelecting(boolean isPirateSelecting) {
        mainPane.setPirateSelecting(isPirateSelecting);
    }
    public boolean isPirateVictimSelecting() {
        return mainPane.isPirateVictimSelecting();
    }
    public void setPirateVictimSelecting(boolean isPirateVictimSelecting) {
        mainPane.setPirateVictimSelecting(isPirateVictimSelecting);
    }

    public void showPirateVictimCreatures(CreatureNode creatureNode){
        for (Node node : mainPane.getPlayersPane()) {
            PlayerPane playerPane = (PlayerPane) node;
            playerPane.setPiracyAvailableCreaturesTrue(creatureNode);
        }
        mainPane.getCurrentPlayerPane().setPiracyAvailableCreaturesTrue(creatureNode);
    }
    public void setPirateCreature(CreatureNode creatureNode){
        System.out.println("ControllerGUI: setPirateCreature: " + creatureNode);
        setPirateSelecting(false);
        setPirateVictimSelecting(true);
        showPirateVictimCreatures(creatureNode);
        mainPane.setPirateCreature(creatureNode);
    }

    /// TODO: пиратсво клиента
    public void pirateCreature(CreatureNode pirateVictimCreature){

        controller.pirateCreature(
                mainPane.getPirateCreature().getPlayerPane().getPlayerNumber(),
                pirateVictimCreature.getPlayerPane().getPlayerNumber(),
                mainPane.getPirateCreature().getCreatureId(),
                pirateVictimCreature.getCreatureId()
        );

        mainPane.update(playerNumber);
    }
    ///endregion

    //region attack
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
    ///
    public void attackCreature(CreatureNode defender){
        controller.attackCreature(
                mainPane.getAttackerCreature().getPlayerPane().getPlayerNumber(),
                defender.getPlayerPane().getPlayerNumber(),
                mainPane.getAttackerCreature().getCreatureId(),
                defender.getCreatureId()
        );
        mainPane.getAttackerCreature().setStyleType(0);
        mainPane.update(playerNumber);

        if(type == GameType.CLIENT){
            doNextMove();
        }
    }
    //endregion

    public void setDeckPaneTop(){
        mainPane.deckPane.setTop();
    }
}