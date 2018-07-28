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

    boolean addCreature(Card card){
        if(playerDeck.removeCard(card)){
            creatures.add(new Creature());
            return true;
        }
        return false;
    }

    boolean addTraitToCreature(Creature creature, Card card, boolean isUp){
        if(playerDeck.removeCard(card)){
            creature.addTrait(card.getTrait(isUp));
            return true;
        }
        return false;
    }

    void getCard(){
        playerDeck.addCard(Table.getCard());
    }
}
