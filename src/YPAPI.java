import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.net.*;
import java.util.*;
import javax.swing.*;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.methods.*;
import org.apache.http.util.*;
/**
 * Searches the Yellow Pages API for a specific attraction in a specific context
 * 
 * @author Aidan, Tom, Kevin, Zach
 * @version Final
 */
public class YPAPI
{
    private final String YP_KEY  = Secret.YP_KEY;
    private final HttpClient client = HttpClientBuilder.create().build();

    /**
     * Query the API 
     * @param name a string of the attraction to search
     * @param lat a double of the latitude of the context
     * @param lon a double of the longitude of the context
     * @return a suggestion object of the search results
     */
    public Suggestion performSearch(String name, double lat, double lon)
    throws ParseException, IOException, URISyntaxException
    {
        final URIBuilder builder = new URIBuilder()
            .setScheme("http")
            .setHost("api2.yp.com")
            .setPath("/listings/v1/search");
            
        //necessary paramaters to add for a YellowPagesAPI search. Max search radius is 50 miles
        builder.addParameter("searchloc", lat + "," + lon);
        builder.addParameter("radius", "50"); //radius in miles
        builder.addParameter("term", name);
        builder.addParameter("key", YP_KEY);
        builder.addParameter("format", "json");

        //conduct search with above paramters and recieve String response
        final HttpUriRequest request = new HttpGet(builder.build());
        final HttpResponse execute = this.client.execute(request);
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

        //array of terms used by YellowPagesAPI inside of the JSON response to separate data
        String[] ypTerms = new String[]{ "businessName", "averageRating", "latitude", 
                "longitude", "categories" };

        //retrieves JSON data for all businesses found
        JSONObject cur = (JSONObject) response.get("searchResult");
        cur = (JSONObject) cur.get("searchListings");
        JSONArray results = (JSONArray) (cur.get("searchListing"));

        //Check if Yellow Pages returned any results
        if(results.size() == 0)
        {
            //No results found, return a blank suggestion
            return new Suggestion();
        }
        else
        {
            //obtain information for first business and place data in array "temp"
            String[] temp = new String[ypTerms.length];
            JSONObject unk = (JSONObject) results.get(0);
            for(int j = 0; j < ypTerms.length; j++)
            {
                if(unk.get(ypTerms[j]) != null)
                {
                    temp[j] = (unk.get(ypTerms[j])).toString();
                }
            }
            //Create Suggestion object with collected info
            return new Suggestion(temp[0],temp[1], temp[2], temp[3], temp[4]);
        }
    }

    public static void main(String[] args)
    throws ParseException, IOException, URISyntaxException
    {
        //test example
        YPAPI g = new YPAPI();
        Suggestion s = g.performSearch("Bostons Restaurant  Sports Bar",  47.492874, -111.295362);
        s.print();
    }
}
