package model;

import control.Controller;

import java.util.ArrayList;
import java.util.Comparator;

public class EndGameInfo {
    public boolean isDraw = false;
    public ArrayList<Player> players;

    public EndGameInfo(Controller controller){
        players = controller.getPlayers();
        players.sort(Comparator.comparingInt(Player::getScore).reversed());
        if (players.get(0).getScore() == players.get(1).getScore()) isDraw = true;
    }
}
