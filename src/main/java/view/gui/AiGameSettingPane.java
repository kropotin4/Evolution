package view.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.IOException;

public class AiGameSettingPane extends VBox {

    @FXML GridPane grid_agsp;
    @FXML Slider card_slider_agsp;
    @FXML Button start_button_agsp;

    public AiGameSettingPane(){

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/AiGameSettingPane.fxml")
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

    }

    private void addPlayer(){

    }

}
