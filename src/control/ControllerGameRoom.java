package control;

import model.Phase;
import model.Table;
import server.GamingRoom;
import server.GamingRoomInfo;
import server.PlayerThread;
import server.Server;
import server.message.*;

import java.io.IOException;

public class ControllerGameRoom {

    Server server;
    GamingRoom gamingRoom;

    int stage;
    int endMessage = 0;
    boolean gameOn = false;

    Controller controller;


    public ControllerGameRoom(GamingRoom gamingRoom, Server server){
        this.gamingRoom = gamingRoom;
        this.server = server;
    }

    ////////////

    // Создаем Controller, рассылаем StartMessage
    public void startGame(){
        System.out.println("ControllerGameRoom: startGame (playerNum: " + gamingRoom.getPlayerNumber() + ", qurtCards: " + gamingRoom.getQuarterCardCount() + ")");
        stage = 2;
        gameOn = true;
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

    public void setTable(Table table){
        controller.setTable(table);
    }

    /////////////

    synchronized public void messageHandler(Message message, PlayerThread playerThread){
        // Ждем начала игры
        if(!gameOn){

            if(message instanceof ChatMessage){
                try {
                    distribution(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(message instanceof ReadyToPlayMessage){
                playerReadyToPlay(playerThread.getPlayerNumber());
            }
            else if(message instanceof ExitFromRoomMessage){
                playerThread.setInRoom(false);
                server.exitFromRoom(playerThread, gamingRoom.getId());
            }

            try {
                distribution(createRoomInfoMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Играет
        else {

            if (message instanceof ChatMessage) {
                try {
                    distribution(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {

                System.out.println("Current player: " + message.getTable().getPlayerTurn());

                setTable(message.getTable());
                String serverMessege = message.getMes();

                switch (getCurrentPhase()){
                    case GROWTH:

                        if(message.getTable().isEndMove() && endMessage == 0) {
                            serverMessege = serverMessege.concat("\nВ колоде больше нет карт - это последний ход.");
                            ++endMessage;
                        }

                        try {
                            distribution(new ServerMessage(message.getTable(), serverMessege));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        break;
                    case EATING:

                        if(message.getTable().isEndMove() && endMessage == 1) {
                            serverMessege = serverMessege.concat("\nПосле этой фазы будет определяться победитель");
                            ++endMessage;
                        }

                        try {
                            distribution(new ServerMessage(message.getTable(), serverMessege));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        break;
                    default:
                        System.out.print("Its strange");
                        break;
                }
            }
        }
    }

    ///////////////

    public Phase getCurrentPhase(){
        return controller.getCurrentPhase();
    }
    public GamingRoomInfo getFullGamingRoomInfo(){
        return gamingRoom.getFullGamingRoomInfo();
    }

    ///////////////
}
