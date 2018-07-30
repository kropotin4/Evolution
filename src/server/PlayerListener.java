package server;

import model.Player;
import model.Table;
import server.message.*;

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



        Object message = null;
        while(true){

            try {
                message = is.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(player.getLogin() + "message is strange");
                continue;
            }

            if(player == table.getPlayers().get(table.getPlayerTurn())){

                if(message instanceof Message){
                    switch (table.getCurrentPhase()){
                        case GROWTH:
                            if(!(message instanceof GrowthMessage)) continue; // Надо еще что-то сделать!

                            //TODO: обработка действия игрока (GROWTH)

                            break;
                        case CALC_FODDER_BASE:
                            if(!(message instanceof CFBMessage)) continue; // Надо еще что-то сделать!

                            //TODO: обработка действия игрока (CALC_FODDER_BASE)

                            break;
                        case EATING:
                            if(!(message instanceof EatingMessage)) continue; // Надо еще что-то сделать!

                            //TODO: обработка действия игрока (EATING)

                            break;
                        default:
                            System.out.print("Its strange");
                            break;
                    }
                }
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
