package view.gui;

import com.jfoenix.controls.*;
import control.ControllerClient;
import control.ControllerGUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class AiGameSettingPane extends VBox {

    Stage primaryStage;
    ControllerGUI controller;

    @FXML GridPane grid_agsp;
    @FXML Slider card_slider_agsp;
    @FXML Button start_button_agsp;

    int playerNumber = 0;

    JFXButton addButton01;
    JFXButton addButton11;

    private class PlayerInfo{
        JFXTextField login;
        JFXToggleButton isAi;
        JFXSlider aiDif;
    }
    ArrayList<PlayerInfo> playerInfos = new ArrayList<>();

    public AiGameSettingPane(ControllerGUI controller, Stage primaryStage){
        this.primaryStage = primaryStage;
        this.controller = controller;

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
        addPlayer(0, 0);
        addPlayer(1, 0);

        addButton01 = new JFXButton("Добавить");
        addButton01.setPrefSize(90, 50);
        addButton01.setStyle(
                "     -jfx-button-type: RAISED;\n" +
                        "     -fx-background-color: green;\n" +
                        "     -fx-text-fill: white;\n" +
                        "     -fx-text-alignment: center;\n" +
                        "     -fx-wrap-text: true;\n" +
                        "     -fx-font-size: 14;");
        addButton01.setOnMouseClicked(event -> {
            grid_agsp.getChildren().remove(addButton01);
            addPlayer(0, 1);
        });
        grid_agsp.add(addButton01, 0, 1);

        addButton11 = new JFXButton("Добавить");
        addButton11.setPrefSize(90, 50);
        addButton11.setStyle(
                "     -jfx-button-type: RAISED;\n" +
                        "     -fx-background-color: green;\n" +
                        "     -fx-text-fill: white;\n" +
                        "     -fx-text-alignment: center;\n" +
                        "     -fx-wrap-text: true;\n" +
                        "     -fx-font-size: 14;");
        addButton11.setOnMouseClicked(event -> {
            grid_agsp.getChildren().remove(addButton11);
            addPlayer(1, 1);
        });
        grid_agsp.add(addButton11, 1, 1);


        start_button_agsp.setOnMouseClicked(event -> {
            controller.startGame();
        });
    }

    private void addPlayer(int column, int row){
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(1, 5, 2, 5));
        vBox.setStyle("-fx-border-width: 1; -fx-border-color: green;");

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);

        JFXTextField textField = new JFXTextField();
        textField.setText("Бот " + playerNumber);
        textField.setFocusColor(Paint.valueOf("green"));
        textField.setUnFocusColor(Paint.valueOf("darkgreen"));
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.length() > 10){
                textField.setText(oldValue);
            }
        });

        JFXToggleButton isAiButton = new JFXToggleButton();
        isAiButton.setToggleColor(Paint.valueOf("green"));
        isAiButton.setToggleLineColor(Paint.valueOf("limegreen"));
        isAiButton.setSelected(true);
        isAiButton.setText("Включить ИИ");

        HBox difBox = new HBox();
        difBox.setSpacing(10);

        Label difLabel = new Label("Сложность ИИ:");

        JFXSlider difSlider = new JFXSlider(1, 3, 2);
        difSlider.setBlockIncrement(1);
        difSlider.setShowTickLabels(true);
        difSlider.setShowTickMarks(true);
        difSlider.setSnapToTicks(true);
        difSlider.setMajorTickUnit(1);
        difSlider.setMinorTickCount(0);

        difBox.getChildren().addAll(difLabel, difSlider);
        hBox.getChildren().addAll(textField, isAiButton);

        PlayerInfo playerInfo = new PlayerInfo();
        playerInfo.login = textField;
        playerInfo.isAi = isAiButton;
        playerInfo.aiDif = difSlider;

        if(row == 1){
            JFXButton deleteButton = new JFXButton("Удалить");
            deleteButton.setStyle(
                    "     -jfx-button-type: RAISED;\n" +
                            "     -fx-background-color: red;\n" +
                            "     -fx-text-fill: white;\n" +
                            "     -fx-text-alignment: center;\n" +
                            "     -fx-wrap-text: true;\n" +
                            "     -fx-font-size: 12;");
            deleteButton.setOnMouseClicked(event -> {
                grid_agsp.getChildren().remove(vBox);
                playerInfos.remove(playerInfo);
                if(column == 0){
                    grid_agsp.add(addButton01, 0, 1);
                }
                else{
                    grid_agsp.add(addButton11, 1, 1);
                }
            });
            vBox.getChildren().addAll(hBox, difBox, deleteButton);
        }
        else{
            vBox.getChildren().addAll(hBox, difBox);
        }

        isAiButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue)
                difSlider.setDisable(false);
            else
                difSlider.setDisable(true);
        });

        grid_agsp.add(vBox, column, row);

        playerInfos.add(playerInfo);
    }

    public int getQuarterCardCount(){
        return (int)card_slider_agsp.getValue();
    }
    public int getPlayersNumber(){
        return playerInfos.size();
    }
    public String getLogin(int playerNumber){
        return playerInfos.get(playerNumber).login.getText();
    }
    public boolean isAi(int playerNumber){
        return playerInfos.get(playerNumber).isAi.isSelected();
    }
    public int getAiDif(int playerNumber){
        return (int)playerInfos.get(playerNumber).aiDif.getValue();
    }

    public void show(){
        Scene scene = new Scene(this, Color.TRANSPARENT);
        primaryStage.setMinWidth(this.getPrefWidth() + 20);
        primaryStage.setMinHeight(this.getPrefHeight() + 40);

        primaryStage.setTitle("Эволюция: игра с ИИ");

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
