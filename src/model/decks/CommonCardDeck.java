package model.decks;

import model.Card;
import model.Trait;

import java.util.ArrayList;

public class CommonCardDeck {
    private ArrayList<Card> deck = new ArrayList<>(21);

    public CommonCardDeck(int quarter){
        if(quarter <= 0) throw new RuntimeException("CommonCardDeck: quarter <= 0");

        for(int i = 0; i < quarter; ++i)
            deck.addAll(oneQuarter());
    }

    public Card getCard(){
        Card ret = deck.get(deck.size() - 1);
        deck.remove(deck.size() - 1);
        return ret;
    }

    public int getCardCount(){
        return deck.size();
    }

    private ArrayList<Card> oneQuarter(){
        ArrayList<Card> quarter = new ArrayList<>(21);

        quarter.add(new Card(Trait.CAMOUFLAGE, Trait.FAT_TISSUE));
        quarter.add(new Card(Trait.BURROWING, Trait.FAT_TISSUE));
        quarter.add(new Card(Trait.SHARP_VISION, Trait.FAT_TISSUE));
        quarter.add(new Card(Trait.SYMBIOSYS));
        quarter.add(new Card(Trait.PIRACY));
        quarter.add(new Card(Trait.GRAZING, Trait.FAT_TISSUE));
        quarter.add(new Card(Trait.TAIL_LOSS));
        quarter.add(new Card(Trait.HIBERNATION, Trait.PREDATOR));
        quarter.add(new Card(Trait.POISONOUS, Trait.PREDATOR));
        quarter.add(new Card(Trait.COMMUNICATION, Trait.PREDATOR));
        quarter.add(new Card(Trait.SCAVENGER));
        quarter.add(new Card(Trait.RUNNING));
        quarter.add(new Card(Trait.MIMICRY));
        quarter.add(new Card(Trait.SWIMMING));
        quarter.add(new Card(Trait.SWIMMING));
        quarter.add(new Card(Trait.PARASITE, Trait.PREDATOR));
        quarter.add(new Card(Trait.PARASITE, Trait.FAT_TISSUE));
        quarter.add(new Card(Trait.COOPERATION, Trait.PREDATOR));
        quarter.add(new Card(Trait.COOPERATION, Trait.FAT_TISSUE));
        quarter.add(new Card(Trait.HIGH_BODY, Trait.PREDATOR));
        quarter.add(new Card(Trait.HIGH_BODY, Trait.FAT_TISSUE));

        return quarter;
    }
}
