package server;

import control.ControllerServer;
import model.Trait;
import server.message.*;
import server.message.action.*;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static model.Phase.*;

public class PlayerThread extends Thread {

    Server server;
    PlayerListener playerListener;

    ControllerServer controller;

    Socket socket;

    ObjectInputStream is;
    ObjectOutputStream os;

    String login;

    final int playerNumber;

    public PlayerThread(Server server, Socket socket, int playerNumber) throws IOException{
        super("PlayerThread " + playerNumber);
        this.server = server;
        this.socket = socket;
        this.playerNumber = playerNumber;

        controller = server.controller;

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
        System.out.println(getName() + ": sendMessage " + message.getMessageType());
        os.writeObject(message); // Отправляем сообщение серверу
    }

    public void messageHandler(Message message){

        if(message.getMessageType() == MessageType.CHAT){
            try {
                server.distribution(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{

            System.out.println("Current player: " + message.getTable().getPlayerTurn());

            switch (controller.getCurrentPhase()){
                case GROWTH:

                    try {
                        server.distribution(new ServerMessage(message.getTable(), message.getMes()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                case EATING:

                    try {
                        server.distribution(new ServerMessage(message.getTable(), message.getMes()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                default:
                    System.out.print("Its strange");
                    break;
            }
        }
    }


    /////////////


    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
        controller.setLogin(login, playerNumber);

    }
}
