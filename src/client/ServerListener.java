package client;

import control.Controller;
import control.ControllerClient;
import model.Table;
import server.message.Message;
import server.message.RequestMessage;
import server.message.StartMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerListener extends Thread {

    //Socket server;
    Client client;
    ControllerClient controller;

    private ObjectInputStream is;



    boolean isGameOn = false;

    ServerListener(ControllerClient controller, Client client, ObjectInputStream is) {
        //this.server = server;
        this.client = client;
        this.controller = controller;

        this.is = is;
    }

    @Override
    public void run() {
        System.out.println("ServerListener start");

        Object mesObject = null;

        while(true){

            try {
                mesObject = is.readObject();
            } catch (IOException | ClassNotFoundException e) {
                //TODO: что-то сделать
                continue;
            }

            if(!isGameOn){
                if(mesObject instanceof StartMessage){
                    System.out.println("ServerListener: received StartMessage");
                    controller.startGame((StartMessage) mesObject);
                }
            }
        }

    }
}
