package server;

import server.message.*;

import java.io.IOException;
import java.io.ObjectInputStream;

public class PlayerListener extends Thread {

    Server server;

    PlayerThread playerThread;

    ObjectInputStream is;

    PlayerListener(Server server, PlayerThread playerThread, ObjectInputStream is) {
        super("PlayerListener " + playerThread.number);
        this.server = server;
        this.playerThread = playerThread;

        this.is = is;
    }

    @Override
    public void run() {
        System.out.println(getName() + " start");

        Object mesObject = null;

        try {
            mesObject = is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(getName() + " stop");
            return;
        }

        if(mesObject instanceof ConnectMessage){
            ConnectMessage connectMessage = (ConnectMessage) mesObject;
            System.out.println(getName() + ": received ConnectMessage (login: " + connectMessage.getLogin() + ")");
            playerThread.setLogin(connectMessage.getLogin());

            try {
                playerThread.sendMessage(playerThread.controllerServer.createClientInfoMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }

            playerThread.playerReady = true;

        }
        else{
            System.out.println(getName() + ": stop (ConnectMessage not received).");
            return;
        }


        System.out.println(getName() + ": start message reception cycle");
        while(true){

            try {
                mesObject = is.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(getName() + " stop.");
                return;
            }

            if(mesObject instanceof Message){
                System.out.println(getName() + ": received " + ((Message) mesObject).getMessageType());
                playerThread.messageHandler((Message) mesObject);
            }
            else {
                System.out.println(getName() + ": Unknown message");
            }

        }

    }


}
