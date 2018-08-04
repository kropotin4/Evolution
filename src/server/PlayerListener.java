package server;

import model.Dice;
import model.Player;
import model.Table;
import model.Trait;
import server.message.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PlayerListener extends Thread {

    Server server;

    Socket socket;
    Table table;
    Player player;

    ObjectInputStream is;
    ObjectOutputStream os;

    PlayerListener(Thread server, Socket socket, Table table) throws IOException {
        this.server = (Server) server;
        this.socket = socket;

        os = new ObjectOutputStream(socket.getOutputStream());
        is = new ObjectInputStream(socket.getInputStream());

        this.table = table;
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

                Message message = (Message) mesObject;

                if(player == table.getPlayers().get(table.getPlayerTurn())){

                    switch (table.getCurrentPhase()){
                        case GROWTH:

                            ///region GROWTH
                            if(message.getMessageType() != MessageType.GROWTH) continue; // Надо еще что-то сделать!

                            GrowthMessage growthMessage = (GrowthMessage) message;

                            switch (growthMessage.getType()){
                                case 0:

                                    player.addTraitToCreature(
                                            player.findCreature(growthMessage.getFirstCreatureId()),
                                            growthMessage.getCard(),
                                            growthMessage.isUp()
                                    );

                                    break;

                                case 1:

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

                            server.notify();
                            ///endregion

                            break;
                        case CALC_FODDER_BASE:
                            if(message.getMessageType() != MessageType.CFB) continue; // Надо еще что-то сделать!

                            //TODO: обработка действия игрока (CALC_FODDER_BASE) + notify

                            break;
                        case EATING:
                            if(message.getMessageType() != MessageType.EATING) continue; // Надо еще что-то сделать!
                            EatingMessage eatingMessage = (EatingMessage) message;
                            server.eatingMessage = eatingMessage;

                            //TODO: обработка действия игрока (EATING) + notify

                            switch (eatingMessage.getType()){
                                case 0:

                                    player.getFoodFromFodder(
                                            player.findCreature(eatingMessage.getEatingCreautureId()),
                                            null
                                    );

                                    break;
                                case 1:

                                    player.getFoodFromFodder(
                                            player.findCreature(eatingMessage.getEatingCreautureId()),
                                            null
                                    );

                                    if(eatingMessage.getTrait() == Trait.GRAZING){
                                        table.getFood(eatingMessage.getGrazingCount());
                                    }

                                    break;

                                case 2:

                                    server.notify();

                                    break;

                                case 3:

                                    if(eatingMessage.getTrait() == Trait.RUNNING){
                                        if(Dice.rollOneDice() > 3){
                                            //TODO: Неудачная атака
                                        }
                                        else{
                                            Player attackerPlayer = table.getPlayers().get(eatingMessage.getAttackerPlayerNumber());
                                            attackerPlayer.attackCreature(
                                                    attackerPlayer.findCreature(eatingMessage.getAttackerCreatureId()),
                                                    player.findCreature(eatingMessage.getDefendingCreatureId())
                                            );
                                        }
                                    }

                                    player.defendCreature()

                                    break;

                                case(4):

                                    break;
                            }

                            break;
                        default:
                            System.out.print("Its strange");
                            break;
                    }
                }
                else {

                    if (message.getMessageType() != MessageType.SPECIAL) {
                        //TODO: обработка действия игрока вне хода (Пиратство и т.д.)
                    } else {

                        try {
                            os.writeObject(new ErrorMessage(1)); // Не твоя очередь
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }

        }

    }
}
