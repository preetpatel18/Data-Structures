package venom;

/**
 * This class stores a Symbiote Host node, along with
 * a reference to its left and right children.
 * 
 * @author Shane Haughton
 * @author Ayla Muminovic
 * @author Elian Deogracia-Brito
 */
public class SymbioteHost {
    
    /* Symbiote Host's characteristics */
    private String name;              /* Its name */
    private int symbioteCompatibility;/* Its compatibility with the symbiote (Venom) */
    private int mentalStability;      /* Its mental stability */
    private boolean hasAntibodies;    /* True if it has antibodies, preventing it from being a victim of Venom */
    private SymbioteHost left;        /* BST's left subtree */
    private SymbioteHost right;       /* BST's right subtree */

    /**
     * Default constructor.
     */
    public SymbioteHost() {
        name = null;
        symbioteCompatibility = 0;
        mentalStability = 0;
        hasAntibodies = false;
        left = null;
        right = null;
    }

    /**
     * Parameterized constructor. Takes in parameters and updates instance
     * variables to them.
     * @param name the new name
     * @param symbioteCompatibility the new symbiote compatibility
     * @param hasAntibodies whether or not this new host has antibodies
     * @param left a left reference
     * @param right a right reference
     */
    public SymbioteHost(String name, int symbioteCompatibility, int mentalStability, boolean hasAntibodies,
            SymbioteHost left, SymbioteHost right) {
        this.name = name;
        this.symbioteCompatibility = symbioteCompatibility;
        this.mentalStability = mentalStability;
        this.hasAntibodies = hasAntibodies;
        this.left = left;
        this.right = right;
    }

    public SymbioteHost(String name, int symbioteCompatibility, int mentalStability, boolean hasAntibodies) {
        // References the parameterized constructor
        this(name, symbioteCompatibility, mentalStability, hasAntibodies, null, null);
    }

    public SymbioteHost(String name, int symbioteCompatibility, int mentalStability) {
        // References the parameterized constructor
        this(name, symbioteCompatibility, mentalStability, false, null, null);
    }

    /*
     * Returns the string description of the host
     * @returns the SymbioteHost's characteristics
     */
    @Override
    public String toString() {
        return "Host{" +
                "name='" + name + '\'' +
                ", symbioteCompatibility=" + symbioteCompatibility +
                ", mentalStability=" + mentalStability +
                ", hasAntibodies=" + hasAntibodies +
                ", suitability=" + calculateSuitability() +
                '}';
    }

    /*
     * Compares this object with other
     * @param other the object to compare
     * @return true if this object equals param other, false otherwise
     */
    public boolean equals (Object other) {
        if ( !(other instanceof SymbioteHost) ) {
            return false;
        }
        SymbioteHost o = (SymbioteHost)other;
        return name.equals(o.name) &&
        symbioteCompatibility == o.symbioteCompatibility &&
        mentalStability == o.mentalStability &&
        hasAntibodies == o.hasAntibodies;
    }

    /*
     * Calculates and returns the suitability
     */
    public int calculateSuitability() {
        // This formula assumes that a moderate level of mental stability is ideal
        int stabilityFactor = 100 - Math.abs(50 - mentalStability);
        return symbioteCompatibility + stabilityFactor;
    }

    // Getters and setters.

    public String getName() {
        return name;
    }

    public int getSymbioteCompatibility() {
        return symbioteCompatibility;
    }

    public int getMentalStability() {
        return mentalStability;
    }

    public boolean hasAntibodies() {
        return hasAntibodies;
    }

    public SymbioteHost getLeft() {
        return left;
    }

    public SymbioteHost getRight() {
        return right;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSymbioteCompatibility(int symbioteCompatibility) {
        this.symbioteCompatibility = symbioteCompatibility;
    }

    public void setMentalStability(int mentalStability) {
        this.mentalStability = mentalStability;
    }

    public void setHasAntibodies(boolean hasAntibodies) {
        this.hasAntibodies = hasAntibodies;
    }

    public void setLeft(SymbioteHost left) {
        this.left = left;
    }

    public void setRight(SymbioteHost right) {
        this.right = right;
    }
}
