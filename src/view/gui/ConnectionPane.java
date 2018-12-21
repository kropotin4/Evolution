package view.gui;

import control.ControllerServer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class ConnectionPane extends AnchorPane {

    Stage primaryStage;

    ControllerServer controller;

    @FXML Label player_num_label_cp;
    @FXML Label ip_label_cp;
    @FXML Label port_label_cp;

    @FXML VBox connection_vbox;

    @FXML Button start_button_cp;

    HBox playerBoxs[];
    Image checkOkImage = new Image("/images/checkOk_50.png");
    Image checkFalseImage = new Image("/images/checkFalse_50.png");

    public ConnectionPane(ControllerServer controller, Stage primaryStage){
        this.primaryStage = primaryStage;
        this.controller = controller;

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/ConnectionPane.fxml")
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
        start_button_cp.setOnMouseClicked(event -> {
            controller.startGame();
        });
    }

    // Задаем логин, подключающегося игрока.
    public void setLogin(String login, int playerNumber){
        System.out.println(login + " " + playerNumber);
        ((Label)(playerBoxs[playerNumber].getChildren().get(0))).setText("\"" + login + "\" (" + playerNumber + ")");
    }
    public void playerConnect(int playerNumber){
        ((ImageView)playerBoxs[playerNumber].getChildren().get(1)).setImage(checkOkImage);
    }
    public void setStartButtonDisable(boolean value){
        start_button_cp.setDisable(value);
    }

    public void show(){
        Scene scene = new Scene(this, Color.TRANSPARENT);

        primaryStage.setMinWidth(this.getPrefWidth() + 20);
        primaryStage.setMinHeight(this.getPrefHeight() + 40);

        primaryStage.setTitle("Эволюция: сервер");

        primaryStage.setScene(scene);
        primaryStage.show();

        update();
    }

    void update(){
        player_num_label_cp.setText(String.valueOf(controller.getPlayersNumber()));
        ip_label_cp.setText(controller.getInetAddress().getHostAddress());
        port_label_cp.setText(String.valueOf(controller.getPort()));

        connection_vbox.getChildren().clear();

        playerBoxs = new HBox[controller.getPlayersNumber()];
        for(int i = 0; i < controller.getPlayersNumber(); ++i){
            playerBoxs[i] = new HBox();
            playerBoxs[i].setSpacing(20);
            playerBoxs[i].setAlignment(Pos.CENTER);

            Label playerName = new Label("Игрок " + i);
            playerName.setFont(new Font("Arial Bold", 14));
            //Label connectionLabel = new Label("подключения нет");
            ImageView imageView = new ImageView(checkFalseImage);

            playerBoxs[i].getChildren().addAll(playerName, imageView);

            connection_vbox.getChildren().add(playerBoxs[i]);
        }
    }
}
