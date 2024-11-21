package graph;

/**
 * City node, simple linked list node containing a string
 * @author Colin Sullivan
 */
public class City {
    
    public City next; // Next linked list node
    public String city; // Name of this City

    /**
     * Constructor which initializes city
     * @param city
     */
    public City(String city) {
        this.city = city;
    }

    /**
     * Constructor which initializes city and next
     * @param city
     */
    public City(String city, City next) {
        this.city = city;
        this.next = next;
    }
    
}
