package control;

import client.Client;
import javafx.application.Platform;
import javafx.stage.Stage;
import model.Table;
import server.message.Message;
import server.message.StartMessage;
import view.gui.ClientPane;

import java.io.IOException;

public class ControllerClient {

    Stage primaryStage;

    int stage;

    Controller controller;
    ControllerGUI controllerGUI;

    Client client;

    ClientPane clientPane;

    String login;

    public ControllerClient(Stage primaryStage){
        this.primaryStage = primaryStage;
        this.controller = controller;
        stage = -1;
        //System.out.println("ControllerClient: ip = " + ip + " port = " + port);

        clientPane = new ClientPane(this, primaryStage);

        //this.client = new Client(this, ip, port);
    }

    public void setTable(Table table){
        controller.setTable(table);
        Platform.runLater(() -> {
            // Update UI here.
            controllerGUI.update();
        });
    }

    public ControllerGUI getControllerGUI(){
        return controllerGUI;
    }

    ///////////////

    public void startClientSetting(){
        stage = 0;
        clientPane.show();
    }
    public void startGame(StartMessage startMessage){

        controller = new Controller(startMessage.getTable());
        Platform.runLater(() -> {
            // Update UI here.
            controllerGUI = new ControllerGUI(primaryStage, controller, this, startMessage.getPlayerNumber());
            controllerGUI.startGame();

            controllerGUI.addMessageToChat(startMessage.getMes());
        });

    }

    public void connectToServer(String login, String ip, int port){
        client = new Client(this, login, ip, port);
        client.start();
    }

    //////////////////
    // Посылаем сообщение
    public void sendMessage(Message message){
        System.out.println("ControllerClient: sendMessage: " + message.getMessageType());
        try {
            client.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //////////////////

    public void setLogin(String login){
        this.login = login;
    }
    public String getLogin() {
        return login;
    }



}
