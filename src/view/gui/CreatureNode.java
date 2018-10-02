package view.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import model.Card;
import model.Creature;
import model.Phase;
import model.Trait;

import java.io.IOException;
import java.util.ArrayList;

public class CreatureNode extends VBox {

    PlayerPane playerPane;

    int creatureId;
    int number;

    Button eatButton = new Button();

    //boolean isFalse = false; // Изменить !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    int styleType = 0; // 0 - default, 1 - green(true), 2 - red(false), 3 - attack

    public CreatureNode(PlayerPane playerPane, int creatureId, int creatureNumber){
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
        this.number = creatureNumber;
        this.playerPane = playerPane;
        this.creatureId = creatureId;
    }

    @FXML
    private void initialize(){
        setBorder("green", 1);
        this.setPrefHeight(190);
        this.setMaxWidth(100);
        this.setMinWidth(100);
        this.setAlignment(Pos.BOTTOM_CENTER);
        this.setPadding(new Insets(2));
        this.setSpacing(1);

        eatButton.setAlignment(Pos.CENTER);
        eatButton.setTextAlignment(TextAlignment.CENTER);
        eatButton.setPrefSize(50, 4);
        eatButton.setFont(new Font(12));
        eatButton.setStyle("-fx-border-width: 1; -fx-border-color: green; -fx-border-radius: 30; -fx-background-color: transparent;");
    }

    public void update(){
        this.getChildren().clear();

        ArrayList<Card> cards = playerPane.controler.getCreatureCards(this);
        for(int i = cards.size() - 1; i >= 0; --i){ // Перечисление trait-ов
            Trait trait = cards.get(i).getTrait();

            Label label = new Label(trait.toString());

            label.setWrapText(true);
            label.setAlignment(Pos.CENTER);
            label.setTextAlignment(TextAlignment.CENTER);
            label.setPrefWidth(500);

            label.setFont(new Font(11));

            switchTraitStyle(label, trait);

            this.getChildren().add(label);
        }

        eatButton.setText(playerPane.controler.getCreauterSatiety(this) + "/" + playerPane.controler.getCreauterHunger(this));

        this.getChildren().add(eatButton);

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
            default:
                this.setStyle("");
                setBorder("violet", 2.5);
                System.out.println("CreatureNode: setStyleType: Error -> styleType: " + styleType);
                break;
        }
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

    public PlayerPane getPlayerPane() {
        return playerPane;
    }
    public int getCreatureId(){
        return creatureId;
    }
}
