package view.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Card;

import java.io.IOException;

public class CardNode extends HBox {

    Card card;

    @FXML VBox traits_box;

    public CardNode(Card card){
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/CardNode.fxml")
        );

        //fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.card = card;
    }

    @FXML
    private void initialize(){

        if(card.getTrait(true) == card.getTrait(false)){

            Label labelUp = new Label(card.getTrait(true).toString());
            Label labelDown = new Label(card.getTrait(false).toString());

            labelUp.setPrefWidth(500);
            labelDown.setPrefWidth(500);
            labelUp.setPrefHeight(500);
            labelDown.setPrefHeight(500);
            labelUp.setWrapText(true);
            labelDown.setWrapText(true);

            CreatureNode.switchTraitStyle(labelUp, card.getTrait(true));
            CreatureNode.switchTraitStyle(labelDown, card.getTrait(false));

            traits_box.getChildren().addAll(labelUp, labelDown);
        }
        else{

            Label label = new Label(card.getTrait().toString());

            label.setPrefWidth(500);
            label.setPrefHeight(500);
            label.setWrapText(true);

            CreatureNode.switchTraitStyle(label, card.getTrait());

            traits_box.getChildren().add(label);
        }



    }

}
