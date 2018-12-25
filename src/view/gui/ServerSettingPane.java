package view.gui;

import control.ControllerServer;
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

public class ServerSettingPane extends AnchorPane {

    Stage primaryStage;

    ControllerServer controller;

    @FXML Label server_ip_label_ssp;
    @FXML TextField port_text_field_ssp;
    @FXML Slider max_room_slider_ssp;
    @FXML Button start_button;
    @FXML ImageView check_image;
    @FXML Label cards_number_label_sp;

    Image checkOkImage = new Image("/images/checkOk_100.png");
    Image checkFalseImage = new Image("/images/checkFalse_100.png");

    public ServerSettingPane(ControllerServer controller, Stage primaryStage){
        this.controller = controller;
        this.primaryStage = primaryStage;

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/ServerSettingPane.fxml")
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

        server_ip_label_ssp.setText(controller.getInetAddress().getHostAddress());
        check_image.setImage(checkFalseImage);

        port_text_field_ssp.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("[0-9]*") && newValue.length() < 10){
                port_text_field_ssp.setText(newValue);

                if(!newValue.isEmpty()) {
                    if (controller.availablePort(Integer.parseInt(newValue))) {
                        controller.setPort(Integer.parseInt(newValue));
                        check_image.setImage(checkOkImage);
                        start_button.setDisable(false);
                    } else {
                        check_image.setImage(checkFalseImage);
                        start_button.setDisable(true);
                    }
                }

            }
            else port_text_field_ssp.setText(oldValue);
        });

        max_room_slider_ssp.valueProperty().addListener((observable, oldValue, newValue )-> {
            controller.setMaxRoom(newValue.intValue());
        });

        start_button.setOnMouseClicked(event -> {
            controller.startServer();
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
