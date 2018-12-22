package client;

import control.Controller;
import control.ControllerClient;
import server.message.ConnectMessage;
import server.message.Message;
import server.message.StartMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends Thread{

    ControllerClient controller;

    private Socket server;

    String login;
    String ip;
    int port;



    private ObjectOutputStream os;
    private ObjectInputStream is;

    ServerListener serverListener;

    public Client(ControllerClient controller, String login, String ip, int port) {
        this.controller = controller;
        this.login = login;
        this.ip = ip;
        this.port = port;

        try {
            server = new Socket(ip, port);
        } catch (IOException e) {
            System.err.println("Client: Socket not open (ip = " + ip + " port = " + port + ")");
            throw new RuntimeException("Client caput", e.getCause());
        }

        try {
            os = new ObjectOutputStream(server.getOutputStream());
            is = new ObjectInputStream(server.getInputStream());
        } catch (IOException e) {
            System.err.println("input or output stream not open");
            throw new RuntimeException("Client caput", e.getCause());
        }


        serverListener = new ServerListener(controller, this, is);
    }

    @Override
    public void run() {
        serverListener.start();

        System.out.println("Client start (ip = " + ip + " port = " + port + ")");

        try {
            os.writeObject(new ConnectMessage(login)); // Отправляем первое сообщение серверу
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void sendMessage(Message message) throws IOException {
        System.out.println("Client: sendMessage " + message.getMessageType());
        os.writeObject(message); // Отправляем сообщение серверу
    }
}
