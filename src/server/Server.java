package server;

import control.ControllerServer;
import server.message.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Server extends Thread {

    ControllerServer controller;

    ServerSocket serverSocket;

    String ip = null;
    int port;

    ArrayList<PlayerThread> freePlayersThreads = new ArrayList<>();
    ArrayList<GamingRoom> gamingRooms = new ArrayList<>();

    Timer timer = new Timer();
    TimerTask timerTask;

    public Server(ControllerServer controller) {
        this.controller = controller;

        port = controller.getPort();
        ip = controller.getInetAddress().getHostAddress();

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Server: ServerSocket not open (port = " + port + ")");
            throw new RuntimeException("Server caput", e.getCause());
        }


//        timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    distributionForFreePlayers(controller.createClientInfoMessage());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        timer.schedule(timerTask, 500, 2000);
    }


    @Override
    public void run() {
        System.out.println("Server start (player num: " + /*controllerServer.getPlayersNumber()*/  "-inf )");

        int i = 0;
        while (true) {

            try {
                Socket newPlayer = serverSocket.accept();
                System.out.println("Player connection start");
                PlayerThread playerThread = new PlayerThread(controller, this, newPlayer, i++);
                freePlayersThreads.add(playerThread);

                playerThread.start();

                System.out.println("Player connected");
                //controllerServer.playerConnect();


            } catch (IOException e) {
                System.out.println("Connect with player has failed");
            }
        }

    }

    public void close() throws IOException {
        serverSocket.close();
    }

    /////////

    public void addRoom(String roomName, int roomCapacity, int quarterCardCount){
        GamingRoom gamingRoom = new GamingRoom(roomName, roomCapacity, quarterCardCount, false);
        gamingRooms.add(gamingRoom);
    }
    public int enterTheRoom(PlayerThread playerThread, int roomId){

        for(GamingRoom gamingRoom : gamingRooms){
            if(gamingRoom.id == roomId){
                gamingRoom.addPlayerThread(playerThread);
                freePlayersThreads.remove(playerThread);

                return gamingRoom.getPlayerNumber() - 1;
            }
        }

        return -1;
    }

    //////////

    public synchronized void distributionForFreePlayers(Message message) throws IOException {
        for(PlayerThread playerThread : freePlayersThreads){
            if(playerThread.playerReady)
                playerThread.sendMessage(message);
        }
    }
    
    public synchronized void distribution(Message message) throws IOException {
        distributionForFreePlayers(message);

        for(GamingRoom gamingRoom : gamingRooms){
            for(PlayerThread playerThread : gamingRoom.playerThreads){
                playerThread.sendMessage(message);
            }
        }
    }

    //////////

    public ArrayList<GamingRoomInfo> getRoomsInfo(){
        ArrayList<GamingRoomInfo> roomInfo = new ArrayList<>();

        for(GamingRoom gamingRoom : gamingRooms){
            roomInfo.add(gamingRoom.getGamingRoomInfo());
        }

        return roomInfo;
    }
    public int getPlayerNumber(){
        return freePlayersThreads.size() + getPlayerInRoomsNumber();
    }
    public int getPlayerInRoomsNumber(){
        int res = 0;
        for(GamingRoom gamingRoom : gamingRooms){
            res += gamingRoom.getPlayerNumber();
        }
        return res;
    }
}
