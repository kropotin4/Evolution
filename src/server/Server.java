package server;

import client.Client;
import control.Controller;
import control.ControllerServer;
import model.Player;
import model.Table;
import server.message.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;

public class Server extends Thread{


    ControllerServer controller;

    ServerSocket serverSocket;

    ArrayList<OS> playersStream = new ArrayList<>();

    ArrayList<PlayerThread> playerThreads = new ArrayList<>();

    Message recievedMessage;
    EatingMessage eatingMessage;

    public Server(ControllerServer controller, int port) {
        this.controller = controller;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Server: ServerSocket not open (port = " + port + ")");
            throw new RuntimeException("Server caput", e.getCause());
        }
    }


    @Override
    public void run() {
        beginPlay();

        //middlePlay();

        //endPlay();
    }

    public void beginPlay(){

        System.out.println("Server start (player num: " + controller.getPlayersNumber() + ")");

        for(int i = 0; i < controller.getPlayersNumber(); ++i) {

            try {
                Socket newPlayer = serverSocket.accept();

                PlayerThread playerThread = new PlayerThread(this, newPlayer, i);
                playerThreads.add(playerThread);

                playerThread.start();

                System.out.println("Player connected");
                controller.playerConnect();


            } catch (IOException e) {
                System.out.println("Connect with player has failed");
            }

        }
    }

    public boolean middlePlay(){

        boolean end = false;
        while (!end){

            switch (controller.getCurrentPhase()) {
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

    //////////

    public void startGameDistribution(Table table) throws IOException {
        for(int i = 0; i < playerThreads.size(); ++i){
            playerThreads.get(i).sendMessage(new StartMessage(table, i));
        }
    }

    public synchronized void distribution(Message message) throws IOException {
        for(PlayerThread playerThread : playerThreads){
            playerThread.sendMessage(message);
        }
    }

    //////////

    private void growthPhaseHandler(){

        //Игроки должны получить сообщения типа "положи карту, если можешь, или скажи пас"

        for (int playerNumber = 0; playerNumber < controller.getPlayersNumber(); ++playerNumber) { // Игроки

            if(controller.getPlayerCardsNumber(playerNumber) <= 0){
                //TODO: Он пас
                controller.doNextMove();
            }
            else{
                //TODO: Отправляем запрос
                try {
                    findPlayerStream(playerNumber).writeObject(new RequestMessage(MessageType.GROWTH));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            try {
                wait(); // Ждем пока не получим ответ
            } catch (InterruptedException e) {
                System.out.println("Server: Problems with wait");
            }
            controller.doNextMove();
            sendingAllResults();// Массовая рассылка результата
        }
    }

    private void cfbPhaseHandler(){
        controller.doNextMove();
        sendingAllResults();
    }

    private void eatingPhaseHandler() {
        for (int playerNumber = 0; playerNumber < controller.getPlayersNumber(); ++playerNumber) { // Игроки

            //TODO: что-то им отправляем -> какие действия мы от игрока ждем

            try {
                findPlayerStream(playerNumber).writeObject(new RequestMessage(MessageType.EATING));
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                wait(); // Ждем пока не получим ответ
            } catch (InterruptedException e) {
                System.out.println("Server: wait() has interrupted");
            }

            controller.doNextMove();

            if(eatingMessage.getType() == 2){

                try {
                    findPlayerStream(eatingMessage.getDefendingPlayerNumber()).writeObject(eatingMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }


            sendingAllResults();// Массовая рассылка результата
        }
    }

    private void extinctionPhaseHandler(){
        controller.doNextMove();

        for (int playerNumber = 0; playerNumber < controller.getPlayersNumber(); ++playerNumber) { // Игроки

            //TODO: что-то им отправляем -> какие действия мы от игрока ждем

            try {
                wait(); // Ждем пока не получим ответ
            } catch (InterruptedException e) {
                System.out.println("Server: Problems with wait");
            }

            // Массовая рассылка результата
        }
    }


    ObjectOutputStream findPlayerStream(int playerNumber){
        for(OS playerStream : playersStream){
            if(playerStream.playerNumber == playerNumber) return playerStream.os;
        }

        return null;
    }

    private void sendingAllResults(){
        for (OS playerStream : playersStream) { // Игроки

            try {
                playerStream.os.writeObject(recievedMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    synchronized public void addPlayerOS(OS os){
        playersStream.add(os);
    }
}
