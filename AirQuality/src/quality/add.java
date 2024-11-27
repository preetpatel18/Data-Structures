package quality;

import java.text.DecimalFormat;
import java.util.List;

public class add {

    public static AirQuality test;
     public static void testAddState(String inputFile) {

        test = new AirQuality();
        // Calls buildTable on the inputFile in parameters
        test.buildTable(inputFile);
        
        // Calls printStates on the newly created hash table to just print out states
        printEntireTable(test.getHashTable());

    } 

    public static void main(String[] args) {
        testAddState("rehashCounties.csv");
    }


    // USE the following methods to print out states, counties, and AQIs.
    // Copy this file and use it as a template.

    /**
     * **DO NOT MODIFY THIS METHOD**
     * Prints all states in the hashtable.
     */

    public static void printStates(State[] hashtable) {
        for (int i = 0; i < hashtable.length; i++) {
            if (hashtable[i] == null) StdOut.println("EMPTY: State Index " + i);
            else StdOut.println("State Index " + i);
            for (State ptr = hashtable[i]; ptr != null; ptr = ptr.getNext()) {
                StdOut.print(ptr.getName() + "->");
            }
            StdOut.println();
        }
    }

    public static void printEntireTable(State[] hashtable) {
        StdOut.println();
        int i = 0;
        for (State state : hashtable) {
        
            if (state == null) StdOut.println("State Index " + i + " " + "EMPTY");
            else StdOut.println("State Index " + i);
            for (State ptr = state; ptr != null; ptr = ptr.getNext()) {
                StdOut.println(ptr.getName() + ":");
                County[] counties = ptr.getCounties();
                int j = 0;
                for (County county : counties) {
                    if (county == null){
                        StdOut.println("  County Index " + j +  ": EMPTY");}
                    else
                        StdOut.println("  County Index " + j);
                    for (County ptr2 = county; ptr2 != null; ptr2 = ptr2.getNext()) {
                        StdOut.println( "   -> " + ptr2.getName() + ":");
                        List<Pollutant> pollutants = ptr2.getPollutants();
                        if (pollutants != null && !pollutants.isEmpty()) {
                            for (Pollutant pollutant : pollutants) {
                                StdOut.println("    " + pollutant);
                            }
                        } else {
                            StdOut.println("    No pollutants.");
                        }
                    }
                    j++;

                }
            }
            i++;
        }
    }


    /**
     * **DO NOT MODIFY THIS METHOD**
     * Prints the state's avg AQI, and highest and lowest AQI value counties in the state.
     */
    public static void printAQIs(State[] hashtable) {
        for (int i = 0; i < hashtable.length; i++) {
            if (hashtable[i] == null) StdOut.println("EMPTY: State Index " + i);
            else StdOut.println("State Index " + i);
                for (State ptr = hashtable[i]; ptr != null; ptr = ptr.getNext()) {
                DecimalFormat df = new DecimalFormat("0.00");
                StdOut.print(ptr.getName() + ": AvgAQI= " + df.format(ptr.getAvgAQI()) + ", HighestAQI County= " + ptr.getHighestAQI().getName() + ", LowestAQI County= " + ptr.getLowestAQI().getName());
                StdOut.println();
            }
            StdOut.println();
        }
    }

}
