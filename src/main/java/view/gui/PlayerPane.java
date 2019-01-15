package view.gui;

import control.ControllerGUI;
import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import model.Creature;
import model.CreaturesPair;
import model.Trait;


import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class PlayerPane extends ScrollPane {

    PlayerPane self = this;
    ControllerGUI controller;
    int playerNumber;

    @FXML HBox creatures_box_pp;

    Label playerNumberLabel;

    HBox imageBox = new HBox();
    Image plus1 = new Image("/images/plus1.png");
    Image plus2 = new Image("/images/plus2.png");
    ImageView imageView = new ImageView(plus1);
    boolean imageIsShow = false;

    ArrayList<CreatureNode> creatureNodes = new ArrayList<>(8);

    ArrayList<CheckBox> scavengerCheckBoxes = new ArrayList<>();

    int coopNumber = 0;
    int comNumber = 0;
    int simbNumber = 0;

    CreatureNode firstCreature;
    CreatureNode secondCreature;
    boolean isFirstSelected = false;

    long startTime;
    long endTime;

    public PlayerPane(ControllerGUI controller, int playerNumber){
        this.controller = controller;
        this.playerNumber = playerNumber;

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/PlayerPane.fxml")
        );

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void initialize(){
        this.setStyle("-fx-border-width: 1; -fx-border-color: black;");
        this.setMinSize(600, 190);
        this.setMaxWidth(USE_COMPUTED_SIZE);
        //this.setPrefHeight(USE_COMPUTED_SIZE);
        this.setPrefWidth(USE_COMPUTED_SIZE);
        creatures_box_pp.setAlignment(Pos.CENTER_LEFT);
        creatures_box_pp.setPadding(new Insets(3));
        creatures_box_pp.setSpacing(3);
        //creatures_box_pp.setStyle(creatures_box_pp.getStyle() + "-fx-background-color: " + controller.getPlayerColor(playerNumber) + "50;");
        //Color.web(controller.getPlayerColor(playerNumber), 0.3).toString()

        playerNumberLabel = new Label(" P" + playerNumber + " ");
        playerNumberLabel.setTextAlignment(TextAlignment.CENTER);
        playerNumberLabel.setMinSize(30, 30);
        playerNumberLabel.setMaxSize(30, 30);
        playerNumberLabel.setPrefSize(30, 30);

        playerNumberLabel.setFont(new Font("Arial Bolt", 16));
        playerNumberLabel.setRotate(90);
        playerNumberLabel.setStyle("-fx-border-width: 1; -fx-border-color: red");
        playerNumberLabel.setStyle(creatures_box_pp.getStyle() + "-fx-background-color: " + controller.getPlayerColor(playerNumber) + "48;");
        //playerNumberLabel.setPrefSize(100, 50);

        imageBox.getChildren().add(imageView);
        imageBox.setAlignment(Pos.CENTER);
        imageBox.setPrefSize(80, 300);
        imageView.setCache(true);

        imageView.setOnMousePressed(event -> imageView.setImage(plus2));
        imageView.setOnMouseReleased(event -> imageView.setImage(plus1));

        update();
    }

    ///Здесь добавляются все существа + навешиваются обработчики событий
    public void update(){
/*
        ///region accelerators
        play_server_client.getScene().getAccelerators().put(
                new KeyCodeCombination(KeyCode.X, KeyCombination.SHORTCUT_DOWN),
                () -> play_server_client.fire());
        ///endregion
*/
        if(controller.getCreatures(playerNumber).size() == 0) {
            creatures_box_pp.getChildren().clear();
            creatures_box_pp.getChildren().add(playerNumberLabel);
            return;
        }

        scavengerCheckBoxes.clear();

        int num = 0;
        creatures_box_pp.getChildren().clear(); // Очистка
        creatureNodes.clear();

        creatures_box_pp.getChildren().add(playerNumberLabel);

        for(Creature creature : controller.getCreatures(playerNumber)){
            CreatureNode creatureNode = new CreatureNode(this, creature.getId(), num++);
            creatureNode.update();
            creatures_box_pp.getChildren().add(creatureNode);
            creatureNodes.add(creatureNode);

            setMouseClickedHandle(creatureNode);
        }

        setPairTraits();

        if(imageIsShow)
            creatures_box_pp.getChildren().add(imageBox);
    }

    private void playSound(String sound) {
        try {
            System.out.print("Sound " + sound + " is playing");
            AudioInputStream in = AudioSystem.getAudioInputStream(new File(
                    String.valueOf(Paths.get(getClass().getResource("/sounds/" + sound).toURI()))).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(in);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Обработка нажатий на существо при различных условиях (атакует, кормится и т.д.)
    private void setMouseClickedHandle(CreatureNode creatureNode){
        creatureNode.setOnMouseClicked(event -> {
            controller.selectCreature(creatureNode);
            //System.out.println("Select creature: " + creatureNode.getCreatureId());

            if(event.getClickCount() == 1){
                startTime = System.nanoTime();
                System.out.println("Select creature: " + creatureNode.getCreatureId());
            }
            else{
                endTime = System.nanoTime();
                if(endTime - startTime >= 250){ // Окончание выбора карты + закрытие DeckPane
                    if((creatureNode.isGreenStyle() || creatureNode.isPoisonStyle()) && controller.isCardSelected()){
                        //Добавление свойства
                        if(controller.isPairTraitSelected()){
                            if (isFirstSelected) { // Первое существо уже выбрано
                                secondCreature = creatureNode;
                                setAllCreaturesDefault();
                                isFirstSelected = false;
                                controller.addPairTraitToCreature(firstCreature, creatureNode, controller.getSelectedCard(), controller.isUpTrait());
                                firstCreature = null;
                            } else { // Выбирается первое существо
                                firstCreature = creatureNode;
                                isFirstSelected = true;
                                setAllCreaturesDefault();
                                setCanGettingPairTraitCreaturesTrue(controller.getSelectedCard().getCard().getTrait(controller.isUpTrait()), firstCreature);
                                creatureNode.setStyleType(3);
                            }
                        }
                        else {
                            controller.addTraitToCreature(controller.getPlayerTurn(), creatureNode, controller.getSelectedCard(), controller.isUpTrait());
                        }
                    }
                    else if(creatureNode.isGreenStyle() && controller.isFoodGetting() && !controller.isCreatureSatisfied(creatureNode)){
                        //Взятие еды из кормовой базы + Заполнение жирового запаса//!controller.isCreatureFed(creatureNode)
                        setAllCreaturesDefault();
                        playSound("mouse16.wav");
                        controller.getFoodFromFodder(creatureNode);
                    }
                    else if(creatureNode.isGreenStyle() && controller.isAttackerSelecting()){
                        //Выбор атакующего существа
                        System.out.println("Выбор атакующего существа");
                        setAllCreaturesDefault();
                        creatureNode.setStyleType(3);

                        controller.setAttackerCreature(creatureNode);
                    }
                    else if(creatureNode.isGreenStyle() && controller.isDefenderSelecting()){
                        //Выбор жертвы хищника
                        System.out.println("Выбор жертвы хищника");
                        setAllCreaturesDefault();
                        playSound("rar16.wav");
                        controller.attackCreature(creatureNode);
                    }
                    else if(creatureNode.isGreenStyle() && controller.isPirateSelecting()){
                        //Выбор пирата
                        System.out.println("Выбор пирата");
                        setAllCreaturesDefault();
                        creatureNode.setStyleType(3);

                        controller.setPirateCreature(creatureNode);
                    }
                    else if(creatureNode.isGreenStyle() && controller.isPirateVictimSelecting()){
                        //Выбор жертвы абардажа
                        System.out.println("Выбор жертвы абардажа");
                        setAllCreaturesDefault();
                        playSound("seagull16.wav");
                        controller.pirateCreature(creatureNode);
                    }

                }
            }

            //setFalseStyle(creatureNode);
        });
    }
    //Добавление парных traits -> + обработка наведения
    private void setPairTraits(){
        Label label1 = null;
        Label label2 = null;
        int pairNumber = 1;
        for(CreaturesPair creaturesPairs : controller.getCommunicationCreatures(playerNumber)){
            for(CreatureNode creatureNode : creatureNodes){
                if(creatureNode.getCreatureId() == creaturesPairs.firstCreature.getId()){
                    label1 = creatureNode.addCommunicationLink(pairNumber);
                }
                else if(creatureNode.getCreatureId() == creaturesPairs.secondCreature.getId()){
                    label2 = creatureNode.addCommunicationLink(pairNumber);
                }
            }

            Label finalLabel1 = label1;
            Label finalLabel2 = label2;
            finalLabel1.setOnMouseEntered(event -> {
                CreatureNode.setCommStyle(finalLabel1, true);
                CreatureNode.setCommStyle(finalLabel2, true);
            });
            finalLabel1.setOnMouseExited(event -> {
                CreatureNode.setCommStyle(finalLabel1, false);
                CreatureNode.setCommStyle(finalLabel2, false);
            });

            finalLabel2.setOnMouseEntered(event -> {
                CreatureNode.setCommStyle(finalLabel1, true);
                CreatureNode.setCommStyle(finalLabel2, true);
            });
            finalLabel2.setOnMouseExited(event -> {
                CreatureNode.setCommStyle(finalLabel1, false);
                CreatureNode.setCommStyle(finalLabel2, false);
            });

            ++pairNumber;
        }

        label1 = null;
        label2 = null;
        pairNumber = 1;
        for(CreaturesPair creaturesPairs : controller.getCooperationCreatures(playerNumber)){
            for(CreatureNode creatureNode : creatureNodes){
                if(creatureNode.getCreatureId() == creaturesPairs.firstCreature.getId()){
                    label1 = creatureNode.addCooperationLink(pairNumber);
                }
                else if(creatureNode.getCreatureId() == creaturesPairs.secondCreature.getId()){
                    label2 = creatureNode.addCooperationLink(pairNumber);
                }
            }

            Label finalLabel1 = label1;
            Label finalLabel2 = label2;
            finalLabel1.setOnMouseEntered(event -> {
                CreatureNode.setCoopStyle(finalLabel1, true);
                CreatureNode.setCoopStyle(finalLabel2, true);
            });
            finalLabel1.setOnMouseExited(event -> {
                CreatureNode.setCoopStyle(finalLabel1, false);
                CreatureNode.setCoopStyle(finalLabel2, false);
            });

            finalLabel2.setOnMouseEntered(event -> {
                CreatureNode.setCoopStyle(finalLabel1, true);
                CreatureNode.setCoopStyle(finalLabel2, true);
            });
            finalLabel2.setOnMouseExited(event -> {
                CreatureNode.setCoopStyle(finalLabel1, false);
                CreatureNode.setCoopStyle(finalLabel2, false);
            });
            ++pairNumber;
        }
    }

    ///Различные стили CreatureNode под разные задачи
    ///Стили служать одним из параметров при обработке нажатия
    public void setPiracyAvailableCreaturesTrue(@NamedArg("exceptCreature") CreatureNode exceptCreature){
        int hunger, satiety;
        for(CreatureNode creatureNode : creatureNodes){
            if(creatureNode == exceptCreature) continue;

            hunger = controller.getCreatureHunger(creatureNode);
            satiety = controller.getCreatureSatiety(creatureNode);
            if(satiety < hunger && satiety > 0){
                creatureNode.setStyleType(1);
            }
        }
    }
    public void setCanGettingTraitCreaturesPoison(Trait trait){
        for(CreatureNode creatureNode : creatureNodes){
            if(controller.canAddTrait(creatureNode, trait)){
                creatureNode.setStyleType(4);
            }
        }
    }
    public void setCanGettingTraitCreaturesTrue(Trait trait){
        for(CreatureNode creatureNode : creatureNodes){
            if(controller.canAddTrait(creatureNode, trait)){
                creatureNode.setStyleType(1);
            }
        }
    }
    public void setCanGettingPairTraitCreaturesTrue(Trait trait, CreatureNode firstCreature){
        for(CreatureNode creatureNode : creatureNodes){
            if(controller.canAddPairTrait(firstCreature, creatureNode, trait)){
                creatureNode.setStyleType(1);
            }
        }
    }
    public void setCreaturesWithTraitTrue(Trait trait){
        for(CreatureNode creatureNode : creatureNodes){
            if(controller.findTrait(creatureNode, trait)) {
                creatureNode.setStyleType(1);
            }
        }
    }
    public void setAttackerCreaturesTrue(){
        for(CreatureNode creatureNode : creatureNodes){
            if(controller.findTrait(creatureNode, Trait.PREDATOR) && !controller.isCreatureFed(creatureNode)) {
                creatureNode.setStyleType(1);
            }
        }
    }
    public void setAllCreaturesFalse(){
        for(CreatureNode creatureNode : creatureNodes){
            creatureNode.setStyleType(2);
        }
    }
    public void setAllCreaturesDefault(){
        for(CreatureNode creatureNode : creatureNodes){
            if(controller.isPoisoned(creatureNode))
                //Отравленный стиль сохраняется
                creatureNode.setStyleType(5);
            else
                creatureNode.setStyleType(0);
        }
    }
    public void setHungerCreaturesTrue(){
        for(CreatureNode creatureNode : creatureNodes){
            if(!controller.isCreatureSatisfied(creatureNode)){
                creatureNode.setStyleType(1);
            }
        }
    }

    public void setAttackStyle(CreatureNode creatureNode){
        creatureNode.setStyle("");
        setCreatureBorders(creatureNode, "gold", 2.5);
        creatureNode.setStyle(creatureNode.getStyle() + "-fx-background-color: rgba(255,215,0,0.3);");
    }

    void setCreatureBorders(CreatureNode creatureNode, String color, double width){
        creatureNode.setBorder(color, width);
    }

    public ImageView getAddIcon(){
        return imageView;
    }
    public void showAddIcon(boolean isShow){
        if(isShow && !imageIsShow) {
            creatures_box_pp.getChildren().add(imageBox);
            imageIsShow = true;
        }
        else if(!isShow) {
            creatures_box_pp.getChildren().remove(imageBox);
            imageIsShow = false;
        }
    }

    public int getPlayerNumber(){
        return playerNumber;
    }
    public ArrayList<CreatureNode> getCreatureNodes(){
        return creatureNodes;
    }

    public void clear(){
        creatures_box_pp.getChildren().clear();
    }
}