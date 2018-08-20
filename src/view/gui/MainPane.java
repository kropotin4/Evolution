package view.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import model.Phase;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;

public class MainPane extends BorderPane {

    @FXML private AnchorPane pane;
    @FXML private TextArea text_chat;
    @FXML private TextField text_input;

    @FXML private HBox creature_box;
    @FXML private VBox action_box;

    Chat chat;

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
        chat = new Chat(text_chat);

        chat.addMessage("system","Hello");
        chat.addMessage("system","And hello");
        chat.addMessage("system","And ... hello!!!");

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

                break;
        }

        Button showCards = new Button();
        showCards.setText("Показать свои карты");
        showCards.setPrefWidth(500);

        Separator separator = new Separator(Orientation.HORIZONTAL);
        separator.setPrefWidth(500);
        separator.setPrefHeight(10);
        separator.setValignment(VPos.BASELINE);
        separator.setStyle("-fx-background-color: white");

        action_box.getChildren().addAll(separator, showCards);
    }

}
