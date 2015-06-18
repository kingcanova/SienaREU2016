import java.util.*;
/**
 * Write a description of class Suggestion here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Suggestion
{
    protected String rating, name, url, categories[], phone, address, lat, lng, id,
        placeId, vicinity;
    protected String title, c;
    protected double rate, latitude, longitude;
    protected ArrayList<String> category;

    //constructors
    public Suggestion()
    {
        name = "";
        rating = "";
        lat = "0.0";
        lng = "0.0";
        categories = new String[0];
    }

    public Suggestion(String a, double b, double c, double d, ArrayList<String> e) //merged suggestion
    {
        title = a;
        rate = b;
        latitude = c;
        longitude = d;
        category = e;
    }

    public void printFinal()
    {
        System.out.println("Name: " + title);
        System.out.println("Rating: " + rate);
        System.out.println("Latitude: " + latitude);
        System.out.println("Longitude: " + longitude);
        System.out.println("Categories: ");
        for(String cat : category)
        {
            System.out.println("\t" + cat);
        }
    }

    public Suggestion(String nameIn, String ratingIn, String typesIn, String vicinityIn, 
                      String idIn, String placeIdIn, String latIn, String lngIn) //GooglePlacesAPI
    {
        lat = latIn;
        lng = lngIn;
        idIn = idIn.replaceAll("\"", "");
        id = idIn;
        nameIn = nameIn.replaceAll("\"", "");
        name = nameIn;
        placeIdIn = placeIdIn.replaceAll("\"", "");
        placeId = placeIdIn;
        rating = ratingIn;
        typesIn = typesIn.replaceAll("\\[", "");          
        typesIn = typesIn.replaceAll("\\]", "");
        typesIn = typesIn.replaceAll("\"", "");
        typesIn = typesIn.replaceAll(" ", "");
        typesIn = typesIn.toLowerCase();
        categories = typesIn.split(",");
        vicinityIn = vicinityIn.replaceAll("\"", "");
        address= vicinityIn;
        url="N/A";
        phone="N/A";
    }

    public Suggestion(String nameIn, String latIn, String lngIn, String idIn,
                      String contact, String rate, String[] types) // Foursquare
    {
        name = nameIn;
        lat = latIn;
        lng = lngIn;
        id = idIn;
        phone = contact;
        categories = types;
        url = "";
        address = "";
        //foursquare ratings are rated 1-10, the rest of our APIs are rated 1-5
        double tempRating = Double.parseDouble(rating);
        tempRating = tempRating/2.0;
        rating = Double.toString(tempRating);
    }

    public Suggestion(String nameIn, String ratingIn, String latIn,
                      String lngIn, String categoriesIn) //Yellow Pages
    {
        name = nameIn;
        lat = latIn;
        lng = lngIn;
        categoriesIn = categoriesIn.replaceAll(" ", "");
        categoriesIn = categoriesIn.toLowerCase();
        categories = categoriesIn.split("\\|");
        rating = ratingIn;
        url = "";
        address = "";
        phone = "";
        id = ""; 

    }

    public void print()//for individual API
    {
        System.out.println("Name: " + name);
        System.out.println("Rating: " + rating);
        System.out.println("Latitude: " + lat);
        System.out.println("Longitude: " + lng);
        System.out.println("Categories: ");
        for(String s : categories)
        {
            System.out.print(s + "\n");
        }
    }

    public void printFinal() //for merged suggestion
    {
        System.out.println("Name: " + title);
        System.out.println("Rating: " + rate);
        System.out.println("Latitude: " + latitude);
        System.out.println("Longitude: " + longitude);
        System.out.println("Categories: ");
        for(String cat : category)
        {
            System.out.println("\t" + cat);
        }
    }
}
