package venom;

import java.util.ArrayList;

/**
 * The Venom class represents a binary search tree of SymbioteHost objects.
 * Venom is a sentient alien symbiote with a liquid-like form that requires a
 * host to bond with for its survival. The host is granted superhuman abilities
 * and the symbiote gains a degree of autonomy. The Venom class contains methods
 * that will help venom find the most compatible host. You are Venom.
 * 
 * @author Ayla Muminovic
 * @author Shane Haughton
 * @author Elian D. Deogracia-Brito
 */
public class Venom {
    private SymbioteHost root;

    /**
     * DO NOT EDIT THIS METHOD
     * 
     * Default constructor.
     */
    public Venom() {
        root = null;
    }

    /**
     * This method is provided to you
     * Creates an array of SymbioteHost objects from a file. The file should
     * contain the number of people on the first line, followed by the name,
     * compatibility, stability, and whether they have antibodies on each
     * subsequent line.
     * 
     * @param filename the name of the file
     * @return an array of SymbioteHosts (should not contain children)
     */
    public SymbioteHost[] createSymbioteHosts(String filename) {
        // DO NOT EDIT THIS METHOD
        StdIn.setFile(filename);
        int numOfPeople = StdIn.readInt();
        SymbioteHost[] people = new SymbioteHost[numOfPeople];
        for (int i = 0; i < numOfPeople; i++) {
            StdIn.readLine();
            String name = StdIn.readLine();
            int compatibility = StdIn.readInt();
            int stability = StdIn.readInt();
            boolean hasAntibodies = StdIn.readBoolean();
            SymbioteHost newPerson = new SymbioteHost(name, compatibility, stability, hasAntibodies, null, null);
            people[i] = newPerson;
        }
        return people;
    }

    /**
     * Inserts a SymbioteHost object into the binary search tree.
     * 
     * @param symbioteHost the SymbioteHost object to insert
     */
    public void insertSymbioteHost(SymbioteHost symbioteHost) {
        // WRITE YOUR CODE HERE
        if(root == null){
            root = symbioteHost;
        }else{
            SymbioteHost parent = null;
            SymbioteHost ptr = root;
            while(ptr != null){
                parent = ptr;
                if(ptr.getName().compareTo(symbioteHost.getName()) > 0){ // ptr is greater then the para;
                    ptr = ptr.getLeft();
                }else if(ptr.getName().compareTo(symbioteHost.getName()) < 0){ // ptr is less then the para;
                    ptr = ptr.getRight();
                }else{ // they are the same
                    ptr.setSymbioteCompatibility(symbioteHost.getSymbioteCompatibility());
                    ptr.setMentalStability(symbioteHost.getMentalStability());
                    ptr.setHasAntibodies(symbioteHost.hasAntibodies());
                    return;
                }
            }
            if(parent.getName().compareTo(symbioteHost.getName()) > 0){
                parent.setLeft(symbioteHost);
            }else{
                parent.setRight(symbioteHost);
            }
        }
    }

    /**
     * Builds a binary search tree from an array of SymbioteHost objects.
     * 
     * @param filename filename to read
     */
    public void buildTree(String filename) {
        SymbioteHost[] temp = createSymbioteHosts(filename);
        for(SymbioteHost i: temp){
            insertSymbioteHost(i);
        }
        System.out.println(temp.length);
    }

    /**
     * Finds the most compatible host in the tree. The most compatible host
     * is the one with the highest suitability.
     * PREorder traversal is used to traverse the tree. The host with the highest suitability
     * is returned. If the tree is empty, null is returned.
     * 
     * USE the calculateSuitability method on a SymbioteHost instance to get
     * a host's suitability.
     * 
     * @return the most compatible SymbioteHost object
     */
    public SymbioteHost findMostSuitable() {
        if (root == null) {
            return null;
        }
        return recur(root);
    }
    private SymbioteHost recur(SymbioteHost ptr) {
        if (ptr == null) {
            return null;
        }
        
        // Current Node
        SymbioteHost b = ptr;
        int best = ptr.calculateSuitability();
        
        // Left one
        SymbioteHost lbh = recur(ptr.getLeft());
        if (lbh != null && lbh.calculateSuitability() > best) {
            b = lbh;
            best = lbh.calculateSuitability();
        }
        
        // Right one
        SymbioteHost rbh = recur(ptr.getRight());
        if (rbh != null && rbh.calculateSuitability() > best) {
            b = rbh;
        }
        return b;
    }
    
    /**
     * Finds all hosts in the tree that have antibodies. INorder traversal is used to
     * traverse the tree. The hosts that have antibodies are added to an
     * ArrayList. If the tree is empty, null is returned.
     * 
     * @return an ArrayList of SymbioteHost objects that have antibodies
     */
    public ArrayList<SymbioteHost> findHostsWithAntibodies() {
        // WRITE YOUR CODE HERE
        if (root == null) {
            return null;
        }
        ArrayList<SymbioteHost> array = new ArrayList<>();

        return recur2(root, array);  // UPDATE this line, provided so code compiles
    }

