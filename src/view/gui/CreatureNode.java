package view.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import model.Card;
import model.Creature;
import model.Phase;
import model.Trait;

import java.io.IOException;
import java.util.ArrayList;

public class CreatureNode extends VBox {

    PlayerPane playerPane;

    Creature creature;

    public CreatureNode(PlayerPane playerPane, Creature creature){
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

        this.playerPane = playerPane;
        this.creature = creature;
    }

    @FXML
    private void initialize(){
        //this.getStylesheets().add("-fx-border-color: green; -fx-border-width: 1;");
        this.setStyle("-fx-border-color: green; -fx-border-width: 1;");
        this.setPrefHeight(190);
        this.setMaxWidth(100);
        this.setAlignment(Pos.BOTTOM_CENTER);
        this.setPadding(new Insets(2));
    }

    public void update(){
        this.getChildren().clear();

        ArrayList<Card> cards = creature.getCards();
        for(int i = cards.size() - 1; i >= 0; --i){
            Trait trait = cards.get(i).getTrait();

            Label label = new Label(trait.toString());

            label.setWrapText(true);
            label.setAlignment(Pos.CENTER);
            label.setTextAlignment(TextAlignment.CENTER);
            label.setPrefWidth(500);

            switchTraitStyle(label, trait);

            this.getChildren().add(label);
        }

        Label button = new Label("0");
        button.setAlignment(Pos.CENTER);
        button.setTextAlignment(TextAlignment.CENTER);
        button.setPrefHeight(7);
        button.setPrefWidth(20);
        button.setStyle("-fx-border-width: 1; -fx-border-color: green; -fx-border-radius: 30; -fx-background-color: transparent;");

        this.getChildren().add(button);

    }

    private void switchTraitStyle(Node node, Trait trait){
        switch (trait){
            case PREDATOR:
                setTraitStyle(node, "coral");
                break;
            case FAT_TISSUE:
                setTraitStyle(node, "khaki");
                break;
        }
    }

    private void setTraitStyle(Node node, String color){
        node.setStyle("");

        node.setStyle("-fx-border-width: 1.5 0 0 0; -fx-border-color: green;");
        node.setStyle(node.getStyle() + " -fx-background-color: " + color + ";");
    }
}
