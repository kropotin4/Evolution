import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


public class TestRunner {

    static JUnitCore j = new JUnitCore ();
    public static void main(String[] args) {
        Result result = j.runClasses(DiceTest.class, CardDeckTest.class, CreatureTest.class, StorageTest.class);
        System.out.println("Total number of tests " + 	result.getRunCount());
        System.out.println("Total number of tests failed " + 	result.getFailureCount());

        for(Failure failure : result.getFailures()){
            System.out.println(failure.getMessage());
        }
        System.out.println("All tests passed: " + result.wasSuccessful());
    }
}