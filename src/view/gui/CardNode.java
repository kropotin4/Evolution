package view.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import model.Card;

import java.io.IOException;

public class CardNode extends HBox {

    Card card;
    int number;

    //@FXML
    VBox traits_box;
    //@FXML
    Label card_number;

    public CardNode(Card card, int number){
        this.card = card;
        this.number = number;
        /*
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
        */
        this.setAlignment(Pos.CENTER);

        traits_box = new VBox();
        traits_box.setPrefSize(100, 100);
        traits_box.setAlignment(Pos.CENTER);
        traits_box.setFillWidth(true);


        card_number = new Label("x1");
        card_number.setWrapText(true);

        this.setPrefSize(125, 80);
        this.setSpacing(2);

        this.getChildren().addAll(traits_box, card_number);

        initialize();
    }

    //@FXML
    private void initialize(){


        this.setStyle("-fx-border-width: 1; -fx-border-color: green;");

        if (card.getTrait(true) == card.getTrait(false)) {

            Label label = new Label(card.getTrait().toString());

            label.setPrefWidth(traits_box.getPrefWidth());
            label.setPrefHeight(40);
            label.setWrapText(true);
            label.setTextAlignment(TextAlignment.CENTER);

            //CreatureNode.switchTraitStyle(label, card.getTrait());

            traits_box.getChildren().add(label);
            traits_box.setVisible(true);
        }
        else {

            Label labelUp = new Label(card.getTrait(true).toString());
            Label labelDown = new Label(card.getTrait(false).toString());

            labelUp.setPrefWidth(traits_box.getPrefWidth());
            labelDown.setPrefWidth(traits_box.getPrefWidth());
            labelUp.setPrefHeight(30);
            labelDown.setPrefHeight(30);
            labelUp.setWrapText(true);
            labelDown.setWrapText(true);
            labelUp.setTextAlignment(TextAlignment.CENTER);
            labelDown.setTextAlignment(TextAlignment.CENTER);
            labelUp.setAlignment(Pos.CENTER);
            labelDown.setAlignment(Pos.CENTER);

            CreatureNode.switchTraitStyle(labelUp, card.getTrait(true));
            CreatureNode.switchTraitStyle(labelDown, card.getTrait(false));

            //CreatureNode.switchTraitStyle(labelUp, card.getTrait(true));
            //CreatureNode.switchTraitStyle(labelDown, card.getTrait(false));

            traits_box.getChildren().addAll(labelUp, labelDown);
        }


        setNumber(number);
    }

    public void setNumber(int number){
        if(number < 0) throw new RuntimeException("CardNode: setNumber: number < 0");

        card_number.setText("x" + number);
        card_number.setStyle("-fx-font-weight: bold;");
    }

}
