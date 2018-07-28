package model;

import java.util.Random;

public class Dice {

    int diceCount;
    int addition;

    public Dice(int diceCount, int addition){
        this.diceCount = diceCount;
        this.addition = addition;
    }

    int roll(){
        int res = 0;
        Random r = new Random();

        for(int i = 0; i < diceCount; ++i)
            res += r.nextInt();

        return res + addition;
    }
}
