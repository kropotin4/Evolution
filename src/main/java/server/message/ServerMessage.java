package server.message;

import model.Table;

public class ServerMessage extends Message{

    public ServerMessage(Table table, String message){
        super(MessageType.SERVER);
        setTable(table);
        setMes(message);
    }

}
