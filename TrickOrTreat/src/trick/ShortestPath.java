package trick;

import java.util.ArrayList;

public class ShortestPath {
    public static void main(String[] args) {
        if (args.length < 3) {
            StdOut.println(
                    "Too few arguments. Did you put in command line arguments? If using the debugger, add args to launch.json.");
            StdOut.println(
                    "Execute: java -cp bin trick.ShortestPath input1.in h1 shortestpaths1.out");
            return;
        }

        // WRITE YOUR CODE HERE
        String house = args[1];

        StdIn.setFile(args[0]);
        StdOut.setFile(args[2]);

        Graph graph = new Graph(StdIn.readInt());
        graph.add(args[0]);

        ArrayList<String> s = graph.ShortestPath(graph, house);

        // Write output
        for (String i: s) {
            StdOut.println(i);
        }
    }
}
