import javafx.application.Application;
import javafx.stage.Stage;
import view.gui.StartPane;

public class Main extends Application {
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        StartPane startPane = new StartPane(primaryStage);
        startPane.show();
    }
}