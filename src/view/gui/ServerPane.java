package view.gui;

import control.ControllerServer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import server.GamingRoomInfo;

import java.io.IOException;

public class ServerPane extends AnchorPane {

    Stage primaryStage;

    ControllerServer controller;

    @FXML VBox room_sheet_sp;

    @FXML Label server_ip_label_sp;
    @FXML Label server_port_label_sp;
    @FXML Label max_room_label_sp;
    @FXML Label player_count_label_sp;
    @FXML Button stop_server_button_sp;

    BackgroundSize backgroundSize;
    BackgroundImage backgroundImage;
    Background background;
    Image serverImage = new Image("/images/server_2000.jpg");
    Image lizardTailImage = new Image("/images/lizard_tail.png");
    Image lizardImage = new Image("/images/lizard_cursor.png");

    public ServerPane(ControllerServer controller, Stage primaryStage){
        this.controller = controller;
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

        stop_server_button_sp.setOnMouseClicked(event -> {
            controller.stopServer();
        });
    }

    public void update(){

        server_ip_label_sp.setText(controller.getInetAddress().getHostAddress());
        server_port_label_sp.setText(String.valueOf(controller.getPort()));
        max_room_label_sp.setText(String.valueOf(controller.getMaxRoom()));
        player_count_label_sp.setText(String.valueOf(controller.getPlayerNumber()));

        room_sheet_sp.getChildren().clear();

        for(GamingRoomInfo gamingRoomInfo : controller.getRoomsInfo()){
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(20);

            Label roomName = new Label(gamingRoomInfo.roomName);
            roomName.setWrapText(true);
            Label playerNumber = new Label(gamingRoomInfo.currentRommSize + " / " + gamingRoomInfo.roomCapacity);

            hBox.getChildren().addAll(roomName, playerNumber);

            room_sheet_sp.getChildren().add(hBox);
        }
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
