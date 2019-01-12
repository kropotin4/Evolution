package model;

import model.decks.DropCardDeck;
import model.decks.PlayerCardDeck;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/*********************
 * Что может игрок:
 *
 *      Фаза роста:
 *          *Положить карту на существо (addTrait в разных варациях)
 *      Фаза определения К.Б.
 *          *Бросить кубик (все больше хочется, чтобы это делал сервер)
 *      Фаза питания:
 *          *Атаковать (a)
 *          *Спец умения (это в ответ на соответсвующее сообщение сервера)
 *          *Защита (в ответ на атаку -> у самого существа)
 *          *Взять еду из К.Б.
 *
 *
 *
 *********************/


public class Player implements Serializable {

    Table table;

    String login;
    int playerNumber;

    boolean isAi = false;
    String color;
    boolean isPass = false;

    PlayerCardDeck playerDeck = new PlayerCardDeck();
    DropCardDeck dropDeck = new DropCardDeck();

    ArrayList<Creature> creatures = new ArrayList<>();

    ArrayList<CreaturesPair> communicationCreatures = new ArrayList<>();
    ArrayList<CreaturesPair> cooperationCreatures = new ArrayList<>();
    ArrayList<SymbiosisPair> symbiosisCreatures = new ArrayList<>();


    public Player(Table table, String login, int playerNumber){
        this.table = table;
        this.login = login;
        this.playerNumber = playerNumber;
        color = playerNumber == 0? "#ff0000" :
                playerNumber == 1? "#ffff00" :
                playerNumber == 2? "#00ff00" :
                playerNumber == 3? "#0000ff" : "#ffffff";
    }

    public ArrayList<Creature> getCreatures() {
        return creatures;
    }
    void setFodder(){
        table.setFodder();
    }


