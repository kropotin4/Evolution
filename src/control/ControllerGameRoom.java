package control;

import server.GamingRoom;

import java.io.IOException;

public class ControllerGameRoom {

    GamingRoom gamingRoom;

    int stage;

    Controller controller;


    int playerTurn; // Меняется в doNextMove()

    int playerNumber = 2; // Количество игроков
    int quarterCardCount = 1; // количество четвертей карт

    int connectPlayer; // Сколько в данный момент подключено игроков

    public ControllerGameRoom(GamingRoom gamingRoom){
        this.gamingRoom = gamingRoom;
    }

    ////////////

    // Создаем Controller, рассылаем StartMessage
    public void startGame(){
        stage = 2;
        controller = new Controller(quarterCardCount, playerNumber);

        for(int i = 0; i < gamingRoom.getRoomCapacity(); ++i){
            controller.setLogin(gamingRoom.getPlayerThreads().get(i).getLogin(), i);
        }

        try {
            gamingRoom.startGameDistribution(controller.getTable());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playerConnect(){
//        if(stage == 1) {
//            connectionPane.playerConnect(connectPlayer);
//            connectPlayer++;
//
//            if(connectPlayer >= playerNumber)
//                connectionPane.setStartButtonDisable(false);
//        }
    }
    ////////////

    public void doNextMove(){
        playerTurn = controller.doNextMove();
    }

    /////////////
    public void setPlayerNumber(int playerNumber){
        this.playerNumber = playerNumber;
    }
    public void setQuarterCardCount(int quarterCardCount){
        this.quarterCardCount = quarterCardCount;
    }

    public int getPlayersNumber(){
        return playerNumber;
    }
    public int getQuarterCardCount() {
        return quarterCardCount;
    }
    ///////////////

    public void setLogin(String login, int playerNumber){
//        if(stage == 1){
//            Platform.runLater(() -> {
//                // Update UI here.
//                connectionPane.setLogin(login, playerNumber);
//                //controller.setLogin(login, playerTurn);
//            });
//        }
    }

    ///////////////
}
