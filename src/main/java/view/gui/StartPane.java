package view.gui;

import control.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class StartPane extends VBox {

    Stage primaryStage;
    Scene scene;

    @FXML Button play_yourself;
    @FXML Button play_server;
    @FXML Button play_client;
    @FXML Button play_server_client;



    Image lizard = new Image("/images/lizard_596_380.png");
    Image icon = new Image("/images/icon.png");

    BackgroundSize backgroundSize;
    BackgroundImage backgroundImage;
    Background background;

    public StartPane(Stage primaryStage){
        this.primaryStage = primaryStage;

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/StartPane.fxml")
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
        primaryStage.getIcons().add(icon);

        //this.setPrefSize(500, 300);

        play_yourself.setOnAction(event -> {
            ControllerGUI controllerGUI = new ControllerGUI(primaryStage, 0);
            controllerGUI.startGameSetting();
        });
        play_server.setOnAction(event -> {
            ControllerServer controllerServer = new ControllerServer(primaryStage, this);
            controllerServer.startServerSetting();
        });
        play_client.setOnAction(event -> {
            ControllerClient controllerClient = new ControllerClient(primaryStage, this);
            controllerClient.startClientSetting();
        });
        play_server_client.setOnAction(event -> {
//            ControllerGameRoom controllerGameRoom = new ControllerGameRoom(primaryStage,false);
//            controllerGameRoom.startServerSetting();
        });

        ///region cursor
        Image lizardTailImage = new Image("/images/lizard_tail.png");
        Image lizardImage = new Image("/images/lizard_cursor.png");
        Cursor lizardCursor = new ImageCursor(lizardImage, lizardImage.getWidth() / 2, lizardImage.getHeight() / 2);
        Cursor lizardTailCursor = new ImageCursor(lizardTailImage, lizardTailImage.getWidth() / 2, lizardTailImage.getHeight() / 2);
        setCursor(lizardCursor);
        play_yourself.setOnMouseEntered(event -> setCursor(lizardTailCursor));
        play_server.setOnMouseEntered(event -> setCursor(lizardTailCursor));
        play_client.setOnMouseEntered(event -> setCursor(lizardTailCursor));
        play_server_client.setOnMouseEntered(event -> setCursor(lizardTailCursor));

        play_yourself.setOnMouseExited(event -> setCursor(lizardCursor));
        play_server.setOnMouseExited(event -> setCursor(lizardCursor));
        play_client.setOnMouseExited(event -> setCursor(lizardCursor));
        play_server_client.setOnMouseExited(event -> setCursor(lizardCursor));
        ///endregion

        backgroundSize = new BackgroundSize(lizard.getWidth(), lizard.getHeight(), false, false, true, true);
        backgroundImage = new BackgroundImage(lizard, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        background = new Background(backgroundImage);
        setBackground(background);
    }


    public void show(){
        //Scene scene = new Scene(this, Color.TRANSPARENT);
        ///region accelerators
        play_yourself.getScene().getAccelerators().put(
                new KeyCodeCombination(KeyCode.A, KeyCombination.SHORTCUT_DOWN),
                () -> play_yourself.fire());
        play_server.getScene().getAccelerators().put(
                new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN),
                () -> play_server.fire());
        play_client.getScene().getAccelerators().put(
                new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN),
                () -> play_client.fire());
        play_server_client.getScene().getAccelerators().put(
                new KeyCodeCombination(KeyCode.X, KeyCombination.SHORTCUT_DOWN),
                () -> play_server_client.fire());
        ///endregion

        primaryStage.setMinWidth(this.getPrefWidth() + 20);
        primaryStage.setMinHeight(this.getPrefHeight() + 40);

        primaryStage.setTitle("Эволюция");

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
