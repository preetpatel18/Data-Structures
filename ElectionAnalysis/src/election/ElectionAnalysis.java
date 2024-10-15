package election;

import java.io.File;

/* 
 * Election Analysis class which parses past election data for the house/senate
 * in csv format, and implements methods which can return information about candidates
 * and nationwide election results. 
 * 
 * It stores the election data by year, state, then election using nested linked structures.
 * 
 * The years field is a Singly linked list of YearNodes.
 * 
 * Each YearNode has a states Circularly linked list of StateNodes
 * 
 * Each StateNode has its own singly linked list of ElectionNodes, which are elections
 * that occured in that state, in that year.
 * 
 * This structure allows information about elections to be stored, by year and state.
 * 
 * @author Colin Sullivan
 */
public class ElectionAnalysis {

    // Reference to the front of the Years SLL
    private YearNode years;

    public YearNode years() {
        return years;
    }

    /*
     * Read through the lines in the given elections CSV file
     * 
     * Loop Though lines with StdIn.hasNextLine()
     * 
     * Split each line with:
     * String[] split = StdIn.readLine().split(",");
     * Then access the Year Name with split[4]
     * 
     * For each year you read, search the years Linked List
     * -If it is null, insert a new YearNode with the read year
     * -If you find the target year, skip (since it's already inserted)
     * 
     * If you don't find the read year:
     * -Insert a new YearNode at the end of the years list with the corresponding year.
     * 
     * @param file String filename to parse, in csv format.
     */
    public void readYears(String file) {
		// WRITE YOUR CODE HERE
        StdIn.setFile(file);
        while (StdIn.hasNextLine()) {
           String[] split = StdIn.readLine().split(",");
           String y = split[4];
           YearNode c = years;

           // Checking if the year already exists
           while (c!= null) {
               if (c.getYear() == Integer.parseInt(y)) {
                   break;
               }
               c = c.getNext();
           }
           
           // adding Year at the end of the YearNod
           if (c == null) {
               c = new YearNode(Integer.parseInt(y));

               if (years == null) {
                   years = c;
               } else {
                   YearNode last = years;
                   while (last.getNext()!= null) {
                       last = last.getNext();
                   }
                   last.setNext(c);
               }
           }
        }
    }

    /*
     * Read through the lines in the given elections CSV file
     * 
     * Loop Though lines with StdIn.hasNextLine()
     * 
     * Split each line with:
     * String[] split = StdIn.readLine().split(",");
     * Then access the State Name with split[1] and the year with split[4]
     * 
     * For each line you read, search the years Linked List for the given year.
     * 
     * In that year, search the states list. If the target state exists, continue
     * onto the next csv line. Else, insert a new state node at the END of that year's
     * states list (aka that years "states" reference will now point to that new node).
     * Remember the states list is circularly linked.
     * 
     * @param file String filename to parse, in csv format.
     */
    public void readStates(String file) {
		// WRITE YOUR CODE HERE
        StdIn.setFile(file);
        while (StdIn.hasNextLine()) {
            String[] split = StdIn.readLine().split(",");
            String stateName = split[1];
            int year = Integer.parseInt(split[4]);
            YearNode c = years;

            // Finding that specific Year.
            while(c.getYear() != year){
                c = c.getNext();
            }

            // Each Years - State pointers
            StateNode first = c.getStates(); 

            if(first == null){
                StateNode newNode = new StateNode();
                newNode.setStateName(stateName);
                newNode.setNext(newNode);
                c.setStates(newNode);
            }else{
                // Checking if the state is already inserted;
                StateNode ptr = first;
                boolean b = false;
                do {
                    if (ptr.getStateName().equals(stateName)) {
                        b = true;
                        break;
                    }
                    ptr = ptr.getNext();
                } while (ptr != first);

                //if state Doesn't Exists
                if(!b){
                    StateNode newNode = new StateNode();
                    newNode.setStateName(stateName);
                    StateNode last = first;
                    //get to the last node
                    while(last.getNext() != first){
                        last = last.getNext();
                    }

                    newNode.setNext(first);
                    last.setNext(newNode); 
                }
            }   
        }
    }
    /*
     * Read in Elections from a given CSV file, and insert them in the
     * correct states list, inside the correct year node.
     * 
     * Each election has a unique ID, so multiple people (lines) can be inserted
     * into the same ElectionNode in a single year & state.
     * 
     * Before we insert the candidate, we should check that they dont exist already.
     * If they do exist, instead modify their information new data.
     * 
     * The ElectionNode class contains addCandidate() and modifyCandidate() methods for you to use.
     * 
     * @param file String filename of CSV to read from
     */
    public void readElections(String file) {
        StdIn.setFile(file);
        
        while (StdIn.hasNextLine()) {
            String[] split = StdIn.readLine().split(",");
    
            // Variables:
            int raceID = Integer.parseInt(split[0]);
            String stateName = split[1];
            int officeID = Integer.parseInt(split[2]);
            boolean senate = split[3].equals("U.S. Senate");
            int year = Integer.parseInt(split[4]);
            String canName = split[5];
            String party = split[6];
            int votes = Integer.parseInt(split[7]);
            boolean winner = split[8].toLowerCase().equals("true");
    
            // Finding the specific Year.
            YearNode current = years;
            while (current != null && current.getYear() != year) {
                current = current.getNext();
            }
    
            // Find the specific State
            StateNode curStateNode = current.getStates();
            StateNode first = curStateNode;
    
            do {
                if (curStateNode.getStateName().equals(stateName)) {
                    break;
                }
                curStateNode = curStateNode.getNext();
            } while (curStateNode != first); // Loop over states in a circular list
    

            ElectionNode electYear = curStateNode.getElections();
            ElectionNode findDup = electYear;
    
            
            // Find if the Election year Already Exists
            while (findDup != null && findDup.getRaceID() != raceID) {
                findDup = findDup.getNext();
            }
    
            if (findDup != null && findDup.getRaceID() == raceID) {
                // A) If the candidate already exists, modify their info
                if (findDup.getCandidates().contains(canName)) {
                    findDup.modifyCandidate(canName, votes, party);
                } else {
                    findDup.addCandidate(canName, votes, party, winner);
                }
            } else {
                // B) Create a new ElectionNode if not found
                ElectionNode newElection = new ElectionNode(raceID, senate, officeID, new int[]{votes}, split, votes, null);
    
                if (electYear == null) {
                    curStateNode.setElections(newElection);
                } else {
                    while (electYear.getNext() != null) {
                        electYear = electYear.getNext();
                    }
                    electYear.setNext(newElection);
                }
            }
        }
    }
    /*
     * DO NOT EDIT
     * 
     * Calls the next method to get the difference in voter turnout between two
     * years
     * 
     * @param int firstYear First year to track
     * 
     * @param int secondYear Second year to track
     * 
     * @param String state State name to track elections in
     * 
     * @return int Change in voter turnout between two years in that state
     */
    public int changeInTurnout(int firstYear, int secondYear, String state) {
        // DO NOT EDIT
        int last = totalVotes(firstYear, state);
        int first = totalVotes(secondYear, state);
        return last - first;
    }

