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

    //Должен быть набор действий к каждому Trait
    //Или здесь или в trait или можно создать под каждое свойство свой класс







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
