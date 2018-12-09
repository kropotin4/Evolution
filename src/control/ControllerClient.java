package control;

import client.Client;

public class ControllerClient {

    Controller controller;
    Client client;

    public ControllerClient(Controller controller, String ip, int port){
        this.controller = controller;

        System.out.println("ControllerClient: ip = " + ip + " port = " + port);

        this.client = new Client(this, ip, port);
    }

    public void start(){
        client.start();
    }

}
