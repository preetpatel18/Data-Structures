package trick;

import java.util.ArrayList;


public class FindTreatsRoute {
    public static void main(String[] args) {
        if (args.length < 3) {
            StdOut.println(
                    "Too few arguments. Did you put in command line arguments? If using the debugger, add args to launch.json.");
            StdOut.println(
                    "Run command: java -cp bin trick.FindTreatsRoute input1.in h1 findtreats1.out");
            return;
        }
        
        // WRITE YOUR CODE HERE
        String house = args[1];

        StdIn.setFile(args[0]);
        StdOut.setFile(args[2]);

        Graph V = new Graph(StdIn.readInt());
        V.add(args[0]);

        ArrayList<String> DFS = V.dfs(V, house);
        for(int i = 0; i < DFS.size(); i++) {
            StdOut.print(DFS.get(i)+" ");
        }
    }
}
