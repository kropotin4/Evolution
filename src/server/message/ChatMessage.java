package server.message;

public class ChatMessage extends Message {

    public ChatMessage(String login, String message){
        super(MessageType.CHAT);
        setMes("\"" + login + "\": " + message);
    }

}