    /**
     * Ноль аргументов - абсолютная атака (нужна для клиента)
     * Больше ноля - есть защитные trait => Полноценная атака
     *
     * @param attacker - attack Creature
     * @param defender - defend Creature
     * @param optional - optional Creature
     * @param defendTrait - varlen Defend Traits
     * @return - true if success attack
     */
    public boolean attackCreature(Creature attacker, Creature defender, Creature optional, Card ... defendTrait){
        if(!attacker.isAttackPossible(defender))
            return false;

        if(defendTrait.length == 0 || attacker.isAbsoluteAttackPossible(defender)) {
            attacker.attack(defender);
            table.useScavenger(attacker.getPlayer().playerNumber);

            ///region sound
            try {
                System.out.print("Lion sound");
                AudioInputStream in = AudioSystem.getAudioInputStream(new File("res\\sounds\\rar16.wav").getAbsoluteFile());
                Clip clip = AudioSystem.getClip();
                clip.open(in);
                clip.start();
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
            ///endregion

            return true;
        }



        if(defendTrait[0].getTrait() == Trait.RUNNING){
            if(Dice.rollOneDice() > 3)
                return false;

            if(defendTrait.length == 1)
                return true;
        }

        switch (defendTrait[1].getTrait()){
            case TAIL_LOSS:
                if(Creature.isPairTrait(defendTrait[2].getTrait()))
                    defender.removePairTrait(defendTrait[2].getTrait(), optional);
                else
                    defender.removeTrait(defendTrait[2]);
                break;
            case MIMICRY:
                // Наверное, обойдется без этого
                break;
        }

        return true;
    }
    public boolean defendCreature(Creature defending, Trait  trait){
        //TODO:
        return true;
    }
    public boolean pirateCreature(Creature pirate, Creature victim){
        if(pirate.isPirated() || victim.getTotalSatiety() == 0 || victim.isFed()) return false;

        ///region sound
        try {
            System.out.print("Seagull sound");
            AudioInputStream in = AudioSystem.getAudioInputStream(new File("res\\sounds\\seagull16.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(in);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        ///endregion

        pirate.setPirated(true);

        pirate.addFood();
        victim.reduceFood();

        return true;
    }

    public boolean killCreature(Creature creature){
        ///region sound
        try {
            System.out.print("SOUND IS NOW PLAYING");
            AudioInputStream in = AudioSystem.getAudioInputStream(new File("res\\sounds\\death16.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(in);
            clip.start();

            in.close();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        ///endregion

        if(creatures.remove(creature)){

            for(int i = 0; i < communicationCreatures.size(); ++i){
                if(communicationCreatures.get(i).haveCreature(creature)){

                    communicationCreatures.get(i).firstCreature.removePairTrait(
                            Trait.COMMUNICATION,
                            communicationCreatures.get(i).secondCreature
                    );

                    communicationCreatures.get(i).secondCreature.removePairTrait(
                            Trait.COMMUNICATION,
                            communicationCreatures.get(i).firstCreature
                    );

                    communicationCreatures.remove(i);
                    --i;
                }
            }

            for(int i = 0; i < cooperationCreatures.size(); ++i){
                if(cooperationCreatures.get(i).haveCreature(creature)){

                    cooperationCreatures.get(i).firstCreature.removePairTrait(
                            Trait.COMMUNICATION,
                            cooperationCreatures.get(i).secondCreature
                    );

                    cooperationCreatures.get(i).secondCreature.removePairTrait(
                            Trait.COMMUNICATION,
                            cooperationCreatures.get(i).firstCreature
                    );

                    cooperationCreatures.remove(i);
                    --i;
                }
            }

            for(int i = 0; i < symbiosisCreatures.size(); ++i){
                if(symbiosisCreatures.get(i).haveCreature(creature)){

                    symbiosisCreatures.get(i).crocodile.removePairTrait(
                            Trait.COMMUNICATION,
                            symbiosisCreatures.get(i).bird
                    );

                    symbiosisCreatures.get(i).bird.removePairTrait(
                            Trait.COMMUNICATION,
                            symbiosisCreatures.get(i).crocodile
                    );

                    symbiosisCreatures.remove(i);
                    --i;
                }
            }

            return true;
        }
        return false;
    }
    public boolean addCreature(Card card){
        if(playerDeck.removeCard(card)){
            creatures.add(new Creature(this));
            return true;
        }
        return false;
    }

    public boolean addTraitToCreature(Player player, Creature creature, Card card, boolean isUp){
        if(player.playerDeck.removeCard(card)){
            if(isUp != card.isUp())
                card.turnCard();

            creature.addTrait(card);
            return true;
        }
        return false;
    }
    public boolean addPairTraitToCreature(Creature creature1, Creature creature2, Card card, boolean isUp){
        if(playerDeck.removeCard(card)){
            if(card.getTrait(isUp) == Trait.COOPERATION)
                cooperationCreatures.add(new CreaturesPair(creature1, creature2));
            else if(card.getTrait(isUp) == Trait.COMMUNICATION)
                communicationCreatures.add(new CreaturesPair(creature1, creature2));

            creature1.addPairTrait(card.getTrait(isUp), creature2);
            creature2.addPairTrait(card.getTrait(isUp), creature1);
            return true;
        }
        return false;
    }
    public boolean addSymbiosisTraitToCreature(Creature crocodile, Creature bird, Card card, boolean isUp){
        if(playerDeck.removeCard(card)){
            if(card.getTrait(isUp) != Trait.SYMBIOSIS)
                return false;

            symbiosisCreatures.add(new SymbiosisPair(crocodile, bird));

            crocodile.addSymbiosisTrait(bird, false);
            bird.addSymbiosisTrait(crocodile, true);
            return true;
        }
        return false;
    }

    public boolean getFoodFromFodder(int creatureID){
        Creature creature = findCreature(creatureID);
        if(!table.isFodderBaseEmpty() && !creature.isSatisfied()) {
            creature.addFood();

            //if(creature.isGrazingActive())
            //    table.getFood(2);
            //else
            table.getFood(1 + getGrazingActiveNumber());

            ///region sound
            try {
                System.out.print("Mouse sound");
                AudioInputStream in = AudioSystem.getAudioInputStream(new File("res\\sounds\\mouse16.wav").getAbsoluteFile());
                Clip clip = AudioSystem.getClip();
                clip.open(in);
                clip.start();
                if (getGrazingActiveNumber() != 0){
                    System.out.print("Grazing sound");
                    in = AudioSystem.getAudioInputStream(new File("res\\sounds\\grazing16.wav").getAbsoluteFile());
                    clip = AudioSystem.getClip();
                    clip.open(in);
                    clip.start();
                }
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
            ///endregion

            return true;
        }
        return false;
    }

    public boolean haveCreaturesWithTrait(Trait trait){
        for(Creature creature : creatures){
            if(creature.findTrait(trait))
                return true;
        }
        return false;
    }
    public boolean haveCreaturesWithActiveScavenger(){
        for(Creature creature : creatures){
            if(creature.isActiveScavenger())
                return true;
        }
        return false;
    }

    public boolean haveActiveCreatures() {
        /*for (Creature creature : creatures){
            if (creature.findTrait(Trait.GRAZING)) return true;
        }*/
        for (Creature creature : creatures){
            if (!creature.isSatisfied()){
                if (table.getFodder() > 0) return true;
                if (creature.getFatQuantity() > 0) return true;
                for (Player player : table.getPlayers()) {
                    for (Creature victim : player.getCreatures()) {
                        if (creature.isAttackPossible(victim) ||
                            creature.isPiratingPossible(victim)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public int getGrazingActiveNumber(){
        int res = 0;
        for(Creature creature : creatures){
            if(creature.isGrazingActive())
                ++res;
        }
        return res;
    }
    public int getScavengerNumber(){
        int res = 0;
        for(Creature creature : creatures){
            if(creature.findTrait(Trait.SCAVENGER))
                ++res;
        }
        return res;
    }

    public void getCard(){
        if(table.commonDeck.getCardCount() > 0)
            playerDeck.addCard(table.getCard());
    }
    public ArrayList<CreaturesPair> getCommunicationCreatures(){
        return communicationCreatures;
    }
    public ArrayList<CreaturesPair> getCooperationCreatures(){
        return cooperationCreatures;
    }
    public ArrayList<SymbiosisPair> getSymbiosisCreatures(){
        return symbiosisCreatures;
    }
    public int getPlayerCardsNumber(){
        return playerDeck.getCardsNumber();
    }
    public PlayerCardDeck getPlayerCardDeck(){
        return playerDeck;
    }

    // TODO:Дописать //на мой взгляд, всё нормально
    public boolean canMove(){
        return !(isPass ||
                ((table.getCurrentPhase() == Phase.GROWTH && playerDeck.getCardsNumber() == 0) ||
                        (table.getCurrentPhase() == Phase.EATING && !haveActiveCreatures())));
    }

    public void setAI(boolean isAi){
        this.isAi = isAi;
    }
    public void setPass(boolean isPass){
        this.isPass = isPass;
        if(isPass) table.passNumber++;
        else table.passNumber--;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getLogin(){
        return login;
    }
    public void setPlayerNumber(int playerNumber){
        this.playerNumber = playerNumber;
    }

    public Creature findCreature(int CreatureId){
        for(Creature creature : creatures){
            if(creature.getId() == CreatureId) return creature;
        }

        return null;
    }
    public int getPlayerNumber(){
        return playerNumber;
    }

    public int getScore(){
        int res = 0;
        for (Creature creature : this.creatures){
            res += 1 + creature.getCards().size() + creature.getTotalHunger();
        }
        return res;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "Player{" +
                ", login='" + login + '\'' +
                ", playerNumber=" + playerNumber +
                ", isPass=" + isPass +
                ", playerDeck=" + playerDeck +
                ", dropDeck=" + dropDeck +
                ", creatures=" + creatures +
                ", communicationCreatures=" + communicationCreatures +
                ", cooperationCreatures=" + cooperationCreatures +
                ", symbiosisCreatures=" + symbiosisCreatures +
                '}';
    }
}