    /*
     * Given a state name, find the total number of votes cast
     * in all elections in that state in the given year and return that number
     * 
     * If no elections occured in that state in that year, return 0
     * 
     * Use the ElectionNode method getVotes() to get the total votes for any single
     * election
     * 
     * @param year The year to track votes in
     * 
     * @param stateName The state to track votes for
     * 
     * @return avg number of votes this state in this year
     */
    public int totalVotes(int year, String stateName) {
      	// WRITE YOUR CODE HERE
        // Finding the specific Year.
        YearNode current = years;
        while (current != null && current.getYear() != year) {
            current = current.getNext();
        }
        if(current == null){
            return 0;
        }
        // I SAID ONE OF MY FREIND LOOKS LIKE HER
        // Find the specific State
        StateNode curStateNode = current.getStates();
        StateNode first = curStateNode;
        do {
            if (curStateNode.getStateName().equals(stateName)) {
                ElectionNode ENode = curStateNode.getElections();
                int totalVotes = 0;
                while(ENode != null){
                    totalVotes+=ENode.getVotes();
                    ENode = ENode.getNext();
                }
                return totalVotes;
            }
            curStateNode = curStateNode.getNext();
        } while (curStateNode != first); // Loop over states in a circular list
        
        return 0;
    }

    /*
     * Given a state name and a year, find the average number of votes in that
     * state's elections in the given year
     * 
     * @param year The year to track votes in
     * 
     * @param stateName The state to track votes for
     * 
     * @return avg number of votes this state in this year
     */
    public int averageVotes(int year, String stateName) {
      	// WRITE YOUR CODE HERE
        YearNode current = years;
        while (current != null && current.getYear() != year) {
            current = current.getNext();
        }
        if(current == null){
            return 0;
        }

        // Find the specific State
        StateNode curStateNode = current.getStates();
        StateNode first = curStateNode;
        do {
            if (curStateNode.getStateName().equals(stateName)) {
                ElectionNode ENode = curStateNode.getElections();
                int totalVotes = 0;
                int totalElect = 0;
                while(ENode != null){
                    totalVotes+=ENode.getVotes();
                    totalElect ++;
                    ENode = ENode.getNext();
                }
        
                  return totalVotes/totalElect;
            }
            curStateNode = curStateNode.getNext();
        } while (curStateNode != first); // Loop over states in a circular list
        
        return 0;
    }

    /*
     * Given a candidate name, return the party they most recently ran with 
     * 
     * Search each year node for elections with the given candidate
     * name. Update that party each time you see the candidates name and
     * return the party they most recently ran with
     * 
     * @param candidateName name to find
     * 
     * @return String party abbreviation
     */
    public String candidatesParty(String candidateName) {
		// WRITE YOUR CODE HERE
        YearNode current = years;
        String politicalParty = null;
        while (current != null) {
            
            StateNode curStateNode = current.getStates();
            StateNode first = curStateNode;
            do {
                ElectionNode ENode = curStateNode.getElections();

                while(ENode != null){
                    if(ENode.isCandidate(candidateName)){
                        politicalParty = ENode.getParty(candidateName);
                        break;
                    }
                    ENode = ENode.getNext();
                }
                curStateNode = curStateNode.getNext();
            } while (curStateNode != first); // Loop over states in a circular list
        }
        return politicalParty;
    }
}