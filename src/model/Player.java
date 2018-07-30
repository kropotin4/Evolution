package model;

import model.decks.CommonCardDeck;
import model.decks.DropCardDeck;
import model.decks.PlayerCardDeck;

import java.util.ArrayList;

public class Player {

    Table table;

    String login;

    PlayerCardDeck playerDeck = new PlayerCardDeck();
    DropCardDeck dropDeck = new DropCardDeck();

    ArrayList<Creature> creatures = new ArrayList<>();

    public Player(String login){
        this.login = login;
    }

    boolean killCreature(Creature creature){
        if(creatures.remove(creature)){
            for(Creature creature1 : creature.communicationList){
                creature.removePairTrait(Trait.COMMUNICATION, creature1);
            }
            for(Creature creature1 : creature.cooperationList){
                creature.removePairTrait(Trait.COOPERATION, creature1);
            }
            for(Creature creature1 : creature.symbiontList){
                creature.removePairTrait(Trait.SYMBIOSYS, creature1);
            }
            for(Creature creature1 : creature.otherAnimalList){
                creature.removePairTrait(Trait.SYMBIOSYS, creature1);
            }

            return true;
        }
        return false;
    }
    boolean addCreature(Card card){
        if(playerDeck.removeCard(card)){
            creatures.add(new Creature(this));
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
    boolean addPairTraitToCreature(Creature creature1, Creature creature2, Card card, boolean isUp){
        if(playerDeck.removeCard(card)){
            creature1.addPairTrait(card.getTrait(isUp), creature2);
            creature2.addPairTrait(card.getTrait(isUp), creature1);
            return true;
        }
        return false;
    }

    void getCard(){
        playerDeck.addCard(Table.getCard());
    }

    public String getLogin(){
        return login;
    }
}
