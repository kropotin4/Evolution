package model;

import control.Controller;

import java.util.ArrayList;

public class EndGameInfo {
    public int winner;
    public boolean isDraw = false;
    public ArrayList<Integer> top = new ArrayList();
    public ArrayList<Player> players;
    public int maximum = -1;

    public EndGameInfo(Controller controller){
        players = controller.getPlayers();
        winner = -1;
        int current;
        for (Player player : players) {
            current = player.getScore();
            if (current == maximum){
                isDraw = true;
            }
            else if (current > maximum) {
                isDraw = false;
                maximum = current;
                winner = player.getPlayerNumber();
            }
        }
    }
}
