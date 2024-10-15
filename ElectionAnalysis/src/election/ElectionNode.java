package election;

import java.util.*;

public class ElectionNode {

    // RaceID value
    private int raceID;
    // True if Senate election, False if House election
    private boolean senate;
    // Office Seat
    private int officeID;

    // Parallel ArrayLists for Candidates
    private ArrayList<String> candidates;
    // Parallel ArrayLists for candidates votes
    private ArrayList<Integer> votes;
    // Parallel ArrayLists for candidates parties
    private ArrayList<String> parties;
    // Index of winner in above parallel arrays
    private int winner;

    private ElectionNode next;

    public ElectionNode() {
        this.candidates = new ArrayList<String>();
        this.votes = new ArrayList<Integer>();
        this.parties = new ArrayList<String>();
    }
    
    public ElectionNode(int raceID, boolean senate, int ID, int[] votes,
            String[] parties, int winner, ElectionNode next) {
        this.raceID = raceID;
        this.senate = senate;
        this.officeID = ID;
        this.candidates = new ArrayList<String>();
        this.votes = new ArrayList<Integer>();
        this.parties = new ArrayList<String>();
        this.winner = winner;
        this.next = next;
    }

    public int getRaceID() {
        return raceID;
    }

    public void setRaceID(int raceID) {
        this.raceID = raceID;
    }

    public boolean isSenate() {
        return senate;
    }

    public void setSenate(boolean senate) {
        this.senate = senate;
    }

    public int getOfficeID() {
        return officeID;
    }

    public void setoOfficeID(int officeSeat) {
        this.officeID = officeSeat;
    }

    public ArrayList<String> getCandidates() {
        return candidates;
    }

    public boolean isCandidate(String person) {
        return candidates.contains(person);
    }

    public void addCandidate(String name, int vote, String party, boolean win) {
        candidates.add(name);
        votes.add(vote);
        parties.add(party);
        if (win) {
            this.winner = candidates.indexOf(name);
        }
    }

    public void modifyCandidate(String name, int vote, String party) {
        int index = candidates.indexOf(name);
        if (vote != -1) {
            votes.set(index, vote);
        }
        if (party != null) {
            parties.set(index, party);
        }
    }

    public boolean isWinner(String name) {
        if (name == null || candidates.size() == 0) {return false;}
        return name.equals(candidates.get(this.winner));
    }

    public String getWinner() {
        if (winner == -1) {
            return "No Winner Reported";
        }
        return candidates.get(winner);
    }

    public String winningParty() {
        return parties.get(winner);
    }

    public int getVotes() {
        int sum = 0;
        for (int v : votes) {
            sum += v;
        }
        return sum;
    }

    public int getVotes(String name) {
        if (name == null || votes.size() == 0 || candidates.indexOf(name) == -1) {return 0;}
        return votes.get(candidates.indexOf(name));
    }

    public String getParty(String name) {
        if (name == null || parties.size() == 0 || candidates.indexOf(name) == -1) {return "No Party Found";}
        return parties.get(candidates.indexOf(name));
    }

    public ElectionNode getNext() {
        return next;
    }

    public void setNext(ElectionNode next) {
        this.next = next;
    }
    
}
