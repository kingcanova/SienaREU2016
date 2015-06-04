
/**
 * Write a description of class Suggestion here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Suggestion
{
    private String rating, name, url, categories[], phone, address, geometry, id, 
                    placeId, vicinity;

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
        geometry ="";
        id="";
        placeId="";
    }
    
    public Suggestion(String nameIn, String ratingIn, String typesIn, String vicinityIn, 
    String idIn, String placeIdIn, String geometryIn)//GooglePlacesAPI
    {
        geometry = geometryIn;
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
        url="";
        phone="";
    }
    
    public String toString()
    {
        String output = "";
        output += ("Name: " + this.name + "\n");
        output += ("Rating: " + this.rating + "\n");
        output += ("URL: " + this.url + "\n");
        output += ("Phone: " + this.phone + "\n");
        output += ("Address: " + this.address + "\n");
        output += ("Geometry: " + this.geometry + "\n");
        output += ("Id: " + this.id + "\n");
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
