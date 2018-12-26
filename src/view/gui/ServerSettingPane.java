package view.gui;

import control.ControllerServer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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
    @FXML Button back_button_cp;

    BackgroundSize backgroundSize;
    BackgroundImage backgroundImage;
    Background background;

    Image serverImage = new Image("/images/drops.jpg");
    Image lizardTailImage = new Image("/images/lizard_tail.png");
    Image lizardImage = new Image("/images/lizard_cursor.png");
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
        ///region cursor
        //primaryStage.getIcons().removeAll();
        primaryStage.getIcons().add(new Image("/images/server.png"));
        Cursor lizardCursor = new ImageCursor(lizardImage, lizardImage.getWidth() / 2, lizardImage.getHeight() / 2);
        Cursor lizardTailCursor = new ImageCursor(lizardTailImage, lizardTailImage.getWidth() / 2, lizardTailImage.getHeight() / 2);
        setCursor(lizardCursor);

        //back_button_cp.setOnMouseEntered(event -> setCursor(lizardTailCursor));
        //back_button_cp.setOnMouseExited(event -> setCursor(lizardCursor));

        max_room_slider_ssp.setOnMouseEntered(event -> setCursor(lizardTailCursor));
        max_room_slider_ssp.setOnMouseExited(event -> setCursor(lizardCursor));

        start_button.setOnMouseEntered(event -> setCursor(Cursor.HAND));
        start_button.setOnMouseExited(event -> setCursor(lizardCursor));

        ///endregion
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

        backgroundSize = new BackgroundSize(serverImage.getWidth(), serverImage.getHeight(), false, false, true, true);
        backgroundImage = new BackgroundImage(serverImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        background = new Background(backgroundImage);
        setBackground(background);
    }


    public void show(){
        Scene scene = new Scene(this, Color.TRANSPARENT);
        scene.setCursor(new ImageCursor(lizardImage, lizardImage.getWidth() / 2, lizardImage.getHeight() / 2));

        primaryStage.setMinWidth(this.getPrefWidth() + 20);
        primaryStage.setMinHeight(this.getPrefHeight() + 40);

        primaryStage.setTitle("Эволюция: сервер");

        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
