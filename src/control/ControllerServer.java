package control;

import server.Server;

public class ControllerServer {

    Controller controller;
    Server server;

    public ControllerServer(Controller controller, Server server){
        this.controller = controller;
        this.server = server;
    }

}
