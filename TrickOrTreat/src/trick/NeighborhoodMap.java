package trick;


public class NeighborhoodMap {
    public static void main(String[] args) {
        if (args.length < 2) {
            StdOut.println(
                    "Too few arguments. Did you put in command line arguments? If using the debugger, add args to launch.json.");
            StdOut.println(
                    "Execute: java -cp bin trick.NeighborhoodMap <neighborhoodmap INput file> <neighborhoodmap OUTput file>");
            return;
        }

        // WRITE YOUR CODE HERE
        StdIn.setFile(args[0]);
        Graph V = new Graph(StdIn.readInt());
        V.add(args[0]);
        V.out(args[1]);
    }
}
