package graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import javax.swing.*;

/*
 * CS112 Graph Lab
 * 
 * Implement addEdge(), removeEdge() using an linked adjacency list
 * based representation of an undirected graph
 * 
 * @author Colin Sullivan
 */
public class FlightPathGraph {

    // Adjacency list of cities. Each index corresponds to a City node which is the head of a linked list.
    // Each list is an edge list, meaning the first node has an edge to all the rest of the nodes in the list.
    public City[] flightPaths;

    // i.e. if flightPaths[0].city is "New York", and flightPaths[0].next.city is "London", it means New York
    // has a directed edge to London. If New York is also placed in Londons list, the edge is then undirected.

    /*
     * Constructor which initializes the adjacency list with the given verticies, with no edges
     *  
     * 1) Initiate the flightPaths array to the same size as the cities array
     * 2) Add a new City node to each index in flightPaths, with the corresponding
     *    city name from the same index in cities[]
     * 
     * @param cities String array of cities which are the unique vertices for the graph
     */
    public FlightPathGraph(String[] cities) {
        // WRITE YOUR CODE HERE
        flightPaths = new City[cities.length];
        for(int i = 0; i < cities.length; i++){
            flightPaths[i] = new City(cities[i]); 
        }
    }

    /*
     * Adds an directed edge from the departure location to the arrival location
     * i.e. departure -> arrival
     * 
     * Add a new City node containing the arrival city to the END of the departure citys edgelist
     * 
     * Note: Insert all edges as directed. Directed edges will appear as a red line in the driver.
     * Undirected edges will appear as black (you can test by inserting the same edge twice, once in reverse. i.e u -> v and then v -> u)
     * 
     * @param depature The origin city of the edge
     * @param arrival The destination city of the edge
     */
    public void addEdge(String departure, String arrival) {
        // WRITE YOUR CODE HERE
        if(departure.equals(arrival)){
            return;
        }

        City dc = null;
        for (City i : flightPaths) { // find it
            if (i.city.equals(departure)) {
                dc = i;
                break;
            }
        }
        
        City i = dc;
        while (i.next != null) {
            if (i.next.city.equals(arrival)) {
                return;
            }
            i = i.next;
        }
        i.next = new City(arrival);
    }

    /*
     * Removes the directed edge from the departure to the arrival cities
     * 
     * Remove the City node containing "arrival" from "departure"'s edge list
     * 
     * @param depature The origin city of the edge
     * @param arrival The destination city of the edge
     */
    public void removeEdge(String departure, String arrival) {
        // WRITE YOUR CODE HERE
        if(departure.equals(arrival)){
            return;
        }

        City dc = null;
        for (City i : flightPaths) { // find it
            if (i.city.equals(departure)) {
                dc = i;
                break;
            }
        }
        if (dc.next != null && dc.next.city.equals(arrival)) {
            dc.next = dc.next.next;
            return;
        }
        City i = dc;
        while (i.next != null) {
            if (i.next.city.equals(arrival)) {
                i.next = i.next.next;
                return;
            }
            i = i.next;
        }
    }

    /*
     * Getter method for number of vertices
     * 
     * @return number of riders in line (lineLength)
     */
    public int getNumberOfVertices() {
        // DO NOT MODIFY
        return flightPaths.length;
    }

    // Everything past here runs the driver, no need to modify anything

    private class FlightPathDisplay extends JPanel {
        private FlightPathDisplay() {
            super();
            this.setPreferredSize(new Dimension(550, 450));
        }

