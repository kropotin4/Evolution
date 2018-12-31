package view.gui;

import control.ControllerClient;
import control.ControllerGameRoom;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import server.message.ChatMessage;
import server.message.RoomInfoMessage;

import java.io.IOException;

public class RoomPane extends AnchorPane {

    Stage primaryStage;
    Scene scene;

    ControllerClient controller;

    @FXML Label room_name_label_rp;
    @FXML VBox connection_vbox;
    @FXML Button ready_button_cp;

    @FXML AnchorPane chat_pane_rp;
    Chat chat;

    HBox playerBoxs[];
    Image checkOkImage = new Image("/images/checkOk_50.png");
    Image checkFalseImage = new Image("/images/checkFalse_50.png");

    public RoomPane(ControllerClient controller, Stage primaryStage){
        this.primaryStage = primaryStage;
        this.controller = controller;

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/RoomPane.fxml")
        );

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        scene = new Scene(this, Color.TRANSPARENT);
    }

    @FXML
    private void initialize(){
        ready_button_cp.setOnMouseClicked(event -> {
            controller.readyToPlay();
            ready_button_cp.setDisable(true);
        });


        chat = new Chat();
        chat_pane_rp.getChildren().add(chat);
        chat.getSendButton().setOnMouseClicked(event -> {
            if(!chat.getTextInputField().getText().isEmpty()){
                controller.sendMessage(new ChatMessage(controller.getLogin(), chat.getTextInputField().getText()));
                chat.getTextInputField().setText("");
            }
        });
        AnchorPane.setTopAnchor(chat, 3.0);
        AnchorPane.setRightAnchor(chat, 3.0);
        AnchorPane.setBottomAnchor(chat, 3.0);
        AnchorPane.setLeftAnchor(chat, 3.0);
    }

    public Chat getChat(){
        return chat;
    }

    public void update(RoomInfoMessage roomInfoMessage){
        if(controller.getStage() != 3)
            ready_button_cp.setDisable(false);
        room_name_label_rp.setText("Комната \"" + roomInfoMessage.getRoomName() + "\"");

        connection_vbox.getChildren().clear();

        String[] logins = roomInfoMessage.getPlayersLogins();
        for(int i = 0; i < roomInfoMessage.getRoomCapacity(); ++i){

            HBox hBox = new HBox();
            hBox.setSpacing(18);
            hBox.setAlignment(Pos.CENTER);

            Label playerName;
            ImageView imageView;
            if(logins[i].isEmpty()) {
                playerName = new Label("Игрок " + (i + 1));
                imageView = new ImageView(checkFalseImage);
            }
            else {
                playerName = new Label(logins[i]);
                imageView = new ImageView(checkOkImage);
            }

            hBox.getChildren().addAll(playerName, imageView);
            connection_vbox.getChildren().add(hBox);
        }
    }

    public void show(){

        primaryStage.setMinWidth(this.getPrefWidth() + 20);
        primaryStage.setMinHeight(this.getPrefHeight() + 40);

        primaryStage.setTitle("Эволюция: сервер");

        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
