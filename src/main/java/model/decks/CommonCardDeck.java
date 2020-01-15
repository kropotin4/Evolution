package model.decks;

import model.Card;
import model.Trait;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/******************************************
 * The deck from which players take cards.
 *
 * @author akropotin
 *
 * @see Card
 ******************************************/
public class CommonCardDeck implements Serializable {
    private ArrayList<Card> deck = new ArrayList<>(21);

    /**********************
     * CommonCardDeck class constructor.
     * @param quarter number of common deck quarter in current deck.
     * @see Card
     **********************/
    public CommonCardDeck(int quarter){
        if(quarter <= 0) throw new RuntimeException("CommonCardDeck: quarter <= 0");

        for(int i = 0; i < quarter; ++i)
            deck.addAll(oneQuarter());

        deck = shuffle(deck);

        System.out.println("CommonCardDeck create: " + deck.size());
    }

    /**********************
     * CommonCardDeck class constructor.
     * @return first card in deck
     * @see Card
     **********************/
    public Card getCard(){
        Card ret = deck.get(deck.size() - 1);
        deck.remove(deck.size() - 1);
        return ret;
    }

    /**********************
     * Get number of card in deck.
     * @return cards number
     * @see Card
     **********************/
    public int getCardCount(){
        return deck.size();
    }

    /**********************
     * Set of cards -- quarter of common deck.
     * @return ArrayList<Card> with cards
     * @see Card
     **********************/
    private ArrayList<Card> oneQuarter(){
        ArrayList<Card> quarter = new ArrayList<>(21);

        quarter.add(new Card(Trait.CAMOUFLAGE, Trait.FAT_TISSUE));
        quarter.add(new Card(Trait.BURROWING, Trait.FAT_TISSUE));
        quarter.add(new Card(Trait.SHARP_VISION, Trait.FAT_TISSUE));
        quarter.add(new Card(Trait.SYMBIOSIS));
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

    /**********************
     * Function that shuffle input ArrayList of cards.
     * @param cards ArrayList of cards that need shuffle.
     * @return ArrayList<Card> with shuffle cards
     * @see Card
     **********************/
    private ArrayList<Card> shuffle(ArrayList<Card> cards){
        Random random = new Random();
        int size = cards.size();
        ArrayList<Card> resDeck = new ArrayList<>(size);

        for(int i = 0; i < size; ++i){
            resDeck.add(cards.remove(random.nextInt(cards.size())));
        }

        return resDeck;
    }
}
