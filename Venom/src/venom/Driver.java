package venom;

import java.util.ArrayList;

/**
 * This is a Driver class which YOU implement.
 * You do not have to submit this file, but you can use it
 * to test your Venom.java methods. 
 * 
 * We give you directions for the first task and how to
 * set up this driver. Build off the examples to make this driver work.
 * 
 * Alternatively, you can use JUnit tests to test your code (see VenomTest.java).
 */
public class Driver {

    public static void main(String[] args) {

        // Remove the next two lines once you've implemented the Driver
        System.out.println("You have not yet implemeted the driver!\nIt is not given to you in this assignment.\nView the assignment description or Driver.java for more details.");
        int i = 0; if (i == 0) {return;} // REMOVE THIS


        Venom test = new Venom();
        Driver.printCommands();
        String line = StdIn.readLine();

        while (line != null && !line.equals("6")) {
            switch (line){
                case "1": 
                    test = new Venom(); // Starts over
                    System.out.println("Enter a filename: ");
                    String file = StdIn.readLine();
                    // Call buildTree(file) on the Venom "test" object with the String "file"  
                    // Then, call printTree() on it.

                    // This line just makes sure standard input is reset to the terminal, not your file
                    StdIn.resetFile();

                    break;
                case "2": 
                    // Call findMostSuitable() on the Venom "test" object and print its result. 
                    // You can use the SymbioteHost toString() method when printing, to get a 
                    // host's info as a string

                    break;
                case "3": 
                    System.out.println("Enter Min Suitability: ");
                    int min = Integer.parseInt(StdIn.readLine());
                    System.out.println("Enter Max Suitability: ");
                    int max = Integer.parseInt(StdIn.readLine());
                    // Call findHostsWithinSuitabilityRange(min, max) on the Venom "test" object with above min and max
                    // Then, print out each of the elements in its return ArrayList.
                    // You can use the SymbioteHost toString() method when printing, to get a 
                    // host's info as a string

                    break;
                case "4": 
                    System.out.println("Enter a Host Name to delete:");
                    String name = StdIn.readLine();
                    // Call deleteSymbioteHost(name) on the Venom "test" object with the read in host name
                    // Then, print out the tree with test.printTree()

                    break;
                case "5": 
                    // Call cleanupTree() on the Venom "test" object
                    // Then print the tree with test.printTree()

                    break;
                case "6": 
                    // DO NOT MODIFY
                    System.out.println("Exiting...");
                    return;
            }
            System.out.println("\n");
            Driver.printCommands();
            line = StdIn.readLine();
        }
        System.out.println("Exiting...");
    }

    private static void printCommands() {
        System.out.println("1) buildTree(String filename)");
        System.out.println("2) findMostSuitable()");
        System.out.println("3) findHostsWithinSuitabilityRange(int minSuitability, int maxSuitability)");
        System.out.println("4) deleteSymbioteHost(String name)");
        System.out.println("5) cleanupTree()");
        System.out.println("6) Exit");
        System.out.println("Choose a command (1-5): ");
    }
}