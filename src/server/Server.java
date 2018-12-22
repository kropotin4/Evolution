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



    //////////

    public void startGameDistribution(Table table) throws IOException {
        for(int i = 0; i < playerThreads.size(); ++i){
            playerThreads.get(i).sendMessage(new StartMessage(table, i, "Игра началось (вы - " + (i + 1) + ")"));
        }
    }

    public synchronized void distribution(Message message) throws IOException {
        for(PlayerThread playerThread : playerThreads){
            playerThread.sendMessage(message);
        }
    }

    //////////




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
