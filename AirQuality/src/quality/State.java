package quality;

/**
 * This class represents a State, which has a state code, state name, an array of counties, 
 * and average, highest, and lowest air quality indices (AQI).
 */

public class State {

    private String name;
    private County[] counties = new County[5]; // hash table to store counties
    private int numberOfCounties;
    private int loadFactor;
    private double avgAQI;
    private County highestAQI;
    private County lowestAQI;
    private State next;

    /*
     * Constructor
     */ 
    public State ( String name ) {
        this.name = name;
        this.numberOfCounties = 0;
        this.loadFactor = 2;
    }

    /*
     * Inserts the parameter county at the front of the linked list.
     * Resizing of the counties hash table is handled by rehash in RUquality.java
     * @param county is the county to be added.
     */
    public void addCounty ( County county ) {
        
        int i = Math.abs(county.getName().hashCode()) % counties.length;
        County oldFront = counties[i];
        counties[i] = county;
        county.setNext(oldFront);
        numberOfCounties += 1;
    }

     /*
     * Returns an integer representation of the object
     */
    public int hashCode () {
        return Math.abs(name.hashCode());
    }

    // Getter and Setter methods
    public String getName() { return name; }
    public void setName(String stateName) { this.name = stateName; }

    public State getNext() { return next; }
    public void setNext(State nextState) { this.next = nextState; }

    public County[] getCounties() { return counties; }
    public void setCounties (County[] counties) { this.counties = counties; }
    
    public int getLoadFactor () { return loadFactor; }

    public int getNumberOfCounties () { return numberOfCounties; }
    public void incrementNumberOfCounties (int count) { this.numberOfCounties += count; }

    public double getAvgAQI() { return avgAQI; }
    public void setAvgAQI(double avgAQI) { this.avgAQI = avgAQI; }

    public County getHighestAQI() { return highestAQI; }
    public void setHighestAQI(County highestAQI) { this.highestAQI = highestAQI; }

    public County getLowestAQI() { return lowestAQI; }
    public void setLowestAQI(County lowestAQI) { this.lowestAQI = lowestAQI; }
}
