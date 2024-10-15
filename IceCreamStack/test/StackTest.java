import static org.junit.Assert.*;
import org.junit.*;

import stack.IceCreamStack;

public class StackTest {

    /*
     * This is a Java Test Class, which uses the JUnit package to create
     * and run tests. You do NOT have to submit this class.
     * 
     * You can fill in these tests in order to evaluate your code. Writing tests
     * is a crucial skill to have as a developer.
     */

    @Test
    public void testConstructor() {
        IceCreamStack stack = new IceCreamStack();
        assertTrue(stack.getScoops() == 0);
        assertEquals(stack.getScoops(), 0);
    }

    @Test
    public void testPushAndPop() {

        // Write a test for the push and pop methods here
        IceCreamStack stack = new IceCreamStack();

        // First, we push the following string onto the stack
        String testStr = "Example";
        stack.push(testStr);

        // Then use: assertTrue(arg1.equals(arg2));
        // Replace arg1 with stack.pop() and arg2 with testStr

    }

    @Test
    public void testPushAndPop2() {
        // Now, write more tests to make sure the 
        // stack will resize properly when at capacity
        IceCreamStack stack = new IceCreamStack();

        // You can insert in a loop as follows
        for (int i = 1; i < 5; i++) {
            stack.push("" + i);
        }

        // Write a similar loop to pop from the stack and 
        // compare the output to what is expected using assertTrue( )
        
    }

}