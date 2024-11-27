package quality;

import java.util.ArrayList;
import java.util.List;

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
            State s = addState(line);
            addCountyAndPollutant(s, line);
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

     public boolean checkCountiesHTLoadFactor(State state) {
        return ((double)state.getNumberOfCounties() / (double)state.getCounties().length) >= state.getLoadFactor();
    }
    

    /**
     * Resizes (rehashes) the State's counties hashtable by doubling its size.
     * 
     * USE: county.hashCode() as the key into the State's counties hash table.
     */
    public void rehash ( State state ) {
        if(checkCountiesHTLoadFactor(state)){
            int newSize = state.getCounties().length*2;
            County[] old = state.getCounties();
            County[] ne = new County[newSize];

            for (County c :old) {
                while (c != null) {
                    County next = c.getNext();
                    int index = Math.abs(c.getName().hashCode()) % newSize;
                    c.setNext(ne[index]);
                    ne[index] = c;
                    c = next;
                }
            }
            state.setCounties(ne);
        }
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

     public void addCountyAndPollutant(State state, String inputLine) {
        String[] arr = inputLine.split(",");

        String name = arr[1];
        int aqi = Integer.parseInt(arr[2]);
        double latitude = Double.parseDouble(arr[3]);
        double longitude = Double.parseDouble(arr[4]);
        String pName = arr[5];
        String color = arr[6];
        
        // Indexs - For the hash Code
        int key = Math.abs(name.hashCode());
        int index = key % state.getCounties().length;

        County head = state.getCounties()[index];
        County i = head;
        while (i != null) { // County Exists? 
            if (i.getName().equals(name)) {
                break;
            }
            i = i.getNext();
        }


        if (i == null) { // Nope, does not
            County add = new County(name, latitude, longitude, head);
            state.addCounty(add);
            state.incrementNumberOfCounties(0);
            i = add;
            
        }

        // Previous Method: 
        if (checkCountiesHTLoadFactor(state)){
            rehash(state);
            
            index = Math.abs(name.hashCode()) % state.getCounties().length; // New Index
            i = state.getCounties()[index];

            while (i != null && !i.getName().equals(name)) { // Find where the added STUFF is.
                i = i.getNext();
            }
        }
        // pollutant
        boolean b = false;
        for (Pollutant p : i.getPollutants()) { // Found ?
            if (p.getName().equals(pName)) {
                p.setAQI(aqi);
                p.setColor(color);
                b = true;
                break;
            }
        }
        if (!b) { // Nope, AGAIN.
            Pollutant newPollutant = new Pollutant(pName, aqi, color);
            i.getPollutants().add(newPollutant);
        }
    }    

    /**
     * Sets states' simple stats AQI for each State in the hash table.
     */
    public void setStatesAQIStats() {
        for (State i : states) { // State
            if (i == null) {
                continue;
            }
            State index = i;
            while(index != null ){
                double avg = 0.0;
                double totalPol = 0;
                double high = Double.MIN_VALUE;
                double low = Double.MAX_VALUE;
                County HC = null;
                County LC = null;

                for (County c : index.getCounties()) { // Each County
                    County temp = c;
                    while(temp != null){
                        ArrayList<Pollutant> p = temp.getPollutants();
                        double totP = 0;
                        for(Pollutant poll: p){
                            avg+=poll.getAQI();
                            totalPol++;
                            totP=poll.getAQI();
                        }

                        if(high < totP){
                            high = totP;
                            HC = temp;
                        }
                        if(low > totP){
                            low = totP;
                            LC=temp;
                        }
                        temp = temp.getNext();
                    }
                }
                index.setAvgAQI(totalPol > 0 ? avg/totalPol : 0);
                index.setHighestAQI(HC);
                index.setLowestAQI(LC);
                index = index.getNext(); // Next State
            }
        }
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

    public ArrayList<County> meetsThreshold(String stateName, String pollutantName, int AQITheshold) {
        ArrayList<County> result = new ArrayList<>();
        OUTER: for (State i : states) { // State
            if (i == null) continue;
            State index = i;
            
            while(index != null){
                if(stateName.equals(index.getName())){
                    for (County c : index.getCounties()) { // Each County
                        County temp = c;
                        while (temp != null) {
                            for (Pollutant p : temp.getPollutants()) { // Each Pollutants
                                if(p.getName().equals(pollutantName) && (p.getAQI() >= AQITheshold)){
                                    result.add(temp);
                                }
                            }
        
                            temp = temp.getNext();
                        }
                    }
                    break OUTER;
                }
                index = index.getNext();
            }
        }
        return result;
    } 

}
