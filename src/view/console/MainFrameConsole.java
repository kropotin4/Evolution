package view.console;

import model.Creature;
import model.Table;

import java.util.Scanner;

public class MainFrameConsole{
    Table table = null;

    MainFrameConsole(){
        System.out.print("How much quarters of Decks would be used?\n");
        Scanner s = new Scanner(System.in);
        int i = s.nextInt();
        table = new Table(i, 2);

        printAll();
    }

    void clear(){
        System.out.print("\f");
    }

    void printTable(){
        System.out.println("Cards in deck:" + table.getCommonDeck().getCardCount());
        System.out.println("Food in fodder:" + table.getFodder());
        for (int i = 0; i < table.getPlayerNumber(); ++i){
            printPlayer(i);
        }
    }

    void printPlayer(int player){
        if (player == -1){
            //TODO: print myself
        }
        System.out.println("Player " + player + ":");
        for (Creature creature : table.getPlayers().get(player).getCreatures()) {
            System.out.print(creature);
        }
    }

    void printPlayers(){
        System.out.println("There is " + table.getPlayerNumber() + "players. Who do you wat to print?");
        System.out.println("Print [1.." + table.getPlayerNumber() + "] to print player");

        Scanner s = new Scanner(System.in);
        char c;
        while (true) {
            c = s.next().charAt(0);
            if ('0' <= c && c <= '9'){
                int i = c - '0';
                if (i < 0 || i > table.getPlayerNumber()){
                    System.out.print("Incorrect number. Try again");
                } else {
                    printPlayer(i - 1);
                }
            }
            switch (c) {
                case 'Q':
                case 'q':
                case 'E':
                case 'e':
                case 'R':
                case 'r':
                case 'B':
                case 'b':
                case '\u001B':
                    return;
                default:
                    System.out.println("An error occurred. Try again...");
            }
        }
    }

    void printAll() {
        System.out.println("Print T for table\nPrint P for players\n");

        Scanner s = new Scanner(System.in);
        char c;
        while (true) {
            c = s.next().charAt(0);
            switch (c) {
                case 'T':
                case 't':
                    clear();
                    printTable();
                case 'P':
                case 'p':
                    clear();
                    printPlayers();
                case 'Q':
                case 'q':
                case 'E':
                case 'e':
                case '\u001B':
                    return;
                default:
                    System.out.println("An error occurred. Try again...");
            }
        }

    }
}
