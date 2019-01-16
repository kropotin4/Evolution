//package java;

import model.Table;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import storage.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.*;

public class StorageTest {
    Table t;

    @Before
    public void setUp() {
        System.out.print("Storage test: testing... ");
        t = new Table(1, 1);
    }

    @After
    public void tearDown() {
        System.out.print("finished.\n");
    }

    @Test
    public void storageTest() throws IOException {
        Saver s = new Saver();
        Loader l = new Loader();
        try{
            s.saveTable(t, "file.evo");
            l.loadTable("file.evo");
        } catch (IOException e) {
            assertTrue("IOException thrown", false);
        } catch (ClassNotFoundException e) {
            assertTrue("ClassNotFoundException thrown", false);
        }

        try{
            l.loadTable("no_file.evo");
        } catch (ClassNotFoundException e) {
            assertTrue("IOException thrown", false);
        } catch (FileNotFoundException e){
            assertTrue("FileNotFoundException thrown", true);
        }
    }
}