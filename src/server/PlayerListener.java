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

            player = new Player(startMessage.getLogin());
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
                System.out.println(player.getLogin() + "mesObject is strange");
                continue;
            }

            if(mesObject instanceof Message){

                message = (Message) mesObject;


                if(player == controler.getPlayPlayer()){

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

                player.addTraitToCreature(
                        player.findCreature(growthMessage.getFirstCreatureId()),
                        growthMessage.getCard(),
                        growthMessage.isUp()
                );

                break;

            case 1: //GrowthMessage(UUID creature1,UUID creature2, Card card, boolean isUp)

                if(growthMessage.getCard().getTrait(growthMessage.isUp()) == Trait.SYMBIOSYS){
                    //TODO: Работа с симбионтом
                }
                else{
                    player.addPairTraitToCreature(
                            player.findCreature(growthMessage.getFirstCreatureId()),
                            player.findCreature(growthMessage.getSecondCreatureId()),
                            growthMessage.getCard(),
                            growthMessage.isUp()
                    );
                }

                break;
        }
    }

    private void eatingMessageHandler(EatingMessage eatingMessage){
        switch (eatingMessage.getType()){

            case 0: //Взятие еды из К.Б. (Существо)

                player.getFoodFromFodder(
                        player.findCreature(eatingMessage.getEatingCreautureId()),
                        null
                );

                if(eatingMessage.isHaveAction()){
                    Object mesObject = null;
                    try {
                        mesObject = is.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println(player.getLogin() + "mesObject is strange");
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

                break;
            case 1: //Атака существа (Существо + Свойства, Существо) Пока без свойств
                    //EatingMessage(UUID attackerCreature, int playerDefending, UUID defendingCreature)

               if(controler.attackCreature())

            case 2: //Защита от атаки (Существо + Свойства)
                    //EatingMessage(int playerAttacker, UUID defendingCreature, Trait trait)

                if(eatingMessage.getTrait() == Trait.RUNNING){
                    if(Dice.rollOneDice() > 3){
                        //TODO: Неудачная атака
                    }
                    else{
                        Player attackerPlayer = controler.findPlayer(eatingMessage.getAttackerPlayerNumber());
                        attackerPlayer.attackCreature(
                                attackerPlayer.findCreature(eatingMessage.getAttackerCreatureId()),
                                player.findCreature(eatingMessage.getDefendingCreatureId())
                        );
                    }
                }

                //player.defendCreature()

                break;


        }
    }
}
