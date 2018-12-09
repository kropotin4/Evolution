package model;

import model.decks.DropCardDeck;
import model.decks.PlayerCardDeck;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

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

        if(attacker.isAbsoluteAttackPossible(defender)) {
            attacker.attack(defender);
            return true;
        }

        if(defendTrait.length == 0){
            attacker.attack(defender);
            return true;
        }

        //Правило постановки trait: первый всегда running
        //Больше 3 trait быть (???) не должно

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

    public boolean killCreature(Creature creature){
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
        //TODO: Проработать механику взятия еда на всех уровнях
        if(!table.isFodderBaseEmpty()) {
            this.findCreature(creatureID).addFood();
            table.getFood(1);
            return true;
        }
        return false;
    }
    public boolean getFoodFromFodderToFat(int creatureID){
        Creature creature = findCreature(creatureID);
        if(!table.isFodderBaseEmpty() && !creature.isSatisfied()) {
            this.findCreature(creatureID).addFat();
            table.getFood(1);
            return true;
        }
        return false;
    }

    public void getCard(){
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

    public void setPass(boolean isPass){
        this.isPass = isPass;
        if(isPass) table.passNumber++;
        else table.passNumber--;
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
}
