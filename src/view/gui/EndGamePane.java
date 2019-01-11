package view.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


///region pieChart
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
///endregion

import model.EndGameInfo;
import model.Player;

import java.io.IOException;
import java.util.Comparator;

public class EndGamePane extends AnchorPane {
    Stage primaryStage;
    Scene scene = new Scene(this, Color.TRANSPARENT);

    boolean isShow = false;

    @FXML Label result;
    @FXML PieChart chart;

    EndGameInfo info;
    int player;

    public void setInfo(EndGameInfo info) {
        this.info = info;
        int i = 0;
        for (Player player : this.info.players){
            if (player.getPlayerNumber() == i){
                this.player = player.getPlayerNumber();
                break;
            }
            ++i;
        }
    }

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
        primaryStage.setOnCloseRequest(event -> close());
    }

    public void show(){

        primaryStage.setMinWidth(this.getPrefWidth() + 20);
        primaryStage.setMinHeight(this.getPrefHeight() + 40);

        primaryStage.setTitle("Эволюция: конец игры");

        if (info.isDraw && info.players.get(0).getScore() > info.players.get(player).getScore())
            result.setText("Никто не выиграл... Но ты всё равно проиграл :/");
        else if (info.isDraw) result.setText("Ничья.");
        else if (player == info.players.get(0).getPlayerNumber()) result.setText("Ура! Победа!");
        else result.setText("Поражение.");

        ///region chart
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList();
        for (Player player : info.players){
            pieChartData.add(new PieChart.Data((player.getLogin() +
                    (player.getPlayerNumber() == this.player ? " (you)" : "") +
                    " — " + player.getScore() + " point" +
                    (player.getScore() == 11? "s" : player.getScore() % 10 == 1? "" : "s")), player.getScore()));
        }

        chart.setData(pieChartData);
        chart.setTitle("Результаты");
        chart.setLegendSide(Side.BOTTOM);
        ///region angle
        int winner = info.players.get(0).getScore();
        int total = 0;
        for (Player player : info.players){
            total += player.getScore();
        }
        float angle = (float)winner / (float)total;
        chart.setStartAngle(angle * 180);
        ///endregion angle
        ///endregion chart

        ///region chart coloring
        int num = 0;
        for (PieChart.Data data : pieChartData) {
            data.getNode().setStyle("-fx-pie-color: " + info.players.get(num++).getColor() + ";");
        }
        ///endregion

        primaryStage.setScene(scene);
        primaryStage.show();
        isShow = true;
    }

    public boolean isShow() {
        return isShow;
    }

    public void close(){
        primaryStage.close();
    }
}