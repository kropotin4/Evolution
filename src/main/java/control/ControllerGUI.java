package control;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.*;
import model.decks.PlayerCardDeck;
import server.message.ChatMessage;
import server.message.ClientMessage;

import view.gui.*;

import java.util.ArrayList;

public class ControllerGUI {

    Controller controller;
    ControllerClient controllerClient;

    AiGameSettingPane aiGameSettingPane;

    MainPane mainPane;
    EndGamePane endGamePane;
    public SoundPane soundPane;
    DefensePane defensePane;

    GameType type;

    boolean blockActions = false;

    int playerNumber; // doNextMove()

    private class CommAndCoopLinks{

        private class CreatureLinks{

            CreatureNode creatureNode;
            Creature creature;
            Creature otherCreature;

            boolean wasClicked = false;

            int commLinksNumber = 0;
            int coopLinksNumber = 0;

            void addCommLinks(int linkNumber){
                if(linkNumber <= 0 || linkNumber > 2)
                    throw new RuntimeException("CreatureLinks: addCommLinks");

                commLinksNumber += linkNumber;
            }
            void deleteCommLinks(int linkNumber){
                if(linkNumber <= 0 || linkNumber > 2)
                    throw new RuntimeException("CreatureLinks: deleteCommLinks");

                commLinksNumber -= linkNumber;
            }

            void addCoopLinks(int linkNumber){
                if(linkNumber <= 0 || linkNumber > 2)
                    throw new RuntimeException("CreatureLinks: addCoopLinks");

                coopLinksNumber += linkNumber;
            }
            void deleteCoopLinks(int linkNumber){
                if(linkNumber <= 0 || linkNumber > 2)
                    throw new RuntimeException("CreatureLinks: deleteCoopLinks");

                coopLinksNumber -= linkNumber;
            }
        }

        private ArrayList<CreatureLinks> creatures = new ArrayList<>();

        public void add(CreatureNode creatureNode){
            Creature creature = controller.getPlayPlayer().findCreature(creatureNode.getCreatureId());
            ArrayList[] arrayLists = creature.getCommAndCoopLists();

            boolean isFind = false;
            for(Creature creature1 : (ArrayList<Creature>)arrayLists[0]){
                isFind = false;
                for(CreatureLinks creatureLinks : creatures){
                    if(creatureLinks.creature == creature1){
                        creatureLinks.addCommLinks(1);
                        isFind = true;
                        break;
                    }
                }
                if(!isFind){
                    CreatureLinks creatureLinks = new CreatureLinks();
                    creatureLinks.creature = creature1;
                    creatureLinks.otherCreature = creature;
                    creatureLinks.creatureNode = creatureNode.getPlayerPane().findCreatureNode(creature1.getId());
                    creatureLinks.addCommLinks(1);
                    creatures.add(creatureLinks);
                }
            }
            for(Creature creature1 : (ArrayList<Creature>)arrayLists[1]){
                isFind = false;
                for(CreatureLinks creatureLinks : creatures){
                    if(creatureLinks.creature == creature1){
                        creatureLinks.addCoopLinks(1);
                        isFind = true;
                        break;
                    }
                }
                if(!isFind){
                    CreatureLinks creatureLinks = new CreatureLinks();
                    creatureLinks.creature = creature1;
                    creatureLinks.otherCreature = creature;
                    creatureLinks.creatureNode = creatureNode.getPlayerPane().findCreatureNode(creature1.getId());;
                    creatureLinks.addCoopLinks(1);
                    creatures.add(creatureLinks);
                }
            }
        }

        public void delete(Creature creature, boolean isCoomLink){
            for(CreatureLinks creatureLinks : creatures){
                if(creatureLinks.creature == creature){
                    if(isCoomLink)
                        creatureLinks.deleteCommLinks(1);
                    else
                        creatureLinks.deleteCoopLinks(1);
                    return;
                }
            }
        }

        public void deleteCreature(Creature creature){
            for(CreatureLinks creatureLinks : creatures){
                if(creatureLinks.creature == creature){
                    creatures.remove(creatureLinks);
                    return;
                }
            }
        }

