package view.gui;

import control.Controler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import model.Creature;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerPane extends VBox {

    Controler controler;
    int playerNumber;

    public PlayerPane(Controler controler, int playerNumber){
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
        update();
    }

    public void update(){
        for(Creature creature : controler.getCreatures(playerNumber)){
            CreatureNode creatureNode = new CreatureNode(creature);
            this.getChildren().add(creatureNode);
        }
    }
}
