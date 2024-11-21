package quality;

import java.util.ArrayList;

/**
 * This class represents a County in a State.
 * A county may have many pollutants in the air.
 */

public class County {
    private String name;     // air quality index
    private double latitude;
    private double longitude;
    private ArrayList<Pollutant> pollutants; // keeps all pollutants
    private County next;

    /*
     * Constructor
     */
    public County (String countyName, double latitude, double longitude, County next) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.pollutants = new ArrayList<>();
        this.name = countyName;
        this.next = next;
    }

    /*
     * Returns an integer representation of the object
     */
    public int hashCode () {
        return Math.abs(name.hashCode());
    }
    
    // Getter and Setter methods for instance variables
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public ArrayList<Pollutant> getPollutants() { return pollutants; }
    public String getName() { return name; }

    public County getNext() { return next; }
    public void setNext(County next) { this.next = next; }

}
