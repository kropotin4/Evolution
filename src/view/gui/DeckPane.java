package view.gui;

import com.jfoenix.controls.JFXMasonryPane;
import control.ControlerGUI;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Card;
import model.CardPair;
import model.decks.PlayerCardDeck;

import java.io.IOException;

public class DeckPane extends JFXMasonryPane {

    DeckPane self = this;

    ControlerGUI controler;

    boolean isShow = false;

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

                    if(event.getButton().equals(MouseButton.PRIMARY)) {

                        System.out.println("Select card: " + cardNode.card.getId());
                        cardNode.setSelected(true);

                        for (Node node : self.getChildren()) {
                            CardNode cardNode1 = (CardNode) node;
                            if (cardNode == cardNode1) continue;
                            cardNode1.setSelected(false);
                        }

                        controler.selectCard(cardNode);

                        if(event.getClickCount() >= 2){
                            //TODO

                        }

                    }
                    else if(event.getButton().equals(MouseButton.SECONDARY)){
                        //TODO: Справка по свойствам, наверное
                    }
                }
            });

            this.getChildren().add(cardNode);
        }
    }

    public void show(){
        isShow = true;

        Scene scene = new Scene(this, Color.TRANSPARENT);

        Stage cardsStage = new Stage();

        cardsStage.setTitle("Карты игрока");
        cardsStage.setHeight(250);
        cardsStage.setWidth(450);
        cardsStage.setScene(scene);

        cardsStage.show();

        cardsStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                isShow = false;
                cardsStage.close();
            }
        });
    }

}
