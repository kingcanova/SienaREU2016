import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.net.*;
import java.util.*;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.methods.*;
import org.apache.http.util.*;
import javax.swing.*;

/**
 * Searches the Google Places API for a specific attraction in a specific context
 * 
 * @author Aidan, Tom, Kevin, Zach
 * @version Final
 */
public class GooglePlacesAPI
{
    private final String GOOGLE_API_KEY  = Secret.GOOGLE_API_KEY;
    private final HttpClient client = HttpClientBuilder.create().build();

    public Suggestion performSearch(final String name, final double lat, 
    final double lon) throws ParseException, IOException, URISyntaxException
    {
        final URIBuilder builder = new URIBuilder().setScheme("https").setHost("maps." + 
                "googleapis.com").setPath("/maps/api/place/nearbysearch/json");
        //necessary paramaters to add for a GooglePlacesAPI search. Max for radius is 50,000 meters
        builder.addParameter("location", lat + "," + lon);
        builder.addParameter("radius", "15000");//radius in meters
        builder.addParameter("name", name);
        builder.addParameter("key", GOOGLE_API_KEY);

        //conduct search with above paramters and recieve String response
        final HttpUriRequest request = new HttpGet(builder.build());
        final HttpResponse execute = this.client.execute(request);
        final String r = EntityUtils.toString(execute.getEntity());
        //System.out.println(r);

        //turn String into JSON
        JSONParser parser = new JSONParser();
        JSONObject response = null;
        try {
            response = (JSONObject) parser.parse(r);
        } catch (ParseException pe) {
            System.out.println("Error: could not parse JSON response:");
            System.out.println(r);
            System.exit(1);
        }

        //array of terms used by GooglePlacesAPI inside of the JSON response to separate data
        String[] googleTerms = new String[]{"name", "rating", "types","vicinity", "id", "place_id", "geometry", };

        //retrieves JSON data for all businesses found
        JSONArray results = (JSONArray) response.get("results");
        if(results.size() == 0)
        {
            //System.out.println("Sorry, no results were found for your request.");
            return new Suggestion();
        }
        else
        {
            //obtain information for first business and place data in array "temp"
            String[] temp = new String[googleTerms.length+1];
            JSONObject unk = (JSONObject) results.get(0);
            for(int j = 0; j < googleTerms.length-1; j++)
            {
                if(unk.get(googleTerms[j]) != null)
                {
                    temp[j] = (unk.get(googleTerms[j])).toString();
                }
            }
            //retrieve lng and lat, which were under two sub categories. Create Suggestion object with collected info
            temp[googleTerms.length-1] = (((JSONObject)((JSONObject)unk.get("geometry")).get("location")).get("lat")).toString();
            temp[googleTerms.length]   = (((JSONObject)((JSONObject)unk.get("geometry")).get("location")).get("lng")).toString();
            return new Suggestion(temp[0],temp[1], temp[2], temp[3], temp[4], temp[5], temp[6],temp[7]);
        }
    }

    public static void main(final String[] args) throws ParseException, IOException, URISyntaxException
    {
        GooglePlacesAPI g = new GooglePlacesAPI();
        Suggestion s = g.performSearch("Topolobampo/Frontera Grill", 41.85003, -87.65005);
        s.print();
    }
}
