package view.gui;

import control.Controler;
import control.ControlerGUI;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Creature;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerPane extends HBox {

    ControlerGUI controler;
    int playerNumber;

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
        //update();
    }

    public void update(){
        for(Creature creature : controler.getCreatures(playerNumber)){
            CreatureNode creatureNode = new CreatureNode(this, creature);
            creatureNode.update();
            this.getChildren().add(creatureNode);

            creatureNode.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    controler.selectCreature(creatureNode);
                    System.out.println("Select creature: " + creature.getId());
                }
            });
        }
    }
}