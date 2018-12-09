package server;

import control.Controller;
import control.ControllerServer;
import model.*;
import server.message.*;
import server.message.action.ActionMessage;
import server.message.action.GrazingAction;
import server.message.action.PirateAction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PlayerListener extends Thread {

    Server server;

    PlayerThread playerThread;

    String login;
    int playerNumber;

    ObjectInputStream is;
    ObjectOutputStream os;

    Message message;

    PlayerListener(Server server, PlayerThread playerThread, ObjectInputStream is) {
        super("PlayerListener " + playerThread.playerNumber);
        this.server = server;
        this.playerThread = playerThread;

        this.is = is;
    }

    @Override
    public void run() {

        Object mesObject = null;
        while(true){

            try {
                mesObject = is.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(getName() + " stop.");

                return;
            }

            if(mesObject instanceof Message){
                playerThread.messageHandler((Message) mesObject);
            }
            else {
                System.out.println("");
            }

        }

    }


}
