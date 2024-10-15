package mystery;

import java.util.*;

/*
   𝘞𝘌𝘓𝘊𝘖𝘔𝘌 𝘛𝘖.....
   ___ _  _ ____    _  _ ____ _  _ ____ _ ____ _  _ 
    |  |__| |___    |\/| |__| |\ | [__  | |  | |\ | 
    |  |  | |___    |  | |  | | \| ___] | |__| | \|                                            

 * This is where the game takes place, both in the story and in the code.
 * 
 * A group of mysterious strangers have been invited to a dinner party, 
 * at a mansion in a secluded part of town, for an unknown reason. 
 * Little do they know, they may not all survive until dessert. 
 * 
 * You will have to investigate the mansion in different rooms throughout
 * the night, to answer questions about the events which take place.
 * 
 * Just be sure to stay away from the champagne, I heard it's 𝓉𝑜 𝒹𝒾𝑒 𝒻𝑜𝓇...
 * 
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * 
 * This class simulates an generic version of a Murder Mystery board game.
 * 
 * Use breakpoints, code modifications, added print statements, and more
 * debugging techiques to solve the mystery, and close the case. 
 *  
 * @author Colin Sullivan
 * @author Steven Chen
 */
public class Mansion {

    // 2D array of Rooms to be filled in createMansion()
    private Room[][] roomMap;
    // Array of items in mansion
    private ArrayList<Item> items;
    // Total items to spawn
    private int totalItems;

    // Array of players in game, length = # of players
    private Person[] players;
    // Index of murderer in players[]
    private int murderer;
    // If cooldown > time, the murderer will 99/100 times not kill
    private int cooldown;

    // Starting hour of game
    private int start;
    // End hour of the game
    private int end;
    // Time, meaning minutes past the starting hour, incremented by 5
    private int time;

    /*
     * Mansion constructor which sets random seed based on netID
     * 
     * Time starts at 0, representing minutes passed since 8pm
     * 
     * @param netID The netID to use for random seeding
     */
    public Mansion(String netID, boolean debug) {

        // Time starts at 0 minutes past starting time, moves in +5 (min) increments
        time = 0;
        // Game start at 6pm and ends at 10pm
        start = 6;
        end = 11;
        // The murderer has to wait 5 turns to attack initially
        cooldown = 25;
        // Set custom netID seed for game
        StdRandom.setSeed(MurderMystery.hash(netID));
        // Generate rooms, players, items
        this.createMansion();

        // Print intro and lore
        if (!debug) {
            MurderMystery.printIntro(players);
        }
    }

