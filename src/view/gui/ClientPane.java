package view.gui;

import control.ControllerClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import server.GamingRoomInfo;
import server.message.CreateRoomMessage;

import java.io.IOException;

public class ClientPane extends AnchorPane {

    Stage primaryStage;

    ControllerClient controller;

    @FXML Label server_ip_label_cp;
    @FXML Label server_port_label_cp;
    @FXML Label max_room_label_cp;

    @FXML VBox room_sheet_cp;

    @FXML TextField room_name_text_field_cp;
    @FXML Slider player_count_slider_cp;
    @FXML Slider card_count_slider_cp;
    @FXML Button create_room_button_cp;


    public ClientPane(ControllerClient controller, Stage primaryStage){
        this.controller = controller;
        this.primaryStage = primaryStage;

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/ClientPane.fxml")
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
        //room_name_text_field_cp.setText(controller.getLogin() + " room");

        room_name_text_field_cp.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.length() <= 10){
                room_name_text_field_cp.setText(newValue);
                if(newValue.length() == 0)
                    create_room_button_cp.setDisable(true);
                else
                    create_room_button_cp.setDisable(false);
            }
            else room_name_text_field_cp.setText(oldValue);
        });

        create_room_button_cp.setOnMouseClicked(event -> {
            controller.sendMessage(new CreateRoomMessage(
                    room_name_text_field_cp.getText(),
                    (int)player_count_slider_cp.getValue(),
                    (int)card_count_slider_cp.getValue())
            );
        });
    }

    public void update(){
        server_ip_label_cp.setText(controller.getServerIP());
        server_port_label_cp.setText(String.valueOf(String.valueOf(controller.getServerPort())));
        max_room_label_cp.setText(String.valueOf(controller.getRoomCapacity()));

        room_sheet_cp.getChildren().clear();

        for(GamingRoomInfo gamingRoomInfo : controller.getGamingRoomInfo()){
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(18);

            Label roomName = new Label(gamingRoomInfo.roomName);
            roomName.setWrapText(true);
            Label playerNumber = new Label(gamingRoomInfo.currentRommSize + " / " + gamingRoomInfo.roomCapacity);
            Button enter = new Button("Присоединиться");

            enter.setOnMouseClicked(event -> {
                controller.enterTheRoom(gamingRoomInfo.id);
            });

            hBox.getChildren().addAll(roomName, playerNumber, enter);

            room_sheet_cp.getChildren().add(hBox);
        }
    }

    public void show(){
        Scene scene = new Scene(this, Color.TRANSPARENT);

        primaryStage.setMinWidth(this.getPrefWidth() + 20);
        primaryStage.setMinHeight(this.getPrefHeight() + 40);

        primaryStage.setTitle("Эволюция: клиент");

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
