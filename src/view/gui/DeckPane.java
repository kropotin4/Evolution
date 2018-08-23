package view.gui;

import com.jfoenix.controls.JFXMasonryPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import model.Card;

import java.io.IOException;

public class DeckPane extends JFXMasonryPane {

    public DeckPane(){
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/DeckPane.fxml")
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
    private void initialize(){}

}
