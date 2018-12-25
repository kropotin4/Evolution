package server.message;

public class RoomInfoMessage extends Message {

    final String roomName;
    final int roomCapacity;
    final String[] players;

    public RoomInfoMessage(String roomName, int roomCapacity, String[] players){
        this.roomName = roomName;
        this.roomCapacity = roomCapacity;
        this.players = players;
    }

    public String getRoomName() {
        return roomName;
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }

    public String[] getPlayers() {
        return players;
    }
}
