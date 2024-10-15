package election;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class implements a map visualizer for state voting data and party
 * results
 * 
 * This is a JPanel class which will display a given map png "US_Outline.png"
 * and
 * use a set of predetermined coordinates and information about read election
 * data
 * from the driver to floodfill the map, and display color information about
 * party results
 * and voter turnout
 * 
 * @author Colin Sullivan
 * @author Sumedh Sinha
 */
public class MapVisualizer extends JPanel {

    // BufferedImage for the US Map
    private BufferedImage map;
    // Weighted Quick Union of map
    private WQU union;
    // Hash map of each state coordinate
    private HashMap<String, Coordinate> states;
    // HashMap of each state abbreviation to the color it should diplay
    private HashMap<String, Color> colors;
    // True if the map is displaying total or change in votes, false if displaying
    // senate/house
    private boolean tot;

    /* Colors for Map Display. */
    private static final Color DEM = new Color(0, 85, 164); // blue
    private static final Color REP = new Color(232, 27, 35); // red
    private static final Color GREEN = Color.green;

    /* US_Outline.jpg dimensions */
    private static final int DEFAULT_WIDTH = 500;
    private static final int DEFAULT_HEIGHT = 407;

    private class Coordinate {
        public int x;
        public int y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean equals(Coordinate comp) {
            return (comp.x == this.x && comp.y == this.y);
        }
    }

    /*
     * Private Weighted Quick Union Class
     * 
     * Used to "label" the pixels on the state map, both to reduce state painting
     * logic and to
     * add the ability to identify the state at clicked coordinates on map
     */
    private class WQU {
        private Coordinate[][] parent;
        private int[][] size;

        public WQU(int height, int width) {
            this.parent = new Coordinate[height][width];
            this.size = new int[height][width];
            for (int y = 0; y < parent.length; y++) {
                for (int x = 0; x < parent[y].length; x++) {
                    parent[y][x] = new Coordinate(x, y);
                    size[y][x] = 1;
                }
            }
        }

        public void union(Coordinate n1, Coordinate n2) {
            Coordinate r2 = find(n1);
            Coordinate r1 = find(n2);
            if (r1.equals(r2)) {
                return;
            } else {
                if (size[r2.y][r2.x] < size[r1.y][r1.x]) {
                    Coordinate swap = r1;
                    r1 = r2;
                    r2 = swap;
                }
                parent[r1.y][r1.x] = r2;
                size[r2.y][r2.x] += size[r1.y][r1.x];
            }
        }

        public Coordinate find(Coordinate search) {
            if (search.x < 0 || search.y < 0) {
                if (search.x >= parent.length || search.y >= parent[0].length) {
                    return null;
                }
            }
            if (parent[search.y][search.x].equals(search)) {
                return search;
            } else {
                parent[search.y][search.x] = find(parent[search.y][search.x]);
            }
            return parent[search.y][search.x];
        }
    }

