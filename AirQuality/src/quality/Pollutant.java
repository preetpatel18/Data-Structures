package quality;

/*
 * Defines a Pollutant. A pollutant includes PM2.5 and PM10, ground level ozone,
 * carbon monoxide, sulfur dioxide, nitrogen dioxide. 
 */
public class Pollutant {
    
    private String name;   // pollutant's name
    private int AQI;       // air quality index associated with Pollutant
    private String color;  // hazard label associated with this pollutant and AQI

    public Pollutant (String name, int AQI, String color) {
        this.name = name;
        this.AQI = AQI;
        this.color = color;
    }

    public String getName() { return name;}

    public void setAQI (int value) { this.AQI = value; }
    public int getAQI () { return AQI; }
    public void setColor (String value) { this.color = value;}
    public String getColor () { return color; }
    @Override
    public String toString() { return String.format("{%s, %d, %s}", name, AQI, color);}
 
}
