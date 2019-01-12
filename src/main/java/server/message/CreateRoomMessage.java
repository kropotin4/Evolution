package server.message;

public class CreateRoomMessage extends Message {

    String roomName;
    int playerNumber;
    int quarterCardCount;

    public CreateRoomMessage(String roomName, int playerNumber, int quarterCardCount){
        this.roomName = roomName;
        this.playerNumber = playerNumber;
        this.quarterCardCount = quarterCardCount;
    }

    public String getRoomName() {
        return roomName;
    }
    public int getPlayerNumber() {
        return playerNumber;
    }
    public int getQuarterCardCount() {
        return quarterCardCount;
    }

    @Override
    public String toString() {
        return "CreateRoomMessage{" +
                "roomName='" + roomName + '\'' +
                ", playerNumber=" + playerNumber +
                ", quarterCardCount=" + quarterCardCount +
                '}';
    }
}
