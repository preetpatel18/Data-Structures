package mystery;

import java.util.*;

/*
 * Person class which includes attributes for each player
 * Attributes include name, item, alive, murderer, moves left, and current room
 * Includes methods which make movement/murderer decisions
 * 
 * @author Colin Sullivan
 * @author Steven Chen
 */
public class Person {

    // Name of the person
    private String name;
    // What item this person holds
    private Item holds;
    // Is this person the murderer
    private boolean isMurderer;
    // Is this person alive
    private boolean isAlive;
    // How many moves left does this person have
    private int moves;
    // The (x,y) coordinates of the player in roomMap
    private int[] room;

    /*
     * Constructor which names the Person
     * 
     * @param named String name of person
     */
    public Person(String named) {
        name = named;
        holds = null;
        isMurderer = false;
        isAlive = true;
        moves = 0;
    }

    /*
     * Given info about four doors (weight and cost array, plus location and bounds)
     * this method chooses which one to go through Wrandomly.
     * 
     * @param weights Double[4] array of doorweights
     * 
     * @param costs int[4] array of door costs
     * 
     * @param coords int[4] starting coordinates of player
     * 
     * @param dim int[2] dimensions of the mansion
     */
    public int[] chooseDoor(double[] weights, int[] costs, int[] coords, int[] dim) {

        // Holds the doors we have yet to check (0,1,2,3) == (N,S,E,W)
        ArrayList<Integer> doors = new ArrayList<Integer>();
        for (int i = 0; i < 4; i++) {
            doors.add(i);
        }

        // While 3/4 doors not tried
        while (doors.size() - 1 > 0) {
            // Randomly choose one of the four doors
            int chosenDoor = doors.remove(StdRandom.uniformInt(doors.size()));
            // Decide probability we go through the door
            double doorCheck = StdRandom.uniformDouble(0.00, 1.0);

            // If the player is not trying to move out of bounds or through a door
            if (chosenDoor == 0 && coords[0] != 0 && weights[0] != 0.0) {
                // If player randomly chooses the door and they can go through it
                if (doorCheck > weights[chosenDoor] && this.moves >= costs[chosenDoor]) {
                    // Walk through door
                    this.moves -= costs[chosenDoor];
                    coords[0]--;
                    return coords;
                }
            }
            // If the player is tring to move out of bounds, don't let them
            else if (chosenDoor == 1 && coords[0] != dim[0] - 1 && weights[1] != 0.0) {
                // If player randomly chooses the door and they can go through it
                if (doorCheck > weights[chosenDoor] && this.moves >= costs[chosenDoor]) {
                    this.moves -= costs[chosenDoor];
                    coords[0]++;
                    return coords;
                }
            }
            // If the player is tring to move out of bounds, don't let them
            else if (chosenDoor == 2 && coords[1] != dim[1] - 1 && weights[2] != 0.0) {
                // If player randomly chooses the door and they can go through it
                if (doorCheck > weights[chosenDoor] && this.moves >= costs[chosenDoor]) {
                    // Walk through door
                    this.moves -= costs[chosenDoor];
                    coords[1]++;
                    return coords;
                }
            }
            // If the player is tring to move out of bounds, don't let them
            else if (chosenDoor == 3 && coords[1] != 0 && weights[3] != 0.0) {
                if (doorCheck > weights[chosenDoor] && this.moves >= costs[chosenDoor]) {
                    // Walk through door
                    this.moves -= costs[chosenDoor];
                    coords[1]--;
                    return coords;
                }
            }
        }
        // If no door chosen, stay in the same room
        return coords;
    }

