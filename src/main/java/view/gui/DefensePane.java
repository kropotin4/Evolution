package view.gui;

import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSpinner;
import control.ControllerGUI;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.*;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class DefensePane extends VBox {

    DefensePane self = this;

    Stage stage = new Stage();

    ControllerGUI controller;

    Player player;
    ArrayList<Trait> defenseTraits;

    Image upImage = new Image("/images/up2.png", 40, 40, true, true);
    Image upImage2 = new Image("/images/up2_2.png", 35, 35, true, true);
    Image downImage = new Image("/images/down.png", 40, 40, true, true);
    Image downImage2 = new Image("/images/down_2.png",35, 35, true, true);

    public DefensePane(ControllerGUI controller){
        this.setPrefSize(300, 300);
        this.setMinSize(getPrefWidth(), getPrefHeight());

        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);

        this.controller = controller;

        setStage();
    }

    public void setDefensePlayer(Player player){
        this.player = player;
        this.defenseTraits = player.getDefendCreature().getDefenseTraitList(player.getAttackCreature());
        update();
    }


    private void update(){

        if(defenseTraits.size() == 1){
            switch (defenseTraits.get(0)){
                case TAIL_LOSS:
                    tailLoss(player.getDefendCreature());
                    break;
                case MIMICRY:
                    mimicry();
                    break;
                case RUNNING:
                    running();
                default:
                    break;
            }
        }
        else{
            defenseOrder();
        }

    }

    private void tailLoss(Creature creature){
        this.getChildren().clear();

        int height =  creature.getCards().size() + creature.getPlayer().getCooperationCreatures().size()
                + creature.getPlayer().getCommunicationCreatures().size() + creature.getCrocodileList().size()
                + creature.getBirdList().size();
        this.setPrefSize(300, height * (80 + 5) + 60);
        updateStage();

        ArrayList<JFXRadioButton> radioButtons = new ArrayList<>();
        ArrayList<Card> cardsList = new ArrayList<>();
        ToggleGroup group = new ToggleGroup();
        for(Card card : creature.getCards()){

            HBox traitBox = new HBox();
            traitBox.setPrefSize(this.getPrefWidth(), 80);
            traitBox.setMinSize(traitBox.getPrefWidth(), traitBox.getPrefHeight());
            traitBox.setMaxSize(traitBox.getPrefWidth(), traitBox.getPrefHeight());
            traitBox.setAlignment(Pos.CENTER);
            traitBox.setPadding(new Insets(2, 3, 2, 3));

            Label traitLabel = new Label(card.getTrait().toString());
            traitLabel.setPrefSize(traitBox.getPrefWidth() - 100, traitBox.getPrefHeight());
            traitLabel.setMinSize(traitBox.getPrefWidth() - 100, traitBox.getPrefHeight());
            traitLabel.setFont(new Font(15));
            traitLabel.setAlignment(Pos.CENTER);
            traitLabel.setTextAlignment(TextAlignment.CENTER);

            JFXRadioButton radioButton = new JFXRadioButton();
            radioButton.setToggleGroup(group);
            radioButtons.add(radioButton);
            cardsList.add(card);

            traitBox.getChildren().addAll(radioButton, traitLabel);
            this.getChildren().add(traitBox);
        }

        if(creature.getCooperationList().size() > 0){
            int num = 0;
            for(CreaturesPair creaturesPair : creature.getPlayer().getCooperationCreatures()){
                if(creaturesPair.haveCreature(creature)){
                    HBox traitBox = new HBox();
                    traitBox.setPrefSize(this.getPrefWidth(), 80);
                    traitBox.setMinSize(traitBox.getPrefWidth(), traitBox.getPrefHeight());
                    traitBox.setMaxSize(traitBox.getPrefWidth(), traitBox.getPrefHeight());
                    traitBox.setAlignment(Pos.CENTER);
                    traitBox.setPadding(new Insets(2, 3, 2, 3));

                    Label traitLabel = new Label(Trait.COOPERATION.toString() + " " + ++num);
                    traitLabel.setPrefSize(traitBox.getPrefWidth() - 100, traitBox.getPrefHeight());
                    traitLabel.setMinSize(traitBox.getPrefWidth() - 100, traitBox.getPrefHeight());
                    traitLabel.setFont(new Font(15));
                    traitLabel.setAlignment(Pos.CENTER);
                    traitLabel.setTextAlignment(TextAlignment.CENTER);

                    JFXRadioButton radioButton = new JFXRadioButton();
                    radioButton.setToggleGroup(group);
                    radioButtons.add(radioButton);
                    cardsList.add(creaturesPair.card);

                    traitBox.getChildren().addAll(radioButton, traitLabel);
                    this.getChildren().add(traitBox);
                }
            }
        }
        if(creature.getCommunicationList().size() > 0){
            int num = 0;
            for(CreaturesPair creaturesPair : creature.getPlayer().getCommunicationCreatures()){
                if(creaturesPair.haveCreature(creature)){
                    HBox traitBox = new HBox();
                    traitBox.setPrefSize(this.getPrefWidth(), 80);
                    traitBox.setMinSize(traitBox.getPrefWidth(), traitBox.getPrefHeight());
                    traitBox.setMaxSize(traitBox.getPrefWidth(), traitBox.getPrefHeight());
                    traitBox.setAlignment(Pos.CENTER);
                    traitBox.setPadding(new Insets(2, 3, 2, 3));

                    Label traitLabel = new Label(Trait.COMMUNICATION.toString() + " " + ++num);
                    traitLabel.setPrefSize(traitBox.getPrefWidth() - 100, traitBox.getPrefHeight());
                    traitLabel.setMinSize(traitBox.getPrefWidth() - 100, traitBox.getPrefHeight());
                    traitLabel.setFont(new Font(15));
                    traitLabel.setAlignment(Pos.CENTER);
                    traitLabel.setTextAlignment(TextAlignment.CENTER);

                    JFXRadioButton radioButton = new JFXRadioButton();
                    radioButton.setToggleGroup(group);
                    radioButtons.add(radioButton);
                    cardsList.add(creaturesPair.card);

                    traitBox.getChildren().addAll(radioButton, traitLabel);
                    this.getChildren().add(traitBox);
                }
            }
        }
        if(creature.getCrocodileList().size() > 0){
            //TODO: доделать
        }
        if(creature.getBirdList().size() > 0){

        }

        radioButtons.get(0).setSelected(true);

        Button lossButton = new Button("Отбросить свойство");
        lossButton.setOnMouseClicked(event -> {
            for(int i = 0; i < radioButtons.size(); ++i){
                if(radioButtons.get(i).isSelected()){
                    controller.doTailLoss(cardsList.get(i), player.getAttackCreature(), player.getDefendCreature());
                    close();
                    break;
                }
            }
        });
        this.getChildren().add(lossButton);
    }
    private void running(){
        this.getChildren().clear();

        defenseTraits.remove(Trait.RUNNING);

        Label label = new Label("Быстрое");
        Label label2 = new Label("Бог рандома бросает кости");

        JFXSpinner spinner = new JFXSpinner();
        spinner.setRadius(20);

        Timer timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    self.getChildren().remove(spinner);

                    Label label1 = new Label();
                    boolean trueRes = true;
                    if(controller.doRunning(player.getAttackCreature(), player.getDefendCreature())){
                        label1.setText("Существо сбежало!");
                    }
                    else{
                        label1.setText("Побег не удался.");
                        trueRes = false;
                    }
                    Button button = new Button(trueRes ? "Ok :)" : "Ok :(");
                    boolean finalTrueRes = trueRes;
                    button.setOnMouseClicked(event -> {
                        if(finalTrueRes) {
                            self.getChildren().addAll(label1, button);
                            close();
                        }
                        else{
                            if(defenseTraits.size() >= 1) {
                                self.getChildren().addAll(label1);
                                update();
                            }
                            else {
                                self.getChildren().addAll(label1, button);
                                close();
                            }
                        }
                    });
                });
            }
        };
        timer.schedule(timerTask, 3000);

        this.getChildren().addAll(label, label2, spinner);
    }
    private void mimicry(){
        this.getChildren().clear();

        Label label = new Label("Мимикрия");
        Label label2 = new Label("Выберите существо, на которое перенаправите атаку.");
        Button button = new Button("Выбрать");

        button.setOnMouseClicked(event -> {
            Platform.runLater(() -> {
                controller.showMimicryTargets(
                        player.getAttackCreature().getPlayer().getPlayerNumber(),
                        player.getDefendCreature().getPlayer().getPlayerNumber(),
                        player.getAttackCreature().getId(),
                        player.getDefendCreature().getId()
                );
                close();
            });

        });

        this.getChildren().addAll(label, label2, button);
    }
    private void defenseOrder(){
        int num = 1;
        this.getChildren().clear();

        this.setPrefSize(300, defenseTraits.size() * (80 + 5) + 60);
        updateStage();


        ArrayList<JFXRadioButton> radioButtons = new ArrayList<>();
        ToggleGroup group = new ToggleGroup();
        for(Trait trait : defenseTraits){

            HBox traitBox = new HBox();
            traitBox.setPrefSize(this.getPrefWidth(), 80);
            traitBox.setMinSize(traitBox.getPrefWidth(), traitBox.getPrefHeight());
            traitBox.setMaxSize(traitBox.getPrefWidth(), traitBox.getPrefHeight());
            traitBox.setAlignment(Pos.CENTER);
            traitBox.setPadding(new Insets(2, 3, 2, 3));

            Label traitLabel = new Label(trait.toString());
            traitLabel.setPrefSize(traitBox.getPrefWidth() - 100, traitBox.getPrefHeight());
            traitLabel.setMinSize(traitBox.getPrefWidth() - 100, traitBox.getPrefHeight());
            traitLabel.setFont(new Font(15));
            traitLabel.setAlignment(Pos.CENTER);
            traitLabel.setTextAlignment(TextAlignment.CENTER);

            JFXRadioButton radioButton = new JFXRadioButton();
            radioButton.setToggleGroup(group);
            radioButtons.add(radioButton);

            traitBox.getChildren().addAll(radioButton, traitLabel);
            this.getChildren().add(traitBox);

        }

        Button okButton = new Button("OK");

        okButton.setOnMouseClicked(event -> {

            for(int i = 0; i < radioButtons.size(); ++i){
                if(radioButtons.get(i).isSelected()){
                    switch (defenseTraits.get(i)){
                        case TAIL_LOSS:
                            tailLoss(player.getDefendCreature());
                            break;
                        case MIMICRY:
                            mimicry();
                            break;
                        case RUNNING:
                            running();
                            break;
                    }
                }
            }

        });

        this.getChildren().add(okButton);
    }

    private void defenseOrderOld(){
        int num = 1;

        this.setPrefSize(300, defenseTraits.size() * (80 + 5) + 60);
        updateStage();
        this.getChildren().clear();
        for(Trait trait : defenseTraits){

            HBox traitBox = new HBox();
            traitBox.setPrefSize(this.getPrefWidth(), 80);
            traitBox.setMinSize(traitBox.getPrefWidth(), traitBox.getPrefHeight());
            traitBox.setMaxSize(traitBox.getPrefWidth(), traitBox.getPrefHeight());
            traitBox.setAlignment(Pos.CENTER);
            traitBox.setPadding(new Insets(2, 3, 2, 3));

            Label numberLabel = new Label(Integer.toString(num));
            numberLabel.setPrefSize(traitBox.getPrefHeight(), traitBox.getPrefHeight());
            numberLabel.setMinSize(traitBox.getPrefHeight(), traitBox.getPrefHeight());
            numberLabel.setFont(new Font(15));
            numberLabel.setAlignment(Pos.CENTER);
            numberLabel.setTextAlignment(TextAlignment.CENTER);

            Label traitLabel = new Label(trait.toString());
            traitLabel.setPrefSize(traitBox.getPrefWidth() - numberLabel.getPrefWidth() - 100, traitBox.getPrefHeight());
            traitLabel.setMinSize(traitBox.getPrefWidth() - numberLabel.getPrefWidth() - 100, traitBox.getPrefHeight());
            traitLabel.setFont(new Font(15));
            traitLabel.setAlignment(Pos.CENTER);
            traitLabel.setTextAlignment(TextAlignment.CENTER);

            traitBox.getChildren().addAll(numberLabel, traitLabel);

            if(num == 1){
                ImageView downButton = new ImageView(downImage);

                downButton.setOnMouseClicked(event -> {
                    Trait t = defenseTraits.get(0);
                    defenseTraits.set(0, defenseTraits.get(1));
                    defenseTraits.set(1, t);
                    update();
                });

                downButton.setOnMousePressed(event -> {
                    downButton.setImage(downImage2);
                });
                downButton.setOnMouseReleased(event -> {
                    downButton.setImage(downImage);
                });

                traitBox.getChildren().add(downButton);
            }
            else if(num == defenseTraits.size()){
                traitBox.setStyle("-fx-border-width: 1.5 0 0 0; -fx-border-color: green");

                ImageView upButton = new ImageView(upImage);
                //upButton.setFont(new Font(12));
                //upButton.setPrefHeight(traitBox.getPrefHeight());
                //upButton.setPrefWidth(60);

                upButton.setOnMouseClicked(event -> {
                    Trait t = defenseTraits.get(defenseTraits.size() - 2);
                    defenseTraits.set(defenseTraits.size() - 2, defenseTraits.get(defenseTraits.size() - 1));
                    defenseTraits.set(defenseTraits.size() - 1, t);
                    update();
                });

                upButton.setOnMousePressed(event -> {
                    upButton.setImage(upImage2);
                });
                upButton.setOnMouseReleased(event -> {
                    upButton.setImage(upImage);
                });

                traitBox.getChildren().add(upButton);
            }
            else{
                traitBox.setStyle("-fx-border-width: 1.5 0 0 0; -fx-border-color: green");

                HBox buttonBox = new HBox();

                buttonBox.setPrefHeight(traitBox.getPrefHeight());
                buttonBox.setMinHeight(traitBox.getPrefHeight());
                buttonBox.setMaxHeight(traitBox.getPrefHeight());
                buttonBox.setAlignment(Pos.CENTER);

                ImageView upButton = new ImageView(upImage);
                ImageView downButton = new ImageView(downImage);
                upButton.setFitHeight(40);
                downButton.setFitHeight(40);


                //upButton.setFont(new Font(12));
                //downButton.setFont(new Font(12));
                //upButton.setPrefWidth(60);
                //downButton.setPrefWidth(60);

                upButton.setOnMouseClicked(event -> {
                    int in = Integer.parseInt(((Label)(((HBox)upButton.getParent().getParent()).getChildren().get(0))).getText());
                    --in;
                    Trait t = defenseTraits.get(in);
                    defenseTraits.set(in, defenseTraits.get(in - 1));
                    defenseTraits.set(in - 1, t);
                    update();
                });
                upButton.setOnMousePressed(event -> {
                    upButton.setImage(upImage2);
                });
                upButton.setOnMouseReleased(event -> {
                    upButton.setImage(upImage);
                });

                downButton.setOnMouseClicked(event -> {
                    int in = Integer.parseInt(((Label)(((HBox)upButton.getParent().getParent()).getChildren().get(0))).getText());
                    --in;
                    Trait t = defenseTraits.get(in);
                    defenseTraits.set(in, defenseTraits.get(in + 1));
                    defenseTraits.set(in + 1, t);
                    update();
                });
                downButton.setOnMousePressed(event -> {
                    downButton.setImage(downImage2);
                });
                downButton.setOnMouseReleased(event -> {
                    downButton.setImage(downImage);
                });

                buttonBox.getChildren().addAll(upButton, downButton);

                traitBox.getChildren().add(buttonBox);
            }


            this.getChildren().add(traitBox);

            ++num;
        }

        Button okButton = new Button("OK");

        okButton.setOnMouseClicked(event -> {
            close();
        });

        this.getChildren().add(okButton);
    }

    private void setStage(){
        Scene scene = new Scene(this, Color.TRANSPARENT);

        stage.setTitle("Порядок защиты");
        stage.setHeight(this.getPrefHeight());
        stage.setWidth(this.getPrefWidth());
        stage.setMinHeight(this.getMinHeight());
        stage.setMinWidth(this.getMinWidth());
        stage.initStyle(StageStyle.UTILITY);

        stage.setAlwaysOnTop(true);

        stage.setScene(scene);

        stage.setOnCloseRequest(event -> close());
    }
    private void updateStage(){
        stage.setHeight(this.getPrefHeight());
        stage.setWidth(this.getPrefWidth());
        stage.setMinHeight(this.getMinHeight());
        stage.setMinWidth(this.getMinWidth());
        stage.setMaxHeight(this.getPrefHeight());
        stage.setMaxWidth(this.getPrefWidth());
    }


    public void show(){
        stage.show();
    }
    public void close(){
        stage.close();
    }
}
