package server;

import model.Player;
import model.Table;
import server.message.Message;
import server.message.StartMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PlayerListener extends Thread {

    Socket socket;
    Table table;
    Player player;

    ObjectInputStream is;
    ObjectOutputStream os;

    PlayerListener(Socket socket, Table table) throws IOException {
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
            System.out.println("New player start message not received");
            return;
        }

        if(start instanceof StartMessage){
            StartMessage startMessage = (StartMessage) start;

            player = new Player(startMessage.getLogin());
        }
        else{
            System.out.println("New player start message not received");
            return;
        }



        Object message;
        while(true){

            try {
                message = is.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(player.getLogin() + "message is strange");
            }

            if(player == table.getPlayers().get(table.getPlayerTurn())){
                //TODO: обработка действия игрока
            }
            else{

                try {
                    os.writeObject(new Message()); // Не твоя очередь
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
