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

public class ControlerGUI {

    Controler controler;
    MainPane mainPane;

    int playerNumber; // Меняется в doNextMove()

    public ControlerGUI(Controler controler, MainPane mainPane, int playerNumber){
        this.controler = controler;
        this.mainPane = mainPane;
        this.playerNumber = playerNumber;
    }

    public void startGame(){
        mainPane.update(0);
    }

    public void doNextMove(){
        playerNumber = controler.doNextMove();
        mainPane.update(controler.getPlayerTurn());
    }

    public void passPlayer(){
        controler.setPlayerPass(playerNumber);
        doNextMove();
    }
    public int getPlayersNumber(){
        return controler.getPlayersNumber();
    }
    public Phase getCurrentPhase(){
        return controler.getCurrentPhase();
    }
    public int getFoodNumber(){
        return controler.getFoodNumber();
    }

    public boolean havePlayerPredator(){
        return controler.havePlayerPredator(playerNumber);
    }

    public boolean haveHungryCreature(){
        return controler.haveHungryCreature(playerNumber);
    }
    public int getCreauterHunger(CreatureNode creatureNode){
        return controler.getCreauterHunger(creatureNode.getPlayerPane().getPlayerNumber(), creatureNode.getCreatureId());
    }
    public int getCreauterSatiety(CreatureNode creatureNode){
        return controler.getCreauterSatiety(creatureNode.getPlayerPane().getPlayerNumber(), creatureNode.getCreatureId());
    }
    public boolean isCreatureFed(CreatureNode creatureNode){
        return controler.isCreatureFed(creatureNode.getPlayerPane().getPlayerNumber(), creatureNode.getCreatureId());
    }

    public void addCreature(CardNode cardNode){
        controler.addCreature(playerNumber, cardNode.getCard());
        mainPane.showSelectedCard(false);
        mainPane.setIsCreatureAdding(false);
        mainPane.setIsCardSelecting(false);
        mainPane.updateCurrentPlayer();
    }

    public boolean findTrait(CreatureNode creatureNode, Trait trait){
        return controler.findTrait(creatureNode.getPlayerPane().getPlayerNumber(), creatureNode.getCreatureId(), trait);
    }

    public void showAddTraitPane(){
        mainPane.showAddTraitPane();
    }
    public void addTraitToCreature(CreatureNode creatureNode, CardNode cardNode){

    }
    public void addTraitToSelectedCreature(CardNode cardNode, boolean isUp){
        if(!controler.findTrait(playerNumber, mainPane.getSelectedCreature().getCreatureId(), cardNode.getCard(), isUp)
        && cardNode.getCard().getTrait(isUp) != Trait.PARASITE) {
            controler.addTraitToCreature(playerNumber, mainPane.getSelectedCreature().getCreatureId(), cardNode.getCard(), isUp);
            mainPane.showSelectedCard(false);
            mainPane.setIsCreatureAdding(false);
            mainPane.setIsCardSelecting(false);
            mainPane.updateCurrentPlayer();
        }
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
        mainPane.setIsFoodGetting(false);
        controler.getFoodFromFodder(playerNumber, creatureNode.getCreatureId());
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
                if (controler.isAttackPossible(
                        attackerPlayer,
                        playerPane.getPlayerNumber(),
                        attackerCreature,
                        creatureNode1.getCreatureId())) {

                    playerPane.setAttackStyle(creatureNode1);
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
        return controler.getCreatures(playerNumber);
    }

    //public boolean findCard(CreatureNode creatureNode, CardNode cardNode){
    //   return controler.findCard(playerNumber, creatureNode.getCreatureId(), cardNode.getCard());
    //}
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
