package election;

public class YearNode {
    
    // Current year
    private int year;
    // Circular Linked Lists of states with an election in the current cycle
    private StateNode states;
    // Next YearNode
    private YearNode next;

    public YearNode(int yr) {
        year = yr;
        states = null;
        next = null;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public StateNode getStates() {
        return states;
    }

    public void setStates(StateNode states) {
        this.states = states;
    }

    public void setNext(YearNode nxt) {
        next = nxt;
    }

    public YearNode getNext() {
        return next;
    }
}
