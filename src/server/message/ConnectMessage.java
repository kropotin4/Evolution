package server.message;

import java.io.Serializable;

public class ConnectMessage implements Serializable {

    // Сообщение, передаваемое игроком при подключении к серверу.

    String login;

    public ConnectMessage(String login){
    this.login = login;
    }

    public String getLogin() {
        return login;
    }
}
