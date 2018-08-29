package control;

import model.Card;
import model.Creature;
import model.Player;
import view.gui.CreatureNode;
import view.gui.MainPane;

import java.util.ArrayList;

public class ControlerGUI {

    Controler controler;
    MainPane mainPane;

    public ControlerGUI(Controler controler, MainPane mainPane){
        this.controler = controler;
        this.mainPane = mainPane;
    }

    public void addTraitToCreature(CreatureNode creatureNode, Card card){

    }
    public void addPairTraitroCreature(CreatureNode creatureNode1, CreatureNode creatureNode2, Card card){

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
}
