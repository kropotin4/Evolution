package model;

import model.decks.CommonCardDeck;

public class Table {

    static int fodder = 0;

    CommonCardDeck commonDeck;

    Table(){


    }


    static boolean isEmpty()
    {
        if (fodder == 0)
            return true;
        return false;
    }
}
