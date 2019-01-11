package view.gui;

import com.jfoenix.controls.JFXRadioButton;
import control.ControllerGUI;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.Node;
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

    ///region field
    Image cancel1 = new Image("/images/cancel1.png");
    Image cancel2 = new Image("/images/cancel2.png");
    Image cancel3 = new Image("/images/cancel3.png");
    Image cancel4 = new Image("/images/cancel4.png");

    @FXML private AnchorPane pane;
    @FXML private VBox players_pane;

    @FXML private AnchorPane chat_pane_mp;
//    @FXML private TextArea text_chat;
//    @FXML private TextField text_input;
//    @FXML private Button send_button_chat;

    @FXML private AnchorPane playing_pane;
    PlayerPane playerPane;

    @FXML private HBox info_pane;
    @FXML private VBox bottom_action_box;
    @FXML private VBox top_action_box;



    Button showCardsButton = new Button();
    Button addTraitButton = new Button();
    HBox eatButtonBox = new HBox();
    Button getEatButton = new Button();
    ImageView cancelEatImage = new ImageView(cancel3);
    HBox attackButtonBox = new HBox();
    Button attackButton = new Button();
    ImageView cancelAttackImage = new ImageView(cancel3);
    Button passButton = new Button();
    HBox piracyButtonBox = new HBox();
    Button piracyButton = new Button();
    ImageView cancelPiracyImage = new ImageView(cancel3);

    Label foodLabel = new Label();
    Label phaseLabel = new Label();

    ControllerGUI controler;
    Stage primaryStage;

    AddTraitPane addTraitPane;
    boolean pressedCreatureNode = false;
    public DeckPane deckPane;
    boolean pressedPlusImage = false;
    Chat chat;

    ImageView cancelImage = new ImageView(cancel1);

    Image cursorAsk = new Image("/images/cur4.png");

    String system = "system";

    CreatureNode selectedCreature;
    CreatureNode attackerCreature;
    CreatureNode pirateCreature;
    CardNode selectedCard;

    boolean isPirateSelecting = false; // Выбираем пирата
    boolean isPirateVictimSelecting = false; // Выбираем жертву пирата
    boolean isFoodGetting = false; // Нажали на "Взять еду из кормовой базы"
    boolean isAttackerSelecting = false; // Нажали на "Атака" и выбирают атакующее существо
    boolean isDefenderSelecting = false; // Выбирают кого атаковать

    boolean isCardSelecting = false; // Выбор карты (нажали "Положить свойство")
    boolean isCardSelected = false; // Выбрали карту
    boolean isPairTraitSelected = false; // Хотят положить парное свойство
    boolean isUp = false; // Выбрано верхнее/нижнее свойство на двойной карте
    boolean isCreatureAdding = false; // Нажали на большой зеленый плюс
    ///endregion

    public MainPane(Stage primaryStage, ControllerGUI controller){
        this.primaryStage = primaryStage;
        this.controler = controller;



        //Scene scene = new Scene(this, Color.TRANSPARENT);

        //primaryStage.setMinWidth(this.getPrefWidth() + 20);
        //primaryStage.setMinHeight(this.getPrefHeight() + 40);
        //primaryStage.setTitle("Эволюция");

        //primaryStage.setScene(scene);

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/MainPane.fxml")
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

        addTraitPane = new AddTraitPane(controler);
        deckPane = new DeckPane(controler);

        chat = new Chat();
        chat_pane_mp.getChildren().add(chat);
        chat.getSendButton().setOnMouseClicked(event -> {
            if(!chat.getTextInputField().getText().isEmpty()){
                controler.sendChatMessage(chat.getTextInputField().getText());
                chat.getTextInputField().setText("");
            }
        });
        AnchorPane.setTopAnchor(chat, 3.0);
        AnchorPane.setRightAnchor(chat, 3.0);
        AnchorPane.setBottomAnchor(chat, 3.0);
        AnchorPane.setLeftAnchor(chat, 3.0);

        this.setOnMouseClicked(event -> {
            if(!pressedPlusImage && !pressedCreatureNode) {
                primaryStage.setAlwaysOnTop(true);
                primaryStage.setAlwaysOnTop(false);
            }
            pressedPlusImage = false;
            pressedCreatureNode = false;
        });
        primaryStage.setOnCloseRequest(event -> close());

        initButton();

        top_action_box.setSpacing(5);
        bottom_action_box.setSpacing(5);

        top_action_box.setPadding(new Insets(6));
        bottom_action_box.setPadding(new Insets(5));

        info_pane.setPadding(new Insets(3));
        info_pane.setSpacing(10);
        info_pane.setAlignment(Pos.CENTER);

        //controler.startGame();
        setPhaseElement(Phase.GROWTH);

        ///temp
        //DefenseOrderPane defenseOrderPane = new DefenseOrderPane(controler);
        //ArrayList<Trait> traits = new ArrayList<>();
        //traits.add(Trait.FAT_TISSUE);
        //traits.add(Trait.CAMOUFLAGE);
        //traits.add(Trait.PARASITE);
        //traits.add(Trait.SYMBIOSIS);
        //defenseOrderPane.setDefenseTraits(traits);
        //defenseOrderPane.show();
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

        ///region getEatButton + cancelEatImage + eatButtonBox init
        eatButtonBox.getChildren().add(getEatButton);
        eatButtonBox.setPrefWidth(500);
        eatButtonBox.setPrefHeight(25);
        eatButtonBox.setMinHeight(25);
        eatButtonBox.setAlignment(Pos.CENTER);

        getEatButton.setText("Взять еду из кормовой базы");
        getEatButton.setPrefWidth(500);

        getEatButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                isFoodGetting = true;
                playerPane.setHungerCreaturesTrue();
                if(eatButtonBox.getChildren().size() < 2)
                    eatButtonBox.getChildren().add(cancelEatImage);
            }
        });
        cancelEatImage.setCache(true);
        cancelEatImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                isFoodGetting = false;
                playerPane.setAllCreaturesDefault();
                eatButtonBox.getChildren().remove(1);
            }
        });

        cancelEatImage.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                cancelEatImage.setImage(cancel4);
            }
        });
        cancelEatImage.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                cancelEatImage.setImage(cancel3);
            }
        });
        ///endregion

        ///region attackButton + cancelAttackImage + attackButtonBox init
        attackButtonBox.getChildren().add(attackButton);
        attackButtonBox.setPrefWidth(500);
        attackButtonBox.setPrefHeight(25);
        attackButtonBox.setMinHeight(25);
        attackButtonBox.setAlignment(Pos.CENTER);

        attackButton.setText("Атака");
        attackButton.setPrefWidth(500);

        attackButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Нажата атака");
                isAttackerSelecting = true;
                playerPane.setAttackerCreaturesTrue();
                //playerPane.setCreaturesWithTraitTrue(Trait.PREDATOR);
                if(attackButtonBox.getChildren().size() < 2)
                    attackButtonBox.getChildren().add(cancelAttackImage);
            }
        });
        cancelAttackImage.setCache(true);
        cancelAttackImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                isAttackerSelecting = false;
                setAllCreaturesDefault();
                attackButtonBox.getChildren().remove(1);
            }
        });

        cancelAttackImage.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                cancelAttackImage.setImage(cancel4);
            }
        });
        cancelAttackImage.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                cancelAttackImage.setImage(cancel3);
            }
        });
        ///endregion

        ///region piracyButton + cancelPiracyImage + piracyButtonBox init
        piracyButtonBox.getChildren().add(piracyButton);
        piracyButtonBox.setPrefWidth(500);
        piracyButtonBox.setPrefHeight(25);
        piracyButtonBox.setMinHeight(25);
        piracyButtonBox.setAlignment(Pos.CENTER);

        piracyButton.setText("Взять этих сухопутных крыс на абардаж");
        piracyButton.setPrefWidth(500);

        piracyButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Нажато пиратсво.");
                isPirateSelecting = true;
                playerPane.setCreaturesWithTraitTrue(Trait.PIRACY);
                if(piracyButtonBox.getChildren().size() < 2)
                    piracyButtonBox.getChildren().add(cancelPiracyImage);
            }
        });
        cancelPiracyImage.setCache(true);
        cancelPiracyImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                isPirateSelecting = false;
                setAllCreaturesDefault();
                piracyButtonBox.getChildren().remove(1);
            }
        });

        cancelPiracyImage.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                cancelPiracyImage.setImage(cancel4);
            }
        });
        cancelPiracyImage.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                cancelPiracyImage.setImage(cancel3);
            }
        });
        ///endregion

        ///region showCards init
        showCardsButton.setText("Показать свои карты");
        showCardsButton.setPrefWidth(500);

        showCardsButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                isCardSelecting = false;
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
        cancelImage.setCache(true);
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

    // Здесь формируется нижняя-правая панель с кнопками действий
    public void setPhaseElement(Phase phase) {
        top_action_box.getChildren().clear();

        top_action_box.setDisable(controler.isBlockActions()); // Блокируем действия если не твой ход

        getEatButton.setDisable(false);
        attackButton.setDisable(false);
        piracyButton.setDisable(false);

        switch (phase){
            case GROWTH:

                top_action_box.getChildren().add(addTraitButton);

                break;

            case EATING:
                if(eatButtonBox.getChildren().size() == 2)
                    eatButtonBox.getChildren().remove(1);
                if(attackButtonBox.getChildren().size() == 2)
                    attackButtonBox.getChildren().remove(1);
                if(piracyButtonBox.getChildren().size() == 2)
                    piracyButtonBox.getChildren().remove(1);

                playerPane.showAddIcon(false);

                top_action_box.getChildren().addAll(eatButtonBox, attackButtonBox);

                if(controler.havePiracyCreatures())
                    top_action_box.getChildren().add(piracyButtonBox);

                if(!controler.haveCanPiracyCreatures())
                    piracyButton.setDisable(true);

                if(controler.getFoodNumber() <= 0 || !controler.haveHungryCreature())
                    getEatButton.setDisable(true);

                if(!controler.havePlayerPredator()){
                    attackButton.setDisable(true);
                }

                break;
            default:

                break;
        }

        top_action_box.getChildren().add(passButton);
        //if(controller.getFoodNumber() <= 0)
            //passButton.setDisable(true);
    }

    public void setSelectedCreature(CreatureNode creatureNode){
        selectedCreature = creatureNode;
    }
    public void setAttackerCreature(CreatureNode creatureNode){
        this.attackerCreature = creatureNode;
    }
    public void setPirateCreature(CreatureNode creatureNode){
        this.pirateCreature = creatureNode;
    }
    public void setSelectedCard(CardNode cardNode){
        selectedCard = cardNode;
    }

    public CreatureNode getSelectedCreature(){
        return selectedCreature;
    }
    public CreatureNode getAttackerCreature(){
        return attackerCreature;
    }
    public CreatureNode getPirateCreature(){
        return pirateCreature;
    }
    public CardNode getSelectedCard(){
        return selectedCard;
    }


    public void showSelectedCard(boolean isShow){
        bottom_action_box.getChildren().clear();

        if(isShow) {
            isCardSelected = true;
            isCardSelecting = false;

            HBox cardBox = new HBox();
            cardBox.setAlignment(Pos.CENTER);
            CardNode cardNode = new CardNode(selectedCard.card, 1);

            if(selectedCard.card.getTrait(true) != selectedCard.card.getTrait(false)){
                ToggleGroup group = new ToggleGroup();

                JFXRadioButton firtsTrait = new JFXRadioButton();
                JFXRadioButton secondTrait = new JFXRadioButton();
                firtsTrait.setToggleGroup(group);
                secondTrait.setToggleGroup(group);

                firtsTrait.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        isUp = true;
                        System.out.print("MainPain: firstTrait -> ");
                        if(Creature.isPairTrait(cardNode.getCard().getTrait(true)))
                            isPairTraitSelected = true;
                        else
                            isPairTraitSelected = false;

                        System.out.println("isPairTraitSelected = " + isPairTraitSelected + " ");

                        if(cardNode.getCard().getTrait(true) != Trait.PARASITE){
                            setAllCreaturesDefault();
                            playerPane.setCanGettingTraitCreaturesTrue(cardNode.getCard().getTrait(true));
                        }
                        else{
                            setAllCreaturesDefault();
                            for (Node node : players_pane.getChildren()) {
                                PlayerPane playerPane = (PlayerPane) node;

                                playerPane.setCanGettingTraitCreaturesPoison(cardNode.getCard().getTrait(true));
                            }
                        }
                    }
                });
                secondTrait.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        isUp = false;
                        System.out.print("MainPain: secondTrait -> ");
                        if(Creature.isPairTrait(cardNode.getCard().getTrait(false)))
                            isPairTraitSelected = true;
                        else
                            isPairTraitSelected = false;
                        System.out.println("isPairTraitSelected = " + isPairTraitSelected + " ");
                        if(cardNode.getCard().getTrait(false) != Trait.PARASITE){
                            setAllCreaturesDefault();
                            playerPane.setCanGettingTraitCreaturesTrue(cardNode.getCard().getTrait(false));
                        }
                        else{
                            setAllCreaturesDefault();
                            for (Node node : players_pane.getChildren()) {
                                PlayerPane playerPane = (PlayerPane) node;

                                playerPane.setCanGettingTraitCreaturesPoison(cardNode.getCard().getTrait(false));
                            }
                        }
                    }
                });

                VBox radioBox = new VBox();
                radioBox.setPrefWidth(15);
                radioBox.setPrefHeight(80);
                radioBox.setAlignment(Pos.CENTER);
                radioBox.setSpacing(12);

                radioBox.getChildren().addAll(firtsTrait, secondTrait);
                cardBox.getChildren().addAll(radioBox, cardNode, cancelImage);
            }
            else {

                if(Creature.isPairTrait(cardNode.getCard().getTrait(true)))
                    isPairTraitSelected = true;
                else
                    isPairTraitSelected = false;

                if(cardNode.getCard().getTrait() != Trait.PARASITE){
                    setAllCreaturesDefault();
                    playerPane.setCanGettingTraitCreaturesTrue(cardNode.getCard().getTrait());
                }
                else{
                    setAllCreaturesDefault();
                    for (Node node : players_pane.getChildren()) {
                        PlayerPane playerPane = (PlayerPane) node;

                        playerPane.setCanGettingTraitCreaturesPoison(cardNode.getCard().getTrait());
                    }
                }

                cardBox.getChildren().addAll(cardNode, cancelImage);
            }

            bottom_action_box.getChildren().addAll(cardBox, showCardsButton);

        }
        else{
            bottom_action_box.getChildren().add(showCardsButton);

            isCardSelected = false;
            isCardSelecting = false;
            isCreatureAdding = false;
            isUp = false;
            isPairTraitSelected = false;
            setAllCreaturesDefault();
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
    public boolean isUpTrait(){
        return isUp;
    }
    public boolean isPairTraitSelected(){
        return isPairTraitSelected;
    }

    public boolean isPirateSelecting() {
        return isPirateSelecting;
    }
    public void setPirateSelecting(boolean isPirateSelecting) {
        this.isPirateSelecting = isPirateSelecting;
    }
    public boolean isPirateVictimSelecting() {
        return isPirateVictimSelecting;
    }
    public void setPirateVictimSelecting(boolean isPirateVictimSelecting) {
        this.isPirateVictimSelecting = isPirateVictimSelecting;
    }
    public boolean isFoodGetting(){
        return isFoodGetting;
    }
    public void setIsFoodGetting(boolean isFoodGetting){
        this.isFoodGetting = isFoodGetting;
    }
    public boolean isAttackerSelecting(){
        return isAttackerSelecting;
    }
    public void setIsAttackerSelecting(boolean isAttackerSelecting){
        this.isAttackerSelecting = isAttackerSelecting;
    }
    public boolean isDefenderSelecting(){
        return isDefenderSelecting;
    }
    public void setIsDefenderSelecting(boolean isDefenderSelecting){
        this.isDefenderSelecting = isDefenderSelecting;
    }

    public void showAddTraitPane(CreatureNode selectedCreature, double X, double Y){
        addTraitPane.show();
        addTraitPane.setCardNode(selectedCard);
        addTraitPane.setCreatureNode(selectedCreature);
        pressedCreatureNode = true;
        addTraitPane.setTop(true);
    }
    private void showDeckPane(){
        deckPane.update();
        deckPane.show();
    }

    public ObservableList<Node> getPlayersPane() {
        return players_pane.getChildren();
    }
    public PlayerPane getCurrentPlayerPane(){
        return playerPane;
    }

    public void setAllCreaturesDefault(){
        playerPane.setAllCreaturesDefault();
        for (Node node : players_pane.getChildren()) {
            PlayerPane playerPane = (PlayerPane) node;

            playerPane.setAllCreaturesDefault();
        }
    }

    // playerNumber - main player
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
        checkShowCardsButton();
    }
    public void updateCurrentPlayer(){
        playerPane.update();
        setPhaseElement(controler.getCurrentPhase());
        checkAddImage();
        checkInfoPane();
        checkShowCardsButton();
    }
    /*Плюсик в фазе GROWTH для удобного добавления существа
    * + обработка нажатия на него*/
    private void checkAddImage(){
        if(controler.getCurrentPhase() == Phase.GROWTH
                && controler.getPlayerCardsNumber() > 0){

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
    private void checkShowCardsButton(){
        if(controler.getPlayerCardsNumber() == 0){
            showCardsButton.setText("Нет карт");
            showCardsButton.setDisable(true);
        }
        else{
            showCardsButton.setText("Показать свои карты");
            showCardsButton.setDisable(false);
        }
    }

    public Chat getChat(){
        return chat;
    }

    public void show(){
        Scene scene = new Scene(this, Color.TRANSPARENT);

        primaryStage.setMinWidth(this.getPrefWidth() + 20);
        primaryStage.setMinHeight(this.getPrefHeight() + 40);
        primaryStage.setTitle("Эволюция");

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public void close(){
        if(deckPane.isShow)
            deckPane.close();
        primaryStage.close();
    }
}
