package model;

import java.util.Random;

public class Dice {

    private int diceCount;
    private int addition;
    private static final int faces = 6;

    public Dice(int diceCount, int addition){
        this.diceCount = diceCount;
        this.addition = addition;
    }

    int roll(){
        int res = 0;

        for(int i = 0; i < diceCount; ++i)
            res += rollOneDice();

        return res + addition;
    }

    public static int rollOneDice(){
        Random r = new Random();
        return r.nextInt() % faces + 1;
    }
}
