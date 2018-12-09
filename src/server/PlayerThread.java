package server;

import control.ControllerServer;
import model.Trait;
import server.message.*;
import server.message.action.*;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static model.Phase.*;

public class PlayerThread extends Thread {

    Server server;
    PlayerListener playerListener;

    ControllerServer controller;

    Socket socket;

    ObjectInputStream is;
    ObjectOutputStream os;

    final int playerNumber;

    public PlayerThread(Server server, Socket socket, int playerNumber) throws IOException{
        super("PlayerThread " + playerNumber);
        this.server = server;
        this.socket = socket;
        this.playerNumber = playerNumber;

        controller = server.controller;

        is = new ObjectInputStream(socket.getInputStream());
        os = new ObjectOutputStream(socket.getOutputStream());

        playerListener = new PlayerListener(server, this, is);
    }

    public void messageHandler(Message message){

        if(controller.isPlayersTurn(playerNumber)){

            switch (controller.getCurrentPhase()){
                case GROWTH:
                    if(message.getMessageType() != MessageType.GROWTH){
                        // Надо еще что-то сделать!
                    }

                    growthMessageHandler((GrowthMessage) message);

                    server.notify();

                    break;
                case EATING:
                    if(message.getMessageType() != MessageType.EATING){
                        // Надо еще что-то сделать!
                    }

                    server.eatingMessage = (EatingMessage) message;

                    eatingMessageHandler((EatingMessage) message); // Здесь будет обработка, дабы не нагромождать

                    server.notify();

                    break;
                default:
                    System.out.print("Its strange");
                    break;
            }
        }
        else {

            if (message.getMessageType() == MessageType.SPECIAL) {

                try {
                    os.writeObject(new ErrorMessage(1)); // Не твоя очередь
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                //TODO: обработка действия игрока вне хода (Пиратство и т.д.)
            }
        }
    }

    private void growthMessageHandler(GrowthMessage growthMessage){
        switch (growthMessage.getType()){
            case 0: //GrowthMessage(UUID creature, Card card, boolean isUp)

                /*controller.addTraitToCreature(playerNumber,
                        growthMessage.getFirstCreatureId(),
                        growthMessage.getCard(),
                        growthMessage.isUp()
                );*/

                break;

            case 1: //GrowthMessage(UUID creature1,UUID creature2, Card card, boolean isUp)

                if(growthMessage.getCard().getTrait(growthMessage.isUp()) == Trait.SYMBIOSIS){
                    //TODO: Работа с симбионтом
                }
                else{
                    /*controller.addPairTraitToCreature(
                            playerNumber,
                            growthMessage.getFirstCreatureId(),
                            growthMessage.getSecondCreatureId(),
                            growthMessage.getCard(),
                            growthMessage.isUp()
                    );*/
                }

                break;
        }

        server.recievedMessage = growthMessage;
    }

    private void eatingMessageHandler(EatingMessage eatingMessage){
        switch (eatingMessage.getType()){

            case 0: //Взятие еды из К.Б. (Существо)
                //EatingMessage(int eatingCreature, boolean haveAction)

                //controller.getFoodFromFodder(eatingMessage.getEatingCreautureId());

                ///region haveAction handle
                if(eatingMessage.isHaveAction()){
                    Object mesObject = null;
                    try {
                        mesObject = is.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println(getName() + ": mesObject is strange");
                    }

                    if(!(mesObject instanceof ActionMessage)){
                        //TODO: Придумай что-нибудь
                    }

                    switch (((ActionMessage) mesObject).getTrait()){
                        case GRAZING:

                            GrazingAction grazingAction = (GrazingAction) mesObject;

                            //TODO: Обработка

                            break;

                        case PIRACY:

                            PirateAction pirateAction = (PirateAction) mesObject;

                            //TODO: Обработка

                            break;
                    }
                }
                ///endregion

                break;
            case 1: //Атака существа (Существо + Свойства, Существо) Пока без свойств
                //EatingMessage(UUID attackerCreature, int playerDefending, UUID defendingCreature)

                /*controller.attackCreature(
                        playerNumber,
                        eatingMessage.getDefendingPlayerNumber(),
                        eatingMessage.getAttackerCreatureId(),
                        eatingMessage.getDefendingCreatureId()
                );*/

            case 2: //Защита от атаки (Существо + Свойства)
                //EatingMessage(int playerAttacker, UUID defendingCreature, Trait trait)


                /*controller.attackCreature(
                        eatingMessage.getAttackerPlayerNumber(),
                        playerNumber,
                        eatingMessage.getAttackerCreatureId(),
                        eatingMessage.getDefendingCreatureId()
                );*/


                //player.defendCreature()

                break;


        }

        server.recievedMessage = eatingMessage;
    }
}
