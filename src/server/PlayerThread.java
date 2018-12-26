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
    boolean gameOn = false;

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
        System.out.println(getName() + ": sendMessage " + message.getMessageType());
        os.writeObject(message); // Отправляем сообщение серверу
        os.flush();
    }

    public void messageHandler(Message message){
        System.out.println(getName() + ": messageHandler");

        // Он не в игровой комнате.
        if(!inRoom){
            System.out.println(getName() + ": received message from free player");

            if(message instanceof CreateRoomMessage){
                controllerServer.createRoom((CreateRoomMessage) message);
            }
            else if(message instanceof EnterTheRoomMessage){
                playerNumber = controllerServer.enterTheRoom(this, ((EnterTheRoomMessage) message).getRoomId());
            }

            return;
        }
        else if(!gameOn){
            System.out.println(getName() + ": received message from room player");

            if(message instanceof ReadyToPlayMessage){
                controllerGameRoom.playerReadyToPlay(playerNumber);
            }

            return;
        }
        else {

            if (message.getMessageType() == MessageType.CHAT) {

                try {
                    controllerGameRoom.distribution(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            else {

                System.out.println("Current player: " + message.getTable().getPlayerTurn());

                String serverMessege = message.getMes();

            switch (controllerGameRoom.getCurrentPhase()){
                case GROWTH:

                    if(message.getTable().isEndMove())
                        serverMessege.concat("\nВ колоде больше нет карт - это последний ход.");


                    try {
                        controllerGameRoom.distribution(new ServerMessage(message.getTable(), serverMessege));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                case EATING:

                    if(message.getTable().isEndMove())
                        serverMessege.concat("\nПосле этой фазы будет определяться победитель");

                    try {
                        controllerGameRoom.distribution(new ServerMessage(message.getTable(), serverMessege));
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
    }

    public void setControllerGameRoom(ControllerGameRoom controller){
        this.controllerGameRoom = controller;
    }

    /////////////


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
