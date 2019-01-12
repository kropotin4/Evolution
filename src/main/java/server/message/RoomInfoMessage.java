package server.message;

import server.GamingRoomInfo;

public class RoomInfoMessage extends Message {


    final GamingRoomInfo gamingRoomInfo;

    public RoomInfoMessage(GamingRoomInfo gamingRoomInfo){
        this.gamingRoomInfo = gamingRoomInfo;
    }

    public String getRoomName() {
        return gamingRoomInfo.roomName;
    }

    public int getRoomCapacity() {
        return gamingRoomInfo.roomCapacity;
    }

    public String[] getPlayersLogins() {
        return gamingRoomInfo.playersLogins;
    }

    public boolean[] getPlayersReady() {
        return gamingRoomInfo.playersReady;
    }

    @Override
    public String toString() {
        return gamingRoomInfo.toString();
    }
}
