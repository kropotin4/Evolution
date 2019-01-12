import model.Table;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import storage.*;

import java.io.IOException;

import static org.junit.Assert.*;

public class StorageTest {
    Table t;
    @Before
    public void setUp() throws Exception {
        t = new Table(1, 1);
    }

    @After
    public void tearDown() throws Exception {
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
    }
}