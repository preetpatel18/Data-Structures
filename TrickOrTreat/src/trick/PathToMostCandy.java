package trick;

import java.util.ArrayList;

public class PathToMostCandy {
    public static void main(String[] args) {
        if (args.length < 4) {
            StdOut.println(
                    "Too few arguments. Did you put in command line arguments? If using the debugger, add args to launch.json.");
            StdOut.println(
                    "execute: java -cp bin trick.PathToMostCandy input1.in h1 KitKat mostcandy1.out");
            return;
        }

        // WRITE YOUR CODE HERE
        String house = args[1];
        String candy = args[2];

        StdIn.setFile(args[0]);
        StdOut.setFile(args[3]);

        Graph V = new Graph(StdIn.readInt());
        V.add(args[0]);

        ArrayList<String> s = V.bfs(V, house, candy);
        for(String i: s){
            StdOut.print(i+" ");
        }

    }
}
