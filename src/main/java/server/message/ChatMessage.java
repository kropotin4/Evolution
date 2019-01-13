package server.message;

public class ChatMessage extends Message {

    public final String login;
    private String color = null;

    public ChatMessage(String login, String message){
        super(MessageType.CHAT);
        this.login = login;
        setMes(message);
    }
    public ChatMessage(String login, String message, String color){
        super(MessageType.CHAT);
        this.login = login;
        this.color = color;
        setMes(message);
    }

    public void setColor(String color) {
        this.color = color;
    }
    public String getColor() {
        return color;
    }
}
