package model;

import control.Controller;

import java.util.ArrayList;
import java.util.Comparator;

public class EndGameInfo {
    public int winner;
    public boolean isDraw = false;
    public ArrayList<Player> players;
    public int maximum = -1;

    public EndGameInfo(Controller controller){
        players = controller.getPlayers();
        players.sort(Comparator.comparingInt(Player::getScore).reversed());
        winner = -1;
        if (players.get(0).getScore() == players.get(1).getScore()) isDraw = true;
    }
}
