package quality;

import java.util.ArrayList;

/**
 * This class represents the AirQuality system which populates a hashtable with states and counties 
 * and calculates various statistics related to air quality.
 * 
 * This class is a part of the AirQuality project.
 * 
 * @author Anna Lu
 * @author Srimathi Vadivel
 */

public class AirQuality {

    private State[] states; // hash table used to store states. This HT won't need rehashing.

    /**
     * **DO NOT MODIFY THIS METHOD**
     * Constructor creates a table of size 10.
     */

    public AirQuality () {
        states  = new State[10];
    }     

    /**
     ** *DO NOT MODIFY THIS METHOD**
     * Returns the hash table.
     * @return the value held to represent the hash table
     */
    public State[] getHashTable() {
        return states;
    }
    
    /**
     * 
     * DO NOT UPDATE THIS METHOD
     * 
     * This method populates the hashtable with the information from the inputFile parameter.
     * It is expected to insert a state and then the counties into each state.
     * 
     * Once a state is added, use the load factor to check if a resize of the hash table
     * needs to occur.
     * 
     * @param inputLine A line from the inputFile with the following format:
     * State Name,County Name,AQI,Latitude,Longitude,Pollutant Name,Color
     */

    public void buildTable ( String inputFile ) {
        
        StdIn.setFile(inputFile); // opens the inputFile to be read
        StdIn.readLine();         // skips header
        
        while ( !StdIn.isEmpty() ) {

            String line = StdIn.readLine(); 
            State s = addState( line );
            addCountyAndPollutant(s, line );
        }
    }
    
    /**
     * Inserts a single State into the hash table states.
     * 
     * Note: No duplicate states allowed. If the state is already present, simply
     * return the State object. Otherwise, insert at the front of the list.
     * 
     * @param inputLine A line from the inputFile with the following format:
     * State Name,County Name,AQI,Latitude,Longitude,Pollutant Name,Color
     * 
     * USE: Math.abs("State Name".hashCode()) as the key into the states hash table.
     * USE: hash function as: hash(key) = key % array length
     * 
     * @return the State object if already present in the table or the newly created
     * State object inserted.
     */

    public State addState ( String inputLine ) {
        String[] data = inputLine.split(",");
        String stateName = data[0];
        int key = Math.abs(stateName.hashCode());
        int i = key % states.length;


        // Checkin for dups
        for(State c = states[i]; c != null; c = c.getNext()){
            if (c.getName().equals(stateName)) {
                return c;
            }
        }
        
        State ns = new State(stateName);
        ns.setNext(states[i]);
        states[i] = ns;
        
        return ns;
        
    }
    
    /**
     * Returns true if the counties hash table (within State) needs to be resized (re-hashed) 
     *
     * Resize the hash table when (number of counties)/(array size) >= loadFactor
     * 
     * @return true if resizing needs to happen, false otherwise
     */

     public boolean checkCountiesHTLoadFactor ( State state ) {

	    // WRITE YOUR CODE HERE
	 
	    return true; // update this line
    }

    /**
     * Resizes (rehashes) the State's counties hashtable by doubling its size.
     * 
     * USE: county.hashCode() as the key into the State's counties hash table.
     */
    public void rehash ( State state ) {

	// WRITE YOUR CODE HERE
	
    }

    /**
     * This method:
     * 1) Inserts the county (from the input line) into State, if not already present.
     *    Check the State's counties hash table load factor after inserting. The hash table may need
     *    to be resized.
     * 
     * 2) Then inserts the pollutant (from the input line) into County (from the input line), if not already present.
     *    If pollutant is present, update AQI.
     * 
     * Note: no duplicate counties in the State.
     * Note: no duplicate pollutants in the County.
     * 
     * @param inputLine A line from the inputFile with the following format:
     * State Name,County Name,AQI,Latitude,Longitude,Pollutant Name,Color
     * 
     * USE: Math.abs("County Name".hashCode()) as the key into the State's counties hash table.
     * USE: the hash function as: hash(key) = key % array length
     */

    public void addCountyAndPollutant ( State state, String inputLine ) {

        // WRITE YOUR CODE HERE
        
    }

    /**
     * Sets states' simple stats AQI for each State in the hash table.
     */
    public void setStatesAQIStats() {

	// WRITE YOUR CODE HERE
        
    }

    /**
     * In this method you will find all the counties within a state that have the same parameter name
     * and above the AQI threshold.
     * 
     * @param stateName The name of the state
     * @param pollutantName The parameter name to filter by
     * @param AQIThreshold The AQI threshold
     * @return ArrayList<County> List of counties that meet the criteria
     */

    public ArrayList<County> meetsThreshold(String stateName, String pollutantName, int AQIThreshold) {

	// WRITE YOUR CODE HERE
        
        return null; // update this line
    } 

}
