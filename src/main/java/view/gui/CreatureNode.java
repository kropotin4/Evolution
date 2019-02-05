package view.gui;

import com.jfoenix.controls.JFXButton;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import model.Card;
import model.Phase;
import model.Trait;

import java.io.IOException;
import java.util.ArrayList;

public class CreatureNode extends AnchorPane {
    ///region fields
    private PlayerPane playerPane;

    private int creatureId;
    private int number;

    @FXML VBox traits_box_cn;


    JFXButton commButton = new JFXButton();
    JFXButton coopButton = new JFXButton();

    int commLinksNumber = 0;
    int coopLinksNumber = 0;

    Image poisonedImage = new Image("/images/skull.png");
    Image dreamB = new Image("/images/dream_17b.png");
    Image dreamW = new Image("/images/dream_17w.png");

    Background poisonedBG;

    private HBox bottomBox = new HBox();

    private Button eatButton = new Button();
    private String eatButtonStyle = "-fx-border-width: 1; -fx-border-color: green; -fx-border-radius: 30; -fx-background-color: transparent;";

    private VBox leftBox = new VBox();
    private HBox firstBox = new HBox();
    private HBox secondBox = new HBox();
    Image crocodile = new Image("/images/crocodile_20.png");
    Image bird = new Image("/images/bird_20.png");
    Image crocodileBGImage = new Image("/images/crocodile_96.png");
    Background crocodileBG;
    Image birdBGImage = new Image("/images/bird_96.png");
    Background birdBG;
    private static final String symbStyle = "-fx-border-width: 1; -fx-border-color: green; -fx-background-color: transparent;";
    private static final String symbStyleFull = "-fx-border-width: 1; -fx-border-color: green; -fx-background-color: rgba(0,255,0,0.4);";


    private VBox rightBox = new VBox();
    private HBox commBox = new HBox();
    private static final String commStyle = "-fx-border-width: 1; -fx-border-color: red; -fx-border-radius: 30; -fx-background-color: transparent;";
    private static final String commStyleFull = "-fx-border-width: 1; -fx-border-color: red; -fx-border-radius: 30; -fx-background-color: rgba(255,99,71,0.4); -fx-background-radius: 30;";
    private HBox coopBox = new HBox();
    private static final String coopStyle = "-fx-border-width: 1; -fx-border-color: blue; -fx-border-radius: 30; -fx-background-color: transparent;";
    private static final String coopStyleFull = "-fx-border-width: 1; -fx-border-color: blue; -fx-border-radius: 30; -fx-background-color: rgba(0,0,255,0.4); -fx-background-radius: 30;";

    private int styleType = 0; // 0 - default, 1 - green(true), 2 - red(false), 3 - attack, 4 - parasite, 5 - poisoned
    // 6 - crocodile, 7 - bird

    ///endregion

