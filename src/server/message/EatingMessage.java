package server.message;

import model.Creature;
import model.Phase;
import model.Player;
import model.Trait;

import java.util.ArrayList;
import java.util.UUID;

/***************************************
 * Сообщения о действиях в фазе питания
 **************************************/


public class EatingMessage extends Message{

    private final int type;
    private boolean haveAction = false;

    private int eating;

    private Trait trait = null;

    private int attacker;
    private int defending;

    private int playerDefending;
    private int playerAttacker;

    //Питание:
    public EatingMessage(int eatingCreature, boolean haveAction){ //Взятие еды из К.Б. (Существо)
        super(Phase.EATING, MessageType.EATING);
        type = 0;

        this.haveAction = haveAction;
        this.eating = eatingCreature;
    }
    public EatingMessage(int attackerCreature, int playerDefending, int defendingCreature, boolean haveAction){ //Атака существа (Существо + Свойства, Существо) Пока без свойств
        super(Phase.EATING, MessageType.EATING);
        type = 1;

        this.haveAction = haveAction;
        this.attacker = attackerCreature;
        this.defending = defendingCreature;
        this.playerDefending = playerDefending; // Атакует тот, кто ходит.
    }
    public EatingMessage(int playerAttacker, int defendingCreature, Trait trait){ //Защита от атаки (Существо + Свойства)
        super(Phase.EATING, MessageType.EATING);
        type = 2;

        this.playerAttacker = playerAttacker;
        this.defending = defendingCreature;
        this.trait = trait;
    }

    public int getEatingCreautureId(){
        if(type == 0 || type == 1) return eating;
        return attacker;
    }

    public int getAttackerCreatureId(){
        return attacker;
    }
    public int getDefendingCreatureId(){
        return defending;
    }

    public int getDefendingPlayerNumber(){
        return playerDefending;
    }
    public int getAttackerPlayerNumber(){
        return playerAttacker;
    }

    public Trait getTrait(){
        return trait;
    }

    public int getType(){
        return type;
    }
    public boolean isHaveAction(){
        return haveAction;
    }
}
