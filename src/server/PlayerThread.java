package server;

import control.ControllerGameRoom;
import control.ControllerServer;
import server.message.*;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static model.Phase.*;

public class PlayerThread extends Thread {

    Server server;
    PlayerListener playerListener;

    ControllerServer controllerServer;
    ControllerGameRoom controllerGameRoom;

    Socket socket;

    ObjectInputStream is;
    ObjectOutputStream os;

    String login;
    final int number;
    int playerNumber;

    boolean playerReady = false;
    boolean inRoom = false;

    public PlayerThread(ControllerServer controllerServer, Server server, Socket socket, int number) throws IOException{
        super("PlayerThread " + number);
        this.server = server;
        this.socket = socket;
        this.number = number;
        //this.playerNumber = playerNumber;

        this.controllerServer = controllerServer;

        is = new ObjectInputStream(socket.getInputStream());
        os = new ObjectOutputStream(socket.getOutputStream());

        playerListener = new PlayerListener(server, this, is);
    }

    @Override
    public void run() {
        playerListener.start();
    }


    // Отправляем сообщение клиенту
    public void sendMessage(Message message) throws IOException {
        System.out.println(getName() + ": sendMessage " + message.toString());
        os.writeObject(message); // Отправляем сообщение серверу
        os.flush();
    }

    public void messageHandler(Message message){
        System.out.println(getName() + ": messageHandler");

        // Он не в игровой комнате
        if(!inRoom){
            System.out.println(getName() + ": received message from free player");

            controllerServer.messageHandler(message, this);

        }
        // В комнате
        else {
            System.out.println(getName() + ": received message from room player");

            controllerGameRoom.messageHandler(message, this);

        }

    }

    public void setControllerGameRoom(ControllerGameRoom controller){
        this.controllerGameRoom = controller;
    }

    /////////////

    public void setInRoom(boolean inRoom){
        this.inRoom = inRoom;
    }

    public ControllerGameRoom getControllerGameRoom() {
        return controllerGameRoom;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }
    public int getPlayerNumber() {
        return playerNumber;
    }

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
        //controllerServer.setLogin(login, playerNumber);

    }

    public boolean isConnected(){
        return socket.isConnected();
    }
}
