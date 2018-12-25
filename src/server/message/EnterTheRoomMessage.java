package server.message;

public class EnterTheRoomMessage extends Message {

    final int roomId;

    public EnterTheRoomMessage(int roomId){
        this.roomId = roomId;
    }

    public int getRoomId() {
        return roomId;
    }
}
