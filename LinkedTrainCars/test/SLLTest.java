import static org.junit.Assert.*;
import org.junit.*;

import singly.*;

public class SLLTest {

    /*
     * This is a Java Test Class, which uses the JUnit package to create
     * and run tests
     * 
     * You can use this to evaluate your code. Examine these tests, as writing
     * similar test cases will help you immensly on other Assignments/Labs, as
     * well as moving forward in your CS career.
     */

    @Test
    public void testConstructor() {
        TrainCar t = new TrainCar();
        LinkedTrainCars test = new LinkedTrainCars("test1");
        assertTrue(test.getFront().getName().equals("test1"));
    }
    
    @Test
    public void testNumCars() {
        LinkedTrainCars test = new LinkedTrainCars("2");
        test.insertAt("1", 1);
        test.insertAt("3", 3);

        TrainCar ptr = test.getFront();
        assertTrue(ptr.getName().equals("1"));
        ptr = ptr.getNext();
        assertTrue(ptr.getName().equals("2"));
        ptr = ptr.getNext();
        assertTrue(ptr.getName().equals("3"));
        ptr = ptr.getNext();
        assertTrue(ptr == null);
    }

    @Test
    public void testInsertAt() {
        // Expand this test to more fully test your code.
        
        // You can manually create new TrainCar objects and insert them,
        // then check that the list is as you expect.
        LinkedTrainCars test = new LinkedTrainCars();

        test.insertAt("2", 1);
        test.insertAt("1", 1);
        test.insertAt("3", 3);
        test.insertAt("4", 10);

        // Uncomment the following block, and fill in the assertTrue( ) statements 
        // to complete this test

        // TrainCar ptr = test.getFront();
        // assertTrue(ptr.getName().equals("2"));
        // ptr = ptr.getNext();
        // assertTrue(ptr.getName().equals("1"));
        // ptr = ptr.getNext();
        // assertTrue(ptr.getName().equals("3"));
        // ptr = ptr.getNext();        	
	    // assertTrue(ptr.getName().equals("4"));
        // ptr = ptr.getNext();
        // assertTrue(ptr == null);
    }

}