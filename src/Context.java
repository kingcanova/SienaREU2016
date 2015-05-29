import java.util.*;

/**
 * Context also known as a City has a name and id num.
 * For each city there is a list of POI's 
 * 
 * @author Siena - Aidan
 * @version May
 */
public class Context
{
    //protected ArrayList<POI> attractions = new ArrayList<POI>();
    protected String name;
    protected String state;
    protected int id_num;
    protected double latitude;
    protected double longitude;
    
    public Context(int id, String city, String state, double lati, double longi)
    {
        this.name = city;
        this.state = state;
        this.id_num = id;
        this.latitude = lati;
        this.longitude = longi;
    }
    
    public void print()
    {
        System.out.println(name + "\t" + state);
    }
}
