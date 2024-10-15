package mystery;
import java.util.*;

/*
 * Room class, representing a room in the mansion
 * 
 * @author Colin Sullivan
 * @author Steven Chen
 */
public class Room {
    // The name of the room
    private String roomName;
    // The people who are currently in this room
    private ArrayList<Person> people;
    // The items currently in this room
    private ArrayList<Item> items;
    // The weights (probability) of each door in the room
    private double[] doorWeights;
    // The costs of traveling through a door, for use with dice roll
    private int[] doorCosts;

    /*
     * Full Constructor for room
     * 
     * @param name The name of the room
     * @param roomItem The item which should spawn in the room (null if none)
     * @param weights A double array of door weights (probabilities)
     * @param costs An int array of door costs (for use with dice roll)
     */
    public Room(String name, double[] weights, int[] costs) {
        roomName = name;
        people = new ArrayList<Person>();
        items = new ArrayList<Item>();
        doorWeights = weights;
        doorCosts = costs;
    }
    /*
    * Adds an item to the room
    * 
    * @param item The Item object to add
    */
    public void addItem(Item item) {
    items.add(item);
    }
    /*
     * Adds a player into a room
     * 
     * @param player The Person object to add
     */
    public void addPlayer(Person player) {
        people.add(player);
    }
     

    /*
    * @return The Person arraylist
    */
    public ArrayList<Person> getPeople() {
        return people;
    }

    /* 
    * @return the number of alive people in a room
    */
    public int alivePeople() {
        int alive = 0;
        for (int i = 0; i < people.size(); i++) {
            if (people.get(i).alive()) {
                alive++;
            }
        }
        return alive;
    }

    /*
     * Removes a player from a room
     * 
     * @param name The name of the player to remove 
     */
    public void removePlayer(String name) {
        for (int i = 0; i < people.size(); i++) {
            if (people.get(i).getName().equals(name)) {
                people.remove(i);
                return;
            }
        }
    }

    /*
     * Gets the name of the room
     * 
     * @returns String name of the room
     */
    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public double[] getDoorWeights() {
        return doorWeights;
    }

    public void setDoorWeights(double[] doorWeights) {
        this.doorWeights = doorWeights;
    }

    public int[] getDoorCosts() {
        return doorCosts;
    }

    public void setDoorCosts(int[] doorCosts) {
        this.doorCosts = doorCosts;
    }

}