    public MapVisualizer() {
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));

        // Set Map Background
        try {
            map = ImageIO.read(new File("US_Outline.jpg"));
        } catch (Exception io) {
            System.out.println(io.getMessage());
            System.out.println(
                    "Eror Reading US_Outline.jpg image. Make sure you have not modified the project structure, and you have opened the innermost ElectionAnalysis folder.");
        }

        setupStateMap();

        // Create hashmap which will store the State-Color pairs
        colors = new HashMap<String, Color>();
    }

    /*
     * Sets up states hashmap with state name - source coordinate pairs
     * 
     * Runs floodfill and unions each pixel of each state to its corresponding
     * source
     * This is used to quickly find() and identify which state a pixel belongs to
     */
    private void setupStateMap() {
        // Setup state hashmap for source coordinates
        states = new HashMap<>();

        // Each index in coords[] cooresponds to the state with the same index in
        // names[]
        Integer[][] coords = { { 330, 211 }, { 50, 330 }, { 105, 200 }, { 282, 190 }, { 174, 146 }, { 45, 150 },
                { 423, 150 }, { 445, 115 }, { 380, 270 }, { 357, 200 }, { 454, 375 }, { 85, 70 }, { 295, 130 },
                { 336, 129 }, { 255, 105 }, { 215, 150 }, { 350, 155 }, { 280, 229 }, { 478, 71 }, { 411, 141 },
                { 454, 108 }, { 346, 98 }, { 262, 70 }, { 305, 215 }, { 280, 156 }, { 115, 60 }, { 221, 118 },
                { 80, 143 }, { 456, 95 }, { 431, 137 }, { 165, 180 }, { 430, 100 }, { 380, 175 }, { 210, 40 },
                { 362, 134 }, { 240, 183 }, { 50, 100 }, { 410, 120 }, { 460, 117 }, { 382, 200 }, { 214, 83 },
                { 327, 179 }, { 225, 250 }, { 110, 135 }, { 446, 86 }, { 402, 162 }, { 49, 39 }, { 380, 149 },
                { 302, 80 }, { 145, 100 } };
        String[] names = { "AL", "AK", "AZ", "AR", "CO", "CA", "DE", "CT", "FL", "GA", "HI", "ID", "IL", "IN", "IA",
                "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY",
                "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI",
                "WY" };
        for (int i = 0; i < names.length; i++) {
            states.put(names[i], new Coordinate(coords[i][0], coords[i][1]));
        }

        union = new WQU(MapVisualizer.DEFAULT_HEIGHT, MapVisualizer.DEFAULT_WIDTH);

        // For each state
        for (HashMap.Entry<String, Coordinate> e : states.entrySet()) {
            String state = e.getKey();

            // Start the source arraylist at the one state we want to apply
            ArrayList<Coordinate> source = new ArrayList<Coordinate>();
            source.add(e.getValue());

            // Add sources for each island of HI
            if (state.equals("HI")) {
                Coordinate maui = new Coordinate(431, 340);
                source.add(maui);
                Coordinate oahu = new Coordinate(386, 319);
                source.add(oahu);
                Coordinate kauai = new Coordinate(343, 301);
                source.add(kauai);
                Coordinate oah2 = new Coordinate(414, 329);
                source.add(oah2);
                Coordinate oah3 = new Coordinate(416, 339);
                source.add(oah3);
            }

            // Flood fill to union each states pixels to its source. Given a single source
            // coordinate on the
            // map, will paint all contiguous white pixels to the party color

            // Keep track of visited pixels so we don't repeat
            boolean[][] visited = new boolean[map.getHeight()][map.getWidth()];
            // While we have more white pixels to paint
            while (!source.isEmpty()) {
                // Grab current coords
                Coordinate src = source.remove(0);

                // For all four cardinal directions, check if the pixel is white and in bounds
                // If it is and we haven't painted it, add it to the list, paint it, and mark
                // it.

                if (src.x - 1 > 0) {
                    Color temp = new Color(map.getRGB(src.x - 1, src.y));
                    if (isWhite(temp) && !visited[src.y][src.x - 1]) {
                        Coordinate add = new Coordinate(src.x - 1, src.y);
                        visited[src.y][src.x - 1] = true;
                        source.add(add);
                        union.union(new Coordinate(e.getValue().x, e.getValue().y), new Coordinate(src.x - 1, src.y));
                    }
                }
                if (src.x + 1 < map.getWidth()) {
                    Color temp = new Color(map.getRGB(src.x + 1, src.y));
                    if (isWhite(temp) && !visited[src.y][src.x + 1]) {
                        Coordinate add = new Coordinate(src.x + 1, src.y);
                        visited[src.y][src.x + 1] = true;
                        source.add(add);
                        union.union(new Coordinate(e.getValue().x, e.getValue().y), new Coordinate(src.x + 1, src.y));
                    }
                }
                if (src.y - 1 > 0) {
                    Color temp = new Color(map.getRGB(src.x, src.y - 1));
                    if (isWhite(temp) && !visited[src.y - 1][src.x]) {
                        Coordinate add = new Coordinate(src.x, src.y - 1);
                        visited[src.y - 1][src.x] = true;
                        source.add(add);
                        union.union(new Coordinate(e.getValue().x, e.getValue().y), new Coordinate(src.x, src.y - 1));
                    }
                }
                if (src.y + 1 < map.getHeight()) {
                    Color temp = new Color(map.getRGB(src.x, src.y + 1));
                    if (isWhite(temp) && !visited[src.y + 1][src.x]) {
                        Coordinate add = new Coordinate(src.x, src.y + 1);
                        visited[src.y + 1][src.x] = true;
                        source.add(add);
                        union.union(new Coordinate(e.getValue().x, e.getValue().y), new Coordinate(src.x, src.y + 1));
                    }
                }
            }
        }
    }

    /*
     * @return true if the given color is white
     */
    private boolean isWhite(Color c) {
        return (c.getRed() > 210 && c.getBlue() > 210 && c.getGreen() > 210);
    }

    /*
     * @return String state name found at (x, y). Null if no state.
     */
    public String state(int x, int y) {
        Coordinate root = union.find(new Coordinate(x, y));
        for (HashMap.Entry<String, Coordinate> pair : states.entrySet()) {
            if (pair.getValue().equals(root)) {
                return pair.getKey();
            }
        }
        return null;
    }

    /*
     * Reads a HashMap where the key is a State abbreviation, and the value
     * is Party abbeviation with a majority in each state (for house and/or senate)
     * 
     * This wil set the color map for the display so that
     * each state displays the color correlated to the majority party (Dem/Rep)
     * 
     * @param parties HashMap with keys being string state abbrev. and Value being
     * string party abbrev.
     */
    public void setColor(HashMap<String, String> parties) {
        tot = false;
        colors.clear();
        for (String s : parties.keySet()) {
            String party = parties.get(s);
            if (party.equals("DEM")) {
                colors.put(s, DEM);
            } else if (party.equals("REP")) {
                colors.put(s, REP);
            }
        }
    }

    /*
     * Reads a HashMap where the key is a State abbreviation, and the value is
     * either the
     * total votes for that state, or the change in votes between two years in that
     * state
     * 
     * This wil set the color map for the display so that each state displays:
     * -A color from grey to green if displaying total votes (scaled based on
     * highest increase in votes for country)
     * -A color from red to green if displaying change in votes (red for decrease,
     * green for increase)
     * 
     * @param votes HashMap with keys being string state abbrev. and Value being a
     * number of votes
     * 
     * @param total Boolean true if the hashmap contains total votes for a state in
     * a year, false if displaying change between 2 years
     */
    public void setColor(HashMap<String, Integer> votes, boolean total) {
        tot = true;
        colors.clear();
        int high = 0;
        int low = 0;
        for (String s : votes.keySet()) {
            int tot = (int) votes.get(s);
            if (tot > high) {
                high = tot;
            }
            if (tot < low) {
                low = tot;
            }
        }
        if (total) {
            for (String s : votes.keySet()) {
                int tot = (int) votes.get(s);
                double percent = (double) tot / (double) high;
                if (percent > 0.85) {
                    percent = 0.85;
                }
                int r = (int) Math.round((GREEN.getRed() * percent + 0.15) + (Color.white.getRed() * (0.85 - percent)));
                int g = (int) Math
                        .round((GREEN.getGreen() * percent + 0.15) + (Color.white.getGreen() * (0.85 - percent)));
                int b = (int) Math
                        .round((GREEN.getBlue() * percent + 0.15) + (Color.white.getBlue() * (0.85 - percent)));
                colors.put(s, new Color(r, g, b));
            }
        } else {
            for (String s : votes.keySet()) {
                int chan = (int) votes.get(s);
                if (chan < 0) {
                    double percent = (double) chan / (double) low;
                    if (percent > 0.85 || percent < -0.85) {
                        percent = 0.85;
                    }
                    int r = (int) Math
                            .round((REP.getRed() * (percent + 0.15)) + (Color.white.getRed() * (0.85 - percent)));
                    int g = (int) Math
                            .round((REP.getGreen() * (percent + 0.15)) + (Color.white.getGreen() * (0.85 - percent)));
                    int b = (int) Math
                            .round((REP.getBlue() * (percent + 0.15)) + (Color.white.getBlue() * (0.85 - percent)));
                    colors.put(s, new Color(r, g, b));
                } else if (chan > 0) {
                    double percent = (double) chan / (double) high;
                    if (percent > 0.85) {
                        percent = 0.85;
                    }
                    int r = (int) Math
                            .round((GREEN.getRed() * percent + 0.15) + (Color.white.getRed() * (0.85 - percent)));
                    int g = (int) Math
                            .round((GREEN.getGreen() * percent + 0.15) + (Color.white.getGreen() * (0.85 - percent)));
                    int b = (int) Math
                            .round((GREEN.getBlue() * percent + 0.15) + (Color.white.getBlue() * (0.85 - percent)));
                    colors.put(s, new Color(r, g, b));
                }
            }
        }
    }

    /*
     * Overridden paint method for the JComponent class. Invoked by Swing to draw
     * components.
     * 
     * For each display refresh:
     * -Draws the map background and key's for correct display mode (vote / party)
     * -
     */
    @Override
    public void paint(Graphics g) {

        super.paint(g);
        g.drawImage(map, 0, 0, this);
        g.setPaintMode();

        // Draw the REP / DEM Color keys
        if (!tot) {
            // Draw Color Key
            g.setColor(DEM);
            g.fillRect(212, 326, 15, 15);
            g.setColor(Color.black);
            g.drawRect(212, 326, 15, 15);
            g.drawString("Democrat Gain", 230, 337);

            g.setColor(REP);
            g.fillRect(212, 346, 15, 15);
            g.setColor(Color.black);
            g.drawRect(212, 346, 15, 15);
            g.drawString("Republican Gain", 230, 357);
        }
        // Draw the Increase / Decrease in votes color keys
        else {
            // Draw Color Key
            g.setColor(GREEN);
            g.fillRect(212, 326, 15, 15);
            g.setColor(Color.black);
            g.drawRect(212, 326, 15, 15);
            g.drawString("Voter Turnout Increase", 230, 337);

            g.setColor(REP);
            g.fillRect(212, 346, 15, 15);
            g.setColor(Color.black);
            g.drawRect(212, 346, 15, 15);
            g.drawString("Voter Turnout Decrease", 230, 357);
        }

        // For the whole canvas (minus some border)
        for (int y = 10; y < map.getHeight() - 10; y++) {
            for (int x = 10; x < map.getWidth() - 10; x++) {
                Coordinate state = new Coordinate(x, y);
                // If the current pixel is white and should be painted
                if (isWhite(new Color(map.getRGB(x, y))) && colors.get(this.state(state.x, state.y)) != null) {
                    g.setColor(colors.get(this.state(state.x, state.y)));
                    g.drawOval(x, y, 1, 1);
                }
            }
        }
    }
}