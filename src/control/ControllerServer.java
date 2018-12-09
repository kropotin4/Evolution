package control;

import model.Phase;
import server.Server;
import server.message.Message;

import java.io.IOException;

public class ControllerServer {

    Controller controller;
    int port;
    Server server;

    int playerNumber; // Меняется в doNextMove()

    public ControllerServer(Controller controller, int port){
        this.controller = controller;
        this.port = port;

        System.out.println("ControllerServer: port = " + port);

        server = new Server(this, port);
    }

    public void start(){
        server.start();
    }

    public void doNextMove(){
        playerNumber = controller.doNextMove();
    }

    public void addTraitToCreature(){
        /*controller.addTraitToCreature(
                creatureNode.getPlayerPane().getPlayerNumber(),
                creatureNode.getCreatureId(),
                cardNode.getCard(),
                isUp
        );*/

    }

    public int getPlayerCardsNumber(int playerNumber){
        return controller.getPlayerCardsNumber(playerNumber);
    }
    public int getPlayersNumber(){
        return controller.getPlayersNumber();
    }
    public boolean isPlayersTurn(int playerNumber){
        return controller.isPlayersTurn(playerNumber);
    }

    public Phase getCurrentPhase(){
        return controller.getCurrentPhase();
    }


    public void messageHandler(Message message, int playerNumber){

    }

}
