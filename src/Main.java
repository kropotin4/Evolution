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
        MainPane mainPane = new MainPane();
        Scene scene = new Scene(mainPane, Color.TRANSPARENT);

        primaryStage.setMinWidth(mainPane.getMinWidth() + 5);
        primaryStage.setMinHeight(mainPane.getMinHeight() + 5);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
