package storage;
import java.io.*;
import model.Table;

public class Loader {
    public Table loadTable(String filepath) throws IOException, ClassNotFoundException {
        ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(filepath));
        Object t = objectIn.readObject();
        System.out.println("The Object has been read from the file");
        objectIn.close();
        return (Table)t;
    }
}
