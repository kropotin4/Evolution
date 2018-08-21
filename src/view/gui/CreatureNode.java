package view.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import model.Card;
import model.Creature;
import model.Phase;

import java.io.IOException;
import java.util.ArrayList;

public class CreatureNode extends VBox {

    Creature creature;

    public CreatureNode(Creature creature){
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/CreatureNode.fxml")
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
        //this.getStylesheets().add("-fx-border-color: green; -fx-border-width: 1;");
        this.setStyle("-fx-border-color: green; -fx-border-width: 1;");
        this.setMaxWidth(100);
        this.setAlignment(Pos.BOTTOM_CENTER);
        this.setPadding(new Insets(2));
    }

    public void update(){
        this.getChildren().clear();

        ArrayList<Card> cards = creature.getCards();
        for(int i = cards.size() - 1; i >= 0; --i){
            Label label = new Label(cards.get(i).getTrait().toString());
            label.setAlignment(Pos.CENTER);
            label.setPrefWidth(500);
            label.setStyle("-fx-background-color: yellow;");

            this.getChildren().add(label);
        }

    }
}
