package quality;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

public class MapView extends JFrame {
    private List<CountyMarker> countyMarkers;
    private ImageIcon mapImage;

    // Constants for contiguous USA boundaries
    private static final double MIN_LAT = 24.396308;
    private static final double MAX_LAT = 49.384358; 
    private static final double MIN_LON = -124.848974; 
    private static final double MAX_LON = -66.885444; 

    public MapView() {
        countyMarkers = new ArrayList<>();

        mapImage = new ImageIcon("map.png");

        setTitle("Air Quality");
        setSize(1200, 800);

        add(new MapPanel());
    }

    public void plotCounties(State[] hashtable) {
        for (State state : hashtable) {
            for (State sPtr = state; sPtr != null; sPtr = sPtr.getNext()) {
                County[] counties = sPtr.getCounties();
                if (counties != null) {
                    for (County county : counties) {
                        for (County ptr = county; ptr != null; ptr = ptr.getNext()) {
                            int c = 0;
                            for (Pollutant p : ptr.getPollutants()) {
                                double longitude = county.getLongitude();
                                double latitude = county.getLatitude();
                                Color color = getColorFromString(p.getColor());
                                c += 0.0001;

                                countyMarkers.add(new CountyMarker(latitude + c, longitude + c, color));
                            }
                        }
                    }
                }
            }
        }
        repaint();
    }

    private Color getColorFromString(String colorName) {
        switch (colorName.toLowerCase()) {
            case "green": return Color.GREEN;
            case "yellow": return Color.YELLOW;
            case "orange": return Color.ORANGE;
            case "red": return Color.RED;
            case "purple": return new Color(128, 0, 128);
            case "maroon": return new Color(128, 0, 0);
            default: return Color.BLACK;
        }
    }

    private class MapPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (mapImage != null) {
                g2d.drawImage(mapImage.getImage(), 0, 0, getWidth(), getHeight(), null);
            }

            for (CountyMarker marker : countyMarkers) {
                Point p = convertToPoint(marker.latitude, marker.longitude);

                g2d.setColor(marker.color);
                Ellipse2D circle = new Ellipse2D.Double(p.x - 4, p.y - 4, 14, 14);
                g2d.fill(circle);
            }
        }
    }

    private Point convertToPoint(double lat, double lon) {
        int x = (int) ((lon - MIN_LON) / (MAX_LON - MIN_LON) * getWidth() * 0.985);
        int y = (int) ((MAX_LAT - lat) / (MAX_LAT - MIN_LAT) * getHeight() * 0.98);
    
        return new Point(x, y);
    }

    private static class CountyMarker {
        double latitude;
        double longitude;
        Color color;

        CountyMarker(double latitude, double longitude, Color color) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.color = color;
        }
    }

    public void start(State[] ht) {
        SwingUtilities.invokeLater(() -> {
            MapView map = new MapView();


            map.plotCounties(ht);
            map.setVisible(true);
        });
    }

    public static void main(String[] args) {

        StdOut.print("Enter input file => ");
        String inputFile = StdIn.readLine();
        AirQuality ru = new AirQuality();
        MapView mv = new MapView();      
        
        ru.buildTable(inputFile);
        mv.start(ru.getHashTable());
    }

}