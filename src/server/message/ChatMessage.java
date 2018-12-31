package server.message;

public class ChatMessage extends Message {

    final String login;

    public ChatMessage(String login, String message){
        super(MessageType.CHAT);
        this.login = login;
        setMes("\"" + login + "\": " + message);
    }

}
