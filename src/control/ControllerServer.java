package control;

import javafx.application.Platform;
import javafx.stage.Stage;
import model.Phase;
import server.Server;
import server.message.Message;
import server.message.StartMessage;
import view.gui.ConnectionPane;
import view.gui.ServerPane;

import java.io.IOException;
import java.net.*;

public class ControllerServer {

    Stage primaryStage;

    int stage;

    Controller controller;
    Server server;

    ServerPane serverPane;
    ConnectionPane connectionPane;

    int playerNumber; // Меняется в doNextMove()

    int playerNum = 2; // Количество игроков
    InetAddress inetAddress;
    int port = 4444; //
    final int MIN_PORT_NUMBER = 1000;
    final int MAX_PORT_NUMBER = 65536;
    int quarterCardCount = 1; // количество четвертей карт

    int connectPlayer; // Сколько в данный момент подключено игроков

    public ControllerServer(Stage primaryStage, boolean onlyServer){
        this.primaryStage = primaryStage;
        this.controller = controller;

        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            inetAddress = socket.getLocalAddress();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

//        try {
//            inetAddress = InetAddress.getLocalHost();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }

        serverPane = new ServerPane(this, primaryStage);
        connectionPane = new ConnectionPane(this, primaryStage);
    }

    ////////////
    public void startServerSetting(){
        stage = 0;
        serverPane.show();
    }
    public void startConnecting(){
        stage = 1;
        server = new Server(this, port);
        connectionPane.show();
        server.start();
    }
    // Создаем Controller, рассылаем StartMessage
    public void startGame(){
        stage = 2;
        controller = new Controller(quarterCardCount, playerNum);

        try {
            server.startGameDistribution(controller.getTable());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playerConnect(){
        if(stage == 1) {
            connectionPane.playerConnect(connectPlayer);
            connectPlayer++;

            if(connectPlayer >= playerNum)
                connectionPane.setStartButtonDisable(false);
        }
    }
    ////////////

    public void doNextMove(){
        playerNumber = controller.doNextMove();
    }

    /////////////
    public void setPlayerNum(int playerNum){
        this.playerNum = playerNum;
    }
    public void setPort(int port){
        this.port = port;
    }
    public void setQuarterCardCount(int quarterCardCount){
        this.quarterCardCount = quarterCardCount;
    }

    public int getPlayersNumber(){
        return playerNum;
    }
    public int getPort(){
        return  port;
    }
    public InetAddress getInetAddress() {
        return inetAddress;
    }
    public int getQuarterCardCount() {
        return quarterCardCount;
    }
    ///////////////

    public void setLogin(String login, int playerNumber){
        if(stage == 1){
            Platform.runLater(() -> {
                // Update UI here.
                connectionPane.setLogin(login, playerNumber);
            });
        }
    }

    ///////////////

    public void addTraitToCreature(){
        /*controller.addTraitToCreature(
                creatureNode.getPlayerPane().getPlayerNumber(),
                creatureNode.getCreatureId(),
                cardNode.getCard(),
                isUp
        );*/

    }

    public int getPlayerCardsNumber(int playerNumber){
        return controller.getPlayerCardsNumber(playerNumber);
    }
//    public int getPlayersNumber(){
//        return controller.getPlayersNumber();
//    }
    public boolean isPlayersTurn(int playerNumber){
        return controller.isPlayersTurn(playerNumber);
    }

    public boolean availablePort(int port) {
        if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {
            //throw new IllegalArgumentException("Invalid start port: " + port);
            return false;
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }

    public Phase getCurrentPhase(){
        return controller.getCurrentPhase();
    }


    public void messageHandler(Message message, int playerNumber){

    }

}
