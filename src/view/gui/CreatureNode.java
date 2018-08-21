package view.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import model.Card;
import model.Creature;
import model.Phase;

import java.io.IOException;
import java.util.ArrayList;

public class CreatureNode extends VBox {

    Creature creature;

    public CreatureNode(Creature creature){
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/CreaturePane.fxml")
        );

        //fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.creature = creature;
    }

    @FXML
    private void initialize(){

    }

    public void update(){

        ArrayList<Card> cards = creature.getCards();

        for(int i = cards.size() - 1; i >= 0; --i){
            Label label = new Label(cards.get(i).getTrait().toString());
            label.setPrefWidth(500);
            this.getChildren().add(label);
        }

    }
}
