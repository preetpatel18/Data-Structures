package test;

import static org.junit.Assert.*;
import org.junit.*;
import graph.FlightPathGraph;

public class FlightPathGraphTest {
    // For reference, the autolab input
    // Three graphs, so citiesInput[0], edgesInput[0], and removeInput[0] all correspond to the data for one test case.
    String[][] citiesInput =  {{"New York", "London", "Shanghai", "Tokyo", "Paris", "Berlin"}, // First graph vertices
                                {"A", "B", "C", "D", "E", "F"}, // Second graph vertices
                                {"1","2","3","0","13","12","9","5","122"}}; // Third graph vertices

    String[][][] edgesInput = { {{"New York", "London"}, {"Shanghai", "Berlin"}, {"Berlin", "Paris"}, {"New York", "Paris"}, {"New York", "Tokyo"}}, // First graph edges to add
                                {{"A", "B"}, {"B", "D"}, {"A", "C"}, {"E", "F"}, {"D", "B"}}, // Second graph edges to add
                                {{"13", "12"}, {"9", "5"}, {"122", "122"}, {"1", "2"}, {"2", "3"}, {"3", "2"}, {"3", "13"}} }; // Third graph edges to add

    String[][][] removeInput = { {{"New York", "London"}, {"Shanghai", "Berlin"}, {"New York", "Paris"}}, // First graph edges to remove
                                {{"A", "D"}, {"E", "F"}, {"D", "B"}}, // Second graph edges to remove
                                {{"13", "12"}, {"2", "3"}, {"3", "2"}, {"3", "13"}} }; // Third graph edges to remove


    @Test
    public void testConstructor() {
        String[] testCities = {"0","1","2","3","4","5"};
        FlightPathGraph graph = new FlightPathGraph(testCities);
        assertEquals(graph.flightPaths.length, testCities.length);
        for (int i = 0; i < graph.flightPaths.length; i++) {
            assertEquals(graph.flightPaths[i].city, testCities[i]);
            assertNull(graph.flightPaths[i].next);
        }
    } 

    @Test
    public void testAddEdge() {
        String[] testCities = {"0","1","2","3","4","5"};
        FlightPathGraph graph = new FlightPathGraph(testCities);
        
        fail("You haven't written this test case yet!");
    } 

    @Test
    public void testRemoveEdge() {
        String[] testCities = {"0","1","2","3","4","5"};
        FlightPathGraph graph = new FlightPathGraph(testCities);
        
        fail("You haven't written this test case yet!");
    } 
}
