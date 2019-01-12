package server.message;

import server.GamingRoomInfo;

import java.util.ArrayList;

public class ClientInfoMessage extends Message {

    String serverIP;
    int serverPort;
    int roomCapacity;

    ArrayList<GamingRoomInfo> gamingRoomInfo;

    public ClientInfoMessage(
            String serverIP,
            int serverPort,
            int roomCapacity,
            ArrayList<GamingRoomInfo> gamingRoomInfo)
    {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.roomCapacity = roomCapacity;
        this.gamingRoomInfo = gamingRoomInfo;
    }

    public String getServerIP() {
        return serverIP;
    }
    public int getServerPort() {
        return serverPort;
    }
    public int getRoomCapacity() {
        return roomCapacity;
    }
    public ArrayList<GamingRoomInfo> getGamingRoomInfo() {
        return gamingRoomInfo;
    }
}
