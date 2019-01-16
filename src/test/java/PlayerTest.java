import model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PlayerTest {
    static int count = 0;
    Table t = new Table(10, 1);

    @Before
    public void setUp() {
        ++count;
        System.out.print("Player test #" + count + ": testing... ");
    }

    @After
    public void tearDown() {
        System.out.print("finished.\n");
    }

    @Test
    public void addCreature(){
        Table t = new Table(10, 2);
        Player p1 = t.getPlayers().get(0);

        assertFalse("Test failed: addCreature", p1 == null);

        assertFalse("Test failed: addCreature", p1.addCreature(t.getCard()));
        assertFalse("Test failed: addCreature", p1.addCreature(new Card(Trait.CAMOUFLAGE)));
        assertTrue("Test failed: addCreature", p1.addCreature(p1.getPlayerCardDeck().getCard()));
    }

    @Test
    public void attackCreature(){
        Table t = new Table(10, 2);

        Player p1 = t.getPlayers().get(0);
        Player p2 = t.getPlayers().get(1);

        assertFalse("Test failed: attackCreature", p1 == null);
        assertFalse("Test failed: attackCreature", p2 == null);

        p1.addCreature(p1.getPlayerCardDeck().getCard());
        p1.addCreature(p1.getPlayerCardDeck().getCard());

        p2.addCreature(p2.getPlayerCardDeck().getCard());
        p2.addCreature(p2.getPlayerCardDeck().getCard());

        Creature attacker = p1.getCreatures().get(0);
        Creature defender = p2.getCreatures().get(0);

        attacker.addTrait(new Card(Trait.PREDATOR));

        assertTrue("Test failed: attackCreature", p1.attackCreature(attacker, defender));

        defender = p2.getCreatures().get(0);
        defender.addTrait(new Card(Trait.CAMOUFLAGE));

        assertFalse("Test failed: attackCreature", p1.attackCreature(attacker, defender));

    }

    @Test
    public void killCreature(){
        Table t = new Table(10, 1);

        Player p1 = t.getPlayers().get(0);

        p1.addCreature(p1.getPlayerCardDeck().getCard());

        assertTrue("Test failed: killCreature", p1.getCreatures().size() == 1);

        p1.killCreature(p1.getCreatures().get(0));

        assertTrue("Test failed: killCreature", p1.getCreatures().size() == 0);
    }

    @Test
    public void pirateCreature(){
        Table t = new Table(10, 1);

        Player p1 = t.getPlayers().get(0);

        p1.addCreature(p1.getPlayerCardDeck().getCard());
        p1.addCreature(p1.getPlayerCardDeck().getCard());

        p1.getCreatures().get(0).addTrait(new Card(Trait.PIRACY));
        p1.getCreatures().get(1).addTrait(new Card(Trait.HIGH_BODY));
        p1.getCreatures().get(1).addFood();

        assertTrue("Test failed: pirateCreature", p1.pirateCreature(p1.getCreatures().get(0), p1.getCreatures().get(1)));
        assertFalse("Test failed: pirateCreature", p1.pirateCreature(p1.getCreatures().get(0), p1.getCreatures().get(1)));
        p1.getCreatures().get(0).setPirated(false);
        assertFalse("Test failed: pirateCreature", p1.pirateCreature(p1.getCreatures().get(0), p1.getCreatures().get(1)));
        assertFalse("Test failed: pirateCreature", p1.pirateCreature(p1.getCreatures().get(1), p1.getCreatures().get(0)));
    }

    @Test
    public void addTraitToCreature(){
        Table t = new Table(10, 1);

        Player p1 = t.getPlayers().get(0);

        p1.addCreature(p1.getPlayerCardDeck().getCard());
        p1.addCreature(p1.getPlayerCardDeck().getCard());

        p1.getPlayerCardDeck().getCardDeck().clear();
        p1.getPlayerCardDeck().addCard(new Card(Trait.CAMOUFLAGE));
        p1.getPlayerCardDeck().addCard(new Card(Trait.CAMOUFLAGE));
        p1.getPlayerCardDeck().addCard(new Card(Trait.PREDATOR));
        p1.getPlayerCardDeck().addCard(new Card(Trait.SCAVENGER));

        assertFalse("Test failed: addTraitToCreature", p1.addTraitToCreature(p1, p1.getCreatures().get(0), new Card(Trait.HIGH_BODY), true));
        assertTrue("Test failed: addTraitToCreature", p1.addTraitToCreature(p1, p1.getCreatures().get(0), p1.getPlayerCardDeck().getCard(), true));
        assertFalse("Test failed: addTraitToCreature", p1.addTraitToCreature(p1, p1.getCreatures().get(0), p1.getPlayerCardDeck().getCard(), true));
        assertTrue("Test failed: addTraitToCreature", p1.addTraitToCreature(p1, p1.getCreatures().get(0), p1.getPlayerCardDeck().getCard(), true));
        assertFalse("Test failed: addTraitToCreature", p1.addTraitToCreature(p1, p1.getCreatures().get(0), p1.getPlayerCardDeck().getCard(), true));
    }
}
