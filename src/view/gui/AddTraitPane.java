package view.gui;

import com.jfoenix.controls.JFXRadioButton;
import control.ControlerGUI;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AddTraitPane  extends VBox {

    Stage AddTraitStage = new Stage();

    ControlerGUI controler;

    CardNode cardNode;

    HBox radioBox;
    JFXRadioButton firtsTrait;
    JFXRadioButton secondTrait;
    Label aloneTrait;

    boolean isOneTrait;

    public AddTraitPane(ControlerGUI controler){
        this.controler = controler;
        this.setMinSize(100, 100);

        radioBox = new HBox();
        ToggleGroup group = new ToggleGroup();

        aloneTrait = new Label();
        firtsTrait = new JFXRadioButton();
        secondTrait = new JFXRadioButton();
        firtsTrait.setToggleGroup(group);
        secondTrait.setToggleGroup(group);


        HBox buttonBox = new HBox();
        Button addButton = new Button("Добавить");
        addButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(isOneTrait){
                    controler.addTraitToSelectedCreature(cardNode, true);
                }
                else{
                    if(firtsTrait.isSelected())
                        controler.addTraitToSelectedCreature(cardNode, true);
                    else
                        controler.addTraitToSelectedCreature(cardNode, false);
                }
            }
        });
        Button cancelButton = new Button("Отменить");
        cancelButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                close();
            }
        });

        buttonBox.getChildren().addAll(cancelButton, addButton);

        this.getChildren().addAll(radioBox, buttonBox);

        setStage();
    }

    public void setCardNode(CardNode cardNode){
        this.cardNode = cardNode;
        radioBox.getChildren().clear();
        if(cardNode.card.getTrait(true) == cardNode.card.getTrait(false)){
            isOneTrait = true;
            aloneTrait.setText(cardNode.card.getTrait().toString());
            radioBox.getChildren().add(aloneTrait);
        }
        else{
            isOneTrait = false;
            firtsTrait.setText(cardNode.card.getTrait(true).toString());
            secondTrait.setText(cardNode.card.getTrait(false).toString());
            radioBox.getChildren().addAll(firtsTrait, secondTrait);
        }
    }

    public void show(){
        AddTraitStage.show();
    }
    public void close(){
        AddTraitStage.close();
    }

    private void setStage(){
        Scene scene = new Scene(this, Color.TRANSPARENT);

        AddTraitStage.setTitle("Добавление свойства");
        AddTraitStage.setHeight(this.getPrefHeight());
        AddTraitStage.setWidth(this.getPrefWidth());
        AddTraitStage.setMinHeight(this.getMinHeight());
        AddTraitStage.setMinWidth(this.getMinWidth());

        AddTraitStage.setScene(scene);
        /*
        AddTraitStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {

                AddTraitStage.close();
            }
        });*/
    }
}
