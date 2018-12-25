package view.gui;

import control.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
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

    BackgroundSize backgroundSize;
    // new BackgroundImage(image, repeatX, repeatY, position, size)
    BackgroundImage backgroundImage;
    // new Background(images...)
    Background background;

    public StartPane(Stage primaryStage){
        this.primaryStage = primaryStage;

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/StartPane.fxml")
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
        this.setPrefSize(500, 300);



        Image cursorOnButton = new Image("/images/lizard_tail.png");
        Image cursor = new Image("/images/lizard_cursor.png");

        play_yourself.setOnMouseClicked(event -> {
            Controller controller = new Controller(2, 2);
            ControllerGUI controllerGUI = new ControllerGUI(primaryStage, controller, 0);
            controllerGUI.startGame();
        });

        play_server.setOnMouseClicked(event -> {
            ControllerServer controllerServer = new ControllerServer(primaryStage);
            controllerServer.startServerSetting();
        });

        play_client.setOnMouseClicked(event -> {
            ControllerClient controllerClient = new ControllerClient(primaryStage, this);
            controllerClient.startClientSetting();
        });

        play_server_client.setOnMouseClicked(event -> {
//            ControllerGameRoom controllerGameRoom = new ControllerGameRoom(primaryStage,false);
//            controllerGameRoom.startServerSetting();
        });

        play_yourself.setOnMouseEntered(event -> setCursor(new ImageCursor(cursorOnButton, cursorOnButton.getWidth() / 2,cursorOnButton.getHeight() / 2)));
        play_server.setOnMouseEntered(event -> setCursor(new ImageCursor(cursorOnButton, cursorOnButton.getWidth() / 2,cursorOnButton.getHeight() / 2)));
        play_client.setOnMouseEntered(event -> setCursor(new ImageCursor(cursorOnButton, cursorOnButton.getWidth() / 2,cursorOnButton.getHeight() / 2)));
        play_server_client.setOnMouseEntered(event -> setCursor(new ImageCursor(cursorOnButton, cursorOnButton.getWidth() / 2,cursorOnButton.getHeight() / 2)));

        play_yourself.setOnMouseExited(event -> setCursor(new ImageCursor(cursor, cursor.getWidth() / 2,cursor.getHeight() / 2)));
        play_server.setOnMouseExited(event -> setCursor(new ImageCursor(cursor, cursor.getWidth() / 2,cursor.getHeight() / 2)));
        play_client.setOnMouseExited(event -> setCursor(new ImageCursor(cursor, cursor.getWidth() / 2,cursor.getHeight() / 2)));
        play_server_client.setOnMouseExited(event -> setCursor(new ImageCursor(cursor, cursor.getWidth() / 2,cursor.getHeight() / 2)));

        backgroundSize = new BackgroundSize(lizard.getWidth(), lizard.getHeight(), false, false, true, true);
        backgroundImage = new BackgroundImage(lizard, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        background = new Background(backgroundImage);
        setBackground(background);

        setCursor(new ImageCursor(cursor,
                cursor.getWidth() / 2,
                cursor.getHeight() /2));


    }


    public void show(){
        //Scene scene = new Scene(this, Color.TRANSPARENT);

        primaryStage.setMinWidth(this.getPrefWidth() + 20);
        primaryStage.setMinHeight(this.getPrefHeight() + 40);

        primaryStage.setTitle("Эволюция");

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