        public void showButtons(){
            int activeButtonsNumber = 0;
            for(CreatureLinks creatureLinks : creatures){
                if(controller.isCreatureSatisfied(
                        creatureLinks.creatureNode.getPlayerPane().getPlayerNumber(),
                        creatureLinks.creatureNode.getCreatureId())
                ){
                    creatureLinks.commLinksNumber = 0;
                    creatureLinks.coopLinksNumber = 0;
                    creatureLinks.creatureNode.setFoodStyle(0, 0);
                    continue;
                }

                if(controller.getFoodNumber() == 0){
                    creatureLinks.commLinksNumber = 0;
                }

                creatureLinks.creatureNode.setFoodStyle(creatureLinks.commLinksNumber, creatureLinks.coopLinksNumber);

                if(creatureLinks.commLinksNumber > 0){
                    activeButtonsNumber++;
                    creatureLinks.creatureNode.getCommButton().setOnMouseClicked(event -> {
                        creatureLinks.creature.getPlayer().getFoodFromCommunication(creatureLinks.creature,creatureLinks.otherCreature);
                        creatureLinks.creatureNode.updateEatButton();
                        mainPane.checkInfoPane();
                        creatureLinks.commLinksNumber--;

                        creatureLinks.creatureNode.setFoodStyle(creatureLinks.commLinksNumber, creatureLinks.coopLinksNumber);

                        if(!creatureLinks.wasClicked){
                            creatureLinks.wasClicked = true;
                            add(creatureLinks.creatureNode);
                            showButtons();
                        }

                        boolean endFlag = true;
                        for(CreatureLinks creatureLinks1 : creatures){
                            if(creatureLinks1.coopLinksNumber > 0 || creatureLinks1.commLinksNumber > 0) {
                                endFlag = false;
                                break;
                            }
                        }
                        if(endFlag)
                            endFoodDistribution();
                    });
                }
                if(creatureLinks.coopLinksNumber > 0){
                    activeButtonsNumber++;
                    creatureLinks.creatureNode.getCoopButton().setOnMouseClicked(event -> {
                        creatureLinks.creature.getPlayer().getFoodFromCooperation(creatureLinks.creature,creatureLinks.otherCreature);
                        creatureLinks.creatureNode.updateEatButton();
                        mainPane.checkInfoPane();
                        creatureLinks.coopLinksNumber--;

                        creatureLinks.creatureNode.setFoodStyle(creatureLinks.commLinksNumber, creatureLinks.coopLinksNumber);

                        if(!creatureLinks.wasClicked){
                            creatureLinks.wasClicked = true;
                            add(creatureLinks.creatureNode);
                            showButtons();
                        }

                        boolean endFlag = true;
                        for(CreatureLinks creatureLinks1 : creatures){
                            if(creatureLinks1.coopLinksNumber > 0 || creatureLinks1.commLinksNumber > 0) {
                                endFlag = false;
                                break;
                            }
                        }
                        if(endFlag)
                            endFoodDistribution();
                    });
                }
            }
            if(activeButtonsNumber == 0)
                endFoodDistribution();
        }
        public void clear(){
            creatures.clear();
        }
    }
    CommAndCoopLinks commAndCoopLinks = new CommAndCoopLinks();

    public ControllerGUI(Stage primaryStage, int playerNumber){
        this.playerNumber = playerNumber;

        this.aiGameSettingPane = new AiGameSettingPane(this, primaryStage);
        this.mainPane = new MainPane(primaryStage, this);
        this.endGamePane = new EndGamePane(primaryStage, 0);
        this.soundPane = new SoundPane();
        this.defensePane = new DefensePane(this);

        type = GameType.ALONE;

        //controller.getPlayers().get(1).setAI(true);

        //mainPane.setPhaseElement(Phase.GROWTH);
        //startGame();
    }
    public ControllerGUI(Stage primaryStage, Controller controller, ControllerClient controllerClient, int playerNumber){
        this.controller = controller;
        this.controllerClient = controllerClient;
        this.playerNumber = playerNumber;

        this.mainPane = new MainPane(primaryStage, this);
        this.endGamePane = new EndGamePane(primaryStage, playerNumber);
        this.soundPane = new SoundPane();
        this.defensePane = new DefensePane(this);

        type = GameType.CLIENT;

        if(playerNumber != controller.getPlayerTurn())
            blockActions = true;
    }

    public void startGameSetting(){
        aiGameSettingPane.show();
    }

    public void startGame(){
        System.out.println("ControllerGUI: start game (player number = " + playerNumber + ")");
        mainPane.show();
        mainPane.update(playerNumber);
    }


    public void doNextMove(String message){
        if(type == GameType.ALONE)
            playerNumber = controller.doNextMove();
        else
            controller.doNextMove();

        blockActions = playerNumber != controller.getPlayerTurn();

        if(type == GameType.CLIENT){
            switch (controller.getCurrentPhase()){
                case EATING:
                    controllerClient.sendMessage(new ClientMessage(controller.getTable(), message));
                    break;
                case GROWTH:
                    controllerClient.sendMessage(new ClientMessage(controller.getTable(), message));
                    break;
                default:
                    break;
            }
        }

        update();

    }

