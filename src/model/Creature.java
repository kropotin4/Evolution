package model;

import java.util.ArrayList;

/****************************
 * responsible for the creature
 * **************************/

public class Creature {

    int totalHunger = 1;
    int fatCapacity = 0;
    int fatSize = 0;

    boolean isPredator = false;
    boolean isBig = false;
    boolean isRunning = false;
    boolean isMimetic = false;
    boolean isGrazing = false;
    boolean isPoisonous = false;
    boolean isTailLossable = false;
    boolean isHibernatable = false;
    boolean isScavenger = false;
    boolean isPirate = false;
    boolean isBurrowing = false;
    boolean isCamouflaged = false;
    boolean isSharp = false;
    boolean isInfected = false;
    boolean isSwimming = false;

    ArrayList<Creature> communicationList = new ArrayList<>();
    ArrayList<Creature> cooperationList = new ArrayList<>();

    //crocodile: this creature can not eat if any symbiont is hungry; this creature can not be eaten if this animal is alive
    ArrayList<Creature> symbiontList = new ArrayList<>();

    //birds: can not eat if this creature is hungry; can not be eaten if this animal is alive
    ArrayList<Creature> otherAnimalList = new ArrayList<>();


    //Creature`s traits list (in order of obtaining)
    ArrayList<Trait> traits = new ArrayList<>();

    Creature(){

    }

    //Должен быть набор действий к каждому Trait
    //Или здесь или в trait или можно создать под каждое свойство свой класс







    //Добавить свойство
    boolean addTrait(Trait trait){
        for(Trait t : traits) {
            if(t.equals(trait))
                return false;
        }
        traits.add(trait);
        return true;
    }
    //Удалить свойство
    void deleteTrait(Trait trait){
        traits.remove(trait);
    }
}
