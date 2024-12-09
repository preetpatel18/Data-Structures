package trick;

import java.util.ArrayList;

public class FindHouseWithMostCandy {
    public static void main(String[] args) {
        if (args.length < 4) {
            StdOut.println(
                    "Too few arguments. Did you put in command line arguments? If using the debugger, add args to launch.json.");
            StdOut.println(
                    "Execute: java -cp bin trick.FindHouseWithMostCandy input1.in h1 Skittles findcandy1.out");
            return;
        }
        // WRITE YOUR CODE HERE
        String inputFile = args[0];
        String house = args[1];
        String candy = args[2];
        String outputFile = args[3]; 

        StdIn.setFile(inputFile);
        StdOut.setFile(outputFile);

        Graph V = new Graph(StdIn.readInt());
        V.add(inputFile);

        ArrayList<String> DFS = V.dfs(V, house);
        StdOut.print(V.findHouseWithMostCandy(V, DFS, candy));
        System.out.println(V.findHouseWithMostCandy(V, DFS, candy));
    }
}
