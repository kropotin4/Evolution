//package java;

import model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CreatureTest {
    static int count = 0;
    Table t = new Table(10, 1);
    Player p = new Player(t, "login", 1);

    @Before
    public void setUp() throws Exception {
        ++count;
        System.out.print("Creature test #" + count + ": testing... ");
    }

    @After
    public void tearDown() throws Exception {
        System.out.print("finished.\n");
    }

    @Test
    public void addTrait() {
        Creature creature = new Creature(p);
        Card c = t.getCommonDeck().getCard();
        creature.addTrait(c);
        assertTrue("Test failed: addTrait", creature.findCard(c));
    }

    @Test
    public void findTrait() {
        Creature creature = new Creature(p);
        Card c = t.getCommonDeck().getCard();
        creature.addTrait(c);
        assertTrue("Test failed: findTrait", creature.findTrait(c.getTrait()));
    }

    @Test
    public void isPairTrait() {
        Card c = null;
        while (t.getCommonDeck().getCardCount() != 0) {
            c = t.getCommonDeck().getCard();
            if (c.getTrait().equals(Trait.COMMUNICATION)) break;
            if (c.getTrait().equals(Trait.COOPERATION)) break;
            if (c.getTrait().equals(Trait.SYMBIOSIS)) break;
        }
        assertTrue("Test failed: addTrait", Creature.isPairTrait(c.getTrait()));
    }

    @Test
    public void addFood() {
        Creature creature = new Creature(p);
        creature.addFood();
        assertTrue("Test failed: addFood", creature.isSatisfied());
    }

    @Test
    public void reduceFood() {
        Creature creature = new Creature(p);
        creature.addFood();
        assertTrue("Test failed: addFood in reduce test", creature.isSatisfied());
        creature.reduceFood();
        assertFalse("Test failed: reduceFood", creature.isSatisfied());
    }

    @Test
    public void isHungry() {
        Creature creature = new Creature(p);
        Card c = null;
        while (t.getCommonDeck().getCardCount() != 0) {
            c = t.getCommonDeck().getCard();
            if (c.getTrait().equals(Trait.FAT_TISSUE)) break;
        }
        creature.addTrait(c);
        assertTrue("Test failed: isHungry1", creature.isHungry());
        creature.addFood();
        assertFalse("Test failed: isHungry2", creature.isHungry());
    }

    @Test
    public void getTotalHunger() {
        Creature creature = new Creature(p);
        assertEquals("Test failed: getTotalHunger", 1, creature.getTotalHunger());
        Card c = null;
        while (t.getCommonDeck().getCardCount() != 0) {
            c = t.getCommonDeck().getCard();
            if (c.getTrait().equals(Trait.PREDATOR)) break;
            if (c.getTrait().equals(Trait.HIGH_BODY)) break;
        }
        creature.addTrait(c);
        assertEquals("Test failed: getTotalHunger", 2, creature.getTotalHunger());
    }

    @Test
    public void getTotalSatiety() {
        Creature creature = new Creature(p);
        assertEquals("Test failed: getTotalSatiety", 0, creature.getTotalSatiety());
        creature.addFood();
        assertEquals("Test failed: getTotalSatiety", 1, creature.getTotalSatiety());
    }

    @Test
    public void isSatisfied() {
        Creature creature = new Creature(p);
        assertFalse("Test failed: isSatisfied", creature.isSatisfied());
        creature.addFood();
        assertTrue("Test failed: isSatisfied", creature.isSatisfied());
    }

    @Test
    public void isFed() {
        Creature creature = new Creature(p);
        assertFalse("Test failed: isFed", creature.isFed());
        creature.addFood();
        assertTrue("Test failed: isFed", creature.isFed());
        Card c = null;
        while (t.getCommonDeck().getCardCount() != 0) {
            c = t.getCommonDeck().getCard();
            if (c.getTrait().equals(Trait.FAT_TISSUE)) break;
        }
        creature.addTrait(c);
        assertTrue("Test failed: isFed", creature.isFed());
    }

    @Test
    public void isPoisoned() {
        Creature food = new Creature(p);
        Card c = null;
        while (t.getCommonDeck().getCardCount() != 0) {
            c = t.getCommonDeck().getCard();
            if (c.getTrait().equals(Trait.POISONOUS)) break;
            c.turnCard();
            if (c.getTrait().equals(Trait.POISONOUS)) break;
        }
        food.addTrait(c);
        Creature raubtier = new Creature(p);
        while (t.getCommonDeck().getCardCount() != 0) {
            c = t.getCommonDeck().getCard();
            if (c.getTrait().equals(Trait.PREDATOR)) break;
        }
        raubtier.addTrait(c);
        raubtier.attack(food);
        assertFalse("Test failed: isPoisoned", food.isPoisoned());
        assertTrue("Test failed: isPoisoned", raubtier.isPoisoned());
    }

    @Test
    public void isAttackPossible() {
        Creature food = new Creature(p);
        Creature raubtier = new Creature(p);
        assertFalse("Test failed: isAttackPossible1", raubtier.isAttackPossible(food));
        Card c = null;
        while (t.getCommonDeck().getCardCount() != 0) {
            c = t.getCommonDeck().getCard();
            if (c.getTrait().equals(Trait.PREDATOR)) break;
        }
        raubtier.addTrait(c);
        assertTrue("Test failed: isAttackPossible2", raubtier.isAttackPossible(food));

        while (t.getCommonDeck().getCardCount() != 0) {
            c = t.getCommonDeck().getCard();
            if (c.getTrait().equals(Trait.HIGH_BODY)) break;
            c.turnCard();
            if (c.getTrait().equals(Trait.HIGH_BODY)) break;
        }
        food.addTrait(c);
        assertFalse("Test failed: isAttackPossible3", raubtier.isAttackPossible(food));
        raubtier.addTrait(c);
        assertTrue("Test failed: isAttackPossible4", raubtier.isAttackPossible(food));
    }

    @Test
    public void isAbsoluteAttackPossible() {
        Creature food = new Creature(p);
        Creature raubtier = new Creature(p);
        assertFalse("Test failed: isAbsoluteAttackPossible", raubtier.isAbsoluteAttackPossible(food));
        Card c = null;
        while (t.getCommonDeck().getCardCount() != 0) {
            c = t.getCommonDeck().getCard();
            if (c.getTrait().equals(Trait.PREDATOR)) break;
        }
        raubtier.addTrait(c);
        assertTrue("Test failed: isAbsoluteAttackPossible2", raubtier.isAbsoluteAttackPossible(food));
        while (t.getCommonDeck().getCardCount() != 0) {
            c = t.getCommonDeck().getCard();
            if (c.getTrait().equals(Trait.TAIL_LOSS)) break;
        }
        food.addTrait(c);
        assertFalse("Test failed: isAbsoluteAttackPossible3", raubtier.isAbsoluteAttackPossible(food));
    }

    @Test
    public void stealFood() {
        Creature pirate = new Creature(p);
        Creature victim = new Creature(p);
        Card c = null;
        while (t.getCommonDeck().getCardCount() != 0) {
            c = t.getCommonDeck().getCard();
            if (c.getTrait().equals(Trait.PIRACY)) break;
        }
        pirate.addTrait(c);
        while (t.getCommonDeck().getCardCount() != 0) {
            c = t.getCommonDeck().getCard();
            if (c.getTrait().equals(Trait.HIGH_BODY)) break;
            if (c.getTrait().equals(Trait.PARASITE)) break;
            if (c.getTrait().equals(Trait.PREDATOR)) break;
        }
        victim.addTrait(c);
        victim.addFood();
        pirate.stealFood(victim);
        assertTrue("Test failed: stealFood", pirate.isSatisfied());
        assertEquals("Test failed: stealFood", 0, victim.getTotalSatiety());
    }
}