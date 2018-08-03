package server.message;

import model.Creature;
import model.Phase;
import model.Player;
import model.Trait;

import java.util.ArrayList;
import java.util.UUID;

public class EatingMessage extends Message{

    private final int type;

    private UUID eating;

    private Trait trait = null;
    private int grazingCount = 0;

    private UUID attacker;
    private UUID defending;

    private int playerDefending;

    private ArrayList<Trait> traits;

    //Питание:
    public EatingMessage(UUID eatingCreature){ //Взятие еды из К.Б. (Существо)
        super(Phase.EATING, MessageType.EATING);
        type = 0;

        this.eating = eatingCreature;
    }
    public EatingMessage(UUID pirateCreature, UUID victimCreature){ //Взятие еды из К.Б. + пиратство
        super(Phase.EATING, MessageType.EATING);
        type = 4;

        this.attacker = pirateCreature;
        this.defending = victimCreature;
    }
    public EatingMessage(UUID eatingCreature, Trait trait, int number){ //Взятие еды из К.Б. + Топотун
        super(Phase.EATING, MessageType.EATING);
        type = 1;

        this.eating = eatingCreature;
        this.trait = trait;
        this.grazingCount = number;
    }
    public EatingMessage(UUID attackerCreature, int playerDefending, UUID defendingCreature){ //Атака существа (Существо + Свойства, Существо) Пока без свойств
        super(Phase.EATING, MessageType.EATING);
        type = 2;

        this.attacker = attackerCreature;
        this.defending = defendingCreature;
        this.playerDefending = playerDefending; // Атакует тот, кто ходит.
    }
    public EatingMessage(UUID defendingCreature, ArrayList<Trait> traits){ //Защита от атаки (Существо + Свойства)
        super(Phase.EATING, MessageType.EATING);
        type = 3;

        this.defending = defendingCreature;
        this.traits = traits;
    }

    public UUID getEatingCreautureId(){
        if(type == 0 || type == 1) return eating;
        return attacker;
    }

    public UUID getAttackerCreatureId(){
        return attacker;
    }
    public UUID getDefendingCreatureId(){
        return defending;
    }

    public int getDefendingPlayerNumber(){
        return playerDefending;
    }

    public Trait getTrait(){
        return trait;
    }
    public int getGrazingCount(){
        return grazingCount;
    }

    public ArrayList<Trait> getDefendingTraits(){
        return traits;
    }

    public int getType(){
        return type;
    }
}
