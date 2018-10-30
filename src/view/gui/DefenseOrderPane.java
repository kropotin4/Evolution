package view.gui;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import model.Trait;

import java.util.ArrayList;

public class DefenseOrderPane extends VBox {

    DefenseOrderPane self = this;

    Stage stage = new Stage();

    ArrayList<Trait> defenseTraits;

    int dragID = 0;

    public DefenseOrderPane(){
        this.setPrefSize(200, 300);
        this.setMinSize(200, 300);

        this.setSpacing(3);
        this.setAlignment(Pos.CENTER);

    setStage();

}

    public void setDefenseTraits(ArrayList<Trait> defenseTraits){
        this.defenseTraits = defenseTraits;

        int num = 1;
        for(Trait trait : defenseTraits){

            HBox traitBox = new HBox();
            traitBox.setPrefSize(this.getPrefWidth(), 20);
            traitBox.setAlignment(Pos.CENTER);
            traitBox.setPadding(new Insets(2, 3, 2, 3));

            Label numberLabel = new Label(Integer.toString(num));
            numberLabel.setPrefSize(traitBox.getPrefHeight(), traitBox.getPrefHeight());
            numberLabel.setFont(new Font(18));
            numberLabel.setAlignment(Pos.CENTER);
            numberLabel.setTextAlignment(TextAlignment.CENTER);

            Label traitLabel = new Label(trait.toString());
            traitLabel.setPrefSize(traitBox.getPrefWidth() - numberLabel.getPrefWidth(), traitBox.getPrefHeight());
            traitLabel.setFont(new Font(18));
            traitLabel.setAlignment(Pos.CENTER);
            traitLabel.setTextAlignment(TextAlignment.CENTER);

            traitBox.getChildren().addAll(numberLabel, traitLabel);

            this.getChildren().add(traitBox);

            ///region drag waste
            /*int finalNum = num;
            traitBox.setOnDragDetected(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    System.out.println("DragDetected: " + traitBox);
                    Point2D offSet = new Point2D(event.getX(), event.getY());



                    self.setOnDragOver(new EventHandler<DragEvent>() {
                        @Override
                        public void handle(DragEvent event) {
                            System.out.println("DragOver: " + traitBox);
                            event.acceptTransferModes(TransferMode.ANY);

                            Point2D localCoords = self.sceneToLocal(new Point2D(event.getSceneX(), event.getSceneY()));
                            traitBox.relocate(localCoords.getX() - offSet.getX(), localCoords.getY() - offSet.getY());

                            event.consume();
                        }
                    });

                    self.setOnDragDropped(new EventHandler<DragEvent>() {
                        @Override
                        public void handle(DragEvent event) {
                            System.out.println("DragDropped: " + traitBox);
                            self.setOnDragOver(null);
                            self.setOnDragDropped(null);

                            event.setDropCompleted(true);

                            event.consume();
                        }
                    });

                    Point2D localCoords = self.sceneToLocal(new Point2D(event.getSceneX(), event.getSceneY()));
                    traitBox.relocate(localCoords.getX() - offSet.getX(), localCoords.getY() - offSet.getY());

                    ClipboardContent content = new ClipboardContent();

                    content.put(new DataFormat(String.valueOf(dragID++)), String.valueOf(finalNum));
                    //content.put(DragContainer.DragNode, container);

                    ///Начало перемещения
                    startDragAndDrop (TransferMode.ANY).setContent(content);
                    event.consume();

                }
            });*/
            ///endregion

            ++num;
        }

    }

    void relocateToPoint (Point2D p, Point2D offSet) {
        Point2D localCoords = this.sceneToLocal(p);

        int x = (int) (localCoords.getX() - offSet.getX());
        int y = (int) (localCoords.getY() - offSet.getY());


        relocate (x, y);
    }


    public void show(){
        stage.show();
    }
    public void close(){
        stage.close();
    }

    private void setStage(){
        Scene scene = new Scene(this, Color.TRANSPARENT);

        stage.setTitle("Порядок защиты");
        stage.setHeight(this.getPrefHeight());
        stage.setWidth(this.getPrefWidth());
        stage.setMinHeight(this.getMinHeight());
        stage.setMinWidth(this.getMinWidth());
        stage.initStyle(StageStyle.UTILITY);

        stage.setAlwaysOnTop(true);

        stage.setScene(scene);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                close();
            }
        });
    }

}
