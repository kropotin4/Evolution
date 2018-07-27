package model;

import java.util.ArrayList;

/****************************
 * Класс, отвечающий за существо
 * **************************/

public class Creature {

    //Свойства существа -> в порядке их получения
    ArrayList<Trait> traits = new ArrayList<>();

    Creature(){

    }

    //Добавить свойство
    void addTrait(Trait trait){
        for(Trait t : traits) {
            if(t.equals(trait)) return;
        }

        traits.add(trait);
    }
    //Удалить свойство
    void deleteTrait(Trait trait){
        traits.remove(trait);
    }
}
