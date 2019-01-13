package view.gui;

import com.jfoenix.controls.JFXRadioButton;
import control.ControllerGUI;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AddTraitPane  extends VBox {

    Stage addTraitStage = new Stage();

    ControllerGUI controller;

    CardNode cardNode;
    CreatureNode creatureNode;

    Button addButton;

    HBox radioBox;
    JFXRadioButton firstTrait;
    JFXRadioButton secondTrait;
    Label aloneTrait;

    boolean isOneTrait;

    public AddTraitPane(ControllerGUI controller){
        this.controller = controller;
        this.setMinSize(150, 100);
        this.setPrefSize(250, 100);
        this.setSpacing(5);
        this.setPadding(new Insets(5));

        radioBox = new HBox();
        radioBox.setPrefWidth(this.getPrefWidth());
        radioBox.setAlignment(Pos.CENTER);
        radioBox.setSpacing(7);
        ToggleGroup group = new ToggleGroup();

        aloneTrait = new Label();
        firstTrait = new JFXRadioButton();
        secondTrait = new JFXRadioButton();
        firstTrait.setToggleGroup(group);
        secondTrait.setToggleGroup(group);

        firstTrait.setOnMouseClicked(event -> addButton.setDisable(false));
        secondTrait.setOnMouseClicked(event -> addButton.setDisable(false));

        HBox buttonBox = new HBox();
        buttonBox.setPrefWidth(this.getPrefWidth());
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(7);
        addButton = new Button("Добавить");
        addButton.setOnMouseClicked(event -> {
            if(isOneTrait){
                //controller.addTraitToSelectedCreature(cardNode, true);
                controller.addTraitToCreature(creatureNode, cardNode, true);
            }
            else{
                if(firstTrait.isSelected()) {
                    //controller.addTraitToSelectedCreature(cardNode, true);
                    controller.addTraitToCreature(creatureNode, cardNode, true);
                    firstTrait.setSelected(false);
                }
                else {
                    //controller.addTraitToSelectedCreature(cardNode, false);
                    controller.addTraitToCreature(creatureNode, cardNode, false);
                    secondTrait.setSelected(false);
                }
            }
            close();
        });
        Button cancelButton = new Button("Отменить");
        cancelButton.setOnMouseClicked(event -> close());

        buttonBox.getChildren().addAll(addButton);

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
            addButton.setDisable(true);
            isOneTrait = false;
            firstTrait.setText(cardNode.card.getTrait(true).toString());
            secondTrait.setText(cardNode.card.getTrait(false).toString());
            radioBox.getChildren().addAll(firstTrait, secondTrait);
        }
    }
    public void setCreatureNode(CreatureNode creatureNode){
        this.creatureNode = creatureNode;
    }


    public void show(){
        addTraitStage.show();
    }
    public void close(){
        addTraitStage.close();
    }

    public void setTop(boolean isAlways){
        addTraitStage.setAlwaysOnTop(true);
        if(!isAlways)
            addTraitStage.setAlwaysOnTop(false);
    }

    private void setStage(){
        Scene scene = new Scene(this, Color.TRANSPARENT);

        addTraitStage.setTitle("Добавление свойства");
        addTraitStage.setHeight(this.getPrefHeight());
        addTraitStage.setWidth(this.getPrefWidth());
        addTraitStage.setMinHeight(this.getMinHeight());
        addTraitStage.setMinWidth(this.getMinWidth());

        addTraitStage.initStyle(StageStyle.UTILITY);

        addTraitStage.setScene(scene);
        /*
        addTraitStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {

                addTraitStage.close();
            }
        });*/
    }
}
