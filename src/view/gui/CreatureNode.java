package view.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import model.Card;
import model.Trait;

import java.io.IOException;
import java.util.ArrayList;

public class CreatureNode extends VBox {

    private PlayerPane playerPane;

    private int creatureId;
    private int number;

    Image poisoned  = new Image("/images/skull.png");

    BackgroundSize backgroundSize;
    // new BackgroundImage(image, repeatX, repeatY, position, size)
    BackgroundImage backgroundImage;
    // new Background(images...)
    Background background;

    private HBox bottomBox = new HBox();

    private Button eatButton = new Button();
    private String eatButtonStyle = "-fx-border-width: 1; -fx-border-color: green; -fx-border-radius: 30; -fx-background-color: transparent;";

    private HBox leftBox = new HBox();

    private VBox rightBox = new VBox();
    private HBox commBox = new HBox();
    private static final String commStyle = "-fx-border-width: 1; -fx-border-color: red; -fx-border-radius: 30; -fx-background-color: transparent;";
    private static final String commStyleFull = "-fx-border-width: 1; -fx-border-color: red; -fx-border-radius: 30; -fx-background-color: rgba(255,99,71,0.4); -fx-background-radius: 30;";
    private HBox coopBox = new HBox();
    private static final String coopStyle = "-fx-border-width: 1; -fx-border-color: blue; -fx-border-radius: 30; -fx-background-color: transparent;";
    private static final String coopStyleFull = "-fx-border-width: 1; -fx-border-color: blue; -fx-border-radius: 30; -fx-background-color: rgba(0,0,255,0.4); -fx-background-radius: 30;";

    //boolean isFalse = false; // Изменить !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private int styleType = 0; // 0 - default, 1 - green(true), 2 - red(false), 3 - attack, 4 - poison

    public CreatureNode(PlayerPane playerPane, int creatureId, int creatureNumber){
        this.number = creatureNumber;
        this.playerPane = playerPane;
        this.creatureId = creatureId;

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/CreatureNode.fxml")
        );

        //fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void initialize(){
        setBorder("green", 1);
        this.setPrefHeight(190);
        this.setMaxWidth(110);
        this.setMinWidth(110);
        this.setPrefWidth(this.getMinWidth());
        this.setAlignment(Pos.BOTTOM_CENTER);
        this.setPadding(new Insets(2));
        this.setSpacing(1);

        bottomBox.setPrefSize(500, 15);

        eatButton.setAlignment(Pos.CENTER);
        eatButton.setTextAlignment(TextAlignment.CENTER);
        eatButton.setPrefSize(50, 4);
        eatButton.setFont(new Font(12));
        eatButton.setStyle(eatButtonStyle);

        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPrefSize(this.getMinWidth(), 10);

        leftBox.setAlignment(Pos.CENTER);
        leftBox.setPrefSize(30, 10);

        rightBox.setAlignment(Pos.CENTER);
        rightBox.setPrefSize(30, 10);
        commBox.setPrefSize(30, 5);
        coopBox.setPrefSize(30, 5);


        rightBox.getChildren().addAll(commBox, coopBox);

        bottomBox.getChildren().addAll(leftBox, eatButton, rightBox);

        backgroundSize = new BackgroundSize(this.getMinWidth() - 20, this.getPrefHeight(), false, false, true, false);
        // new BackgroundImage(image, repeatX, repeatY, position, size)
        backgroundImage = new BackgroundImage(poisoned, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        // new Background(images...)
        background = new Background(backgroundImage);
    }

    public void update(){
        this.getChildren().clear();

        if(playerPane.controller.isPoisoned(this)){
            setPoisonedStyle();
        }

        ArrayList<Card> cards = playerPane.controller.getCreatureCards(this);
        for(int i = cards.size() - 1; i >= 0; --i){ // Перечисление trait-ов
            Trait trait = cards.get(i).getTrait();

            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.setPrefWidth(this.getPrefWidth());

            Label label = null;
            if(trait == Trait.FAT_TISSUE){
                if(cards.get(i).isFat())
                    label = new Label(trait.toString() + " (*)");
                else
                    label = new Label(trait.toString() + " ( )");
                hBox.getChildren().add(label);
            }
            else if(trait == Trait.GRAZING){
                CheckBox checkBox = new CheckBox();
                checkBox.setSelected(false);
                checkBox.setOnMouseClicked(e -> {
                    if(checkBox.isSelected())
                        playerPane.controller.setGrazingActive(this, true);
                    else
                        playerPane.controller.setGrazingActive(this, false);
                });
                label = new Label(trait.toString());
                hBox.getChildren().addAll(checkBox, label);
            }
            else {
                label = new Label(trait.toString());
                hBox.getChildren().add(label);
            }

            label.setWrapText(true);
            label.setAlignment(Pos.CENTER);
            label.setTextAlignment(TextAlignment.CENTER);
            label.setPrefWidth(hBox.getPrefWidth());

            label.setFont(new Font(11));

            switchTraitStyle(label, trait);

            this.getChildren().add(hBox);
        }


        int satiety = playerPane.controller.getCreauterSatiety(this);
        int hunger = playerPane.controller.getCreauterHunger(this);
        eatButton.setText(satiety + "/" + hunger);
        if(satiety == hunger)
            eatButton.setStyle(eatButtonStyle + "-fx-background-color: rgba(0,255,127,0.2); -fx-background-radius: 30;");
        else
            eatButton.setStyle(eatButtonStyle);


        this.getChildren().add(bottomBox);

    }