        final int circleRadius = 70;

        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            super.paintComponent(g2);
            int midpoint = (this.getWidth() / 2) - (circleRadius / 2);
            int quarterPoint = midpoint / 2;
            int midheight = (this.getHeight() / 2) - (circleRadius / 2);
            int quarterHeight = (midheight / 2);
            // Locations in the display for each vertice
            // Top, Bottom, Right, Left, BL, BR, TL, TR
            int[] xVals = { midpoint, midpoint, 20, this.getWidth() - 90, quarterPoint - 25, 3 * quarterPoint + 25,
                    quarterPoint - 25, 3 * quarterPoint + 25 };
            int[] yVals = { 5, this.getHeight() - 85, midheight, midheight, 3 * quarterHeight + 25,
                    3 * quarterHeight + 25, quarterHeight - 25, quarterHeight - 25 };
            // Offsets for the connecting lines for each vertice
            int[][] offsets = { { circleRadius / 2, circleRadius }, { circleRadius / 2, 0 },
                    { circleRadius, circleRadius / 2 }, { 0, circleRadius / 2 },
                    { circleRadius - 4, circleRadius / 4 }, { 4, circleRadius / 4 },
                    { circleRadius - 4, (3 * circleRadius) / 4 }, { 4, (3 * circleRadius) / 4 } };
            if (flightPaths == null) {
                return;
            }
            for (int i = 0; i < flightPaths.length && i < xVals.length; i++) {
                g2.setStroke(new BasicStroke(1));
                g2.setColor(new Color(135, 135, 133));
                g2.fillOval(xVals[i], yVals[i], circleRadius, circleRadius);
                g2.setColor(Color.BLACK);
                g2.drawOval(xVals[i], yVals[i], circleRadius, circleRadius);
                g.drawString(flightPaths[i].city, xVals[i] + 5 + (4 * (8 - flightPaths[i].city.length())),
                        yVals[i] + 40);
                for (City ptr = flightPaths[i].next; ptr != null; ptr = ptr.next) {
                    int target = indexOfCity(ptr.city);
                    int[] x = {xVals[i] + offsets[i][0], xVals[target] + offsets[target][0]};
                    int[] y = {yVals[i] + offsets[i][1], yVals[target] + offsets[target][1]};
                    if (isDirected(flightPaths[i].city, ptr.city)) {
                        g2.setColor(Color.RED);
                    } else {
                        g2.setColor(Color.BLACK);
                    }
                    g2.setStroke(new BasicStroke(2));
                    g2.drawPolyline(x, y, 2);
                }
            }
        }

        private int indexOfCity(String s) {
            for (int i = 0; i < cities.length; i++) {
                if (cities[i].equals(s)) {
                    return i;
                }
            }
            return -1;
        }

        private boolean isDirected(String c1, String c2) {
            boolean uv = false;
            boolean vu = false;

            boolean foundc1 = false;
            boolean foundc2 = false;
            for (City u : flightPaths) {
                if (u.city.equals(c1)) {
                    City v = u.next; 
                    while (v != null) {
                        if (v.city.equals(c2)) {
                            uv = true;
                            break;
                        }
                        v = v.next;
                    }
                    foundc1 = true;
                    if (foundc1 && foundc2) {
                        break;
                    }
                } else if (u.city.equals(c2)) {
                    City v = u.next; 
                    while (v != null) {
                        if (v.city.equals(c1)) {
                            vu = true;
                            break;
                        }
                        v = v.next;
                    }
                    foundc2 = true;
                    if (foundc1 && foundc2) {
                        break;
                    }
                }
            }
            return uv ^ vu;
        }
    }

    private FlightPathDisplay flightDisplay;
    private static String[] cities = { "New York", "Paris", "San Fran", "London", "Tokyo", "Sydney", "Dublin",
            "Berlin" };

    public void Driver() {

        JFrame display = new JFrame("Flight Paths Graph Lab");
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel window = new JPanel();
        window.setLayout(new BoxLayout(window, BoxLayout.PAGE_AXIS));

        flightDisplay = new FlightPathDisplay();

        JPanel controls = new JPanel();
        controls.setPreferredSize(new Dimension(550, 100));
        JComboBox<String> from = new JComboBox<>();
        JComboBox<String> to = new JComboBox<>();
        for (int i = 0; flightPaths != null && i < flightPaths.length; i++) {
            from.addItem(flightPaths[i].city);
            to.addItem(flightPaths[i].city);
        }
        JButton addEdge = new JButton("Add Edge");
        addEdge.addActionListener((ActionEvent e) -> {
            String f = ((String) from.getSelectedItem() != null) ? ((String) from.getSelectedItem()) : ("");
            String t = ((String) to.getSelectedItem() != null) ? ((String) to.getSelectedItem()) : ("");
            this.addEdge(f, t);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    flightDisplay.repaint();
                }
            });
        });
        JButton removeEdge = new JButton("Remove Edge");
        removeEdge.addActionListener((ActionEvent e) -> {
            String f = ((String) from.getSelectedItem() != null) ? ((String) from.getSelectedItem()) : ("");
            String t = ((String) to.getSelectedItem() != null) ? ((String) to.getSelectedItem()) : ("");
            this.removeEdge(f, t);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    flightDisplay.repaint();
                }
            });
        });
        controls.add(addEdge);
        controls.add(from);
        controls.add(to);
        controls.add(removeEdge);
        controls.setPreferredSize(new Dimension(320, 90));
        controls.setMaximumSize(new Dimension(450, 30));
        window.add(controls);
        window.add(flightDisplay);

        display.add(window);
        display.pack();
        display.setResizable(false);
        display.setVisible(true);
    }

    public static void main(String args[]) {
        FlightPathGraph undirected = new FlightPathGraph(cities);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                undirected.Driver();
            }
        });
    }

}
