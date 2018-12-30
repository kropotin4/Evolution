package view.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import model.Card;
import model.Phase;
import model.Trait;

import java.io.IOException;
import java.util.ArrayList;

public class CreatureNode extends VBox {
    ///region fields
    private PlayerPane playerPane;

    private int creatureId;
    private int number;

    Image poisoned  = new Image("/images/skull.png");
    Image dreamB = new Image("/images/dream_17b.png");
    Image dreamW = new Image("/images/dream_17w.png");

    BackgroundSize backgroundSize;
    BackgroundImage backgroundImage;
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

    ContextMenu traitContextMenu = new ContextMenu();

    private int styleType = 0; // 0 - default, 1 - green(true), 2 - red(false), 3 - attack, 4 - parasite, 5 - poisoned
    ///endregion

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
        backgroundImage = new BackgroundImage(poisoned, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        background = new Background(backgroundImage);

        traitContextMenu.getItems().add(new MenuItem());
    }

    public void update(){
        this.getChildren().clear();

        if(playerPane.controller.isPoisoned(this)){
            //setPoisonedStyle();
            setStyleType(5);
        }

        ArrayList<Card> cards = playerPane.controller.getCreatureCards(this);
        for(int i = cards.size() - 1; i >= 0; --i){ // Перечисление trait-ов
            Trait trait = cards.get(i).getTrait();

            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.setPrefWidth(this.getPrefWidth());

            Label label = new Label(trait.toString());
            if(trait == Trait.FAT_TISSUE){
                //region FAT_TISSUE
                if(cards.get(i).isFat()) {
                    Button fat = new Button();
                    fat.setMinSize(15, 15);
                    fat.setPrefSize(15, 15);
                    fat.setMaxSize(15, 15);
                    fat.setFont(new Font(11));
                    fat.setStyle("-fx-border-width: 0.5; -fx-border-color: black; -fx-border-radius: 40; -fx-background-color: orange; -fx-background-radius: 40");

                    int finalI = i;
                    fat.setOnMouseClicked(e ->{
                        System.out.println("CreatureNode: fat clicked " + finalI);
                        if(playerPane.controller.getCurrentPhase() == Phase.EATING){
                            playerPane.controller.useFatTissue(this, finalI);
                        }
                    });


                    fat.setVisible(true);
                    fat.setDisable(false);
                    //label = new Label(trait.toString());
                    hBox.getChildren().addAll(fat, label);
                }
                else {
                    //label = new Label(trait.toString());
                    hBox.getChildren().addAll(label);
                }
                //label.setPrefWidth(hBox.getPrefWidth() - 5);
                //endregion
            }
            else if(trait == Trait.GRAZING){
                //region GRAZING
                if(playerPane.getPlayerNumber() == playerPane.controller.getPlayerTurn()) {
                    CheckBox checkBox = new CheckBox();
                    checkBox.setSelected(false);
                    checkBox.setOnMouseClicked(e -> {
                        if (checkBox.isSelected())
                            playerPane.controller.setGrazingActive(this, true);
                        else
                            playerPane.controller.setGrazingActive(this, false);
                    });
                    //label = new Label(trait.toString());
                    hBox.getChildren().addAll(checkBox, label);
                }
                else
                    hBox.getChildren().addAll(label);
                //endregion
            }
            else if(trait == Trait.SCAVENGER){
                //region SCAVENGER

                //label = new Label(trait.toString());

                if(playerPane.controller.getScavengerNumber(this) > 1
                && playerPane.getPlayerNumber() == playerPane.controller.getPlayerTurn()){
                    CheckBox checkBox = new CheckBox();
                    playerPane.scavengerCheckBoxs.add(checkBox);

                    if(playerPane.controller.isActiveScavenger(this))
                        checkBox.setSelected(true);
                    else
                        checkBox.setSelected(false);

                    checkBox.setOnMouseClicked(e -> {
                        playerPane.controller.setPlayerScavenger(this);
                        for(CheckBox checkBox1 : playerPane.scavengerCheckBoxs){
                            checkBox1.setSelected(false);
                        }
                        checkBox.setSelected(true);
                    });

                    hBox.getChildren().addAll(checkBox, label);
                }
                else
                    hBox.getChildren().addAll(label);

                //endregion
            }
            else if(trait == Trait.PREDATOR){
                //region PREDATOR
                Label plusOne = new Label("+1");
                plusOne.setMinSize(15, 15);
                plusOne.setMaxSize(15, 15);
                plusOne.setPrefSize(15, 15);
                plusOne.setFont(new Font("Arial Bold", 11));

                label = new Label(trait.toString());

                hBox.getChildren().addAll(plusOne, label);
                //endregion
            }
            else if(trait == Trait.PARASITE){
                //region PARASITE
                Label plusTwo = new Label("+2");
                plusTwo.setMinSize(15, 15);
                plusTwo.setMaxSize(15, 15);
                plusTwo.setPrefSize(15, 15);
                plusTwo.setFont(new Font("Arial Bold", 11));

                label = new Label(trait.toString());

                hBox.getChildren().addAll(plusTwo, label);
                //endregion
            }
            else if(trait == Trait.HIBERNATION){
                ///region HIBERNATION
                //label = new Label(trait.toString());

                if(playerPane.controller.getCurrentPhase() == Phase.EATING
                && playerPane.controller.getHibernatingTime(this) == 0
                && playerPane.getPlayerNumber() == playerPane.controller.getPlayerTurn()){

                    ImageView dreamImage = new ImageView(dreamB);

                    StackPane stackPane = new StackPane();
                    stackPane.getChildren().add(dreamImage);
                    stackPane.setStyle("-fx-border-width: 1; -fx-border-color: blue;");

                    stackPane.setOnMousePressed(e -> {
                        dreamImage.setImage(dreamW);
                    });
                    stackPane.setOnMouseReleased(e -> {
                        dreamImage.setImage(dreamB);
                    });

                    stackPane.setOnMouseClicked(e -> {
                        playerPane.controller.setCreatureHibernating(this);
                    });

                    hBox.setPadding(new Insets(0, 0, 0, 5));

                    hBox.getChildren().addAll(stackPane, label);
                }
                else
                    hBox.getChildren().addAll(label);
                ///endregion
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

            Label finalLabel = label;

            Tooltip traitTooltip = new Tooltip(trait.getDescription());
            label.setTooltip(traitTooltip);
            traitTooltip.setShowDelay(Duration.ZERO);
            label.setOnMouseEntered(event -> traitTooltip.setShowDuration(Duration.INDEFINITE));
            label.setOnMouseExited(event -> traitTooltip.setShowDuration(Duration.ZERO));

            label.setOnContextMenuRequested(event -> { //это велосипед
                traitContextMenu.getItems().get(0).setText(trait.getDescription());
                traitContextMenu.show(finalLabel, event.getScreenX(), event.getScreenY());
            });

            this.getChildren().add(hBox);
        }


        int satiety = playerPane.controller.getCreatureSatiety(this);
        int hunger = playerPane.controller.getCreatureHunger(this);
        eatButton.setText(satiety + "/" + hunger);
        if(playerPane.controller.isHibernating(this))
            eatButton.setStyle(eatButtonStyle + "-fx-background-color: rgba(0,0,255,0.2); -fx-background-radius: 30;");
        else if(satiety == hunger)
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
            case 5:
                setPoisonedStyle();
                break;
            default:
                this.setStyle("");
                setBorder("hotpink", 2.5);
                System.out.println("CreatureNode: setStyleType: Error -> styleType: " + styleType);
                break;
        }
    }
    public int getStyleType(){
        return styleType;
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