    /*
     * This is the method used to run the game, turn by turn
     * 
     * Completes one turn of the simulation (5 in-game minutes)
     * Gives each player a chance to move/act, ending with the murderer
     * 
     * @return True if the game is not over after the turn
     */
    public boolean nextTurn() {

        // If everyone except the murderer is dead, the game ends
        if (murdererWins()) {
            end = time;
            return false;
        }

        // Each player gets a turn, ending with the murderer
        for (int i = 0; i < players.length; i++) {

            // Grab current player and their location
            Person player = players[(i + murderer + 1) % players.length];

            // Skip dead players
            if (!player.alive()) {
                continue;
            }

            // Set movement based on dice roll
            player.setMoves(StdRandom.uniformInt(3, 8));

            // Each player gets to try to move twice
            for (int tried = 0; (tried < 2 && player.getMoves() > 0);) {

                // People in current room
                ArrayList<Person> peopleInRoom = roomMap[player.getLocation()[0]][player.getLocation()[1]].getPeople();
                // Coordinates to move player to
                int destinationRoom[];
                // Door weights
                double[] weights = roomMap[player.getLocation()[0]][player.getLocation()[1]].getDoorWeights();
                // Door costs
                int[] costs = roomMap[player.getLocation()[0]][player.getLocation()[1]].getDoorCosts();
                // Door position
                int[] dim = { roomMap.length, roomMap[0].length };

                // LOGIC FOR MURDERER KILLING/MOVING
                if (player.isMurderer()) {
                    // If the murderer is alone with someone
                    // And the murderer has an item (potential weapons)
                    // And player.attack() returns true (the murderer is not on cooldown)
                    // If the murderer is on cooldown, they have a 1/100 chance to ignore it and
                    // attack anyway
                    boolean murdererAloneWithPlayer = roomMap[player.getLocation()[0]][player.getLocation()[1]]
                            .alivePeople() == 2;
                    boolean murdererShouldAttack = player.attack(time, cooldown, StdRandom.uniformInt(100) == 0)
                            && player.hasItem() && !player.getHolds().isMarked();
                    if (murdererAloneWithPlayer && murdererShouldAttack) {
                        // find the other alive person and kill them
                        for (int p = 0; p < peopleInRoom.size(); p++) {
                            if (!peopleInRoom.get(p).getName().equals(player.getName())
                                    && peopleInRoom.get(p).alive()) {
                                // Mark victim as dead
                                peopleInRoom.get(p).kill();
                                // Mark the item used in the murder
                                player.getHolds().setMarked(true);
                                // Set the murderers new random cooldown
                                cooldown = time + (StdRandom.uniformInt(1, 4) * 5); // Update cooldown
                            }
                        }
                    }

                    // After possibly committing murder, move like a normal player.
                    // If did not commit murder, then pursure the closest player.

                    // Remove murderer from room in preperation to move
                    roomMap[player.getLocation()[0]][player.getLocation()[1]].removePlayer(player.getName());
                    // If on cooldown, act like a normal player
                    if (cooldown > time) {
                        destinationRoom = player.chooseDoor(weights, costs, player.getLocation(), dim);
                        tried++;

                    }
                    // If not on cooldown, pursue closest alive player
                    else {
                        destinationRoom = player.pursue(costs, player.getLocation(), dim, players);
                        tried++;
                    }
                }
                // If its not the murderer, move randomly
                else {
                    // Remove player from room (in preparation to move)
                    roomMap[player.getLocation()[0]][player.getLocation()[1]].removePlayer(player.getName());
                    // Calculate new coordinates of player
                    destinationRoom = player.chooseDoor(weights, costs, player.getLocation(), dim);
                    tried++;
                }
                // Move player
                player.setLocation(destinationRoom[0], destinationRoom[1]); // Move player by changing location
                                                                            // attribute
                roomMap[destinationRoom[0]][destinationRoom[1]].addPlayer(player); // Add player to new room
            }

            // LOGIC FOR PLAYERS PICKING UP/DROPPING ITEMS

            Room currentRoom = roomMap[player.getLocation()[0]][player.getLocation()[1]];
            boolean playerHasItem = player.hasItem();
            boolean roomHasItem = !currentRoom.getItems().isEmpty();
            if (playerHasItem && !roomHasItem) {
                // If player is murderer and is holding murder weapon, drop it
                if (player.isMurderer() && playerHasItem && player.getHolds().isMarked()) {
                    currentRoom.addItem(player.dropItem());
                }
                // If not murderer, randomly drop item
                else if (!player.isMurderer() && StdRandom.uniformInt(20) == 0) { // 1 / 20 chance
                    currentRoom.addItem(player.dropItem()); // currentRoom holds dropped Item
                }
            }
            // If player doesnt have item and room has item
            else if (!playerHasItem && roomHasItem) {
                if (StdRandom.uniformInt(5) != 0) { // 4 / 5 chance
                    Item pickedUpItem = currentRoom.getItems().remove(0); // pick up Item
                    // If item is valid and not a murder weapon, pick up
                    if (pickedUpItem != null && !pickedUpItem.isMarked()) {
                        player.pickUpItem(pickedUpItem); // players holds Item
                    } else {
                        currentRoom.addItem(pickedUpItem); // put item back if it's marked
                    }
                }
            }
            // If player has item and room has item
            else if (playerHasItem && roomHasItem) {
                // If the player is murderer and is holding a murder weapon
                if (player.isMurderer() && playerHasItem && player.getHolds().isMarked()) {
                    // If the room's item is not a murder weapon
                    if (!currentRoom.getItems().get(0).isMarked()) {
                        // Drop the murder weapons
                        currentRoom.addItem(player.dropItem());
                        // Pick up the fresh item
                        player.pickUpItem(currentRoom.getItems().remove(0));
                    }
                }
                // Else if the player is not the murderer and a coin flip
                else if (!player.isMurderer() && StdRandom.uniformInt(2) == 0) { // 1 / 2 chance
                    Item pickedUpItem = currentRoom.getItems().remove(0);
                    Item droppedItem = player.dropItem();
                    // If items are valid and the rooms item isnt a murder weapon
                    if (pickedUpItem != null && droppedItem != null && !pickedUpItem.isMarked()) {
                        // Player and room swap item
                        player.pickUpItem(pickedUpItem);
                        currentRoom.addItem(droppedItem);
                    } else {
                        // Else, put both back
                        currentRoom.addItem(pickedUpItem);
                        player.pickUpItem(droppedItem);
                    }
                }
            }
        }

        time += 5; // Turn over, increment time by 5 minutes
        if (time >= ((end - start) * 60)) { // If time passes the end time of the game
            return false; // End game
        }
        return true;
    }

