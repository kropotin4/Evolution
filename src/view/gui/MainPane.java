package view.gui;

import control.Controler;
import control.ControlerGUI;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.*;

import java.io.IOException;

public class MainPane extends BorderPane {

    MainPane self = this;

    @FXML private AnchorPane pane;
    @FXML private VBox players_pane;
    @FXML private TextArea text_chat;
    @FXML private TextField text_input;

    @FXML private AnchorPane playing_pane;
    PlayerPane playerPane;

    @FXML private HBox info_pane;
    @FXML private VBox bottom_action_box;
    @FXML private VBox top_action_box;

    Button showCardsButton = new Button();
    Button addTraitButton = new Button();
    Button getEatButton = new Button();
    Button attackButton = new Button();
    Button passButton = new Button();

    Label foodLabel = new Label();
    Label phaseLabel = new Label();

    ControlerGUI controler;
    Stage primaryStage;

    AddTraitPane addTraitPane;
    boolean pressedCreatureNode = false;
    public DeckPane deckPane;
    boolean pressedPlusImage = false;
    Chat chat;

    Image cancel1 = new Image("/images/cancel1.png");
    Image cancel2 = new Image("/images/cancel2.png");
    ImageView cancelImage = new ImageView(cancel1);

    Image cursorAsk = new Image("/images/cur4.png");

    String system = "system";

    CreatureNode selectedCreature;
    CardNode selectedCard;

    boolean isAttackerSelecting = false;
    boolean isAttackedSelecting = false;

    boolean isCardSelecting = false;
    boolean isCardSelected = false;
    boolean isCreatureAdding = false;

    public MainPane(Stage primaryStage, Controler controler){
        this.primaryStage = primaryStage;
        this.controler = new ControlerGUI(controler, this, 0);

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

        Scene scene = new Scene(this, Color.TRANSPARENT);

        primaryStage.setMinWidth(this.getPrefWidth() + 20);
        primaryStage.setMinHeight(this.getPrefHeight() + 40);
        primaryStage.setTitle("Эволюция");

        primaryStage.setScene(scene);
    }

