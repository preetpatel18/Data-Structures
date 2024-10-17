package election;

public class StateNode {
    
    // Name of state
    private String stateName;
    // Elections for this state in this year
    private ElectionNode elections;
    // Next StateNode
    private StateNode next;

    public StateNode() {

    }

    public StateNode(String name, StateNode nxt) {
        stateName = name;
        next = nxt;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public ElectionNode getElections() {
        return elections;
    }

    public void setElections(ElectionNode election) {
        this.elections = election;
    }

    public StateNode getNext() {
        return next;
    }

    public void setNext(StateNode next) {
        this.next = next;
    }

    
}
