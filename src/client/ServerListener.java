package client;

import control.ControllerClient;
import server.message.*;

import java.io.IOException;
import java.io.ObjectInputStream;

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
                System.err.println("ServerListener stop");
                return;
            }

            if(!isGameOn){

                if(mesObject instanceof ClientInfoMessage){
                    System.out.println("ServerListener: received ClientInfoMessage");
                    controller.updateClient((ClientInfoMessage) mesObject);
                }
                else if(mesObject instanceof RoomInfoMessage){
                    System.out.println("ServerListener: received RoomInfoMessage");
                    controller.updateRoom((RoomInfoMessage) mesObject);
                }
                else if(mesObject instanceof StartMessage){
                    System.out.println("ServerListener: received StartMessage");
                    controller.startGame((StartMessage) mesObject);
                    isGameOn = true;
                }
                else if(mesObject instanceof Message){
                    if(((Message) mesObject).getMessageType() == MessageType.CHAT){
                        System.out.println("Received Chat message (in room)");
                        controller.addMessageToChat(((ChatMessage) mesObject).login,((Message) mesObject).getMes());
                    }
                }
                else{
                    System.out.println("Received unknown message (in room)");
                }
            }
            else{
                if(mesObject instanceof Message){
                    if(((Message) mesObject).getMessageType() == MessageType.CHAT){
                        System.out.println("Received Chat message");
                        controller.getControllerGUI().addMessageToChat(((ChatMessage) mesObject).login,((Message) mesObject).getMes());
                    }
                    else if(((Message) mesObject).getMessageType() == MessageType.SERVER){
                        System.out.println("Received Server message");

                        System.out.println("Current player: " + ((Message) mesObject).getTable().getPlayerTurn());
                        controller.setTable(((Message) mesObject).getTable());
                        controller.getControllerGUI().addMessageToChat(((Message) mesObject).getMes());
                    }
                    else {
                        System.out.println("ServerListener: received " + ((Message) mesObject).getMessageType() + " message (empty else)");
                    }
                }
            }
        }

    }
}
