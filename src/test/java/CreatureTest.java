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
    public void setUp() {
        ++count;
        System.out.print("Creature test #" + count + ": testing... ");
    }

    @After
    public void tearDown() {
        System.out.print("finished.\n");
    }

    private Card getCardWithTrait(Trait trait){
        Card c = null;
        while (t.getCommonDeck().getCardCount() != 0) {
            c = t.getCommonDeck().getCard();
            if (c.getTrait().equals(trait)) break;
            c.turnCard();
            if (c.getTrait().equals(trait)) break;
        }
        return c;
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
            if (Trait.isPairTrait(c.getTrait())) break;
        }
        assertTrue("Test failed: addTrait", Trait.isPairTrait(c.getTrait()));
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
        creature.addTrait(getCardWithTrait(Trait.FAT_TISSUE));
        assertTrue("Test failed: isHungry1", creature.isHungry());
        creature.addFood();
        assertFalse("Test failed: isHungry2", creature.isHungry());
    }

    @Test
    public void getTotalHunger() {
        Creature creature = new Creature(p);
        assertEquals("Test failed: getTotalHunger", 1, creature.getTotalHunger());

        creature.addTrait(getCardWithTrait(Trait.PREDATOR));
        creature.addTrait(getCardWithTrait(Trait.HIGH_BODY));

        assertEquals("Test failed: getTotalHunger", 3, creature.getTotalHunger());
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
        creature.addTrait(getCardWithTrait(Trait.FAT_TISSUE));
        assertTrue("Test failed: isFed", creature.isFed());
    }

    @Test
    public void isPoisoned() {
        Creature food = new Creature(p);
        food.addTrait(getCardWithTrait(Trait.POISONOUS));

        Creature predator = new Creature(p);
        predator.addTrait(getCardWithTrait(Trait.PREDATOR));

        predator.attack(food);
        assertFalse("Test failed: isPoisoned", food.isPoisoned());
        assertTrue("Test failed: isPoisoned", predator.isPoisoned());
    }

    @Test
    public void isAttackPossible() {
        Creature food = new Creature(p);
        Creature predator = new Creature(p);
        assertFalse("Test failed: isAttackPossible1", predator.isAttackPossible(food));

        predator.addTrait(getCardWithTrait(Trait.PREDATOR));
        assertTrue("Test failed: isAttackPossible2", predator.isAttackPossible(food));

        food.addTrait(getCardWithTrait(Trait.HIGH_BODY));
        assertFalse("Test failed: isAttackPossible3", predator.isAttackPossible(food));

        predator.addTrait(getCardWithTrait(Trait.HIGH_BODY));
        assertTrue("Test failed: isAttackPossible4", predator.isAttackPossible(food));
    }

    @Test
    public void isAbsoluteAttackPossible() {
        Creature food = new Creature(p);
        Creature predator = new Creature(p);
        assertFalse("Test failed: isAbsoluteAttackPossible", predator.isAbsoluteAttackPossible(food));

        predator.addTrait(getCardWithTrait(Trait.PREDATOR));
        assertTrue("Test failed: isAbsoluteAttackPossible2", predator.isAbsoluteAttackPossible(food));

        food.addTrait(getCardWithTrait(Trait.TAIL_LOSS));
        assertFalse("Test failed: isAbsoluteAttackPossible3", predator.isAbsoluteAttackPossible(food));
    }

    @Test
    public void stealFood() {
        Creature pirate = new Creature(p);
        Creature victim = new Creature(p);

        pirate.addTrait(getCardWithTrait(Trait.PIRACY));

        victim.addTrait(getCardWithTrait(Trait.HIGH_BODY));
        victim.addFood();

        pirate.stealFood(victim);

        assertTrue("Test failed: stealFood", pirate.isSatisfied());
        assertEquals("Test failed: stealFood", 0, victim.getTotalSatiety());
    }
}