//package java;

import model.Card;
import model.decks.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CardDeckTest {
    static int count = 0;
    @Before
    public void setUp() throws Exception {
        ++count;
        System.out.print("Desk test #" + count + ": testing... ");
    }


    @After
    public void tearDown() throws Exception {
        System.out.print("finished.\n");
    }

    @Test
    public void getCard() {
        CommonCardDeck c = new CommonCardDeck(1);
        DropCardDeck d = new DropCardDeck();
        Card q = c.getCard();
        d.addCard(q);
        assertTrue("Test failed: getCard", d.getCardDeck().contains(q));
    }

    @Test
    public void getCardCount() {
        CommonCardDeck c = new CommonCardDeck(1);
        CommonCardDeck c2 = new CommonCardDeck(2);
        assertTrue("Test failed: getCardCount", c.getCardCount() * 2 == c2.getCardCount());
        int cur = c.getCardCount();
        int testnum = 5;
        for (int i = 0; i < testnum; ++i){
            c.getCard();
        }
        assertTrue("Test failed: getCardCount", cur - testnum == c.getCardCount());
    }

    @Test
    public void addCard() {
        CommonCardDeck c = new CommonCardDeck(1);
        PlayerCardDeck p = new PlayerCardDeck();
        Card crd = c.getCard();
        p.addCard(crd);
        assertFalse("Test failed: addCard", p.getCardDeck().isEmpty());
        //(p.getCardDeck().contains(crd));
    }

    @Test
    public void removeCard() {
        CommonCardDeck c = new CommonCardDeck(1);
        PlayerCardDeck p = new PlayerCardDeck();
        Card crd = c.getCard();
        p.addCard(crd);
        p.removeCard(crd);
        assertFalse("Test failed: removeCard", p.getCardDeck().contains(crd));
    }
}