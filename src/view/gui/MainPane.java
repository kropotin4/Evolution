package view.gui;

import control.Controler;
import control.ControlerGUI;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import model.*;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;

public class MainPane extends BorderPane {

    MainPane self = this;

    @FXML private VBox players_pane;
    @FXML private TextArea text_chat;
    @FXML private TextField text_input;

    @FXML private HBox creature_box;

    @FXML private VBox bottom_action_box;
    @FXML private VBox top_action_box;

    Button showCardsButton = new Button();
    Button addTraitButton = new Button();
    Button getEatButton = new Button();
    Button attackButton = new Button();

    ControlerGUI controler;
    Stage primaryStage;

    DeckPane deckPane;
    Chat chat;

    String system = "system";

    CreatureNode selectedCreature;
    CardNode selectedCard;

    boolean isCardSelected = false;

    public MainPane(Stage primaryStage, Controler controler){
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/MainPane.fxml")
        );

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.primaryStage = primaryStage;
        this.controler = new ControlerGUI(controler, this, 0);
    }

    @FXML
    private void initialize(){

        deckPane = new DeckPane(controler);
        chat = new Chat(text_chat);
        setActionBox(Phase.GROWTH);

        ///region addTrait init
        addTraitButton.setText("Положить свойство");
        addTraitButton.setPrefWidth(500);

        addTraitButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //TODO: Сначала выбираем trait, потом тыкаем на существо
                if(!deckPane.isShow)
                    showDeckPane();

            }
        });
        ///endregion

        ///region getEatButton init
        getEatButton.setText("Взять еду из кормовой базы");
        getEatButton.setPrefWidth(500);

        getEatButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

            }
        });
        ///endregion

        ///region attackButton init
        attackButton.setText("Атака");
        attackButton.setPrefWidth(500);

        attackButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

            }
        });
        ///endregion

        ///region showCards init
        showCardsButton.setText("Показать свои карты");
        showCardsButton.setPrefWidth(500);

        showCardsButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                showDeckPane();

            }
        });
        bottom_action_box.getChildren().add(showCardsButton);
        ///endregion

        bottom_action_box.setPadding(new Insets(5, 0, 1, 0));
        bottom_action_box.setSpacing(5);
    }

    public void setActionBox(Phase phase) {
        top_action_box.getChildren().clear();

        switch (phase){
            case GROWTH:

                top_action_box.getChildren().add(addTraitButton);

                break;

            case EATING:

                top_action_box.getChildren().addAll(getEatButton, attackButton);

                break;
        }

    }

    public void setSelectedCreature(CreatureNode creatureNode){
        selectedCreature = creatureNode;
    }
    public void setSelectedCard(CardNode cardNode){
        selectedCard = cardNode;
    }

    public void showSelectedCard(boolean isShow){
        bottom_action_box.getChildren().clear();

        if(isShow) {
            CardNode cardNode = new CardNode(new Card(Trait.FAT_TISSUE, Trait.SYMBIOSYS), 4);
            bottom_action_box.getChildren().addAll(cardNode, showCardsButton);

        }
        else bottom_action_box.getChildren().add(showCardsButton);
    }

    private void showDeckPane(){
        deckPane.update();
        deckPane.show();
    }

    public void update(int playerNumber){

        for(int i = 0; i < controler.getPlayersNumber(); ++i){
            if(i == playerNumber) continue;

            PlayerPane playerPane = new PlayerPane(controler, i);
            players_pane.getChildren().add(playerPane);
        }

        PlayerPane playerPane = new PlayerPane(controler, playerNumber);
        creature_box.getChildren().add(playerPane);

    }

    public void show(){
        Scene scene = new Scene(this, Color.TRANSPARENT);

        primaryStage.setMinWidth(this.getPrefWidth() + 20);
        primaryStage.setMinHeight(this.getPrefHeight() + 40);

        primaryStage.setTitle("Эволюция");

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
