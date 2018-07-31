package server;

import model.Player;
import model.Table;
import server.message.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PlayerListener extends Thread {

    Thread server;

    Socket socket;
    Table table;
    Player player;

    ObjectInputStream is;
    ObjectOutputStream os;

    PlayerListener(Thread server, Socket socket, Table table) throws IOException {
        this.server = server;
        this.socket = socket;

        os = new ObjectOutputStream(socket.getOutputStream());
        is = new ObjectInputStream(socket.getInputStream());

        this.table = table;
    }

    @Override
    public void run() {
        Object start;

        try {
            start = is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("New player start StartMessage not received");
            return;
        }

        if(start instanceof StartMessage){
            StartMessage startMessage = (StartMessage) start;

            player = new Player(startMessage.getLogin());
        }
        else{
            System.out.println("New player start StartMessage not received");
            return;
        }



        Object mesObject = null;
        while(true){

            try {
                mesObject = is.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(player.getLogin() + "mesObject is strange");
                continue;
            }

            if(mesObject instanceof Message){

                Message message = (Message) mesObject;

                if(player == table.getPlayers().get(table.getPlayerTurn())){

                    switch (table.getCurrentPhase()){
                        case GROWTH:
                            if(message.getMessageType() != MessageType.GROWTH) continue; // Надо еще что-то сделать!

                            //TODO: обработка действия игрока (GROWTH) + notify

                            break;
                        case CALC_FODDER_BASE:
                            if(message.getMessageType() != MessageType.CFB) continue; // Надо еще что-то сделать!

                            //TODO: обработка действия игрока (CALC_FODDER_BASE) + notify

                            break;
                        case EATING:
                            if(message.getMessageType() != MessageType.EATING) continue; // Надо еще что-то сделать!

                            //TODO: обработка действия игрока (EATING) + notify

                            break;
                        default:
                            System.out.print("Its strange");
                            break;
                    }
                }
                else{

                    if(message.getMessageType() != MessageType.SPECIAL){
                        //TODO: обработка действия игрока вне хода (Пиратство и т.д.)
                    }
                    else {

                        try {
                            os.writeObject(new ErrorMessage(1)); // Не твоя очередь
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
            }

            }

        }

    }
}
