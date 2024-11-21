package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.*;

import quality.*;

public class AirQualityTest {
   /*
    * This is a Java Test Class, which uses the JUnit package to create
    * and run tests. You do NOT have to submit this class.
    * 
    * You can fill in these tests in order to evaluate your code. Writing tests
    * is a crucial skill to have as a developer.
    * 
    * How to run?
    * - MAKE SURE you are in the right directory. On the right side of your VS Code
    * Explorer, you should see:
    * AirQuality
    * lib
    * src
    * test
    * input files
    * NOT:
    * AirQuality/CS112/Another Folder Name
    * AirQuality
    * ...
    * Open the INNERMOST AirQuality (case sensitive) using File -> Open Folder.
    * - Right click AirQualityTest.java in the VS Code explorer and select
    * "Run Tests"
    */

   @Test
   public void testAddState() {
       AirQuality test = new AirQuality();
       test.buildTable("addState.csv");
   
       State[] res = test.getHashTable();
   
       // Assertion will fail if index 0 is empty
       assertNotNull(res[0]);
       // Assertion will fail if Minnesota is not the first state name
       assertEquals("Minnesota", res[0].getName());
       // Assertion will fail if next node at index 0 is not null
       assertNull(res[0].getNext());
   
       // Assertion will fail if index 4 is empty
       assertNotNull(res[4]);
       // Assertion will fail if Kentucky is not the first state name at index 4
       assertEquals("Kentucky", res[4].getName());
       // Assertion will fail if next node at index 4 is not null
       assertNull(res[4].getNext());
   
       // Assertion will fail if index 5 is empty
       assertNotNull(res[5]);
       // Assertion will fail if New York is not the first state name at index 5
       assertEquals("New York", res[5].getName());
       // Assertion will fail if New York's next node is null
       assertNotNull(res[5].getNext());
       // Assertion will fail if the second node in the chain is not Louisiana
       assertEquals("Louisiana", res[5].getNext().getName());
       // Assertion will fail if Louisiana's next node is null
       assertNotNull(res[5].getNext().getNext());
       // Assertion will fail if the third node in the chain is not Florida
       assertEquals("Florida", res[5].getNext().getNext().getName());
       // Assertion will fail if Florida's next node is not null (indicating the end of the chain)
       assertNull(res[5].getNext().getNext().getNext());
   
       // Assertion will fail if index 6 is empty
       assertNotNull(res[6]);
       // Assertion will fail if Pennsylvania is not the first state name at index 6
       assertEquals("Pennsylvania", res[6].getName());
       // Assertion will fail if Pennsylvania's next node is null
       assertNotNull(res[6].getNext());
       // Assertion will fail if the second node in the chain is not New Jersey
       assertEquals("New Jersey", res[6].getNext().getName());
       // Assertion will fail if New Jersey's next node is null
       assertNotNull(res[6].getNext().getNext());
       // Assertion will fail if the third node in the chain is not California
       assertEquals("California", res[6].getNext().getNext().getName());
       // Assertion will fail if California's next node is not null (indicating the end of the chain)
       assertNull(res[6].getNext().getNext().getNext());
   
       // Assertion will fail if index 7 is empty
       assertNotNull(res[7]);
       // Assertion will fail if Virginia is not the first state name at index 7
       assertEquals("Virginia", res[7].getName());
       // Assertion will fail if Virginia's next node is null
       assertNotNull(res[7].getNext());
       // Assertion will fail if the second node in the chain is not Texas
       assertEquals("Texas", res[7].getNext().getName());
       // Assertion will fail if Texas's next node is null
       assertNotNull(res[7].getNext().getNext());
       // Assertion will fail if the third node in the chain is not North Dakota
       assertEquals("North Dakota", res[7].getNext().getNext().getName());
       // Assertion will fail if North Dakota's next node is not null (indicating the end of the chain)
       assertNull(res[7].getNext().getNext().getNext());
   
       // Assertion will fail if index 8 is empty
       assertNotNull(res[8]);
       // Assertion will fail if Wyoming is not the first state name at index 8
       assertEquals("Wyoming", res[8].getName());
       // Assertion will fail if next node at index 8 is not null
       assertNull(res[8].getNext());
   
       // Assertion will fail if index 9 is empty
       assertNotNull(res[9]);
       // Assertion will fail if Ohio is not the first state name at index 9
       assertEquals("Ohio", res[9].getName());
       // Assertion will fail if Ohio's next node is null
       assertNotNull(res[9].getNext());
       // Assertion will fail if the second node in the chain is not Colorado
       assertEquals("Colorado", res[9].getNext().getName());
       // Assertion will fail if Colorado's next node is not null (indicating the end of the chain)
       assertNull(res[9].getNext().getNext());
   }   

   @Test
   public void testLoadFactor() {
      fail("This test is not yet implemented. Replace this line with code to implement this test.");
   }

   @Test
   public void testRehash() {
      fail("This test is not yet implemented. Replace this line with code to implement this test.");
   }

   @Test
   public void testAddCountyAndPollutant() {
      // Instantiate a new AirQuality object.

      // Call buildTable() on rehashCounties.csv.

      // There are two states, you can check Texas' counties.

      // Check that the counties are added to the correct states.
      // - grab the county table from a state by using state.getCounties()
      // - county hash functions are defined by county code % counties table length
      
      // You can also check if pollutants are added to the correct counties.

      // Remove this line once you have written this test.
      fail("This test is not yet implemented. Replace this line with code to implement this test.");
   }

   @Test
   public void testSetStatesAQIStats() {
      // Remove this line once you have written this test.
      fail("This test is not yet implemented. Replace this line with code to implement this test.");
   }

   @Test
   public void testMeetsThreshold() {
      // Remove this line once you have written this test.
      fail("This test is not yet implemented. Replace this line with code to implement this test.");
   }

}
