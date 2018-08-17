package server;

import control.Controler;
import model.Player;
import server.message.EatingMessage;
import server.message.Message;
import server.message.MessageType;
import server.message.RequestMessage;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;

public class Server extends Thread{


    Controler controler;

    ServerSocket serverSocket;

    ArrayList<PlayerListener> playerListeners = new ArrayList<>();

    Properties properties;

    EatingMessage eatingMessage;

    Server(Controler controler) throws IOException {
        this.controler = controler;

        properties = new Properties();
        properties.load(new FileInputStream("server_properties"));

        serverSocket = new ServerSocket(Integer.parseInt(properties.getProperty("SERVER_PORT")));
    }


    @Override
    public void run() {
        beginPlay();

        middlePlay();

        endPlay();
    }

    public void beginPlay(){

        for(int i = 0; i < controler.getPlayersNumber(); ++i) {

            try {
                Socket newPlayer = serverSocket.accept();

                playerListeners.add(new PlayerListener(this, newPlayer, controler));
                playerListeners.get(playerListeners.size() - 1).start();

            } catch (IOException e) {
                System.out.println("Connect with player has failed");
            }

        }
    }

    public boolean middlePlay(){ // Забавное название ?

        boolean end = false;
        while (!end){

            switch (controler.getCurrentPhase()) {
                case GROWTH:

                    growthPhaseHandler();

                    break;

                case CALC_FODDER_BASE:

                    cfbPhaseHandler();

                    break;

                case EATING:

                    eatingPhaseHandler();

                    break;

                case EXTINCTION:

                    extinctionPhaseHandler();

                    break;

                    // Здесь же будет и раздача карт
            }

            //Вероятно, рассылка результата бедет здесь
        }

        return false;
    }

    public void endPlay(){

    }


    private void growthPhaseHandler(){

        //Игроки должны получить сообщения типа "положи карту, если можешь, или скажи пас"

        for (Player player : controler.getPlayers()) { // Игроки

            if(player.getPlayerCardsNumber() <= 0){
                //TODO: Он пас
            }
            else{
                //TODO: Отправляем запрос
                try {
                    findPlayerListener(player).os.writeObject(new RequestMessage(MessageType.GROWTH));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            try {
                wait(); // Ждем пока не получим ответ
            } catch (InterruptedException e) {
                System.out.println("Server: Problems with wait");
            }

            sendingAllResults();// Массовая рассылка результата
        }
    }

    private void cfbPhaseHandler(){
        controler.setFodder();
        sendingAllResults();
    }

    private void eatingPhaseHandler() {
        for (Player player : controler.getPlayers()) { // Игроки

            //TODO: что-то им отправляем -> какие действия мы от игрока ждем

            try {
                findPlayerListener(player).os.writeObject(new RequestMessage(MessageType.EATING));
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                wait(); // Ждем пока не получим ответ
            } catch (InterruptedException e) {
                System.out.println("Server: wait() has interrupted");
            }

            if(eatingMessage.getType() == 2){

                PlayerListener playerListener = findPlayerListener(
                        controler.findPlayer(eatingMessage.getDefendingPlayerNumber()));

                try {
                    playerListener.os.writeObject(eatingMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }


            sendingAllResults();// Массовая рассылка результата
        }
    }

    private void extinctionPhaseHandler(){
        for (Player player : controler.getPlayers()) { // Игроки

            //TODO: что-то им отправляем -> какие действия мы от игрока ждем

            try {
                wait(); // Ждем пока не получим ответ
            } catch (InterruptedException e) {
                System.out.println("Server: Problems with wait");
            }

            // Массовая рассылка результата
        }
    }

    PlayerListener findPlayerListener(Player player){
        for(PlayerListener playerListener : playerListeners){
            if(playerListener.player == player) return playerListener;
        }

        return null;
    }


    private void sendingAllResults(){
        for (PlayerListener playerListener : playerListeners) { // Игроки

            try {
                playerListener.os.writeObject(table);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
