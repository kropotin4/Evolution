package view.gui;

import control.ControllerClient;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientPane extends AnchorPane {

    Stage primaryStage;

    ControllerClient controller;

    @FXML TextField login_text_field;
    @FXML TextField ip1_text_field;
    @FXML TextField ip2_text_field;
    @FXML TextField ip3_text_field;
    @FXML TextField ip4_text_field;
    @FXML CheckBox localhost_check_box;
    @FXML TextField port2_text_field;
    @FXML Button connect_button;
    @FXML VBox box;

    boolean ip1Pass = false;
    boolean ip2Pass = false;
    boolean ip3Pass = false;
    boolean ip4Pass = false;

    boolean portPass = false;
    boolean loginPass = false;

    Image grass = new Image("/images/grass_960_640.jpg");
    Image cursorOnButton = new Image("/images/lizard_tail.png");
    Image cursor = new Image("/images/lizard_cursor.png");

    BackgroundSize backgroundSize;
    BackgroundImage backgroundImage;
    Background background;

    public ClientPane(ControllerClient controller, Stage primaryStage){
        this.controller = controller;
        this.primaryStage = primaryStage;

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/ClientPane.fxml")
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

        box.setOnMouseEntered(event -> setCursor(new ImageCursor(cursorOnButton, cursorOnButton.getWidth() / 2,cursorOnButton.getHeight() / 2)));
        box.setOnMouseExited(event -> setCursor(new ImageCursor(cursor, cursor.getWidth() / 2,cursor.getHeight() / 2)));

        localhost_check_box.setOnMouseEntered(event -> setCursor(Cursor.HAND));
        localhost_check_box.setOnMouseExited(event -> setCursor(new ImageCursor(cursorOnButton, cursorOnButton.getWidth() / 2,cursorOnButton.getHeight() / 2)));

        // Обработка ввода логина (до 15 символов)
        login_text_field.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.length() <= 12){

                if(!newValue.isEmpty()){
                    controller.setLogin(login_text_field.getText());
                    loginPass = true;
                }
                else
                    loginPass = false;

                controlConnectButton();

                login_text_field.setText(newValue);
            }
            else login_text_field.setText(oldValue);
        });

        // Обработка ввода IP
        ip1_text_field.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("[0-9]*") && newValue.length() <= 3){

                if(!newValue.isEmpty()) {
                    ip1Pass = true;
                    if(Integer.parseInt(newValue) > 255){
                        ip1_text_field.setText(String.valueOf(255));
                        return;
                    }
                }
                else
                    ip1Pass = false;

                controlConnectButton();

                ip1_text_field.setText(newValue);
            }
            else ip1_text_field.setText(oldValue);
        });
        ip2_text_field.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("[0-9]*") && newValue.length() <= 3){

                if(!newValue.isEmpty()) {
                    ip2Pass = true;
                    if(Integer.parseInt(newValue) > 255){
                        ip2_text_field.setText(String.valueOf(255));
                        return;
                    }
                }
                else
                    ip2Pass = false;

                controlConnectButton();

                ip2_text_field.setText(newValue);
            }
            else ip2_text_field.setText(oldValue);
        });
        ip3_text_field.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("[0-9]*") && newValue.length() <= 3){

                if(!newValue.isEmpty()) {
                    ip3Pass = true;
                    if(Integer.parseInt(newValue) > 255){
                        ip3_text_field.setText(String.valueOf(255));
                        return;
                    }
                }
                else
                    ip3Pass = false;

                controlConnectButton();

                ip3_text_field.setText(newValue);
            }
            else ip3_text_field.setText(oldValue);
        });
        ip4_text_field.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("[0-9]*") && newValue.length() <= 3){

                if(!newValue.isEmpty()) {
                    ip4Pass = true;
                    if(Integer.parseInt(newValue) > 255){
                        ip4_text_field.setText(String.valueOf(255));
                        return;
                    }
                }
                else
                    ip4Pass = false;

                controlConnectButton();

                ip4_text_field.setText(newValue);
            }
            else ip4_text_field.setText(oldValue);
        });

        // CheckBox с localhost
        localhost_check_box.setOnMouseClicked(event -> {
            if(localhost_check_box.isSelected()){
                ip1_text_field.setDisable(true);
                ip2_text_field.setDisable(true);
                ip3_text_field.setDisable(true);
                ip4_text_field.setDisable(true);
            }
            else{
                ip1_text_field.setDisable(false);
                ip2_text_field.setDisable(false);
                ip3_text_field.setDisable(false);
                ip4_text_field.setDisable(false);
            }

            controlConnectButton();
        });

        // Обработка ввода порта
        port2_text_field.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("[0-9]*") && newValue.length() <= 6){
                port2_text_field.setText(newValue);

                if(!newValue.isEmpty()) {
                    portPass = Integer.parseInt(newValue) >= 1000 && Integer.parseInt(newValue) <= 65536;
                }
                controlConnectButton();
            }
            else port2_text_field.setText(oldValue);
        });

        connect_button.setOnMouseClicked(event -> {
            if(localhost_check_box.isSelected()){
                if(controller.connectToServer(
                        controller.getLogin(),
                        "localhost",
                        Integer.parseInt(port2_text_field.getText()))
                ){
                    connect_button.setText("Подключено");
                    connect_button.setDisable(true);
                }
                else{
                    connect_button.setText("Подключиться");
                    connect_button.setDisable(false);
                }
            }
            else{
                StringBuilder ip = new StringBuilder();
                ip.append(ip1_text_field.getText() + ".");
                ip.append(ip2_text_field.getText() + ".");
                ip.append(ip3_text_field.getText() + ".");
                ip.append(ip4_text_field.getText());

                System.out.println("ClientPane: ip = " + ip.toString());


                if(controller.connectToServer(
                        controller.getLogin(),
                        ip.toString(),
                        Integer.parseInt(port2_text_field.getText()))
                ){
                    connect_button.setText("Подключено");
                    connect_button.setDisable(true);
                }
                else{
                    connect_button.setText("Подключиться");
                    connect_button.setDisable(false);
                }
            }
        });

        backgroundSize = new BackgroundSize(grass.getWidth(), grass.getHeight(), false, false, true, true);
        // new BackgroundImage(image, repeatX, repeatY, position, size)
        backgroundImage = new BackgroundImage(grass, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        // new Background(images...)
        background = new Background(backgroundImage);

        setBackground(background);
    }

    // Смотрим на правельность заполнения полей
    private void controlConnectButton(){
        if(portPass && loginPass && ((ip1Pass && ip2Pass && ip3Pass && ip4Pass) || localhost_check_box.isSelected()))
            connect_button.setDisable(false);
        else
            connect_button.setDisable(true);
    }

    public void show(){
        Scene scene = new Scene(this, Color.TRANSPARENT);

        primaryStage.setMinWidth(this.getPrefWidth() + 20);
        primaryStage.setMinHeight(this.getPrefHeight() + 40);

        primaryStage.setTitle("Эволюция: клиент");

        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
