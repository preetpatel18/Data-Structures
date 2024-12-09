package trick;
import java.util.*;

public class Graph {
    House[] adj;

    public Graph(int n){
        adj = new House[n];
    }
    public void add(String input){
        StdIn.setFile(input);
        
        // Add Vertices
        int n = Integer.parseInt(StdIn.readLine());
        for (int i = 0; i < n; i++) {
            String[] house = StdIn.readLine().split(" ");
            adj[i] = new House(house[0]);
            
            int CC = Integer.parseInt(house[1]);
            ArrayList<Candy> candies = new ArrayList<>();
            // Candies
            for (int j = 0, name = 2, count = 3; j < CC; j++, name += 2, count += 2) {
                String candyName = house[name];
                int CanCount = Integer.parseInt(house[count]);
                candies.add(new Candy(candyName, CanCount));
            }
            adj[i].setCandies(candies);
        }

        // Edges
        n = Integer.parseInt(StdIn.readLine());
        for (int i = 0; i < n; i++) {
            String[] temp = StdIn.readLine().split(" ");
            String h1 = temp[0];
            String h2 = temp[1];
            int weight = Integer.parseInt(temp[2]);
            addEdge(h1, h2, weight);
        }
    }

    public void addEdge(String h1, String h2, int weight){
        House h11 = null;
        House h22 = null;
        for (int i = 0; i < adj.length; i++) {
            if (adj[i].name.equals(h1)) {
                h11 = adj[i];
            }else if (adj[i].name.equals(h2)) {
                h22 = adj[i];
            }
        }

        h11.addEdge(new Edge(h22, weight)); // h1 -> h2
        h22.addEdge(new Edge(h11, weight)); // h2 -> h1
    }


    public void out(String outputFile) {
        StdOut.setFile(outputFile);
        for (House house : adj) {
           StdOut.print(house.name + " ");
            ArrayList<Candy> candies = house.getCandies();
            for (Candy c : candies) {
                StdOut.print(c.name + " " + c.getCount() + " ");
            }
            StdOut.println();
        }

        for (House house : adj) {
            StdOut.print(house.name + " ");
            ArrayList<Edge> edges = house.getEdges();
            for (Edge e : edges) {
                StdOut.print(e.d.name + " " + e.weight + " ");
            }
            StdOut.println();
        }
    }

    public String findHouseWithMostCandy(Graph graph, ArrayList<String> dfs, String targetCandy) {
        String result = "";
        int max = -1;

        for (String i : dfs) {
            House house = graph.getHouse(i);
            if (house == null){
                continue;
            }
            int count = house.getCandyCount(targetCandy);
            if (count > max) {
                max = count;
                result = i;
            }
        }
        return result;
    }
    public ArrayList<String> dfs(Graph graph, String root) {
        ArrayList<String> res = new ArrayList<>();
        ArrayList<String> visited = new ArrayList<>();
        Stack<String> stack = new Stack<>();

        stack.push(root);

        while (!stack.isEmpty()) {
            String c = stack.pop();
            if (visited.contains(c)){ 
                continue;
            }
            visited.add(c);
            res.add(c);

            ArrayList<Edge> edge = graph.getEdges(c);
            Collections.reverse(edge);
            for (Edge e : edge) {
                if (!visited.contains(e.d.name)) {
                    stack.push(e.d.name);
                }
            }
        }

        return res;
    }

    public ArrayList<String> bfs(Graph graph, String house, String candy) {
        ArrayList<String> dfsOrder = graph.dfs(graph, house);
        String targetHouse = graph.findHouseWithMostCandy(graph, dfsOrder, candy);
    
        Queue<String> q = new LinkedList<>();
        ArrayList<String> vis = new ArrayList<>();
        ArrayList<String> edgeTo = new ArrayList<>();
    
        for (House h : graph.adj) {
            edgeTo.add(null);
        }
    
        q.add(house);
        vis.add(house);
    
        while (!q.isEmpty()) {
            String c = q.poll();

            if (c.equals(targetHouse)) {
                break;
            }

            ArrayList<Edge> edge = graph.getEdges(c);
            for (Edge e : edge) {
                String neigh = e.d.name;
    
                if (!vis.contains(neigh)) {
                    q.add(neigh);
                    vis.add(neigh);
                    edgeTo.set(getIndexOfHouse(graph, neigh), c);
                }
            }
        }
        ArrayList<String> path = new ArrayList<>();
        String v = targetHouse;
        while (v != null) {
            path.add(v);
            v = edgeTo.get(getIndexOfHouse(graph, v));
        }
        Collections.reverse(path); 
        return path;
    }

