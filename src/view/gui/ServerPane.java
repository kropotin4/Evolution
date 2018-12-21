package view.gui;

import control.ControllerServer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerPane extends AnchorPane {

    Stage primaryStage;

    ControllerServer controllerServer;

    @FXML Label ip_label;
    @FXML TextField port_text_field;
    @FXML Slider player_number_slider;
    @FXML Slider deck_size_slider;
    @FXML Button start_button;
    @FXML ImageView check_image;

    Image checkOkImage = new Image("/images/checkOk_100.png");
    Image checkFalseImage = new Image("/images/checkFalse_100.png");

    public ServerPane(ControllerServer controllerServer, Stage primaryStage){
        this.controllerServer = controllerServer;
        this.primaryStage = primaryStage;

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/ServerPane.fxml")
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
        this.setPrefSize(600, 400);

        ip_label.setText(controllerServer.getInetAddress().getHostAddress());
        check_image.setImage(checkFalseImage);

        port_text_field.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("[0-9]*") && newValue.length() < 10){
                port_text_field.setText(newValue);

                if(!newValue.isEmpty()) {
                    if (controllerServer.availablePort(Integer.parseInt(newValue))) {
                        controllerServer.setPort(Integer.parseInt(newValue));
                        port_text_field.setPromptText(newValue);
                        check_image.setImage(checkOkImage);
                        start_button.setDisable(false);
                    } else {
                        check_image.setImage(checkFalseImage);
                        start_button.setDisable(true);
                    }
                }

            }
            else port_text_field.setText(oldValue);
        });


        player_number_slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            controllerServer.setPlayerNum(newValue.intValue());
        });

        deck_size_slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            controllerServer.setQuarterCardCount(newValue.intValue());
        });

        start_button.setOnMouseClicked(event -> {
            controllerServer.startConnecting();
        });

    }


    public void show(){
        Scene scene = new Scene(this, Color.TRANSPARENT);

        primaryStage.setMinWidth(this.getPrefWidth() + 20);
        primaryStage.setMinHeight(this.getPrefHeight() + 40);

        primaryStage.setTitle("Эволюция: сервер");

        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
