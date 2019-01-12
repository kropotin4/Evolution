package server.message;

import model.Table;

public class StartMessage extends Message {

    // Начало игры - сервер передаёт сформированный стол.

    int playerNumber;

    public StartMessage(Table table, int playerNumber, String message){
        this.table = table;
        this.playerNumber = playerNumber;
        setMes(message);
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
}
