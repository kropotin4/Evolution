//package java;

import model.Dice;
import org.junit.After;
import org.junit.Before;

import static java.lang.Math.*;
import static org.junit.Assert.*;

public class DiceTest {
    @Before
    public void setUp() throws Exception {
        System.out.print("Dice tests: testing... ");
    }


    @After
    public void tearDown() throws Exception {
        System.out.print("finished.\n");
    }

    @org.junit.Test
    public void rollOneDice() {
        int i = Dice.rollOneDice();
        assertTrue("Dice logic failed (impossible!)", i >= 1 && i <= 6);

        int freq[] = new int[6];
        for (i = 0; i < 6; ++i){
            freq[i] = 0;
        }
        for (i = 0; i < 60000; ++i) {
            ++freq[Dice.rollOneDice() - 1];
        }
        double sum = 0;
        double exp = 10000;
        for (i = 0; i < 6; ++i) {
            sum += (freq[i] - exp) * (freq[i] - exp);
        }
        double f = sum / exp;
        double sigma = sqrt(5);
        double t1 = 6 - 3 * sigma;
        double t2 = 6 + 3 * sigma;
        assertTrue("Dice freq test (probably it's ok, just try again)", f > t1 && f < t2);
    }
}