    ////////////// You do not have to debug anything past this point to find your
    ////////////// answers//////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////// Getters / Setters
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// ////////////////////////////////////////////////////////////////////////////////////////////////

    /*
     * Returns the time
     * 
     * @return integer time, minutes past start. Increments of 5.
     */
    public int time() {
        return this.time;
    }

    /*
     * @return 2d array of rooms
     */
    public Room[][] getRooms() {
        return roomMap;
    }

    /*
     * @return Array of person objects (players)
     */
    public Person[] getPlayers() {
        return players;
    }

    /*
     * @return Array of item objects spawned in mansion
     */
    public ArrayList<Item> getItems() {
        return items;
    }

    /*
     * @return int number of alive people
     */
    public int alivePlayers() {
        int alive = 0;
        for (int i = 0; i < players.length; i++) {
            if (players[i].alive()) {
                alive++;
            }
        }
        return alive;
    }

    /*
     * @return int[] {start time, end time}
     */
    public int[] getSE() {
        int[] startandend = { start, end };
        return startandend;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////// GENERATION /// METHODS
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// /////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
     * Creates the mansion "game-board" and all the contained pieces
     * Includes choosing players, choosing rooms and items, and more
     */
    private void createMansion() {
        players = this.generatePlayers(); // Generate players, choose murderer
        roomMap = this.generateRooms(); // Generate mansion size, rooms, items, and starting location
        totalItems = (roomMap.length * roomMap[0].length) / 2;
        if (totalItems < 5) {
            totalItems = 5;
        }
        this.spawnItems();
    }

    /*
     * ### NOT REQUIRED TO DEBUG ###
     * 
     * Populates players[] array of Person objects
     * 
     * Reads in all people from People.in, and randomly selects 5-9 of them
     * to be players in the game.
     * 
     * Randomly chooses one of the people to be the murderer
     * 
     * @returns array of Person objects who are the players of the game
     */
    private Person[] generatePlayers() {

        StdIn.setFile("People.in"); // Read from people input file
        int totalAvailiblePeople = Integer.parseInt(StdIn.readLine()); // Number of people contained in People.in

        int numberOfPlayers = 6; // StdRandom.uniformInt(5, 10); // Game will contain 5-9 players
        Person[] castOfPlayers = new Person[numberOfPlayers]; // Temp variable to hold chosen people

        ArrayList<Person> peopleToChooseFrom = new ArrayList<Person>(); // Holds all potential people
        for (int i = 0; i < totalAvailiblePeople; i++) { // Reads in all potential people
            Person toAdd = new Person(StdIn.readLine());
            peopleToChooseFrom.add(toAdd);
        }

        ArrayList<Integer> taken = new ArrayList<Integer>();
        for (int p = 0; p < numberOfPlayers;) { // Loop to chose each character
            int chosenPerson = StdRandom.uniformInt(0, totalAvailiblePeople); // Choose a character forom list
            if (taken.contains(chosenPerson) || (p % 2 == 0 && chosenPerson % 2 != 0)) {
                continue;
            }
            taken.add(chosenPerson);
            castOfPlayers[p] = peopleToChooseFrom.get(chosenPerson); // Remove chosen character from list and add to
                                                                     // game
            p++;
        }

        this.murderer = StdRandom.uniformInt(castOfPlayers.length); // choose index in players[] to be the murderer
        castOfPlayers[murderer].setMurderer(true);

        StdIn.resetFile();
        return castOfPlayers;
    }

    /*
     * ### NOT REQUIRED TO DEBUG ###
     * 
     * Generates randomly the roomMap array of rooms representing the mansion
     * 
     * Creates a mansion (Room[][]) between 4x3 and 6x4 (12-24 rooms)
     * 
     * Reads in all potentail rooms, and populates the mansion randomly from them.
     * While doing so, randomly populates some rooms with their item.
     * 
     * Selects one room on an edge of the array to be the starting point
     * ("The Foyer"),
     * and places all players inside that room.
     * 
     * @returns mansion, where mansion is the 2D array of rooms containing
     * items/people
     */
    private Room[][] generateRooms() {
        StdIn.setFile("Rooms.in"); // Read from rooms input file
        int potentialRooms = Integer.parseInt(StdIn.readLine()); // Total rooms to choose from (minus starting room)

        // Genarate Mansion size
        Room[][] mansion = new Room[StdRandom.uniformInt(4, 7)][StdRandom.uniformInt(3, 5)];

        // Read rooms into a temp array, to randomly choose from
        // Room input file format is: 1 line with #ofRooms, then 3 lines per room
        // One line with name, one line with 4 doubles for weights, one line with 4 ints
        // for costs
        ArrayList<Room> roomsToChooseFrom = new ArrayList<Room>();
        for (int i = 0; i < potentialRooms; i++) { // Read in all potential rooms, including name, weights, cost
            String name = StdIn.readLine();
            double[] doorWeights = { StdIn.readDouble(), StdIn.readDouble(), StdIn.readDouble(), StdIn.readDouble() };
            StdIn.readLine(); // Read newline char
            int[] costs = { StdIn.readInt(), StdIn.readInt(), StdIn.readInt(), StdIn.readInt() };
            StdIn.readLine();
            Room toAdd = new Room(name, doorWeights, costs);
            roomsToChooseFrom.add(toAdd);
        }

        // Populates board with random rooms from potential rooms
        for (int x = 0; x < mansion.length; x++) {
            for (int y = 0; y < mansion[x].length; y++) {
                int chosenRoom = StdRandom.uniformInt(0, roomsToChooseFrom.size());
                mansion[x][y] = roomsToChooseFrom.remove(chosenRoom);
            }
        }

        // Logic for placing starting room, always at one of the four edges of the grid
        int x, y; // Coordinates for starting room
        int sideOfBoard = StdRandom.uniformInt(4); // Decide which edge (N, S, E, W)

        double[] startingWeights = { 0.25, 0.25, 0.25, 0.25 };
        int[] startingCosts = { 3, 3, 3, 3 };
        if (sideOfBoard == 0) { // North
            x = 0; // Top edge
            y = StdRandom.uniformInt(mansion[0].length); // Any spot
            mansion[x][y] = new Room("The Foyer", startingWeights, startingCosts);
        } else if (sideOfBoard == 1) { // South
            x = mansion.length - 1; // Bottom edge
            y = StdRandom.uniformInt(mansion[0].length); // Any spot
            mansion[x][y] = new Room("The Foyer", startingWeights, startingCosts);
        } else if (sideOfBoard == 2) { // East
            x = StdRandom.uniformInt(mansion.length); // Any spot
            y = mansion[0].length - 1; // Right edge
            mansion[x][y] = new Room("The Foyer", startingWeights, startingCosts);
        } else {
            x = StdRandom.uniformInt(mansion.length); // Any spot
            y = 0; // Left edge
            mansion[x][y] = new Room("The Foyer", startingWeights, startingCosts);
        }

        // Add players into starting room (The Foyer)
        for (int p = 0; p < players.length; p++) {
            players[p].setLocation(x, y);
            mansion[x][y].addPlayer(players[p]);
        }

        StdIn.resetFile();
        return mansion;
    }

    /*
     * ### NOT REQUIRED TO DEBUG ###
     * 
     * Randomly spawns items throughout the mansion
     * 
     * Reads in room, item pairs and then iterates through rooms
     * in the mansion. If we should spawn an item in that room (see
     * placeItemInRoom())
     * then remove the item from the hashmap and place it in the room
     */
    private void spawnItems() {

        StdIn.setFile("Items.in");
        int numberOfItems = Integer.parseInt(StdIn.readLine()); // reads the number of Items

        HashMap<String, Item> itemMap = new HashMap<>(); // Create hashMap to hold potential items (value), searchable
                                                         // by room name (key)

        for (int i = 0; i < numberOfItems; i++) {
            String itemName = StdIn.readLine();
            String roomName = StdIn.readLine();

            Item item = new Item(itemName); // create new Item with its room name
            item.setRoomName(roomName);
            itemMap.put(roomName, item); // store in the hashmap
        }

        items = new ArrayList<Item>();
        for (int x = 0; x < roomMap.length; x++) {
            for (int y = 0; (y < roomMap[x].length && items.size() <= totalItems); y++) {
                Room currentRoom = roomMap[x][y];
                // No item spawns in The Foyer
                if (currentRoom.getRoomName().equals("The Foyer")) {
                    continue;
                }
                // if the current room doesn't have an item
                else if (currentRoom.getItems().size() == 0 && placeItemInRoom(x, y)) {
                    Item item = itemMap.remove(currentRoom.getRoomName()); // Get item from hashmap
                    if (item == null) {
                        continue;
                    }
                    currentRoom.addItem(item); // add item to room
                    items.add(item);
                }
            }
        }

        // The murder starts with the reolver
        Item revolver = new Item("Revolver");
        players[murderer].pickUpItem(revolver);
        items.add(revolver);

        StdIn.resetFile();
    }

    /*
     * ### NOT REQUIRED TO DEBUG ###
     * 
     * Helper for spawnItems()
     * Checks the 4 neighboring rooms for items, and none exist
     * flips a coin to decide whether to place an item
     * 
     * @param x The room to check is roomMap[x][y]
     * 
     * @param y The room to check is roomMap[x][y]
     * 
     * @return True if we can and should place an item, false if not
     */
    private boolean placeItemInRoom(int x, int y) {
        // check surrounding 4 rooms for items
        int surround = 0; // true if theres an item in a bordering room
        int[] xarr = { -1, 0, 1, 0 };
        int[] yarr = { 0, -1, 0, 1 };
        for (int i = 0; i < 4; i++) {
            // cannot be out of bounds (x and y)
            if ((x + xarr[i]) >= 0 && (x + xarr[i]) < roomMap.length) {
                if ((y + yarr[i]) >= 0 && (y + yarr[i]) < roomMap[x].length) {
                    if (!((x + xarr[i]) == x && (y + yarr[i]) == y)) {
                        Room surroundingRoom = roomMap[(x + xarr[i])][y + yarr[i]];
                        if (surroundingRoom.getItems() != null && !surroundingRoom.getItems().isEmpty()) {
                            surround++;
                        }
                    }
                }
            }
        }
        return (StdRandom.uniformInt(3) != 0 && surround < 2);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////// GAME /// METHODS ///////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
     * ### NOT REQUIRED TO DEBUG ###
     * 
     * @return True if the murderer is the last alive
     */
    public boolean murdererWins() {
        return (this.alivePlayers() == 1 && players[murderer].alive());
    }

    /*
     * ### NOT REQUIRED TO DEBUG ###
     * 
     * First, calls the "Game Over!" animation
     * Then calls the ending time blurb
     * Finally, calls the sequence of methods which generates
     * and asks questions for the user, and returns the user input
     * 
     * @returns the return value for user question/answer input
     */
    public String[] endGame() {
        MurderMystery.endGameStats();
        return MurderMystery.askQuestions();
    }

    /*
     * ### NOT REQUIRED TO DEBUG ###
     * 
     * Reads in random questions from the questions input file
     * Then selects a random subset of those and returns them
     * 
     * Random Questions are stored as 3 string tokens, which are keywords
     * and parsing to form different questions
     * 
     * Parses the random selection for game specific attributes
     * like item names, times, players, etc
     * 
     * @returns A 2D String array, where each row is a question, and
     * each of 3 columns is a componenet of the question
     */
    public String[][] getRandomQuestions(int NUM_RANDOM_Q) {

        StdIn.resetFile();
        StdIn.setFile("Questions.in");

        // Skip the set questions (read in driver)
        int skip = StdIn.readInt();
        while (skip >= 0) {
            skip--;
            StdIn.readLine();
        }

        // Read in random question pool size
        int randomQuestionCount = Integer.parseInt(StdIn.readLine());

        // Create 2D strs array to hold question parts
        ArrayList<String[]> questionArray = new ArrayList<String[]>();
        for (int i = 0; i < randomQuestionCount; i++) {
            questionArray.add(new String[3]);
        }

        // Store each question in a row of the arraylist, parse the string based on ','
        // delimiter, and store each part in a column
        for (int line = 0; line < randomQuestionCount; line++) {

            String rawString = StdIn.readLine();
            String[] split = rawString.split(",");
            // Read in first part of question
            questionArray.get(line)[0] = split[0];

            // Add random variables to second token
            ArrayList<Item> items = this.getItems();
            if (split[1].equals("ITEM")) {
                // Randomly select item name
                Item item = items.get(StdRandom.uniformInt(items.size()));
                int tried = items.size() * 2;
                while (item.getItemName().equals(item.getRoomName()) && tried > 0) {
                    item = items.get(StdRandom.uniformInt(items.size()));
                    tried--;
                }
                questionArray.get(line)[1] = "the " + items.get(StdRandom.uniformInt(items.size())).getItemName();
            } else if (split[1].equals("TIME")) {
                questionArray.get(line)[1] = Integer.toString(StdRandom.uniformInt((this.time() / 5) + 1) * 5);
            } else {
                // Randomly select player name
                Person[] players = this.getPlayers();
                Person player = players[StdRandom.uniformInt(players.length)];
                int n = 0;
                while (player.alive() && n < 12) {
                    player = players[StdRandom.uniformInt(players.length)];
                    n++;
                }
                questionArray.get(line)[1] = player.getName();
            }

            // Add random variables to third token
            if (split[2].equals("TIME")) {
                questionArray.get(line)[2] = Integer.toString(StdRandom.uniformInt((this.time() / 5) + 1) * 5);
            } else {
                questionArray.get(line)[2] = split[2];
            }
        }

        String[][] chosenToParse = new String[NUM_RANDOM_Q][3];
        for (int i = 0; i < NUM_RANDOM_Q; i++) {
            String[] chosen = questionArray.remove(StdRandom.uniformInt(questionArray.size()));
            chosenToParse[i][0] = chosen[0];
            chosenToParse[i][1] = chosen[1];
            chosenToParse[i][2] = chosen[2];
        }

        return chosenToParse;
    }

    public String[] getFileQuestions(int NUM_RANDOM_Q) {
        StdIn.setFile("Questions.in");
        // Skip the set questions (read in driver)
        int skip = StdIn.readInt();
        while (skip >= 0) {
            skip--;
            StdIn.readLine();
        }
        skip = StdIn.readInt(); // Skip the random questions
        while (skip >= 0) {
            skip--;
            StdIn.readLine();
        }

        int codeQuestions = StdIn.readInt();
        StdIn.readLine();
        ArrayList<String> questions = new ArrayList<>();
        for (int line = 0; line < codeQuestions; line++) {
            questions.add(StdIn.readLine());
        }

        String[] chosenQuestions = new String[NUM_RANDOM_Q];
        for (int chosen = 0; chosen < NUM_RANDOM_Q; chosen++) {
            chosenQuestions[chosen] = questions.remove(StdRandom.uniformInt(questions.size()));
        }
        return chosenQuestions;
    }
}