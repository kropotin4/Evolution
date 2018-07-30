package server;

import model.Player;
import model.Table;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class Server {

    Table table;

    ServerSocket serverSocket;

    Properties properties;

    Server(Table table) throws IOException {

        properties = new Properties();
        properties.load(new FileInputStream("server_properties"));

        serverSocket = new ServerSocket(Integer.parseInt(properties.getProperty("SERVER_PORT")));

        this.table = table;
    }


    public void beginPlay(){

        for(int i = 0; i < table.getPlayerCount(); ++i) {

            try {
                Socket newPlayer = serverSocket.accept();

                new PlayerListener(Thread.currentThread(), newPlayer, table).start();

            } catch (IOException e) {
                System.out.println("Connect with player fall");
            }

        }
    }

    public boolean middlePlay(){ // Забавное название ?

        while (true){

            for(int i = 0; i < 4; ++i){ // Фазы

                for(Player player : table.getPlayers()){ // Игроки

                    //TODO: что-то им отправляем -> какие действия мы от игрока ждем

                    try {
                        wait(); // Ждем пока не получим ответ
                    } catch (InterruptedException e) {
                        System.out.println("Server: problems with wait");
                    }

                    // Массовая рассылка результата
                }

            }


        }

        return false;
    }

    public void endPlay(){

    }

}
