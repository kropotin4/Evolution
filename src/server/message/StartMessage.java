package server.message;

import model.Table;

import java.io.Serializable;

public class StartMessage extends Message {

    // Начало игры - сервер передаёт сформированный стол.

    int playerNumber;

    public StartMessage(Table table, int playerNumber){
        this.table = table;
        this.playerNumber = playerNumber;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
}
