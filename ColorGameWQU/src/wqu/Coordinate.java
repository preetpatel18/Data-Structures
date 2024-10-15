package wqu;

/*
 * Coordinate class representing a (row, col) aka (y,x) pair 
 * @author Colin Sullivan
 */
public class Coordinate {
    // Row and col attributes for this coordinate
    public int row;
    public int col;

    /*
     * Coordinate constructor which takes in and sets the
     * row and col attributes
     */
    public Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /*
     * Coordinate equals method to compare coodinates
     * 
     * @return boolean true if this equals other
     */
    public boolean equals(Coordinate other) {
        return row == other.row && col == other.col;
    }
}
