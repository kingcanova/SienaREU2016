import java.util.*;
/**
 * Suggestion objects contain all information on a certain attraction merged
 * from the three APIs
 * 
 * @author Aidan, Tom, Kevin, Zach
 * @version Final
 */
public class Suggestion implements Comparable<Suggestion>
{
    protected String url, phone, address,id, placeId, vicinity;
    protected String title;
    protected double rate, latitude, longitude;
    protected ArrayList<String> category;
    protected int score = 0;

    //constructors
    public Suggestion()
    {
        title = "";
        rate = 0;
        latitude = 0.0;
        longitude = 0.0;
        category = new ArrayList<String>();
    }

    public Suggestion(String a, double b, double c, double d, ArrayList<String> e) //merged suggestion
    {
        title = a;
        rate = b;
        latitude = c;
        longitude = d;
        category = e;
    }

    public Suggestion(String nameIn, String ratingIn, String typesIn, String vicinityIn, 
    String idIn, String placeIdIn, String latIn, String lngIn) //GooglePlacesAPI
    {
        latitude = Double.parseDouble(latIn);
        longitude = Double.parseDouble(lngIn);
        idIn = idIn.replaceAll("\"", "");
        id = idIn;
        nameIn = nameIn.replaceAll("\"", "");
        title = nameIn;
        placeIdIn = placeIdIn.replaceAll("\"", "");
        placeId = placeIdIn;
        rate = Double.parseDouble(ratingIn);
        typesIn = typesIn.replaceAll("\\[", "");          
        typesIn = typesIn.replaceAll("\\]", "");
        typesIn = typesIn.replaceAll("\"", "");
        typesIn = typesIn.replaceAll(" ", "");
        typesIn = typesIn.toLowerCase();
        category = new ArrayList<String>(Arrays.asList(typesIn.split(",")));
        vicinityIn = vicinityIn.replaceAll("\"", "");
        address= vicinityIn;
        url="";
        phone="";
    }

    public Suggestion(String nameIn, String latIn, String lngIn, String idIn,
    String contact, String ratingIn, String[] types) // Foursquare
    {
        title = nameIn;
        latitude = Double.parseDouble(latIn);
        longitude = Double.parseDouble(lngIn);
        id = idIn;
        phone = contact;
        category = new ArrayList<String>(Arrays.asList(types));
        url = "";
        address = "";
        //foursquare ratings are rated 1-10, the rest of our APIs are rated 1-5
        if (!ratingIn.equals(""))
        {
            double tempRating = Double.parseDouble(ratingIn);
            tempRating = tempRating/2.0;
            rate = tempRating;
        }
        else
        {
            rate = 0;
        }
    }

    public Suggestion(String nameIn, String ratingIn, String latIn,
    String lngIn, String categoriesIn) //Yellow Pages
    {
        title = nameIn;
        latitude = Double.parseDouble(latIn);
        longitude = Double.parseDouble(lngIn);
        categoriesIn = categoriesIn.replaceAll(" ", "");
        categoriesIn = categoriesIn.toLowerCase();
        category = new ArrayList<String>(Arrays.asList(categoriesIn.split("\\|")));
        rate = Double.parseDouble(ratingIn);
        url = "";
        address = "";
        phone = "";
        id = ""; 

    }

    public void print() //for merged suggestion
    {
        System.out.println(title.replaceAll("\"", ""));
        //System.out.println("Rating: " + rate);
        //System.out.println("Latitude: " + latitude);
        //System.out.println("Longitude: " + longitude);
        //System.out.println("Categories: ");
        for(String cat : category)
        {
            System.out.println(cat);
        }
    }

    public int compareTo(Suggestion other)
    {
        if(this.score < other.score)
        {
            return 1;
        }
        else if (this.score > other.score)
        {
            return -1;
        }
        return 0;
    }
}
