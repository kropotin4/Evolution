package view.gui;

import com.jfoenix.controls.JFXMasonryPane;
import control.ControllerGUI;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.CardsStack;
import model.decks.PlayerCardDeck;

public class DeckPane extends JFXMasonryPane {

    DeckPane self = this;

    Stage cardsStage = new Stage();

    ControllerGUI controler;

    long startTime;
    long endTime;

    boolean isShow = false;

    public DeckPane(ControllerGUI controler){
        this.controler = controler;

        this.setPrefSize(450, 400);
        this.setMinSize(200, 200);

        this.setCellHeight(80);
        this.setCellWidth(125);

        this.setPadding(new Insets(5));



        setStage();
    }


    public void update(){
        this.getChildren().clear();

        PlayerCardDeck playerCardDeck = controler.getPlayerCardDeck();

        for(CardsStack cardsStack : playerCardDeck.getCardDeck()){
            CardNode cardNode = new CardNode(cardsStack.getCard(), cardsStack.getNumber());

            cardNode.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {

                    if(event.getButton().equals(MouseButton.PRIMARY)) {

                        if(controler.isCardSelecting()) {
                            System.out.println("Select card: " + cardNode.card.getId());
                            cardNode.setSelected(true);

                            for (Node node : self.getChildren()) {
                                CardNode cardNode1 = (CardNode) node;
                                if (cardNode == cardNode1) continue;
                                cardNode1.setSelected(false);
                            }

                            if(event.getClickCount() == 1){
                                startTime = System.nanoTime();
                            }
                            else{
                                endTime = System.nanoTime();
                                if(endTime - startTime >= 250){ // Окончание выбора карты + закрытие DeckPane
                                    if(controler.isCreatureAdding()){
                                        controler.addCreature(cardNode);
                                    }
                                    else{
                                        controler.selectCard(cardNode);
                                    }

                                    close();
                                }
                            }
                        }
                        else{
                            System.out.println("Clicked card: " + cardNode.card.getId());
                        }

                    }
                    else if(event.getButton().equals(MouseButton.SECONDARY)){
                        //TODO: Справка по свойствам, наверное
                        System.out.println("Справка");
                    }
                }
            });

            this.getChildren().add(cardNode);
        }
    }

    public void show(){
        isShow = true;
        cardsStage.show();
    }
    public void close(){
        isShow = false;
        controler.setIsCreatureAdding(false);
        cardsStage.close();
    }
    public void setTop(){
        cardsStage.setAlwaysOnTop(true);
        cardsStage.setAlwaysOnTop(false);
    }

    private void setStage(){
        Scene scene = new Scene(this, Color.TRANSPARENT);

        cardsStage.setTitle("Карты игрока");
        cardsStage.setHeight(this.getPrefHeight());
        cardsStage.setWidth(this.getPrefWidth());
        cardsStage.setMinHeight(this.getMinHeight());
        cardsStage.setMinWidth(this.getMinWidth());

        cardsStage.setScene(scene);

        cardsStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                close();
            }
        });
    }
}
