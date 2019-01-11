package view.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.EndGameInfo;


import java.io.IOException;

public class EndGamePane extends AnchorPane {
    Stage primaryStage;
    Scene scene = new Scene(this, Color.TRANSPARENT);


    @FXML Label result;

    EndGameInfo info;
    int player;

    public EndGamePane(Stage primaryStage, EndGameInfo info, int player){
        this.primaryStage = primaryStage;
        this.player = player;
        this.info = info;

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/EndGamePane.fxml")
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
        if (info.isDraw && info.maximum > info.players.get(player).countPlayerPoints()) result.setText("Ничья... Но ты всё равно проиграл :/");
        else if (info.isDraw) result.setText("Ничья.");
        else if (info.winner == player) result.setText("Ура! Победа!");
        else result.setText("Поражение.");
    }

    public void show(){

        primaryStage.setMinWidth(this.getPrefWidth() + 20);
        primaryStage.setMinHeight(this.getPrefHeight() + 40);

        primaryStage.setTitle("Эволюция: конец игры");

        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
