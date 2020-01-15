package model;

import java.io.Serializable;
import java.util.Random;

/******************************************
 * Dice for god of random.
 *
 * @author akropotin
 *
 * @see Card
 ******************************************/
public class Dice implements Serializable {

    private int diceCount;
    private int addition;
    private static final int faces = 6;

    /**********************
     * Dice class constructor.
     * @param diceCount number of dices.
     * @param addition value that addition to result.
     * @see Card
     **********************/
    public Dice(int diceCount, int addition){
        this.diceCount = diceCount;
        this.addition = addition;
    }

    /**********************
     * Roll dices for get random number.
     * @return random number
     * @see Card
     **********************/
    int roll(){
        int res = 0;

        for(int i = 0; i < diceCount; ++i)
            res += rollOneDice();

        return res + addition;
    }

    /**********************
     * Roll one dice.
     * @return random number
     * @see Card
     **********************/
    public static int rollOneDice(){
        Random r = new Random();
        return r.nextInt(faces) + 1;
    }
}
