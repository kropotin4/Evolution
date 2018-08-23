package view.gui;

import control.Controler;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.*;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;

public class MainPane extends BorderPane {

    MainPane self = this;

    @FXML private AnchorPane pane;
    @FXML private TextArea text_chat;
    @FXML private TextField text_input;

    @FXML private HBox creature_box;
    @FXML private VBox action_box;

    Chat chat;

    String system = "system";

    public MainPane(){
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
    }

    @FXML
    private void initialize(){
        Controler controler = new Controler();
        controler.initialize(4, 1);

        controler.addPlayer("anton");

        Player player = controler.getPlayers().get(0);

        Creature creature = new Creature(player);
        creature.addTrait(new Card(Trait.PREDATOR));
        creature.addTrait(new Card(Trait.FAT_TISSUE));

        player.getCreatures().add(creature);



        PlayerPane playerPane = new PlayerPane(controler, 0);

        playerPane.update();
        creature_box.getChildren().add(playerPane);

        playerPane.setVisible(true);

        chat = new Chat(text_chat);
        setActionBox(Phase.GROWTH);
    }

    public void setActionBox(Phase phase) {
        action_box.getChildren().clear();


        switch (phase){
            case GROWTH:

                Button addTrait = new Button();
                addTrait.setText("Положить свойство");
                addTrait.setPrefWidth(500);

                //addTrait.setPrefSize();

                Button addPairTrait = new Button();
                addPairTrait.setText("Положить парное свойство");
                addPairTrait.setPrefWidth(500);

                Button addTraitToOtherCreature = new Button();
                addTraitToOtherCreature.setText("Положить свойство на чужое существо");
                addTraitToOtherCreature.setTextAlignment(TextAlignment.CENTER);
                addTraitToOtherCreature.setWrapText(true);
                addTraitToOtherCreature.setPrefWidth(500);

                action_box.getChildren().addAll(addTrait, addPairTrait, addTraitToOtherCreature);

                break;

            case EATING:

                Button getEat = new Button();
                getEat.setText("Взять еду из кормовой базы");
                getEat.setPrefWidth(500);

                Button attack = new Button();
                attack.setText("Атака");
                attack.setPrefWidth(500);

                action_box.getChildren().addAll(getEat, attack);

                break;
        }

        VBox vBox = new VBox();
        vBox.setPrefHeight(100);
        vBox.setPrefWidth(500);
        vBox.setAlignment(Pos.BOTTOM_CENTER);

        Button showCards = new Button();
        showCards.setText("Показать свои карты");
        showCards.setPrefWidth(500);

        showCards.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                Stage cardsStage = new Stage();
                cardsStage.setHeight(250);
                cardsStage.setWidth(450);

                cardsStage.setTitle("Карты игрока");

                cardsStage.show();
            }
        });

        vBox.getChildren().add(showCards);
        vBox.setPadding(new Insets(5, 0, 1, 0));

        action_box.getChildren().addAll(vBox);
    }

}
