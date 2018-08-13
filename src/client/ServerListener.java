package client;

import server.message.Message;
import server.message.RequestMessage;
import server.message.StartMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerListener extends Thread {

    Socket server;
    Client client;

    ObjectOutputStream os;
    ObjectInputStream is;

    ServerListener(Socket server, Client client) throws IOException {
        this.server = server;

        os = new ObjectOutputStream(server.getOutputStream());
        is = new ObjectInputStream(server.getInputStream());
    }

    @Override
    public void run() {

        try {
            os.writeObject(new StartMessage(client.login));
        } catch (IOException e) {
            e.printStackTrace();
        }


        Object mesObject = null;
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
