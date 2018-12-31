package server;

import java.io.Serializable;

public class GamingRoomInfo implements Serializable {

    public final String roomName;
    public final int id;
    public final int currentRommSize;
    public final int roomCapacity;

    public final boolean[] playersReady;
    public final String[] playersLogins;


    public GamingRoomInfo(String roomName, int roomId, int currentRommSize, int roomCapacity){
        this.roomName = roomName;
        this.id = roomId;
        this.currentRommSize = currentRommSize;
        this.roomCapacity = roomCapacity;
        this.playersReady = null;
        this.playersLogins = null;
    }


    public GamingRoomInfo(String roomName, int roomId, int currentRommSize, int roomCapacity, boolean[] playersReady, String[] playersLogins){
        this.roomName = roomName;
        this.id = roomId;
        this.currentRommSize = currentRommSize;
        this.roomCapacity = roomCapacity;
        this.playersReady = playersReady;
        this.playersLogins = playersLogins;
    }

}