    private ArrayList<SymbioteHost> recur2(SymbioteHost ptr, ArrayList<SymbioteHost> arr) {
        if (ptr == null) {
            return null;
        }
        
        // left one
        recur2(ptr.getLeft(), arr);
        SymbioteHost b = ptr;
        boolean has = ptr.hasAntibodies();
        if(has){
            arr.add(b);
        }
        // Right one
        recur2(ptr.getRight(), arr);
        return arr;
    }



    /**
     * Finds all hosts in the tree that have a suitability between the given
     * range. The range is inclusive. Level order traversal is used to traverse the tree. The
     * hosts that fall within the range are added to an ArrayList. If the tree
     * is empty, null is returned.
     * 
     * @param minSuitability the minimum suitability
     * @param maxSuitability the maximum suitability
     * @return an ArrayList of SymbioteHost objects that fall within the range
     */
    public ArrayList<SymbioteHost> findHostsWithinSuitabilityRange(int minSuitability, int maxSuitability) {
        if(root == null){
            return null;
        }
        // result
        ArrayList<SymbioteHost> array = new ArrayList<>();
        // Queue
        Queue<SymbioteHost> queue = new Queue<>();
        queue.enqueue(root);

        // Steps
        while(!queue.isEmpty()){
            SymbioteHost temp = queue.dequeue();
            if(temp != null && temp.calculateSuitability() >= minSuitability && temp.calculateSuitability() <= maxSuitability){
                array.add(temp);
            }
            if(temp.getLeft() != null){
                queue.enqueue(temp.getLeft());
            }
            if(temp.getRight() != null){
                queue.enqueue(temp.getRight());
            }
        }
        
        return array; // UPDATE this line, provided so code compiles
    }


    /**
     * Deletes a node from the binary search tree with the given name.
     * If the node is not found, nothing happens.
     * 
     * @param name the name of the SymbioteHost object to delete
     */
    public void deleteSymbioteHost(String name) {
        root = recur3(root, name);
    }
    private SymbioteHost recur3(SymbioteHost ptr, String name){
        if(ptr == null){
            return null;
        }
        int cmp = ptr.getName().compareTo(name);
        if(cmp > 0){
            ptr.setLeft(recur3(ptr.getLeft(), name));
        }else if(cmp < 0){
            ptr.setRight(recur3(ptr.getRight(), name));
        }else{
            if (ptr.getRight() == null) return ptr.getLeft();
            if (ptr.getLeft() == null) return ptr.getRight();

            SymbioteHost t = ptr;
            ptr = findMin(t.getRight());
            ptr.setRight(deleteMin(t.getRight()));
            ptr.setLeft(t.getLeft());
        }
        return ptr;
    }

    private SymbioteHost findMin(SymbioteHost k) {
        if(k.getLeft() == null) return k;
        else{return findMin(k.getLeft());}
    }
    
    private SymbioteHost deleteMin(SymbioteHost k) {
        if (k.getLeft() == null) {
            return k.getRight();
        }
        k.setLeft(deleteMin(k.getLeft())); 
        return k;
    }

    /**
     * Challenge - worth zero points
     *
     * Heroes have arrived to defeat you! You must clean up the tree to
     * optimize your chances of survival. You must remove hosts with a
     * suitability between 0 and 100 and hosts that have antibodies.
     * 
     * Cleans up the tree by removing nodes with a suitability of 0 to 100
     * and nodes that have antibodies (IN THAT ORDER).
     */
    public void cleanupTree() {
        // WRITE YOUR CODE HERE
    }

    /**
     * Gets the root of the tree.
     * 
     * @return the root of the tree
     */
    public SymbioteHost getRoot() {
        return root;
    }

    /**
     * Prints out the tree.
     */
    public void printTree() {
        if (root == null) {
            return;
        }

        // Modify no. of '\t' based on depth of node
        printTree(root, 0, false, false);
    }

    private void printTree(SymbioteHost root, int depth, boolean isRight, boolean isLeft) {
        System.out.print("\t".repeat(depth));

        if (isRight) {
            System.out.print("|-R- ");
        } else if (isLeft) {
            System.out.print("|-L- ");
        } else {
            System.out.print("+--- ");
        }

        if (root == null) {
            System.out.println("null");
            return;
        }

        System.out.println(root);

        if (root.getLeft() == null && root.getRight() == null) {
            return;
        }

        printTree(root.getLeft(), depth + 1, false, true);
        printTree(root.getRight(), depth + 1, true, false);
    }
}
