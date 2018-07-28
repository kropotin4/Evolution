package model;

import java.util.ArrayList;

public class CardDeck {

    CardDeck(int quarter){
        if(quarter <= 0);


    }

    void oneQuarter(){
        ArrayList<Card> quarter = new ArrayList<>(21);

        quarter.add(new Card(Trait.CAMOUFLAGE));
        quarter.add(new Card(Trait.BURROWING));
        quarter.add(new Card(Trait.SHARP_VISION));
        quarter.add(new Card(Trait.SYMBIOSYS));
        quarter.add(new Card(Trait.PIRACY));
        quarter.add(new Card(Trait.GRAZING));
        quarter.add(new Card(Trait.TAIL_LOSS));
        quarter.add(new Card(Trait.HIBERNATION));
        quarter.add(new Card(Trait.POISONOUS));
        quarter.add(new Card(Trait.COMMUNICATION, Trait.PREDATOR));
        quarter.add(new Card(Trait.SCAVENGER));
        quarter.add(new Card(Trait.RUNNING));
        quarter.add(new Card(Trait.MIMICRY));
        quarter.add(new Card(Trait.SWIMMING));
        quarter.add(new Card(Trait.SWIMMING));
        quarter.add(new Card(Trait.PARASITE, Trait.PREDATOR));

    }
}