    public int getIndexOfHouse(Graph V, String house) {
        for (int i = 0; i < V.adj.length; i++) {
            if (V.adj[i].name.equals(house)) {
                return i;
            }
        }
        return -1; 
    }

    public ArrayList<String> ShortestPath(Graph graph, String house){
        ArrayList<String> done = new ArrayList<>();
        ArrayList<String> fringe = new ArrayList<>();
        ArrayList<Integer> distances = new ArrayList<>();
        ArrayList<String> predecessors = new ArrayList<>();
        ArrayList<String> res = new ArrayList<>();

        // Part 1
        for (int i = 0; i < graph.adj.length; i++) {
            if(graph.adj[i].name.equals(house)){
                distances.add(0);
                predecessors.add(null);
                fringe.add(graph.adj[i].name);
                continue;
            }
            distances.add(Integer.MAX_VALUE);
            predecessors.add(null);
            fringe.add(graph.adj[i].name);
        }

        int ind = findHouseIndex(graph, house);
        distances.set(ind, 0);

        // Part 2
        while (!fringe.isEmpty()) {
            // Min Distance
            int min = findMinDistanceIndex(fringe, graph, distances);
            if (min == -1) {
                break;
            }

            String m = fringe.get(min);
            fringe.remove(min);
            done.add(m);

            // Part 3
            ArrayList<Edge> n = graph.getEdges(m);
            for (Edge e : n) {
                String neig = e.d.name;
                if (done.contains(neig)) {
                    continue;
                }

                int curI = findHouseIndex(graph, m);
                int neigI = findHouseIndex(graph, neig);
                if (curI == -1 || neigI == -1) {
                    continue;
                }
                int dis = distances.get(curI) + e.weight;
                if (dis < distances.get(neigI)) {
                    distances.set(neigI, dis);
                    predecessors.set(neigI, m);
                }
            }
        }
        for (int i = 0; i < graph.adj.length; i++) {
            if (distances.get(i) < Integer.MAX_VALUE) { 
                res.add(graph.adj[i].name + " " + (predecessors.get(i) == null ? "null" : predecessors.get(i)));
            }
        }
        return res;
    }

    public int findHouseIndex(Graph graph, String houseName) {
        for (int i = 0; i < graph.adj.length; i++) {
            if (graph.adj[i].name.equals(houseName)) {
                return i;
            }
        }
        return -1;
    }

    public int findMinDistanceIndex(ArrayList<String> fringe, Graph graph, ArrayList<Integer> distances) {
        int minIndex = -1;
        int minDistance = Integer.MAX_VALUE;

        for (int i = 0; i < fringe.size(); i++) {
            int houseIndex = findHouseIndex(graph, fringe.get(i));
            if (houseIndex != -1 && distances.get(houseIndex) < minDistance) {
                minDistance = distances.get(houseIndex);
                minIndex = i;
            }
        }
        return minIndex;
    }
    
    public String curfew(Graph V, String house1, String house2, int w) {
        ArrayList<String> s = ShortestPath(V, house1);
        for (String i: s) {
            System.out.println(i);
        }
        Map<String, String> path = new HashMap<>();
        for(String i: s){ 
            String hou1 = i.substring(0,i.indexOf(" "));
            String hou2 = i.substring(i.indexOf(" ")+1, i.length());
            path.put(hou1, hou2);
        }

        int total = 0;
        String h1 = house2;
        String h2 = path.get(h1);
        System.out.println(h1);
        while(h2!= null && !h2.equals(house1)){
            for (Edge edge : getHouse(h1).getEdges()) {
                if (edge.d.name.equals(h2)) {
                    total += edge.weight;
                    break;
                }
            }
            h1 = h2;
            h2 = path.get(h1);
        }
        if(h2!=null){
            for (Edge edge : getHouse(h2).getEdges()) {
                if (edge.d.name.equals(h1)) {
                    total += edge.weight;
                    break;
                }
            }
        }
        if(total > w){
            return false + " " + total;
        }else{
            return true + " " + total;
        }
    }

    public ArrayList<Edge> getEdges(String name) {
        for (House house : adj) {
            if (house.name.equals(name)) {
                return house.getEdges();
            }
        }
        return new ArrayList<>();
    }

    public House getHouse(String name){
        for(House house: adj){
            if(house.name.equals(name)){
                return house;
            }
        }
        return null;
    }
}
