package view.gui;

import com.jfoenix.controls.JFXMasonryPane;
import control.ControlerGUI;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import model.Card;
import model.CardPair;
import model.decks.PlayerCardDeck;

import java.io.IOException;

public class DeckPane extends JFXMasonryPane {

    ControlerGUI controler;

    public DeckPane(ControlerGUI controler){
        this.controler = controler;

        this.setPrefSize(500, 150);

        this.setMinSize(130, 85);

        this.setCellHeight(80);
        this.setCellWidth(125);

        this.setPadding(new Insets(5));
    }


    public void update(){
        this.getChildren().clear();
        controler.selectCard(null);

        PlayerCardDeck playerCardDeck = controler.getPlayerCardDeck();

        for(CardPair cardPair : playerCardDeck.getCardDeck()){
            CardNode cardNode = new CardNode(cardPair.getCard(), cardPair.getNumber());

            cardNode.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    System.out.println("Select card: " + cardNode.card.getId());
                    controler.selectCard(cardNode);
                }
            });

            this.getChildren().add(cardNode);
        }
    }

}
