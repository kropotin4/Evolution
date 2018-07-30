package server.message;

public class StartMessage {

    //Стартовое сообщение, его передает игрок, когда подключается к серверу.

    String login;

    public StartMessage(String login){
        this.login = login;
    }

    public String getLogin(){
        return login;
    }
}
