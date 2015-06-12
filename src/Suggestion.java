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

    protected String title;
    protected double rate;
    protected double latitude;
    protected double longitude;
    protected ArrayList<String> category;
    
    public Suggestion(String a, double b, double c, double d, ArrayList<String> e)
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
    
    public Suggestion(String ratingIn, String nameIn, String urlIn, String categoriesIn, 
    String phoneIn, String addressIn)//YelpAPI
    {
        rating = ratingIn;
        nameIn = nameIn.replaceAll("\"", "");
        name = nameIn;
        urlIn = urlIn.replaceAll("\"", "");
        url = urlIn;
        categoriesIn = categoriesIn.replaceAll("\\[", "");          
        categoriesIn = categoriesIn.replaceAll("\\]", "");
        categoriesIn = categoriesIn.replaceAll("\"", "");
        categoriesIn = categoriesIn.replaceAll(" ", "");
        categories = categoriesIn.split(",");
        phoneIn = phoneIn.replaceAll("\"", "");
        phoneIn = phoneIn.replaceAll("\\+", "");
        phoneIn = phoneIn.replaceAll("\\-", "");
        phone = phoneIn;
        addressIn = addressIn.replaceAll("\\[", "");
        addressIn = addressIn.replaceAll("\\]", "");
        addressIn = addressIn.replaceAll("\"", "");
        address = addressIn;       
        lat="N/A";
        lng="N/A";
        id="N/A";
        placeId="N/A";
    }
    
    public Suggestion(String nameIn, String urlIn, String phoneIn, String categoriesIn, 
    String ratingIn, String latIn, String lngIn)//YelpAPI alternate
    {
        rating = ratingIn;
        name = nameIn;
        url = urlIn;
        categoriesIn = categoriesIn.replaceAll("\\[", "");          
        categoriesIn = categoriesIn.replaceAll("\\]", "");
        categoriesIn = categoriesIn.replaceAll("\"", "");
        categoriesIn = categoriesIn.replaceAll(" ", "");
        categories = categoriesIn.split(",");
        phone = phoneIn;
        address = "N/A";
        lat= latIn;
        lng= lngIn;
        id="N/A";
        placeId="N/A";
    }

    public Suggestion(String nameIn, String ratingIn, String typesIn, String vicinityIn, 
    String idIn, String placeIdIn, String latIn, String lngIn)//GooglePlacesAPI
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
        categories = typesIn.split(",");
        vicinityIn = vicinityIn.replaceAll("\"", "");
        address= vicinityIn;
        url="N/A";
        phone="N/A";
    }

    public Suggestion(String nameIn, String latIn, String lngIn, String idIn,
                        String contact, String[] types) //foursquare
    {
        name = nameIn;
        lat = latIn;
        lng = lngIn;
        id = idIn;
        phone = contact;
        categories = types;
        url = "";
        address = "";
        rating = "";
    }

    public String toString()
    {
        String output = "";
        output += ("Name: " + this.name + "\n");
        output += ("Rating: " + this.rating + "\n");
        output += ("URL: " + this.url + "\n");
        output += ("Address: " + this.address + "\n");
        output += ("Latitude: " + this.lat + "\n");
        output += ("Longitude: " + this.lng + "\n");
        output += ("Phone: " + this.phone + "\n");
        output += ("ID: " + this.id + "\n");
        output += ("Place ID: " + this.placeId + "\n");
        output += "Categories: ";
        for (String s : categories)
        {
            output += (s + ", ");
        }

        return output + "\n";
    }

    public void print()
    {
        System.out.println(this.toString());
    }
}
