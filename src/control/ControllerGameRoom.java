package control;

import model.Phase;
import server.GamingRoom;
import server.GamingRoomInfo;
import server.message.Message;
import server.message.RoomInfoMessage;

import java.io.IOException;

public class ControllerGameRoom {

    GamingRoom gamingRoom;

    int stage;

    Controller controller;


    public ControllerGameRoom(GamingRoom gamingRoom){
        this.gamingRoom = gamingRoom;
    }

    ////////////

    // Создаем Controller, рассылаем StartMessage
    public void startGame(){
        System.out.println("ControllerGameRoom: startGame (playerNum: " + gamingRoom.getPlayerNumber() + ", qurtCards: " + gamingRoom.getQuarterCardCount() + ")");
        stage = 2;
        controller = new Controller(gamingRoom.getQuarterCardCount(), gamingRoom.getPlayerNumber());

        for(int i = 0; i < gamingRoom.getRoomCapacity(); ++i){
            controller.setLogin(gamingRoom.getPlayerThreads().get(i).getLogin(), i);
        }

        try {
            gamingRoom.startGameDistribution(controller.getTable());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playerReadyToPlay(int playerNumber){
        gamingRoom.setPlayerReady(playerNumber);
    }
    ////////////

    public void distribution(Message message) throws IOException {
        gamingRoom.distribution(message);
    }

    public RoomInfoMessage createRoomInfoMessage(){
        return new RoomInfoMessage(getFullGamingRoomInfo());
    }

    ////////////


    /////////////


    ///////////////

    public Phase getCurrentPhase(){
        return controller.getCurrentPhase();
    }
    public GamingRoomInfo getFullGamingRoomInfo(){
        return gamingRoom.getFullGamingRoomInfo();
    }

    ///////////////
}
