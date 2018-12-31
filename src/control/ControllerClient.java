package control;

import client.Client;
import javafx.application.Platform;
import javafx.stage.Stage;
import model.Table;
import server.GamingRoomInfo;
import server.message.*;
import view.gui.ClientEnterPane;
import view.gui.ClientPane;
import view.gui.RoomPane;
import view.gui.StartPane;

import java.io.IOException;
import java.util.ArrayList;

public class ControllerClient {

    Stage primaryStage;
    StartPane startPane;

    int stage;

    Controller controller;
    ControllerGUI controllerGUI;

    Client client;

    ClientEnterPane clientEnterPane;
    ClientPane clientPane;
    RoomPane roomPane;

    String login;

    String serverIP;
    int serverPort;
    int roomCapacity;
    ArrayList<GamingRoomInfo> gamingRoomInfo;

    public ControllerClient(Stage primaryStage, StartPane startPane){
        this.primaryStage = primaryStage;
        this.controller = controller;
        this.startPane = startPane;
        stage = -1;
        //System.out.println("ControllerClient: ip = " + ip + " port = " + port);

        clientEnterPane = new ClientEnterPane(this, primaryStage);
        clientPane = new ClientPane(this, primaryStage);
        roomPane = new RoomPane(this, primaryStage);
        //this.client = new Client(this, ip, port);
    }

    public void setTable(Table table){
        controller.setTable(table);
        Platform.runLater(() -> {
            // Update UI here.
            controllerGUI.update();
        });
    }
    public Table getTable(){
        return controller.getTable();
    }

    public ControllerGUI getControllerGUI(){
        return controllerGUI;
    }
    public StartPane getStartPane(){
        return startPane;
    }
    ///////////////

    public void startClientSetting(){
        stage = 0;
        clientEnterPane.show();
    }
    public void startClient(){
        stage = 1;
        clientPane.show();
    }
    public void startRoom(){
        stage = 2;
        Platform.runLater(() -> {
            roomPane.show();
        });
    }
    public void readyToPlay(){
        stage = 3;
        sendMessage(new ReadyToPlayMessage());
    }
    public void startGame(StartMessage startMessage){
        stage = 4;
        controller = new Controller(startMessage.getTable());
        Platform.runLater(() -> {
            // Update UI here.
            controllerGUI = new ControllerGUI(primaryStage, controller, this, startMessage.getPlayerNumber());
            controllerGUI.startGame();

            controllerGUI.addMessageToChat(startMessage.getMes());
        });

    }

    public void updateClient(ClientInfoMessage clientInfoMessage){
        serverIP = clientInfoMessage.getServerIP();
        serverPort = clientInfoMessage.getServerPort();
        roomCapacity = clientInfoMessage.getRoomCapacity();
        gamingRoomInfo = clientInfoMessage.getGamingRoomInfo();

        Platform.runLater(() -> clientPane.update());

    }
    public void updateRoom(RoomInfoMessage roomInfoMessage){
        Platform.runLater(() -> roomPane.update(roomInfoMessage));
    }
    /**
     * @param login name of player
     * @param ip server IP address
     * @param port server port
     * @return true if successfully connected to server
     */
    public boolean connectToServer(String login, String ip, int port){
        try {
            client = new Client(this, login, ip, port);
            client.start();
            return true;
        }
        catch (Exception e){
            System.out.println("ControllerClient: connect to server failed");
            return false;
        }
    }

    public void enterTheRoom(int roomId){
        sendMessage(new EnterTheRoomMessage(roomId));
        startRoom();
    }
    //////////////////
    // Посылаем сообщение
    public void sendMessage(Message message){
        System.out.println("ControllerClient: sendMessage: " + message.getMessageType());
        //System.out.println("\n" + controller.getTable() + "\n");
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

    public String getServerIP() {
        return serverIP;
    }
    public int getServerPort() {
        return serverPort;
    }
    public int getRoomCapacity() {
        return roomCapacity;
    }
    public ArrayList<GamingRoomInfo> getGamingRoomInfo() {
        return gamingRoomInfo;
    }

    public int getStage() {
        return stage;
    }
}
