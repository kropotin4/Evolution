package storage;
import model.Table;

import java.io.*;

public class Saver {

    public void saveTable(Table t, String filepath) throws IOException {
        ObjectOutputStream objectOut = new ObjectOutputStream(new FileOutputStream(filepath));
        objectOut.writeObject(t);
        objectOut.close();
        System.out.println("The Table was successfully save in \"" + filepath + "\"");
    }
}