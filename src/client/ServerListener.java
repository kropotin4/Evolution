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

    Object mesObject = null;

    ServerListener(ControllerClient controller, Client client, ObjectInputStream is) {
        //this.server = server;
        this.client = client;
        this.controller = controller;

        this.is = is;
    }

    @Override
    public void run() {


        try {
            mesObject = is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            //TODO: что-то сделать
        }

        if(!(mesObject instanceof Table))
            throw new RuntimeException("...");

        //controller.initialize((Table) mesObject);

        while(true){

            try {
                mesObject = is.readObject();
            } catch (IOException | ClassNotFoundException e) {
                //TODO: что-то сделать
                continue;
            }

            if(mesObject instanceof Message){

                Message message = (Message) mesObject;

                if(message instanceof RequestMessage){

                    RequestMessage requestMessage = (RequestMessage) message;

                    switch (requestMessage.getMessageType()){
                        case GROWTH:

                            //TODO: Выбор игроком что делать. Передача управления интерфейсу.

                            break;
                        case EATING:

                            //TODO: Выбор игроком что делать. Передача управления интерфейсу.

                            break;
                    }

                }

            }
        }

    }
}
