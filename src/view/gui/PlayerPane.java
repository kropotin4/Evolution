package view.gui;

import control.Controler;
import control.ControlerGUI;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Creature;
import model.Trait;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerPane extends HBox {

    PlayerPane self = this;
    ControlerGUI controler;
    int playerNumber;

    HBox imageBox = new HBox();
    Image plus1 = new Image("/images/plus1.png");
    Image plus2 = new Image("/images/plus2.png");
    ImageView imageView = new ImageView(plus1);
    boolean imageIsShow = false;

    long startTime;
    long endTime;

    public PlayerPane(ControlerGUI controler, int playerNumber){
        this.controler = controler;
        this.playerNumber = playerNumber;

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/PlayerPane.fxml")
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
        this.setStyle("-fx-border-width: 1; -fx-border-color: black;");
        this.setMinHeight(100);
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(3));
        this.setSpacing(3);

        imageBox.getChildren().add(imageView);
        imageBox.setAlignment(Pos.CENTER);
        imageBox.setPrefSize(80, 300);
        imageView.setCache(true);

        imageView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                imageView.setImage(plus2);
            }
        });

        imageView.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                imageView.setImage(plus1);
            }
        });

        update();
    }

    public void update(){
        if(controler.getCreatures(playerNumber).size() == 0)
            return;

        int num = 0;
        this.getChildren().clear();
        for(Creature creature : controler.getCreatures(playerNumber)){
            CreatureNode creatureNode = new CreatureNode(this, creature.getId(), num++);
            creatureNode.update();
            this.getChildren().add(creatureNode);

            creatureNode.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    controler.selectCreature(creatureNode);
                    System.out.println("Select creature: " + creature.getId());

                    if(event.getClickCount() == 1){
                        startTime = System.nanoTime();
                    }
                    else{
                        endTime = System.nanoTime();
                        if(endTime - startTime >= 250){ // Окончание выбора карты + закрытие DeckPane
                            if(controler.isCardSelected()){
                                //controler.addTraitToCreature(creatureNode, controler.getSelectedCard());

                            }

                        }
                    }

                    //setFalseStyle(creatureNode);
                }
            });

            //setTrueStyle(creatureNode);
        }

        if(imageIsShow)
            this.getChildren().add(imageBox);
    }
    public void setCreaturesWithTraitTrue(Trait trait){

        for(Node node : this.getChildren()){
            CreatureNode creatureNode = (CreatureNode) node;

            setTrueStyle(creatureNode);
            creatureNode.isFalse = false;
        }
    }
    public void setAllCreaturesFalse(){

        for(Node node : this.getChildren()){
            CreatureNode creatureNode = (CreatureNode) node;
            setFalseStyle(creatureNode);
            creatureNode.isFalse = true;
        }

    }
    public void setAllCreaturesDefault(){

        for(Node node : this.getChildren()){
            CreatureNode creatureNode = (CreatureNode) node;
            setDefaultStyle(creatureNode);
            creatureNode.isFalse = false;
        }

    }

    void setTrueStyle(CreatureNode creatureNode){
        creatureNode.setStyle("");
        setCreatureBorders(creatureNode, "green", 2.5);
        creatureNode.setStyle(creatureNode.getStyle() + "-fx-background-color: rgba(0,255,127,0.3);");
    }
    void setFalseStyle(CreatureNode creatureNode){
        creatureNode.setStyle("");
        setCreatureBorders(creatureNode, "red", 2.5);
        creatureNode.setStyle(creatureNode.getStyle() + "-fx-background-color: rgba(255,99,71,0.3);");
    }
    void setDefaultStyle(CreatureNode creatureNode){
        creatureNode.setStyle("");
        setCreatureBorders(creatureNode, "green", 1);
    }

    void setCreatureBorders(CreatureNode creatureNode, String color, double width){
        creatureNode.setBorder(color, width);
    }

    public ImageView getAddIcon(){
        return imageView;
    }
    public void showAddIcon(boolean isShow){
        if(isShow && !imageIsShow) {
            this.getChildren().add(imageBox);
            imageIsShow = true;
        }
        else if(!isShow) {
            this.getChildren().remove(imageBox);
            imageIsShow = false;
        }
    }

    public int getPlayerNumber(){
        return playerNumber;
    }
}
