package trick;

public class CanAvoidCurfew {
    public static void main(String[] args) {
        if (args.length < 5) {
            StdOut.println(
                    "Too few arguments. Did you put in command line arguments? If using the debugger, add args to launch.json.");
            StdOut.println(
                    "Execute: java -cp bin trick.CanAvoidCurfew input1.in h1 h8 100 shortestpaths1.out");
            return;
        }

        // WRITE YOUR CODE HERE
        String house1 = args[1];
        String house2 = args[2];
        int num = Integer.parseInt(args[3]);

        StdIn.setFile(args[0]);
        StdOut.setFile(args[4]);

        Graph V = new Graph(StdIn.readInt());
        V.add(args[0]);
        StdOut.print(V.curfew(V, house1, house2, num));
    }
}