    public Label addCommunicationLink(int linkNumber){
        Label label = new Label(Integer.toString(linkNumber));

        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setPrefSize(15, 5);
        label.setFont(new Font(9));
        label.setStyle(commStyle);

        commBox.getChildren().add(label);

        return label;
    }
    public Label addCooperationLink(int linkNumber){
        Label label = new Label(Integer.toString(linkNumber));

        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setPrefSize(15, 5);
        label.setFont(new Font(9));
        label.setStyle(coopStyle);

        coopBox.getChildren().add(label);

        return label;
    }
    public void addSymbiosisLink(int linkNumber, boolean isCrocodile){

    }

    public static void setCommStyle(Node node, boolean isFull){
        if(isFull){
            node.setStyle(commStyleFull);
        }
        else{
            node.setStyle(commStyle);
        }
    }
    public static void setCoopStyle(Node node, boolean isFull){
        if(isFull){
            node.setStyle(coopStyleFull);
        }
        else{
            node.setStyle(coopStyle);
        }
    }

    public static void switchTraitStyle(Node node, Trait trait){
        switch (trait){
            case PREDATOR:
                setTraitStyle(node, "coral");
                break;
            case FAT_TISSUE:
                setTraitStyle(node, "khaki");
                break;
            case SWIMMING:
                setTraitStyle(node, "mediumturquoise");
                break;
            case PARASITE:
                setTraitStyle(node, "orchid");
                break;
        }
    }
    private static void setTraitStyle(Node node, String color){
        node.setStyle("");

        //node.setStyle("-fx-border-width: 1.5 0 0.5 0; -fx-border-color: green;");
        node.setStyle(node.getStyle() + " -fx-background-color: " + color + ";");
    }

    public void setBorder(String color, double width){
        this.setStyle("");

        this.setStyle("-fx-border-color: " + color + "; -fx-border-width: " + width + ";");
    }
    public void setStyleType(int styleType){
        this.styleType = styleType;
        switch (styleType){
            case 0:
                setDefaultStyle();
                break;
            case 1:
                setTrueStyle();
                break;
            case 2:
                setFalseStyle();
                break;
            case 3:
                setAttackStyle();
                break;
            case 4:
                setParasiteStyle();
                break;
            default:
                this.setStyle("");
                setBorder("hotpink", 2.5);
                System.out.println("CreatureNode: setStyleType: Error -> styleType: " + styleType);
                break;
        }
    }

    private void setPoisonedStyle(){
        this.setStyle("");
        setBorder("violet", 2.5);

        setBackground(background);
    }
    private void setParasiteStyle(){
        this.setStyle("");
        setBorder("violet", 2.5);
        this.setStyle(this.getStyle() + "-fx-background-color: rgba(238,130,238,0.3);");
    }
    private void setAttackStyle(){
        this.setStyle("");
        setBorder("gold", 2.5);
        this.setStyle(this.getStyle() + "-fx-background-color: rgba(255,215,0,0.3);");
    }
    private void setTrueStyle(){
        this.setStyle("");
        setBorder("green", 2.5);
        this.setStyle(this.getStyle() + "-fx-background-color: rgba(0,255,127,0.3);");
    }
    private void setFalseStyle(){
        this.setStyle("");
        setBorder("red", 2.5);
        this.setStyle(this.getStyle() + "-fx-background-color: rgba(255,99,71,0.3);");
    }
    private void setDefaultStyle(){
        this.setStyle("");
        setBorder("green", 1);
    }

    public boolean isDefaultStyle(){
        return styleType == 0;
    }
    public boolean isGreenStyle(){
        return styleType == 1;
    }
    public boolean isRedStyle(){
        return styleType == 2;
    }
    public boolean isAttackStyle(){
        return styleType == 3;
    }
    public boolean isPoisonStyle(){
        return styleType == 4;
    }

    public PlayerPane getPlayerPane() {
        return playerPane;
    }
    public int getCreatureId(){
        return creatureId;
    }
}