    public CreatureNode(PlayerPane playerPane, int creatureId, int creatureNumber){
        this.number = creatureNumber;
        this.playerPane = playerPane;
        this.creatureId = creatureId;

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/CreatureNode.fxml")
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
        setBorder("green", 1);
        this.setPrefHeight(190);
        this.setMaxWidth(110);
        this.setMinWidth(110);
        this.setPrefWidth(this.getMinWidth());
        //this.getChildren().addAll(traits_box_cn);
        traits_box_cn.setAlignment(Pos.BOTTOM_CENTER);
        traits_box_cn.setPadding(new Insets(2));
        traits_box_cn.setSpacing(1);

        bottomBox.setPrefSize(500, 15);

        eatButton.setAlignment(Pos.CENTER);
        eatButton.setTextAlignment(TextAlignment.CENTER);
        eatButton.setPrefSize(45, 4);
        eatButton.setFont(new Font(12));
        eatButton.setStyle(eatButtonStyle);

        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPrefSize(this.getMinWidth(), 10);

        ///region leftBox (symb)
        leftBox.setAlignment(Pos.CENTER);
        leftBox.setPrefSize(30, 10);

        firstBox.setPrefSize(30, 5);
        secondBox.setPrefSize(30, 5);

        leftBox.getChildren().addAll(firstBox, secondBox);
        ///endregion

        ///region rightBox (coop & comm)
        rightBox.setAlignment(Pos.CENTER);
        rightBox.setPrefSize(30, 10);
        commBox.setPrefSize(30, 5);
        coopBox.setPrefSize(30, 5);

        rightBox.getChildren().addAll(commBox, coopBox);
        ///endregion

        bottomBox.getChildren().addAll(leftBox, eatButton, rightBox);

        ///region backgrounds
        BackgroundSize backgroundSize = new BackgroundSize(this.getMinWidth() - 20, this.getPrefHeight(), false, false, true, false);

        BackgroundImage backgroundImage = new BackgroundImage(poisonedImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        poisonedBG = new Background(backgroundImage);

        BackgroundImage backgroundImage2 = new BackgroundImage(crocodileBGImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        crocodileBG = new Background(backgroundImage2);

        BackgroundImage backgroundImage3 = new BackgroundImage(birdBGImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        birdBG = new Background(backgroundImage3);
        ///endregion

        ///region comm & coop buttons
        commButton.setStyle(
                "     -jfx-button-type: RAISED;\n" +
                        "     -fx-background-color: rgba(255,99,71,0.4);\n" +
                        "     -fx-text-fill: white;\n" +
                        "     -fx-text-alignment: center;\n" +
                        "     -fx-wrap-text: true;\n" +
                        "     -fx-font-size: 12;");
        AnchorPane.setLeftAnchor(commButton, 1.0);
        AnchorPane.setTopAnchor(commButton, 1.0);
        AnchorPane.setRightAnchor(commButton, 1.0);

        coopButton.setStyle(
                "     -jfx-button-type: RAISED;\n" +
                        "     -fx-background-color: rgba(0,0,255,0.4);\n" +
                        "     -fx-text-fill: white;\n" +
                        "     -fx-text-alignment: center;\n" +
                        "     -fx-wrap-text: true;\n" +
                        "     -fx-font-size: 12;");
        AnchorPane.setLeftAnchor(coopButton, 1.0);
        AnchorPane.setRightAnchor(coopButton, 1.0);
        AnchorPane.setBottomAnchor(coopButton, 1.0);
        ///endregion
    }

    public void update(){
        //this.getChildren().clear();
        //this.getChildren().addAll(traits_box_cn);
        traits_box_cn.getChildren().clear();



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
                    playerPane.scavengerCheckBoxes.add(checkBox);

                    if(playerPane.controller.isActiveScavenger(this))
                        checkBox.setSelected(true);
                    else
                        checkBox.setSelected(false);

                    checkBox.setOnMouseClicked(e -> {
                        playerPane.controller.setPlayerScavenger(this);
                        for(CheckBox checkBox1 : playerPane.scavengerCheckBoxes){
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
            else if(trait == Trait.PREDATOR || trait == Trait.HIGH_BODY){
                //region PREDATOR & HIGH_BODY
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

            traits_box_cn.getChildren().add(hBox);
        }

        updateEatButton();

        traits_box_cn.getChildren().add(bottomBox);
    }
    public void updateEatButton(){
        int satiety = playerPane.controller.getCreatureSatiety(this);
        int hunger = playerPane.controller.getCreatureHunger(this);
        eatButton.setText(satiety + "/" + hunger);
        if(playerPane.controller.isHibernating(this))
            eatButton.setStyle(eatButtonStyle + "-fx-background-color: rgba(0,0,255,0.2); -fx-background-radius: 30;");
        else if(satiety == hunger)
            eatButton.setStyle(eatButtonStyle + "-fx-background-color: rgba(0,255,127,0.2); -fx-background-radius: 30;");
        else
            eatButton.setStyle(eatButtonStyle);
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
    public Label addSymbiosisLink(int linkNumber, boolean isCrocodile){
        if(firstBox.getChildren().size() == 0){
            ImageView imageView = new ImageView(isCrocodile ? crocodile : bird);

            Label label = new Label(Integer.toString(linkNumber));

            label.setAlignment(Pos.CENTER);
            label.setTextAlignment(TextAlignment.CENTER);
            label.setPrefSize(15, 5);
            label.setFont(new Font(9));
            label.setStyle(symbStyle);

            firstBox.getChildren().addAll(imageView, label);
            return label;
        }
        else{
            ImageView imageView = new ImageView(isCrocodile ? crocodile : bird);

            Label label = new Label(Integer.toString(linkNumber));

            label.setAlignment(Pos.CENTER);
            label.setTextAlignment(TextAlignment.CENTER);
            label.setPrefSize(15, 5);
            label.setFont(new Font(9));
            label.setStyle(symbStyle);

            secondBox.getChildren().addAll(label, imageView);
            return label;
        }
    }

    // 0 - all, 1 - comm (up), 2 - coop (down)
    private void setFoodStyle(){
        setFoodStyle(commLinksNumber, coopLinksNumber);
    }
    public void setFoodStyle(int commLinksNumber, int coopLinksNumber){
        this.getChildren().clear();
        this.getChildren().add(traits_box_cn);

        if(commLinksNumber <= 0 && coopLinksNumber <= 0){ // Ничего нет

        }
        else if(commLinksNumber > 0 && coopLinksNumber <= 0){ // Только comm
            commButton.setText(commLinksNumber == 1 ? "Взаимодействие" : "Взаимодействие X" + commLinksNumber);

            this.getChildren().add(commButton);
            AnchorPane.setLeftAnchor(commButton, 1.0);
            AnchorPane.setTopAnchor(commButton, 1.0);
            AnchorPane.setRightAnchor(commButton, 1.0);
            AnchorPane.setBottomAnchor(commButton, 1.0);
        }
        else if (commLinksNumber <= 0){ // Только coop
            coopButton.setText(coopLinksNumber == 1 ? "Сотрудничество" : "Сотрудничество X" + coopLinksNumber);

            this.getChildren().add(coopButton);
            AnchorPane.setLeftAnchor(coopButton, 1.0);
            AnchorPane.setTopAnchor(coopButton, 1.0);
            AnchorPane.setRightAnchor(coopButton, 1.0);
            AnchorPane.setBottomAnchor(coopButton, 1.0);
        }
        else{ // Все есть
            commButton.setText(commLinksNumber == 1 ? "Взаимодействие" : "Взаимодействие X" + commLinksNumber);
            commButton.setPrefHeight(92);

            this.getChildren().add(commButton);
            AnchorPane.clearConstraints(commButton);
            AnchorPane.setLeftAnchor(commButton, 1.0);
            AnchorPane.setTopAnchor(commButton, 1.0);
            AnchorPane.setRightAnchor(commButton, 1.0);

            coopButton.setText(coopLinksNumber == 1 ? "Сотрудничество" : "Сотрудничество X" + coopLinksNumber);
            coopButton.setPrefHeight(92);

            this.getChildren().add(coopButton);
            AnchorPane.clearConstraints(coopButton);
            AnchorPane.setLeftAnchor(coopButton, 1.0);
            AnchorPane.setRightAnchor(coopButton, 1.0);
            AnchorPane.setBottomAnchor(coopButton, 1.0);
        }

    }
    public void addFoodStyle(int type){
        updateEatButton();
        switch (type){
            case 0:
                ++commLinksNumber;
                ++coopLinksNumber;
                break;
            case 1:
                ++commLinksNumber;
                break;
            case 2:
                ++coopLinksNumber;
                break;
        }
        setFoodStyle();
    }
    public void deleteFoodStyle(int type){
        updateEatButton();
        switch (type){
            case 0:
                --commLinksNumber;
                --coopLinksNumber;
                break;
            case 1:
                --commLinksNumber;
                break;
            case 2:
                --coopLinksNumber;
                break;
        }
        setFoodStyle();
    }
    public Button getCommButton(){
        return commButton;
    }
    public Button getCoopButton(){
        return coopButton;
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
    public static void setSymbStyle(Node node, boolean isFull){
        if(isFull){
            node.setStyle(symbStyleFull);
        }
        else{
            node.setStyle(symbStyle);
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
            case 6:
                setCrocodileStyle();
                break;
            case 7:
                setBirdStyle();
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

    private void setCrocodileStyle(){
        this.setStyle("");
        setBorder("green", 2.5);
        //this.setStyle(this.getStyle() + "-fx-background-color: rgba(0,255,127,0.3);");
        setBackground(crocodileBG);
    }
    private void setBirdStyle(){
        this.setStyle("");
        setBorder("green", 2.5);
        //this.setStyle(this.getStyle() + "-fx-background-color: rgba(0,255,127,0.3);");
        setBackground(birdBG);
    }
    private void setPoisonedStyle(){
        this.setStyle("");
        setBorder("violet", 2.5);

        setBackground(poisonedBG);
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
        setBackground(Background.EMPTY);
    }

    public boolean isCrocodileStyle(){
        return styleType == 6;
    }
    public boolean isBirdStyle(){
        return styleType == 7;
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
