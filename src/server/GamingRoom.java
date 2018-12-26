package server;

import control.ControllerGameRoom;
import model.Table;
import server.message.Message;
import server.message.RoomInfoMessage;
import server.message.StartMessage;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GamingRoom implements Serializable {
    static int idCounter = 0;

    static final String[] enumerate = {"первый","второй","третий","четвёртый","пятый"};
    int id = idCounter++;
    String defaultName = "GamingRoom " + id;
    String roomName;
    final int roomCapacity;
    final int quarterCardCount;

    ControllerGameRoom controller;

    ArrayList<PlayerThread> playerThreads = new ArrayList<>();
    int readyNumber = 0;
    boolean[] playersReady;

    boolean gameOn = false;
    boolean startGameWithFullPlayers = false;

    Timer timer = new Timer();

    public  GamingRoom(String roomName, int roomCapacity, int quarterCardCount, boolean startGameWithFullPlayers){
        this.roomName = roomName;
        this.roomCapacity = roomCapacity;
        this.quarterCardCount = quarterCardCount;
        controller = new ControllerGameRoom(this);
        playersReady = new boolean[roomCapacity];

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                for(PlayerThread playerThread : playerThreads){
                    try {
                        playerThread.sendMessage(new RoomInfoMessage(roomName, roomCapacity, getPlayersLogins()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        timer.schedule(timerTask, 500, 2000);
    }

    public void addPlayerThread(PlayerThread playerThread){
        if(playerThreads.size() < roomCapacity) {
            System.out.println(roomName + ": add new player");
            playerThreads.add(playerThread);
            playerThread.setControllerGameRoom(controller);
            playerThread.inRoom = true;
        }
        else{
            System.out.println(roomName + ": add new player fails");
        }
    }

    public void startGameDistribution(Table table) throws IOException {
        for(int i = 0; i < playerThreads.size(); ++i){
            playerThreads.get(i).sendMessage(new StartMessage(table, i, "Игра началось (вы - " + enumerate[i] + ")"));
            playerThreads.get(i).gameOn = true;
        }
    }

    public synchronized void distribution(Message message) throws IOException {
        for(PlayerThread playerThread : playerThreads){
            playerThread.sendMessage(message);
        }
    }


    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    public GamingRoomInfo getGamingRoomInfo(){
        return new GamingRoomInfo(roomName, id, playerThreads.size(), roomCapacity);
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }

    public int getPlayerNumber(){
        return playerThreads.size();
    }
    public void setPlayerReady(int playerNumber){
        playersReady[playerNumber] = true;
        readyNumber++;

        if(readyNumber == roomCapacity){
            controller.startGame();
        }
    }
    public ArrayList<PlayerThread> getPlayerThreads() {
        return playerThreads;
    }
    public boolean[] getPlayersReady() {
        return playersReady;
    }
    private String[] getPlayersLogins(){
        String[] res = new String[roomCapacity];

        int i = 0;

        for(; i < playerThreads.size(); ++i){
            res[i] = playerThreads.get(i).getLogin();
        }

        for(; i < roomCapacity; ++i){
            res[i] = "";
        }

        return res;
    }
}
