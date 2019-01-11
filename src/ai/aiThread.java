package ai;

import control.Controller;
import model.Table;

public class aiThread extends Thread {

    Controller controller;
    Table table;

    int playerNumber = -1;

    public aiThread(Controller controller){
        super("AI Thread");
        this.controller = controller;
    }
    public aiThread(Table table, int playerNumber){
        super("AI Thread");
        this.table = table;
        this.playerNumber = playerNumber;
    }

    @Override
    public void run() {
        System.out.println("ai run");

        // Заглушка
        if(table.getPlayerTurn() != playerNumber)
            throw new RuntimeException("aiThread: table.getPlayerTurn() != playerNumber");

        switch (table.getCurrentPhase()){
            case GROWTH:

                switch (calcGrowth()){
                    case 0:
                        //controller.setPlayerPass(controller.getPlayerTurn());
                        table.getPlayers().get(playerNumber).setPass(true);
                        break;

                    default:
                        break;
                }

                break;
            case EATING:

                switch (calcEating()){
                    case 0:
                        //controller.setPlayerPass(controller.getPlayerTurn());
                        table.getPlayers().get(playerNumber).setPass(true);
                        break;

                    default:
                        break;
                }

                break;
            default:
                break;
        }

        table.doNextMove();
    }

    //фаза роста

    int calcGrowth(){
        return 0;
    }

    //фаза питания

    int calcEating(){
        return 0;
    }
}
