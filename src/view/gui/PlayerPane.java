package view.gui;

import control.Controler;
import control.ControlerGUI;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Creature;
import model.Trait;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerPane extends HBox {

    ControlerGUI controler;
    int playerNumber;

    ImageView imageView = new ImageView("/images/icon1.png");

    public PlayerPane(ControlerGUI controler, int playerNumber){
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/PlayerPane.fxml")
        );

        //fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.controler = controler;
        this.playerNumber = playerNumber;
    }

    @FXML
    private void initialize(){
        this.setStyle("-fx-border-width: 1; -fx-border-color: black;");
        update();
    }

    public void update(){
        for(Creature creature : controler.getCreatures(playerNumber)){
            CreatureNode creatureNode = new CreatureNode(this, creature.getId());
            creatureNode.update();
            this.getChildren().add(creatureNode);

            creatureNode.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    controler.selectCreature(creatureNode);
                    System.out.println("Select creature: " + creature.getId());

                    setFalseStyle(creatureNode);
                }
            });

            setTrueStyle(creatureNode);
        }
    }
    public void setCreaturesWithTraitTrue(Trait trait){

        for(Node node : this.getChildren()){
            CreatureNode creatureNode = (CreatureNode) node;

            setTrueStyle(creatureNode);
            creatureNode.isFalse = false;
        }
    }
    public void setAllCreaturesFalse(){

        for(Node node : this.getChildren()){
            CreatureNode creatureNode = (CreatureNode) node;
            setFalseStyle(creatureNode);
            creatureNode.isFalse = true;
        }

    }
    public void setAllCreaturesDefault(){

        for(Node node : this.getChildren()){
            CreatureNode creatureNode = (CreatureNode) node;
            setDefaultStyle(creatureNode);
            creatureNode.isFalse = false;
        }

    }

    void setTrueStyle(CreatureNode creatureNode){
        creatureNode.setStyle("");
        setCreatureBorders(creatureNode, "green", 2.5);
        creatureNode.setStyle(creatureNode.getStyle() + "-fx-background-color: rgba(0,255,127,0.3);");
    }
    void setFalseStyle(CreatureNode creatureNode){
        creatureNode.setStyle("");
        setCreatureBorders(creatureNode, "red", 2.5);
        creatureNode.setStyle(creatureNode.getStyle() + "-fx-background-color: rgba(255,99,71,0.3);");
    }
    void setDefaultStyle(CreatureNode creatureNode){
        creatureNode.setStyle("");
        setCreatureBorders(creatureNode, "green", 1);
    }

    void setCreatureBorders(CreatureNode creatureNode, String color, double width){
        creatureNode.setBorder(color, width);
    }

    public ImageView showAddIcon(){
        this.getChildren().add(imageView);

        return imageView;
    }

    public int getPlayerNumber(){
        return playerNumber;
    }
}
