package control;

import javafx.application.Platform;
import javafx.stage.Stage;
import server.GamingRoomInfo;
import server.PlayerThread;
import server.Server;
import server.message.*;
import view.gui.ServerPane;
import view.gui.ServerSettingPane;
import view.gui.StartPane;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class ControllerServer {

    Stage primaryStage;
    StartPane startPane;

    Server server;

    ServerSettingPane serverSettingPane;
    ServerPane serverPane;

    int stage = 0;

    InetAddress inetAddress;
    int port = 4444; //
    final int MIN_PORT_NUMBER = 1000;
    final int MAX_PORT_NUMBER = 65536;
    int maxRoom = 4;

    public ControllerServer(Stage primaryStage, StartPane startPane){
        this.primaryStage = primaryStage;
        this.startPane = startPane;

        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            inetAddress = socket.getLocalAddress();
            socket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        serverSettingPane = new ServerSettingPane(this, primaryStage);
        serverPane = new ServerPane(this, primaryStage);
    }

    public void startServerSetting(){
        stage = 0;
        serverSettingPane.show();
    }
    public void startServer(){
        stage = 1;
        server = new Server(this);
        server.start();
        serverPane.show();
        serverPane.update();
    }

    public void stopServer(){

    }

    public void createRoom(CreateRoomMessage createRoomMessage){
        System.out.println("ControllerServer: createRoom " + createRoomMessage);
        server.addRoom(
                createRoomMessage.getRoomName(),
                createRoomMessage.getPlayerNumber(),
                createRoomMessage.getQuarterCardCount()
        );
        Platform.runLater(() ->{
            serverPane.update();
        });

    }
    public int enterTheRoom(PlayerThread playerThread, int roomId){
        return server.enterTheRoom(playerThread, roomId);
    }
    ////////////////////////

    synchronized public void messageHandler(Message message, PlayerThread playerThread){

        if(message instanceof ChatMessage){
            try {
                server.distributionForFreePlayers(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(message instanceof CreateRoomMessage){
            createRoom((CreateRoomMessage) message);
        }
        else if(message instanceof EnterTheRoomMessage){
            int playerNumber = enterTheRoom(playerThread, ((EnterTheRoomMessage) message).getRoomId());
            playerThread.setPlayerNumber(playerNumber);
            playerThread.setInRoom(true);

            try {
                playerThread.getControllerGameRoom().distribution(playerThread.getControllerGameRoom().createRoomInfoMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            server.distribution(createClientInfoMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Platform.runLater(()->{
            serverPane.update();
        });

    }

    public ClientInfoMessage createClientInfoMessage(){
        return new ClientInfoMessage(
                inetAddress.getHostAddress(),
                port,
                maxRoom,
                getRoomsInfo());
    }

    ///////////////////////

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
    public void setPort(int port) {
        this.port = port;
    }
    public int getPort() {
        return port;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }
    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    public void setMaxRoom(int maxRoom) {
        this.maxRoom = maxRoom;
    }
    public int getMaxRoom() {
        return maxRoom;
    }

    public int getPlayerNumber(){
        return server.getPlayerNumber();
    }
    public ArrayList<GamingRoomInfo> getRoomsInfo(){
        return server.getRoomsInfo();
    }


    public StartPane getStartPane(){
        return startPane;
    }
    //////////////////
}
