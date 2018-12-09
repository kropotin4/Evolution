package view.gui;

import control.ControllerGUI;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    ControllerGUI controller;

    ArrayList<Trait> defenseTraits;

    Image upImage = new Image("/images/up2.png", 40, 40, true, true);
    Image upImage2 = new Image("/images/up2_2.png", 35, 35, true, true);
    Image downImage = new Image("/images/down.png", 40, 40, true, true);
    Image downImage2 = new Image("/images/down_2.png",35, 35, true, true);

    public DefenseOrderPane(ControllerGUI controller){
        this.setPrefSize(300, 300);
        this.setMinSize(getPrefWidth(), getPrefHeight());

        //this.setSpacing(20);
        this.setAlignment(Pos.CENTER);

        this.controller = controller;

        setStage();
    }

    public void setDefenseTraits(ArrayList<Trait> defenseTraits){
        this.defenseTraits = defenseTraits;
        update();
    }

    private void update(){
        int num = 1;

        this.setPrefSize(300, defenseTraits.size() * (80 + 5) + 60);
        updateStage();
        this.getChildren().clear();
        for(Trait trait : defenseTraits){

            HBox traitBox = new HBox();
            traitBox.setPrefSize(this.getPrefWidth(), 80);
            traitBox.setMinSize(traitBox.getPrefWidth(), traitBox.getPrefHeight());
            traitBox.setMaxSize(traitBox.getPrefWidth(), traitBox.getPrefHeight());
            traitBox.setAlignment(Pos.CENTER);
            traitBox.setPadding(new Insets(2, 3, 2, 3));

            Label numberLabel = new Label(Integer.toString(num));
            numberLabel.setPrefSize(traitBox.getPrefHeight(), traitBox.getPrefHeight());
            numberLabel.setMinSize(traitBox.getPrefHeight(), traitBox.getPrefHeight());
            numberLabel.setFont(new Font(15));
            numberLabel.setAlignment(Pos.CENTER);
            numberLabel.setTextAlignment(TextAlignment.CENTER);

            Label traitLabel = new Label(trait.toString());
            traitLabel.setPrefSize(traitBox.getPrefWidth() - numberLabel.getPrefWidth() - 100, traitBox.getPrefHeight());
            traitLabel.setMinSize(traitBox.getPrefWidth() - numberLabel.getPrefWidth() - 100, traitBox.getPrefHeight());
            traitLabel.setFont(new Font(15));
            traitLabel.setAlignment(Pos.CENTER);
            traitLabel.setTextAlignment(TextAlignment.CENTER);

            traitBox.getChildren().addAll(numberLabel, traitLabel);

            if(num == 1){
                ImageView downButton = new ImageView(downImage);

                downButton.setOnMouseClicked(event -> {
                    Trait t = defenseTraits.get(0);
                    defenseTraits.set(0, defenseTraits.get(1));
                    defenseTraits.set(1, t);
                    update();
                });

                downButton.setOnMousePressed(event -> {
                    downButton.setImage(downImage2);
                });
                downButton.setOnMouseReleased(event -> {
                    downButton.setImage(downImage);
                });

                traitBox.getChildren().add(downButton);
            }
            else if(num == defenseTraits.size()){
                traitBox.setStyle("-fx-border-width: 1.5 0 0 0; -fx-border-color: green");

                ImageView upButton = new ImageView(upImage);
                //upButton.setFont(new Font(12));
                //upButton.setPrefHeight(traitBox.getPrefHeight());
                //upButton.setPrefWidth(60);

                upButton.setOnMouseClicked(event -> {
                    Trait t = defenseTraits.get(defenseTraits.size() - 2);
                    defenseTraits.set(defenseTraits.size() - 2, defenseTraits.get(defenseTraits.size() - 1));
                    defenseTraits.set(defenseTraits.size() - 1, t);
                    update();
                });

                upButton.setOnMousePressed(event -> {
                    upButton.setImage(upImage2);
                });
                upButton.setOnMouseReleased(event -> {
                    upButton.setImage(upImage);
                });

                traitBox.getChildren().add(upButton);
            }
            else{
                traitBox.setStyle("-fx-border-width: 1.5 0 0 0; -fx-border-color: green");

                HBox buttonBox = new HBox();

                buttonBox.setPrefHeight(traitBox.getPrefHeight());
                buttonBox.setMinHeight(traitBox.getPrefHeight());
                buttonBox.setMaxHeight(traitBox.getPrefHeight());
                buttonBox.setAlignment(Pos.CENTER);

                ImageView upButton = new ImageView(upImage);
                ImageView downButton = new ImageView(downImage);
                upButton.setFitHeight(40);
                downButton.setFitHeight(40);


                //upButton.setFont(new Font(12));
                //downButton.setFont(new Font(12));
                //upButton.setPrefWidth(60);
                //downButton.setPrefWidth(60);

                upButton.setOnMouseClicked(event -> {
                    int in = Integer.parseInt(((Label)(((HBox)upButton.getParent().getParent()).getChildren().get(0))).getText());
                    --in;
                    Trait t = defenseTraits.get(in);
                    defenseTraits.set(in, defenseTraits.get(in - 1));
                    defenseTraits.set(in - 1, t);
                    update();
                });
                upButton.setOnMousePressed(event -> {
                    upButton.setImage(upImage2);
                });
                upButton.setOnMouseReleased(event -> {
                    upButton.setImage(upImage);
                });

                downButton.setOnMouseClicked(event -> {
                    int in = Integer.parseInt(((Label)(((HBox)upButton.getParent().getParent()).getChildren().get(0))).getText());
                    --in;
                    Trait t = defenseTraits.get(in);
                    defenseTraits.set(in, defenseTraits.get(in + 1));
                    defenseTraits.set(in + 1, t);
                    update();
                });
                downButton.setOnMousePressed(event -> {
                    downButton.setImage(downImage2);
                });
                downButton.setOnMouseReleased(event -> {
                    downButton.setImage(downImage);
                });

                buttonBox.getChildren().addAll(upButton, downButton);

                traitBox.getChildren().add(buttonBox);
            }


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

        Button okButton = new Button("OK");

        okButton.setOnMouseClicked(event -> {
            close();
        });

        this.getChildren().add(okButton);
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
    private void updateStage(){
        stage.setHeight(this.getPrefHeight());
        stage.setWidth(this.getPrefWidth());
        stage.setMinHeight(this.getMinHeight());
        stage.setMinWidth(this.getMinWidth());
        stage.setMaxHeight(this.getPrefHeight());
        stage.setMaxWidth(this.getPrefWidth());
    }

}
