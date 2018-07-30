public class StartMessage {

    //Стартовое сообщение, его передает игрок, когда подключается к серверу.

    String login;

    StartMessage(String login){
        this.login = login;
    }

    public String getLogin(){
        return login;
    }
}
