package model;

public class Table {
   int fodder = 0;

    Table(){}


    boolean isEmpty()
    {
        if (fodder == 0)
            return true;
        return false;
    }
}
