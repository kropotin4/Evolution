package view.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Phase;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;

public class MainPane extends BorderPane {

    @FXML private TextArea text_common;
    @FXML private TextArea text_chat;
    @FXML private TextField text_input;

    @FXML private HBox creature_box;
    @FXML private VBox action_box;

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
        text_common.setText("Hello");
        text_chat.setText("Hello");
        text_input.setText("Hello");

        setAction_box(Phase.GROWTH);
    }

    public void setAction_box(Phase phase) {
        action_box.getChildren().clear();

        switch (phase){
            case GROWTH:

                Button addTrait = new Button();
                addTrait.setText("Положить свойство");
                //addTrait.setPrefSize();

                Button addPairTrait = new Button();
                addPairTrait.setText("Положить парное свойство");

                Button addTraitToOtherCreature = new Button();
                addTraitToOtherCreature.setText("Положить свойство на чужое существо");

                action_box.getChildren().addAll(addTrait, addPairTrait, addTraitToOtherCreature);

                break;

            case EATING:

                break;
        }
    }
}
