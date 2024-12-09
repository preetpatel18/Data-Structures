package trick;

import java.util.ArrayList;

public class House {
    String name;
    ArrayList<Edge> edges;
    ArrayList<Candy> candies;
    
    public House(String name) {
        this.name = name;
        this.edges = new ArrayList<>();
        this.candies = new ArrayList<>();
    }

    public void setCandies(ArrayList<Candy> candies) {
        this.candies = candies;
    }

    public ArrayList<Candy> getCandies() {
        return candies;
    }

    public int getCandyCount(String name){
        for(Candy i: candies){
            if(i.getName().equals(name)){
                return i.getCount();
            }
        }
        return 0;
    }

    public void addEdge(Edge edge) {
        this.edges.add(edge);
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }
}
