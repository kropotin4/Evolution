package view.gui;

import control.ControllerGUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import model.Creature;
import model.CreaturesPair;
import model.SymbiosisPair;
import model.Trait;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerPane extends ScrollPane {

    ControllerGUI controller;
    MainPane mainPane;
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
        this.mainPane = controller.getMainPane();

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

    //Обработка нажатий на существо при различных условиях (атакует, кормится и т.д.)
    private void setMouseClickedHandle(CreatureNode creatureNode){
        creatureNode.setOnMouseClicked(event -> {
            //controller.selectCreature(creatureNode);
            mainPane.setSelectedCreature(creatureNode);
            //System.out.println("Select creature: " + creatureNode.getCreatureId());

            if(event.getClickCount() == 1){
                startTime = System.nanoTime();
                System.out.println("Select creature: " + creatureNode.getCreatureId());
            }
            else{
                endTime = System.nanoTime();
                if(endTime - startTime >= 250){ // Окончание выбора карты + закрытие DeckPane
                    if((creatureNode.isGreenStyle()
                            || creatureNode.isPoisonStyle()
                            || creatureNode.isCrocodileStyle()
                            || creatureNode.isBirdStyle())
                            && mainPane.isCardSelected()
                    ){
                        //Добавление свойства
                        if(mainPane.isPairTraitSelected()){
                            if (isFirstSelected) { // Первое существо уже выбрано
                                secondCreature = creatureNode;
                                setAllCreaturesDefault();
                                isFirstSelected = false;
                                if(controller.getSelectedCard().getCard().getTrait(controller.isUpTrait()) == Trait.SYMBIOSIS)
                                    controller.addSymbiosisTraitToCreature(firstCreature, creatureNode, controller.getSelectedCard(), controller.isUpTrait());
                                else
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
                    else if(creatureNode.isGreenStyle() && mainPane.isFoodGetting() && controller.isCreatureCanEat(creatureNode)){
                        //Взятие еды из кормовой базы + Заполнение жирового запаса//!controller.isCreatureFed(creatureNode)
                        setAllCreaturesDefault();
                        controller.getFoodFromFodder(creatureNode);
                    }
                    else if(creatureNode.isGreenStyle() && mainPane.isAttackerSelecting()){
                        //Выбор атакующего существа
                        System.out.println("Выбор атакующего существа");
                        setAllCreaturesDefault();
                        creatureNode.setStyleType(3);

                        controller.setAttackerCreature(creatureNode);
                    }
                    else if(creatureNode.isGreenStyle() && mainPane.isDefenderSelecting()){
                        //Выбор жертвы хищника
                        System.out.println("Выбор жертвы хищника");
                        setAllCreaturesDefault();
                        controller.attackCreature(creatureNode);
                    }
                    else if(creatureNode.isGreenStyle() && mainPane.isPirateSelecting()){
                        //Выбор пирата
                        System.out.println("Выбор пирата");
                        setAllCreaturesDefault();
                        creatureNode.setStyleType(3);

                        controller.setPirateCreature(creatureNode);
                    }
                    else if(creatureNode.isGreenStyle() && mainPane.isPirateVictimSelecting()){
                        //Выбор жертвы абардажа
                        System.out.println("Выбор жертвы абардажа");
                        setAllCreaturesDefault();
                        controller.pirateCreature(creatureNode);
                    }
                    else if(creatureNode.isGreenStyle() && mainPane.isMimicryTargetSelecting()){
                        //Выбор жертвы мимикрии
                        System.out.println("Выбор жертвы мимикрии");
                        setAllCreaturesDefault();
                        controller.doMimicry(creatureNode);
                    }

                }
            }

            //setFalseStyle(creatureNode);
        });
    }
    //Добавление парных traits -> + обработка наведения
    private void setPairTraits(){
        ///region communication
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

            Tooltip traitTooltip1 = new Tooltip(Trait.COMMUNICATION.getDescription());
            label1.setTooltip(traitTooltip1);
            traitTooltip1.setShowDelay(Duration.ZERO);

            Tooltip traitTooltip2 = new Tooltip(Trait.COMMUNICATION.getDescription());
            label2.setTooltip(traitTooltip2);
            traitTooltip2.setShowDelay(Duration.ZERO);


            Label finalLabel1 = label1;
            Label finalLabel2 = label2;
            finalLabel1.setOnMouseEntered(event -> {
                CreatureNode.setCommStyle(finalLabel1, true);
                CreatureNode.setCommStyle(finalLabel2, true);

                traitTooltip1.setShowDuration(Duration.INDEFINITE);
            });
            finalLabel1.setOnMouseExited(event -> {
                CreatureNode.setCommStyle(finalLabel1, false);
                CreatureNode.setCommStyle(finalLabel2, false);

                traitTooltip1.setShowDuration(Duration.ZERO);
            });

            finalLabel2.setOnMouseEntered(event -> {
                CreatureNode.setCommStyle(finalLabel1, true);
                CreatureNode.setCommStyle(finalLabel2, true);

                traitTooltip2.setShowDuration(Duration.INDEFINITE);
            });
            finalLabel2.setOnMouseExited(event -> {
                CreatureNode.setCommStyle(finalLabel1, false);
                CreatureNode.setCommStyle(finalLabel2, false);

                traitTooltip2.setShowDuration(Duration.ZERO);
            });

            ++pairNumber;
        }
        ///endregion

        ///region cooperation
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

            Tooltip traitTooltip1 = new Tooltip(Trait.COOPERATION.getDescription());
            label1.setTooltip(traitTooltip1);
            traitTooltip1.setShowDelay(Duration.ZERO);

            Tooltip traitTooltip2 = new Tooltip(Trait.COOPERATION.getDescription());
            label2.setTooltip(traitTooltip2);
            traitTooltip2.setShowDelay(Duration.ZERO);

            Label finalLabel1 = label1;
            Label finalLabel2 = label2;
            finalLabel1.setOnMouseEntered(event -> {
                CreatureNode.setCoopStyle(finalLabel1, true);
                CreatureNode.setCoopStyle(finalLabel2, true);

                traitTooltip1.setShowDuration(Duration.INDEFINITE);
            });
            finalLabel1.setOnMouseExited(event -> {
                CreatureNode.setCoopStyle(finalLabel1, false);
                CreatureNode.setCoopStyle(finalLabel2, false);

                traitTooltip1.setShowDuration(Duration.ZERO);
            });

            finalLabel2.setOnMouseEntered(event -> {
                CreatureNode.setCoopStyle(finalLabel1, true);
                CreatureNode.setCoopStyle(finalLabel2, true);

                traitTooltip2.setShowDuration(Duration.INDEFINITE);
            });
            finalLabel2.setOnMouseExited(event -> {
                CreatureNode.setCoopStyle(finalLabel1, false);
                CreatureNode.setCoopStyle(finalLabel2, false);

                traitTooltip2.setShowDuration(Duration.ZERO);
            });
            ++pairNumber;
        }
        ///endregion

        ///region symbiosis
        label1 = null;
        label2 = null;
        pairNumber = 1;
        for(SymbiosisPair symbiosisPair : controller.getSymbiosisCreatures(playerNumber)){
            for(CreatureNode creatureNode : creatureNodes){
                if(creatureNode.getCreatureId() == symbiosisPair.crocodile.getId()){
                    label1 = creatureNode.addSymbiosisLink(pairNumber, true);
                }
                else if(creatureNode.getCreatureId() == symbiosisPair.bird.getId()){
                    label2 = creatureNode.addSymbiosisLink(pairNumber, false);
                }
            }

            Tooltip traitTooltip1 = new Tooltip(Trait.SYMBIOSIS.getDescription());
            label1.setTooltip(traitTooltip1);
            traitTooltip1.setShowDelay(Duration.ZERO);

            Tooltip traitTooltip2 = new Tooltip(Trait.SYMBIOSIS.getDescription());
            label2.setTooltip(traitTooltip2);
            traitTooltip2.setShowDelay(Duration.ZERO);

            Label finalLabel1 = label1;
            Label finalLabel2 = label2;
            finalLabel1.setOnMouseEntered(event -> {
                CreatureNode.setSymbStyle(finalLabel1, true);
                CreatureNode.setSymbStyle(finalLabel2, true);

                traitTooltip1.setShowDuration(Duration.INDEFINITE);
            });
            finalLabel1.setOnMouseExited(event -> {
                CreatureNode.setSymbStyle(finalLabel1, false);
                CreatureNode.setSymbStyle(finalLabel2, false);

                traitTooltip1.setShowDuration(Duration.ZERO);
            });

            finalLabel2.setOnMouseEntered(event -> {
                CreatureNode.setSymbStyle(finalLabel1, true);
                CreatureNode.setSymbStyle(finalLabel2, true);

                traitTooltip2.setShowDuration(Duration.INDEFINITE);
            });
            finalLabel2.setOnMouseExited(event -> {
                CreatureNode.setSymbStyle(finalLabel1, false);
                CreatureNode.setSymbStyle(finalLabel2, false);

                traitTooltip2.setShowDuration(Duration.ZERO);
            });
            ++pairNumber;
        }
        ///endregion
    }

    ///Различные стили CreatureNode под разные задачи
    ///Стили служать одним из параметров при обработке нажатия
    public void setPiracyAvailableCreaturesTrue(CreatureNode exceptCreature){
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
                if(trait == Trait.SYMBIOSIS)
                    creatureNode.setStyleType(6);
                else
                    creatureNode.setStyleType(1);
            }
        }
    }
    public void setCanGettingPairTraitCreaturesTrue(Trait trait, CreatureNode firstCreature){
        for(CreatureNode creatureNode : creatureNodes){
            if(controller.canAddPairTrait(firstCreature, creatureNode, trait)){
                if(trait == Trait.SYMBIOSIS)
                    creatureNode.setStyleType(7);
                else
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
            if(controller.isCreatureCanEat(creatureNode)){
                creatureNode.setStyleType(1);
            }
        }
    }
    public void setMimicryTargetTrue(Creature attacker, Creature mimicry){
        for(CreatureNode creatureNode : creatureNodes){
            if(creatureNode.getCreatureId() == mimicry.getId()) continue;
            //if(attacker.isAttackPossible(creatureNode))
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
    public CreatureNode findCreatureNode(int creatureID){
        for(CreatureNode creatureNode : creatureNodes){
            if(creatureNode.getCreatureId() == creatureID)
                return creatureNode;
        }
        return null;
    }

    public void clear(){
        creatures_box_pp.getChildren().clear();
    }
}
