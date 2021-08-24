package model.ai;

import control.Controller;
import model.*;

import java.util.ArrayList;

public class AiThread extends Thread {

    Controller controller;
    Table table;

    int playerNumber = -1;

    Creature pirate;
    Creature predator;
    Creature victim;

    Creature first;
    Creature second;
    Card card;

    public AiThread(Controller controller){
        super("AI Thread");
        this.controller = controller;
    }
    public AiThread(Table table, int playerNumber){
        super("AI Thread");
        this.table = table;
        this.playerNumber = playerNumber;
    }

    @Override
    public void run() {
        System.out.println("AI run");

        if(table.getPlayerTurn() != playerNumber)
            throw new RuntimeException("AiThread: table.getPlayerTurn() != playerNumber");

        switch (table.getCurrentPhase()){
            case GROWTH:

                switch (calcGrowth()){
                    case 0:
                        table.getPlayers().get(playerNumber).setPass(true);
                        break;

                    case 1:
                        table.getPlayers().get(playerNumber).addCreature(card);
                        break;

                    case 2:
                        first.addPairTrait(card, second);
                        second.addPairTrait(card, first);
                        table.getPlayers().get(playerNumber).getPlayerCardDeck().removeCard(card);
                        break;

                    case 3:
                        first.addTrait(card);
                        table.getPlayers().get(playerNumber).getPlayerCardDeck().removeCard(card);
                        break;

                    default:
                        break;
                }
                break;

            case EATING:

                switch (calcEating()){
                    case 0:
                        if (predator.getHibernatingTime() == 0) predator.setHibernating(true);
                        else if (!predator.getFood()) table.getPlayers().get(playerNumber).setPass(true);
                        break;

                    case 1:
                        predator.attack(victim);
                        break;

                    case 2:
                        pirate.pirate(victim);
                        break;

                    default:
                        //controller.setPlayerPass(controller.getPlayerTurn());
                        table.getPlayers().get(playerNumber).setPass(true);
                        break;
                }

                break;
            default:
                break;
        }

        table.doNextMove();
    }

    // Growth phase move calculation
    int calcGrowth() {
        if (table.getPlayers().get(playerNumber).getPlayerCardsNumber() == 0) {
            return 0;
        }
        int i = 0;
        if (table.getPlayers().get(playerNumber).getCreatures().size() < 3) {
            for (Card card = table.getPlayers().get(playerNumber).getPlayerCardDeck().getCardDeck().get(i).getCard();
                 i < table.getPlayers().get(playerNumber).getPlayerCardsNumber();
                 card = table.getPlayers().get(playerNumber).getPlayerCardDeck().getCardDeck().get(i++).getCard()) {
                if (card.getTrait() != Trait.PREDATOR || card.getTrait() != Trait.PIRACY || card.getTrait() != Trait.PARASITE) {
                    card.turnCard();
                    if (card.getTrait() == Trait.PREDATOR || card.getTrait() == Trait.PIRACY || card.getTrait() == Trait.PARASITE) continue;
                }
                this.card = card;
                return 1;
            }
        }
        if (table.getPlayers().get(playerNumber).getCreatures().size() == 0) {
            this.card = table.getPlayers().get(playerNumber).getPlayerCardDeck().getCardDeck().get(0).getCard();
            return 1;
        }

        i = 0;
        for (Card card = table.getPlayers().get(playerNumber).getPlayerCardDeck().getCardDeck().get(i).getCard();
             i < table.getPlayers().get(playerNumber).getPlayerCardsNumber();
             card = table.getPlayers().get(playerNumber).getPlayerCardDeck().getCardDeck().get(++i).getCard()) {
            if (card.getTrait() == Trait.COMMUNICATION || card.getTrait() == Trait.COOPERATION) {
                for (Creature first : table.getPlayers().get(playerNumber).getCreatures()) {
                    for (Creature second : table.getPlayers().get(playerNumber).getCreatures()) {
                        if (second == first) continue;
                        if (first.canAddPairTrait(card.getTrait(), second)) {
                            this.first = first;
                            this.second = second;
                            this.card = card;
                            return 2;
                        }
                    }
                }
            }
            if (card.getTrait() == Trait.PARASITE) {
                for (Creature creature : table.getPlayers().get(0).getCreatures()) {
                    if (creature.canAddTrait(Trait.PARASITE)) {
                        first = creature;
                        this.card = card;
                        return 3;
                    }
                }
            }
            for (Creature creature : table.getPlayers().get(playerNumber).getCreatures()) {
                if (creature.canAddTrait(card.getTrait()) &&
                        !(card.getTrait() == Trait.SCAVENGER && creature.findTrait(Trait.PREDATOR)) &&
                        !(card.getTrait() == Trait.PREDATOR && creature.findTrait(Trait.SCAVENGER)) &&
                        !(card.getTrait() == Trait.PREDATOR && creature.getFatQuantity() > 1)) {
                    first = creature;
                    this.card = card;
                    return 3;
                }
            }
            if (card.getTrait(false) == card.getTrait(true)) {
                ++i;
                continue;
            }
        }

        return 0;
    }

    // Eating phase move calculation
    int calcEating(){
        ArrayList<Creature> predatorList = new ArrayList<>();
        Player player = table.getPlayers().get(playerNumber);
        for (Creature creature : player.getCreatures()){
            if (creature.findTrait(Trait.PREDATOR)){
                predatorList.add(creature);
            }
        }
        for (Creature creature : predatorList){
            predator = creature;
            for (Creature prey : table.getPlayers().get(0).getCreatures()){
                if (predator.isAbsoluteAttackPossible(prey)){
                    victim = prey;
                    return 1;
                }
            }
        }
        ArrayList<Creature> pirateList = new ArrayList<>();
        for (Creature creature : player.getCreatures()){
            if (creature.findTrait(Trait.PIRACY)){
                predatorList.add(creature);
            }
        }
        for (Creature creature : pirateList){
            pirate = creature;
            for (Creature prey : table.getPlayers().get(0).getCreatures()){
                if (predator.isPiratingPossible(prey)){
                    victim = prey;
                    return 2;
                }
            }
        }
        for (Creature creature : table.getPlayers().get(playerNumber).getCreatures()){
            if (!creature.isSatisfied()){
                predator = creature;
                return 0;
            }
        }
        return -1;
    }
}