    @FXML
    private void initialize(){

        addTraitPane = new AddTraitPane(controler);
        deckPane = new DeckPane(controler);
        chat = new Chat(text_chat);

        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!pressedPlusImage && !pressedCreatureNode) {
                    primaryStage.setAlwaysOnTop(true);
                    primaryStage.setAlwaysOnTop(false);
                }
                pressedPlusImage = false;
                pressedCreatureNode = false;
            }
        });
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                close();
            }
        });

        initButton();

        bottom_action_box.setPadding(new Insets(5, 0, 1, 0));
        bottom_action_box.setSpacing(5);

        top_action_box.setPadding(new Insets(3));
        bottom_action_box.setPadding(new Insets(3));

        info_pane.setPadding(new Insets(3));
        info_pane.setSpacing(10);
        info_pane.setAlignment(Pos.CENTER);

        controler.startGame();
        setPhaseElement(Phase.GROWTH);
    }

    private void initButton(){
        ///region addTrait init
        addTraitButton.setText("Положить свойство");
        addTraitButton.setPrefWidth(500);

        addTraitButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //TODO: Сначала выбираем trait, потом тыкаем на существо
                if(!isCardSelected) {
                    if (!deckPane.isShow)
                        showDeckPane();
                    deckPane.setTop();

                    isCardSelecting = true;
                }
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

                if(!deckPane.isShow)
                    showDeckPane();

                deckPane.setTop();
            }
        });
        bottom_action_box.getChildren().add(showCardsButton);
        ///endregion

        ///region passButton init
        passButton.setText("Пасс");
        passButton.setPrefWidth(500);

        passButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                controler.passPlayer();
            }
        });
        ///endregion

        ///region cancelImage init
        cancelImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(isCardSelected){
                    showSelectedCard(false);
                }
            }
        });
        cancelImage.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                cancelImage.setImage(cancel2);
            }
        });
        cancelImage.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                cancelImage.setImage(cancel1);
            }
        });
        ///endregion
    }

    // Заплатка -> Phase = GROWTH;
    public void setPhaseElement(Phase phase) {
        top_action_box.getChildren().clear();

        switch (phase){
            case GROWTH:

                top_action_box.getChildren().add(addTraitButton);

                break;

            case EATING:

                playerPane.showAddIcon(false);
                top_action_box.getChildren().addAll(getEatButton, attackButton);

                break;
        }

        top_action_box.getChildren().add(passButton);
    }

    public void setSelectedCreature(CreatureNode creatureNode){
        selectedCreature = creatureNode;
    }
    public void setSelectedCard(CardNode cardNode){
        selectedCard = cardNode;
    }

    public CardNode getSelectedCard(){
        return selectedCard;
    }
    public CreatureNode getSelectedCreature(){
        return selectedCreature;
    }

    public void showSelectedCard(boolean isShow){
        bottom_action_box.getChildren().clear();

        if(isShow) {
            isCardSelected = true;
            isCardSelecting = false;

            HBox cardBox = new HBox();
            cardBox.setAlignment(Pos.CENTER);
            CardNode cardNode = new CardNode(selectedCard.card, 1);
            cardBox.getChildren().addAll(cardNode, cancelImage);
            bottom_action_box.getChildren().addAll(cardBox, showCardsButton);

        }
        else{
            bottom_action_box.getChildren().add(showCardsButton);

            isCardSelected = false;
            isCardSelecting = false;
        }
    }
    public boolean isCardSelecting(){
        return isCardSelecting;
    }
    public boolean isCardSelected(){
        return isCardSelected;
    }
    public boolean isCreatureAdding(){
        return isCreatureAdding;
    }
    public void setIsCardSelecting(boolean isCardSelecting){
        this.isCardSelecting = isCardSelecting;
    }
    public void setIsCardSelected(boolean isCardSelected){
        this.isCardSelected = isCardSelected;
    }
    public void setIsCreatureAdding(boolean isCreatureAdding){
        this.isCreatureAdding = isCreatureAdding;
    }

    public void showAddTraitPane(){
        addTraitPane.show();
        addTraitPane.setCardNode(selectedCard);
        pressedCreatureNode = true;
        addTraitPane.setTop(true);
    }
    private void showDeckPane(){
        deckPane.update();
        deckPane.show();
    }

    public void update(int playerNumber){
        players_pane.getChildren().clear();
        playing_pane.getChildren().clear();

        for(int i = 0; i < controler.getPlayersNumber(); ++i){
            if(i == playerNumber) continue;

            PlayerPane playerPane = new PlayerPane(controler, i);
            players_pane.getChildren().add(playerPane);
        }

        PlayerPane playerPane = new PlayerPane(controler, playerNumber);
        this.playerPane = playerPane;
        playing_pane.getChildren().add(playerPane);

        AnchorPane.setLeftAnchor(playerPane, 5.0);
        AnchorPane.setRightAnchor(playerPane, 5.0);
        AnchorPane.setTopAnchor(playerPane, 0.0);
        AnchorPane.setBottomAnchor(playerPane, 0.0);

        setPhaseElement(controler.getCurrentPhase());
        checkAddImage();
        checkInfoPane();
    }
    public void updateCurrentPlayer(){
        playerPane.update();
        setPhaseElement(controler.getCurrentPhase());
        checkAddImage();
        checkInfoPane();
    }
    /*Плюсик в фазе GROWTH для удобного добавления существа
    * + обработка нажатия на него*/
    private void checkAddImage(){
        if(controler.getCurrentPhase() == Phase.GROWTH
                || controler.getPlayerCardNumber() > 0){

            playerPane.showAddIcon(true);
            ImageView addIcon = playerPane.getAddIcon();
            addIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(isCardSelected){
                        controler.addCreature(selectedCard);
                    }
                    else {
                        if (!deckPane.isShow)
                            showDeckPane();
                        pressedPlusImage = true;
                        isCardSelecting = true;
                        isCreatureAdding = true;
                        deckPane.setTop();
                    }
                }
            });
        }
        else{
            playerPane.showAddIcon(false);
        }
    }
    private void checkInfoPane(){
        info_pane.getChildren().clear();
        phaseLabel.setText(controler.getCurrentPhase().toString());
        info_pane.getChildren().add(phaseLabel);
        if(controler.getCurrentPhase() == Phase.EATING){
            foodLabel.setText("Еды: " + controler.getFoodNumber());
            info_pane.getChildren().add(foodLabel);
        }
    }

    public void show(){
        primaryStage.show();
    }
    public void close(){
        if(deckPane.isShow)
            deckPane.close();
        primaryStage.close();
    }
}