    public void update(){
        if(!endGamePane.isShow()) {
            blockActions = playerNumber != controller.getPlayerTurn();

            if (controller.isGameOver()) {
                endGamePane.setInfo(new EndGameInfo(controller));
                endGamePane.show();
            }

            Player player = controller.hasIntention();
            if(player != null){ // Есть кто-то с намереньем защиты
                System.out.println("Intention exist");
                blockActions = true;
                if(player.getPlayerNumber() == playerNumber || type == GameType.ALONE){ // Это ты
                    System.out.println("Show DefensePane");
                    defensePane.setDefensePlayer(player);
                    defensePane.show();
                }
            }

            mainPane.update(playerNumber);
        }
    }

    public boolean isBlockActions(){
        return blockActions;
    }

    public void sendChatMessage(String message){
        if(type == GameType.CLIENT){
            controllerClient.sendMessage(
                    new ChatMessage(
                            controllerClient.getLogin(),
                            message,
                            getPlayerColor(playerNumber))
            );
        }
        else {
            addMessageToChat(
                    "Я",
                    message,
                    getPlayerColor(playerNumber)
            );
        }
    }
    public void addMessageToChat(String message){
        mainPane.getChat().addMessage(message);
    }
    public void addMessageToChat(String login, String message, String color){
        mainPane.getChat().addMessage(login, message, color);
    }

