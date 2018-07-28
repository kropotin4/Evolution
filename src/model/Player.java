package model;

import model.decks.CommonCardDeck;
import model.decks.DropCardDeck;
import model.decks.PlayerCardDeck;

import java.util.ArrayList;

public class Player {

    PlayerCardDeck playerDeck = new PlayerCardDeck();
    DropCardDeck dropDeck = new DropCardDeck();

    ArrayList<Creature> creatures = new ArrayList<>();

    Player(){}



}
