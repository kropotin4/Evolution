package client;

import control.Controler;

import java.io.IOException;
import java.net.Socket;

public class Client extends Thread{

    String ip;
    int port;

    String login;

    ServerListener serverListener;

    Client(Controler controler, String login) throws IOException {
        this.login = login;
        Socket server = new Socket(ip, port);

        serverListener = new ServerListener(server, this, controler);
        serverListener.start();
    }

    @Override
    public void run() {

        while (true){

            //TODO: Получение команды -> отправка сообщения серверу


            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
