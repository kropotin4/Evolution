package server.message;

import model.Creature;
import model.Phase;
import model.Trait;

import java.util.ArrayList;

public class EatingMessage extends Message{

    //Питание:
    EatingMessage(Phase phase, Creature creature){ //Взятие еды из К.Б. (Существо)
        super(Phase.EATING);
    }
    EatingMessage(Phase phase, int type, Creature creature, Trait trait){ //Взятие еды из К.Б. + Топотун + пец действие вне хода (Существо + Свойство)
        super(Phase.EATING);
    }
    EatingMessage(Phase phase, Creature attacker, Creature defending){ //Атака существа (Существо + Свойства, Существо) Пока без свойств
        super(Phase.EATING);
    }
    EatingMessage(Phase phase, Creature defending, ArrayList<Trait> traits){ //Защита от атаки (Существо + Свойства)
        super(Phase.EATING);
    }
}