    public void passPlayer(){
        controller.setPlayerPass(playerNumber);
        doNextMove("Игрок спасовал");
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

            doNextMove("Использован жировой запас");
        }
    }

    public MainPane getMainPane(){
        return mainPane;
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
    public int getCreatureHunger(CreatureNode creatureNode){
        return controller.getCreatureHunger(creatureNode.getPlayerPane().getPlayerNumber(), creatureNode.getCreatureId());
    }
    public int getCreatureSatiety(CreatureNode creatureNode){
        return controller.getCreatureSatiety(creatureNode.getPlayerPane().getPlayerNumber(), creatureNode.getCreatureId());
    }
    public boolean isCreatureFed(CreatureNode creatureNode){
        return controller.isCreatureFed(creatureNode.getPlayerPane().getPlayerNumber(), creatureNode.getCreatureId());
    }
    public boolean isCreatureCanEat(CreatureNode creatureNode){
        return controller.isCreatureCanEat(creatureNode.getPlayerPane().getPlayerNumber(), creatureNode.getCreatureId());
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

            doNextMove("Добавили существо");
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

            doNextMove("К существу добавлено свойство");
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

            doNextMove("К чужому существу добавлено свойство");
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

        doNextMove("К существам добавлено парное свойство");
    }
    public void addSymbiosisTraitToCreature(CreatureNode crocodileNode, CreatureNode birdNode, CardNode cardNode, boolean isUp){

        controller.addSymbiosisTraitToCreature(
                playerNumber,
                crocodileNode.getCreatureId(),
                birdNode.getCreatureId(),
                cardNode.getCard(),
                isUp
        );

        mainPane.showSelectedCard(false);
        mainPane.setIsCreatureAdding(false);
        mainPane.setIsCardSelecting(false);
        mainPane.updateCurrentPlayer();

        doNextMove("Мир приветствует нового паразита");
    }
    /////////////////////

    public ArrayList<Card> getCreatureCards(CreatureNode creatureNode){
        return controller.getCreatureCards(creatureNode.getPlayerPane().getPlayerNumber(), creatureNode.getCreatureId());
    }

    public int getPlayerCardsNumber(){
        return controller.getPlayerCardsNumber(playerNumber);
    }
    public String getPlayerColor(int playerNumber){
        return controller.getPlayers().get(playerNumber).getColor();
    }


    public void getFoodFromFodder(CreatureNode creatureNode){
        controller.getFoodFromFodder(playerNumber, creatureNode.getCreatureId());
        mainPane.setEaten(true);

        Creature eatCreature = controller.getPlayPlayer().findCreature(creatureNode.getCreatureId());
        if(eatCreature.getCommunicationListSize() > 0 || eatCreature.getCooperationListSize() > 0){
            startFoodDistribution(creatureNode);
        }
        else{
            mainPane.setIsFoodGetting(false);
            mainPane.updateCurrentPlayer();
            soundPane.playSound("eating");
            doNextMove("Из кормовой базы взята еда");
        }
    }
    private void startFoodDistribution(CreatureNode creatureNode){
        creatureNode.update();
        mainPane.checkInfoPane();
        commAndCoopLinks.clear();
        commAndCoopLinks.add(creatureNode);
        commAndCoopLinks.showButtons();
    }
    public void endFoodDistribution(){
        mainPane.setEaten(false);
        mainPane.setIsFoodGetting(false);
        mainPane.updateCurrentPlayer();
        doNextMove("Из кормовой базы взята еда");
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
    //   return controller.findCard(playerTurn, creatureNode.getCreatureId(), cardNode.getCardFromCommonDeck());
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

    public void setPlayerScavenger(CreatureNode scavenger){
        controller.setPlayerScavenger(
                scavenger.getPlayerPane().getPlayerNumber(),
                scavenger.getCreatureId()
        );
    }
    public boolean isActiveScavenger(CreatureNode scavenger){
        return controller.isActiveScavenger(
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
            soundPane.playSound("sleeping");
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

    public void pirateCreature(CreatureNode pirateVictimCreature){

        controller.pirateCreature(
                mainPane.getPirateCreature().getPlayerPane().getPlayerNumber(),
                pirateVictimCreature.getPlayerPane().getPlayerNumber(),
                mainPane.getPirateCreature().getCreatureId(),
                pirateVictimCreature.getCreatureId()
        );
        soundPane.playSound("pirating");
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
    public  void setDefenderCreature(CreatureNode creatureNode){
        System.out.println("ControllerGUI: setDefenderCreature: " + creatureNode);
        mainPane.setDefenderCreature(creatureNode);
    }
    ///
    public void attackCreature(CreatureNode defender){
        setDefenderCreature(defender);

        controller.attackCreature(
                mainPane.getAttackerCreature().getPlayerPane().getPlayerNumber(),
                defender.getPlayerPane().getPlayerNumber(),
                mainPane.getAttackerCreature().getCreatureId(),
                defender.getCreatureId()
        );
        mainPane.getAttackerCreature().setStyleType(0);
        mainPane.update(playerNumber);

        setIsDefenderSelecting(false);
        setIsAttackerSelecting(false);
        soundPane.playSound("attack");
        doNextMove("Атаковано существо");
    }
    //endregion

    public void doTailLoss(Card lostCard, Creature attacker, Creature victim, Creature ... creatures){
        if(controller.doTailLoss(lostCard, attacker, victim, creatures)){
            doNextMove("Отбрасование хвоста");
        }
        else{
            doNextMove("Отбрасование хвоста не удалось");
        }
    }

    public boolean isMimicryTargetSelecting() {
        return mainPane.isMimicryTargetSelecting();
    }
    public void setMimicryTargetSelecting(boolean isMimicryTargetSelecting) {
        mainPane.setMimicryTargetSelecting(isMimicryTargetSelecting);
    }
    public void showMimicryTargets(int attackerPlayer, int defenderPlayer, int attackerCreatureID, int defenderCreatureID){

        PlayerPane defPlayerPane = null;
        for(Node node : mainPane.getPlayersPane()){
            PlayerPane playerPane = (PlayerPane) node;
            if(playerPane.getPlayerNumber() == defenderPlayer){
                defPlayerPane = playerPane;
            }
        }

        if(defPlayerPane == null){
            defPlayerPane = mainPane.getCurrentPlayerPane();
        }

        for(CreatureNode creatureNode1 : defPlayerPane.getCreatureNodes()){
            if(defenderCreatureID == creatureNode1.getCreatureId()) continue;

            if (controller.isAttackPossible(
                    attackerPlayer,
                    defenderPlayer,
                    attackerCreatureID,
                    creatureNode1.getCreatureId())) {

                creatureNode1.setStyleType(1);
            }
        }
        setMimicryTargetSelecting(true);
    }
    public void doMimicry(CreatureNode victim){
        if(controller.doMimicry(
                victim.getPlayerPane().getPlayerNumber(),
                victim.getCreatureId())
        ){
            setMimicryTargetSelecting(false);
            doNextMove("Мимикрия сработала");
        }
        else {
            setMimicryTargetSelecting(false);
            doNextMove("Мимикрия не сработала");
        }
    }

    public boolean doRunning(Creature attacker, Creature victim){
        if(controller.doRunning(attacker, victim)){
            doNextMove("Побег удался");
            return true;
        }
        else {
            doNextMove("Побег не удался");
            return false;
        }
    }

    public void setDeckPaneTop(){
        mainPane.deckPane.setTop();
    }
}