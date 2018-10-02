package view.gui;

import control.Controller;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class StartPane extends VBox {

    @FXML Button play_yourself;

    Stage primaryStage;

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

        Controller controller = new Controller(2, 2);


        play_yourself.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                MainPane mainPane = new MainPane(primaryStage, controller);
                mainPane.show();
            }
        });
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
