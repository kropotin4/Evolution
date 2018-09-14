package control;

import model.Card;
import model.Creature;
import model.Player;
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

    public void doNextMove(){

    }

    public int getPlayersNumber(){
        return controler.getPlayersNumber();
    }

    public void addTraitToCreature(CreatureNode creatureNode, Card card){

    }
    public void addPairTraitToCreature(CreatureNode creatureNode1, CreatureNode creatureNode2, Card card){

    }

    public ArrayList<Card> getCreatureCards(CreatureNode creatureNode){
        return controler.getCreatureCards(creatureNode.getPlayerPane().getPlayerNumber(), creatureNode.getCreatureId());
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
    }
    public PlayerCardDeck getPlayerCardDeck(){
        return controler.getPlayers().get(playerNumber).getPlayerCardDeck();
    }
}
