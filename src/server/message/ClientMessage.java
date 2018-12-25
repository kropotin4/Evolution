package server.message;

import model.Phase;
import model.Table;


public class ClientMessage extends Message{

    public ClientMessage(Table table){
        setTable(table);
    }
    public ClientMessage(Table table, String message){
        setTable(table);
        setMes(message);
    }

}

