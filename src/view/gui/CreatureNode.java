package view.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import model.Card;
import model.Creature;
import model.Phase;

import java.io.IOException;

public class CreatureNode extends AnchorPane {

    @FXML AnchorPane creature_pane;

    Creature creature;

    public CreatureNode(Creature creature){
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/CreaturePane.fxml")
        );

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.creature = creature;
    }

    @FXML
    private void initialize(){



    }

}
