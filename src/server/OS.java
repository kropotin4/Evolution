package server;

import java.io.ObjectOutputStream;

public class OS {
    public final ObjectOutputStream os;
    public final int playerNumber;

    public OS(ObjectOutputStream os, int playerNumber){
        this.os = os;
        this.playerNumber = playerNumber;
    }
}
