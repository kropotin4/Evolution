package view.gui;

import control.Controller;
import control.ControllerClient;
import control.ControllerGUI;
import control.ControllerServer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class StartPane extends VBox {

    @FXML Button play_yourself;
    @FXML Button play_server;
    @FXML Button play_client;
    @FXML Button play_server_client;

    Stage primaryStage;

    Image lizard = new Image("/images/lizard_596_380.png");

    BackgroundSize backgroundSize;
    // new BackgroundImage(image, repeatX, repeatY, position, size)
    BackgroundImage backgroundImage;
    // new Background(images...)
    Background background;

    public StartPane(Stage primaryStage){
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

        this.primaryStage = primaryStage;
    }

    @FXML
    private void initialize(){
        this.setPrefSize(500, 300);

        //Controller controller = new Controller(2, 2);

        Image cursorOnButton = new Image("/images/lizard_tail.png");
        Image cursor = new Image("/images/lizard_cursor.png");

        play_yourself.setOnMouseClicked(event -> {
            Controller controller = new Controller(2, 2);
            ControllerGUI controllerGUI = new ControllerGUI(primaryStage, controller, 0);
            controllerGUI.startGame();
        });

        play_server.setOnMouseClicked(event -> {
//            Properties properties = new Properties();
//            FileInputStream propertiesFile = null;
//            int port = 0;
//            try {
//                propertiesFile = new FileInputStream("server.properties");
//                properties.load(propertiesFile);
//
//                port = Integer.parseInt(properties.getProperty("PORT"));
//
//
//            } catch (FileNotFoundException ex) {
//                System.err.println("Properties config file not found");
//            } catch (IOException ex) {
//                System.err.println("Error while reading file");
//            } finally {
//                try {
//                    propertiesFile.close();
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            }
            ControllerServer controllerServer = new ControllerServer(primaryStage,true);
            controllerServer.startServerSetting();

        });

        play_client.setOnMouseClicked(event -> {
            ControllerClient controllerClient = new ControllerClient(primaryStage);
            controllerClient.startClientSetting();
        });

        play_server_client.setOnMouseClicked(event -> {
            ControllerServer controllerServer = new ControllerServer(primaryStage,false);
            controllerServer.startServerSetting();
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
        // new BackgroundImage(image, repeatX, repeatY, position, size)
        backgroundImage = new BackgroundImage(lizard, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        // new Background(images...)
        background = new Background(backgroundImage);
        setBackground(background);

        setCursor(new ImageCursor(cursor,
                cursor.getWidth() / 2,
                cursor.getHeight() /2));


    }


    public void show(){
        Scene scene = new Scene(this, Color.TRANSPARENT);

        primaryStage.setMinWidth(this.getPrefWidth() + 20);
        primaryStage.setMinHeight(this.getPrefHeight() + 40);

        primaryStage.setTitle("Эволюция");

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
