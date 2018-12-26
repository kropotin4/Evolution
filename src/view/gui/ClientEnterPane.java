package view.gui;

import control.ControllerClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class ClientEnterPane extends AnchorPane {

    Stage primaryStage;

    ControllerClient controller;

    @FXML TextField login_text_field;
    @FXML TextField ip1_text_field;
    @FXML TextField ip2_text_field;
    @FXML TextField ip3_text_field;
    @FXML TextField ip4_text_field;
    @FXML RadioButton localhost_check_box;
    @FXML TextField port2_text_field;
    @FXML Button connect_button;
    @FXML Button  back_button_cp;
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

    public ClientEnterPane(ControllerClient controller, Stage primaryStage){
        this.controller = controller;
        this.primaryStage = primaryStage;

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/ClientEnterPane.fxml")
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
        ///region Cursor
        setCursor(new ImageCursor(cursor, cursor.getWidth() / 2,cursor.getHeight() / 2));

        back_button_cp.setOnMouseEntered(event -> setCursor(new ImageCursor(cursorOnButton, cursorOnButton.getWidth() / 2,cursorOnButton.getHeight() / 2)));
        back_button_cp.setOnMouseExited(event -> setCursor(new ImageCursor(cursor, cursor.getWidth() / 2,cursor.getHeight() / 2)));

        connect_button.setOnMouseEntered(event -> setCursor(new ImageCursor(cursorOnButton, cursorOnButton.getWidth() / 2,cursorOnButton.getHeight() / 2)));
        connect_button.setOnMouseExited(event -> setCursor(new ImageCursor(cursor, cursor.getWidth() / 2,cursor.getHeight() / 2)));

        localhost_check_box.setOnMouseEntered(event -> setCursor(Cursor.HAND));
        localhost_check_box.setOnMouseExited(event -> setCursor(new ImageCursor(cursor, cursor.getWidth() / 2,cursor.getHeight() / 2)));
        ///endregion

        connect_button.setTooltip(new Tooltip("Подключиться к серверу"));
        connect_button.getTooltip().setShowDelay(Duration.ZERO);
        back_button_cp.setTooltip(new Tooltip("Возврат в главное меню"));
        back_button_cp.getTooltip().setShowDelay(Duration.ZERO);

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

        // RadioButton с localhost
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

        ///region alert
        class modAlert extends Alert {

            private Thread thread;
            private javafx.stage.Window window;

            public modAlert(AlertType alertType, String contentText, ButtonType... buttons) {
                super(alertType, contentText, buttons);
                setCursor(new ImageCursor(cursor, cursor.getWidth() / 2,cursor.getHeight() / 2));
                setHeaderText("Произошла ошибка");
                setTitle("Ошибка");
                window = getDialogPane().getScene().getWindow();
                window.getScene().setCursor(new ImageCursor(cursor, cursor.getWidth() / 2,cursor.getHeight() / 2));
                System.out.println(window);
                thread = new Thread(() -> {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ignored) {
                    }
                    long startTime = System.currentTimeMillis();
                    while (System.currentTimeMillis() - startTime < 1000) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException ignored) { }
                        Platform.runLater(() -> window.setOpacity(Math.max(0, window.getOpacity() - .05)));
                    }
                    Platform.runLater(this::close);
                });
            }

            void Show() {
                show();
                thread.start();
            }
        }
        ///endregion
        connect_button.setOnMousePressed(event -> setCursor(Cursor.WAIT));
        connect_button.setOnMouseClicked(event -> {
            setCursor(Cursor.WAIT);
            if(localhost_check_box.isSelected()){
                if(controller.connectToServer(
                        controller.getLogin(),
                        "localhost",
                        Integer.parseInt(port2_text_field.getText()))
                ){
                    connect_button.setText("Подключено");
                    connect_button.setDisable(true);
                    controller.startClient();
                }
                else{
                    connect_button.setTooltip(new Tooltip("Произошла ошибка. Проверьте, включен ли сервер"));
                    new modAlert(Alert.AlertType.INFORMATION, "Проверьте, включен ли сервер, а также правильно ли указан порт").Show();
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

                System.out.println("ClientEnterPane: ip = " + ip.toString());


                if(controller.connectToServer(
                        controller.getLogin(),
                        ip.toString(),
                        Integer.parseInt(port2_text_field.getText()))
                ){
                    connect_button.setText("Подключено");
                    connect_button.setDisable(true);
                }
                else{
                    connect_button.setTooltip(new Tooltip("Произошла ошибка. Проверьте правильность введённых данных"));
                    new modAlert(Alert.AlertType.INFORMATION, "Проверьте, правильно ли указаны все данные (ip-адрес, порт)").Show();
                    connect_button.setText("Подключиться");
                    connect_button.setDisable(false);
                }
            }
        });

        back_button_cp.setOnMouseClicked(event -> controller.getStartPane().show());

        backgroundSize = new BackgroundSize(grass.getWidth(), grass.getHeight(), false, false, true, true);
        backgroundImage = new BackgroundImage(grass, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
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
