import java.util.*;
import java.io.*;
import java.net.*;
import javax.json.JsonReader;
import javax.json.Json;
import java.nio.charset.Charset;
import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.CompactVenue;
import fi.foyt.foursquare.api.entities.VenuesSearchResult;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.methods.*;
import org.apache.http.util.*;

/**
 * Searches the Four Square API for a specific attraction in a specific context
 * 
 * @author Aidan, Tom, Kevin, Zach
 * @version Final
 */
public class FSqAPI
{
    protected String client_id = Secret.FOURSQUARE_CLIENT_ID, 
    client_secret = Secret.FOURSQUARE_CLIENT_SECRET,
    version = "20120609";

    /**
     * Query the API 
     * @param ll a string of the latitude and longitude of the context
     * @param name a string of the attraction to search
     * @return a suggestion object of the search results
     */
    public Suggestion queryAPI(String ll, String name) throws URISyntaxException, IOException
    {
        final URIBuilder builder = new URIBuilder()
            .setScheme("https")
            .setHost("api.foursquare.com")
            .setPath("/v2/venues/search");

        //necessary paramaters to add for a FourSquareAPI search       
        builder.addParameter("client_id", client_id);
        builder.addParameter("client_secret", client_secret);
        builder.addParameter("v", version);
        builder.addParameter("ll", ll);//latitude and longitude
        builder.addParameter("query", name);

        //conduct search with above paramters and recieve String response
        final HttpUriRequest request = new HttpGet(builder.build());
        HttpClient client = HttpClientBuilder.create().build();
        final HttpResponse execute = client.execute(request);
        final String r = EntityUtils.toString(execute.getEntity());

        //turn String into JSON
        JSONParser parser = new JSONParser();
        JSONObject response = null;

        try {
            response = (JSONObject) parser.parse(r);
        } 
        catch (ParseException pe) {
            System.err.println("Error: could not parse JSON response:");
            System.out.println(r);
            System.exit(1);
        } 
        catch (NullPointerException e) {
            System.err.println("Error: null pointer in FSqAPI query:\n" + e);
            return new Suggestion();
        }

        //array of terms used by FourSquareAPI inside of the JSON response to separate data
        String[] fqTerms = new String[]{"name", "location", "id", "contact","rating", "categories"};

        //retrieves JSON data for all businesses found
        JSONObject venues = (JSONObject) response.get("response");
        JSONArray results = (JSONArray) venues.get("venues");

        //Check if Yellow Pages returned any results
        if (results.size() == 0)
        {
            //No results found, return a blank suggestion
            return new Suggestion();
        }

        //obtain information for first business and place data in array "temp"
        String[] temp = new String[7];//name, lat, lng, id, contact, categoriesID, catName
        JSONObject curr = (JSONObject) results.get(0);
        temp[0] = (curr.get("name")).toString();
        temp[1] = ((JSONObject)(curr.get("location"))).get("lat").toString();
        temp[2] = ((JSONObject)(curr.get("location"))).get("lng").toString();
        temp[3] = (curr.get("id")).toString();
        temp[4] = ""; 
        if(curr.get("rating") == null)
        {
            temp[5] = "";
        }
        else
        {
            temp[5] = (curr.get("rating")).toString();
        }
        JSONObject four = ((JSONObject)(curr.get("contact")));

        if(four.size() != 0)
        {
            temp[4] = (((JSONObject)(curr.get("contact"))).get("phone")).toString();
        }
        JSONArray cats = ((JSONArray)(curr.get("categories")));
        String[] types = new String[cats.size()];
        for(int x = 0; x < cats.size(); x++)
        {
            types[x] = ((JSONObject)cats.get(x)).get("shortName").toString().toLowerCase().replace(" ","");
        }
        return new Suggestion(temp[0],temp[1],temp[2],temp[3],temp[4],temp[5],types);
    }

    public static void main(String[] args) throws ParseException, IOException, URISyntaxException
    {
        FSqAPI f = new FSqAPI();
        Suggestion s = f.queryAPI("42.652580, -73.756233","Bombers");
        s.print();

    }
}
