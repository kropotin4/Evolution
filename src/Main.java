import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import view.gui.MainPane;

public class Main extends Application {
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainPane mainPane = new MainPane(primaryStage);
        mainPane.show();
        /*Scene scene = new Scene(mainPane, Color.TRANSPARENT);

        primaryStage.setMinWidth(mainPane.getPrefWidth() + 20);
        primaryStage.setMinHeight(mainPane.getPrefHeight() + 40);

        primaryStage.setTitle("Эволюция");

        primaryStage.setScene(scene);
        primaryStage.show();*/
    }
}
