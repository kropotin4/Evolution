package server;

import control.ControllerGameRoom;
import model.Table;
import server.message.Message;
import server.message.StartMessage;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class GamingRoom implements Serializable {
    static int idCounter = 0;

    static final String[] enumerate = {"первый","второй","третий","четвёртый","пятый"};
    int id = idCounter++;
    String defaultName = "GamingRoom " + id;
    String roomName;
    final int roomCapacity;
    final int quarterCardCount;

    ControllerGameRoom controller;

    private ArrayList<PlayerThread> playerThreads = new ArrayList<>();
    private boolean[] playersReady;



    //Timer timer = new Timer();

    public  GamingRoom(String roomName, int roomCapacity, int quarterCardCount, Server server){
        this.roomName = roomName;
        this.roomCapacity = roomCapacity;
        this.quarterCardCount = quarterCardCount;
        controller = new ControllerGameRoom(this, server);
        playersReady = new boolean[roomCapacity];

//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                for(PlayerThread playerThread : playerThreads){
//                    try {
//                        playerThread.sendMessage(new RoomInfoMessage(roomName, roomCapacity, getPlayersLogins()));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };
//        timer.schedule(timerTask, 500, 2000);
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
    public void deletePlayerThread(PlayerThread playerThread){
        System.out.println("GamingRoom: deletePlayerThread " + playerThread.getPlayerNumber());
        playerThreads.remove(playerThread);
        playersReady[playerThread.getPlayerNumber()] = false;
        playerThread.inRoom = false;
    }

    //////////////////////
    public void startGameDistribution(Table table) throws IOException {
        for(int i = 0; i < playerThreads.size(); ++i){
            playerThreads.get(i).sendMessage(new StartMessage(table, i, "Игра началось (вы - " + enumerate[i] + ")"));
        }
    }
    public synchronized void distribution(Message message) throws IOException {
        for(PlayerThread playerThread : playerThreads){
            playerThread.sendMessage(message);
        }
    }
    //////////////////////

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    public GamingRoomInfo getGamingRoomInfo(){
        return new GamingRoomInfo(roomName, id, playerThreads.size(), roomCapacity);
    }
    public GamingRoomInfo getFullGamingRoomInfo(){
        return new GamingRoomInfo(roomName, id, playerThreads.size(), roomCapacity, playersReady, getPlayersLogins());
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }
    public int getId() {
        return id;
    }
    public int getQuarterCardCount() {
        return quarterCardCount;
    }

    public int getPlayerNumber(){
        return playerThreads.size();
    }
    public void setPlayerReady(int playerNumber){
        System.out.println("GamingRoom: setPlayerReady " + playerNumber);
        playersReady[playerNumber] = true;
        System.out.println(Arrays.toString(playersReady));
        if(isAllReady()){
            controller.startGame();
        }
    }
    public boolean isAllReady(){
        for(boolean ready : playersReady) {
            if (!ready) return false;
        }

        return true;
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