    /*
     * Murderer movement method which located the closest player and moves towards
     * them.
     * The murderer disregards doorweights, only worrying about costs
     * 
     * @param costs int[4] array of door costs
     * 
     * @param coords int[4] starting coordinates of player
     * 
     * @param dim int[2] dimensions of the mansion
     * 
     * @param players Person[6] the players in the game
     */
    public int[] pursue(int[] costs, int[] coords, int[] dim, Person[] players) {

        // Index and distance of the chosen player to pursue
        int playerIndex = -1;
        double minDistance = Double.MAX_VALUE;

        // Check all players
        for (int p = 0; p < players.length; p++) {
            // Get player
            Person victim = players[p];
            // If chosen player is murderer or dead, skip
            if (this.getName().equals(victim.getName()) || !victim.alive()) {
                continue;
            }
            // Get location
            int[] dest = victim.getLocation();
            // Calculate distance formula, piece by piece
            double x_dist = Math.pow(dest[0] - room[0], 2);
            double y_dist = Math.pow(dest[1] - room[1], 2);
            double distance = Math.sqrt(x_dist + y_dist);
            // If this player is closer to the murderer than our currently chosen player
            if (distance < minDistance) {
                // Choose this player to pursue
                minDistance = distance;
                playerIndex = p;
            }
        }

        // If no players to pursue
        if (playerIndex == -1) {
            return coords;
        }

        // Return modified coords which pursues closest player
        int[] dest = players[playerIndex].getLocation();
        if (coords[0] < dest[0]) {
            coords[0]++;
            return coords;
        } else if (coords[0] > dest[0]) {
            coords[0]--;
            return coords;
        } else if (coords[1] < dest[1]) {
            coords[1]++;
            return coords;
        } else if (coords[1] > dest[1]) {
            coords[1]--;
            return coords;
        }
        return coords;
    }

    /*
     * Decide whether or not to kill someone based on if not on cooldown
     * 
     * RNG factor is also allowed which is decided when calling the method,
     * if true, the murderer will always kill
     * 
     * @param time The current round time
     * 
     * @param cooldown The cooldown period for the attack
     * 
     * @return true if the attack is successful, false otherwise
     */
    public boolean attack(int time, int cooldown, boolean rng) {
        if (cooldown >= time && !rng) {
            return false;
        }
        return true;
    }

    /*
     * Set the person to dead
     */
    public void kill() {
        this.isAlive = false;
    }

    /*
     * @return True if alive
     */
    public boolean alive() {
        return this.isAlive;
    }

    /*
     * Sets the (x,y) location
     * 
     * @param x -> roomMap[x,y]
     * 
     * @param y -> roomMap[x,y]
     */
    public void setLocation(int x, int y) {
        int[] location = { x, y };
        room = location;
    }

    /*
     * @return int[2] array of (x,y) location
     */
    public int[] getLocation() {
        return room;
    }

    /*
     * @return String name
     */
    public String getName() {
        return name;
    }

    /*
     * @param name Person's new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /*
     * @return Item object the person holds
     */
    public Item getHolds() {
        return holds;
    }

    /*
     * Item name method which safeguards against NPE
     * 
     * @return String item name, null if no item
     */
    public String getItemName() {
        if (holds == null) {
            return null;
        }
        return holds.getItemName();
    }

    /*
     * @return True if murderer
     */
    public boolean isMurderer() {
        return isMurderer;
    }

    /*
     * Seths the isMurderer field
     * 
     * @param isMurderer Boolean true if murderer
     */
    public void setMurderer(boolean isMurderer) {
        this.isMurderer = isMurderer;
    }

    /*
     * @return int number of spaces left the person can move
     */
    public int getMoves() {
        return moves;
    }

    /*
     * Sets the number of moves field
     * 
     * @param moves The number of spaces the person can now move
     */
    public void setMoves(int moves) {
        this.moves = moves;
    }

    /*
     * if holds = null (person isn't holding an item)
     * 
     * @return True if person is holding an item
     */
    public boolean hasItem() {
        return holds != null;
    }

    /*
     * Makes player pick up item
     * 
     * @param item Item to be held
     */
    public void pickUpItem(Item item) {
        holds = item;
    }

    /*
     * Removes item from player possession
     * 
     * @return Item dropped item
     */
    public Item dropItem() {
        Item item = holds;
        holds = null;
        return item;
    }

}
