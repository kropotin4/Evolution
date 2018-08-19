package server;

import control.Controler;
import model.*;
import server.message.*;
import server.message.action.ActionMessage;
import server.message.action.GrazingAction;
import server.message.action.PirateAction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PlayerListener extends Thread {

    Server server;

    Socket socket;
    Controler controler;

    String login;
    int playerNumber;

    ObjectInputStream is;
    ObjectOutputStream os;

    Message message;

    PlayerListener(Server server, Socket socket, Controler controler) throws IOException {
        this.server = server;
        this.socket = socket;

        os = new ObjectOutputStream(socket.getOutputStream());
        is = new ObjectInputStream(socket.getInputStream());

        this.controler = controler;
    }

    @Override
    public void run() {
        Object start;

        try {
            start = is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("New player start StartMessage not received");
            return;
        }

        if(start instanceof StartMessage){
            StartMessage startMessage = (StartMessage) start;
            login = startMessage.getLogin();
            playerNumber = controler.addPlayer(login);
        }
        else{
            System.out.println("New player start StartMessage not received");
            return;
        }



        Object mesObject = null;
        while(true){

            try {
                mesObject = is.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(login + "mesObject is strange");
                continue;
            }

            if(mesObject instanceof Message){

                message = (Message) mesObject;


                if(controler.isPlayersTurn(playerNumber)){

                    switch (controler.getCurrentPhase()){
                        case GROWTH:
                            if(message.getMessageType() != MessageType.GROWTH) continue; // Надо еще что-то сделать!

                            growthMessageHandler((GrowthMessage) message);

                            server.notify();

                            break;
                        case EATING:
                            if(message.getMessageType() != MessageType.EATING) continue; // Надо еще что-то сделать!

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

        }

    }

    private void growthMessageHandler(GrowthMessage growthMessage){
        switch (growthMessage.getType()){
            case 0: //GrowthMessage(UUID creature, Card card, boolean isUp)

                controler.addTraitToCreature(playerNumber,
                        growthMessage.getFirstCreatureId(),
                        growthMessage.getCard(),
                        growthMessage.isUp()
                );

                break;

            case 1: //GrowthMessage(UUID creature1,UUID creature2, Card card, boolean isUp)

                if(growthMessage.getCard().getTrait(growthMessage.isUp()) == Trait.SYMBIOSYS){
                    //TODO: Работа с симбионтом
                }
                else{
                    controler.addPairTraitToCreature(
                            playerNumber,
                            growthMessage.getFirstCreatureId(),
                            growthMessage.getSecondCreatureId(),
                            growthMessage.getCard(),
                            growthMessage.isUp()
                    );
                }

                break;
        }

        server.recievedMessage = growthMessage;
    }

    private void eatingMessageHandler(EatingMessage eatingMessage){
        switch (eatingMessage.getType()){

            case 0: //Взятие еды из К.Б. (Существо)
                    //EatingMessage(int eatingCreature, boolean haveAction)

                controler.getFoodFromFodder(eatingMessage.getEatingCreautureId());

                ///region haveAction handle
                if(eatingMessage.isHaveAction()){
                    Object mesObject = null;
                    try {
                        mesObject = is.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println(login + "mesObject is strange");
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

               controler.attackCreature(
                       playerNumber,
                       eatingMessage.getDefendingPlayerNumber(),
                       eatingMessage.getAttackerCreatureId(),
                       eatingMessage.getDefendingCreatureId()
               );

            case 2: //Защита от атаки (Существо + Свойства)
                    //EatingMessage(int playerAttacker, UUID defendingCreature, Trait trait)

                if(eatingMessage.getTrait() == Trait.RUNNING){
                    if(Dice.rollOneDice() > 3){
                        //TODO: Неудачная атака
                    }
                    else{
                        controler.attackCreature(
                                eatingMessage.getAttackerPlayerNumber(),
                                playerNumber,
                                eatingMessage.getAttackerCreatureId(),
                                eatingMessage.getDefendingCreatureId()
                        );
                    }
                }

                //player.defendCreature()

                break;


        }

        server.recievedMessage = eatingMessage;
    }

    OS getOS(){
        return new OS(os, playerNumber);
    }
}
