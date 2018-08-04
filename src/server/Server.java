package server;

import model.Player;
import model.Table;
import server.message.EatingMessage;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;

public class Server extends Thread{

    Table table;

    ServerSocket serverSocket;

    ArrayList<PlayerListener> playerListeners = new ArrayList<>();

    Properties properties;


    EatingMessage eatingMessage;

    Server(Table table) throws IOException {

        properties = new Properties();
        properties.load(new FileInputStream("server_properties"));

        serverSocket = new ServerSocket(Integer.parseInt(properties.getProperty("SERVER_PORT")));

        this.table = table;
    }


    @Override
    public void run() {
        beginPlay();

        middlePlay();

        endPlay();
    }

    public void beginPlay(){

        for(int i = 0; i < table.getPlayerCount(); ++i) {

            try {
                Socket newPlayer = serverSocket.accept();

                playerListeners.add(new PlayerListener(this, newPlayer, table));
                playerListeners.get(playerListeners.size() - 1).start();

            } catch (IOException e) {
                System.out.println("Connect with player has failed");
            }

        }
    }

    public boolean middlePlay(){ // Забавное название ?

        boolean end = false;
        while (table.getFodder() != 0){

            for(int phase = 0; phase < 5; ++phase){ // Фазы

                switch (phase) {
                    case 0:

                        ///region GROWTH
                        for (Player player : table.getPlayers()) { // Игроки

                            //TODO: что-то им отправляем -> какие действия мы от игрока ждем

                            try {
                                wait(); // Ждем пока не получим ответ
                            } catch (InterruptedException e) {
                                System.out.println("Server: Problems with wait");
                            }

                            // Массовая рассылка результата
                        }

                        break;
                        ///endregion

                    case 1:

                        ///region CALC_FODDER_BASE
                        for (Player player : table.getPlayers()) { // Игроки

                            //TODO: что-то им отправляем -> какие действия мы от игрока ждем

                            try {
                                wait(); // Ждем пока не получим ответ
                            } catch (InterruptedException e) {
                                System.out.println("Server: Problems with wait");
                            }

                            // Массовая рассылка результата
                        }

                        break;
                        ///endregion

                    case 2:

                        ///region EATING
                        for (Player player : table.getPlayers()) { // Игроки

                            //TODO: что-то им отправляем -> какие действия мы от игрока ждем

                            try {
                                wait(); // Ждем пока не получим ответ
                            } catch (InterruptedException e) {
                                System.out.println("Server: wait() has interrupted");
                            }

                            if(eatingMessage.getType() == 2){

                                PlayerListener playerListener = findPlayerListener(
                                        table.getPlayers().get(eatingMessage.getDefendingPlayerNumber()));

                                try {
                                    playerListener.os.writeObject(eatingMessage);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }



                            }


                            // Массовая рассылка результата
                        }

                        break;
                        ///endregion

                    case 3:

                        ///region EXTINCTION
                        for (Player player : table.getPlayers()) { // Игроки

                            //TODO: что-то им отправляем -> какие действия мы от игрока ждем

                            try {
                                wait(); // Ждем пока не получим ответ
                            } catch (InterruptedException e) {
                                System.out.println("Server: Problems with wait");
                            }

                            // Массовая рассылка результата
                        }

                        break;
                        ///endregion

                        // Здесь же будет и раздача карт
                }

                //Вероятно, рассылка результата бедет здесь
            }

            if(end) return true;
        }

        return false;
    }

    public void endPlay(){

    }

    PlayerListener findPlayerListener(Player player){
        for(PlayerListener playerListener : playerListeners){
            if(playerListener.player == player) return playerListener;
        }
    }

}
