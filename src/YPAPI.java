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

public class YPAPI
{
    private final String YP_KEY  = Secret.YP_KEY;
    private final HttpClient client = HttpClientBuilder.create().build();

    public Suggestion performSearch(final String name, final double lat, 
    final double lon) throws ParseException, IOException, URISyntaxException
    {      
        final URIBuilder builder = new URIBuilder().setScheme("http").setHost("api2." + 
                "yp.com").setPath("/listings/v1/search");
        //necessary paramaters to add for a GooglePlacesAPI search. Max for radius is 50,000 meters
        builder.addParameter("searchloc", lat + "," + lon);
        builder.addParameter("radius", "50");//radius in meters
        builder.addParameter("term", name);
        builder.addParameter("key", YP_KEY);
        builder.addParameter("format", "json");

        //conduct search with above paramters and recieve String response
        final HttpUriRequest request = new HttpGet(builder.build());
        final HttpResponse execute = this.client.execute(request);
        final String r = EntityUtils.toString(execute.getEntity());
        System.out.println(r);

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

        //System.out.println(r);
        //System.out.println(response);

        //array of terms used by GooglePlacesAPI inside of the JSON response to separate data
        String[] ypTerms = new String[]{"businessName", "averageRating", "latitude", 
                                            "longitude","categories"};

        //retrieves JSON data for all businesses found
        JSONObject cur = (JSONObject) response.get("searchResult");
        cur = (JSONObject) cur.get("searchListings");
        JSONArray results = (JSONArray) (cur.get("searchListing"));
        if(results.size() == 0)
        {
            //System.out.println("Sorry, no results were found for your request.");
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
            //retrieve lng and lat, which were under two sub categories. Create Suggestion object with collected info
            return new Suggestion(temp[0],temp[1], temp[2], temp[3], temp[4]);
        }
    }

    public static void main(final String[] args) throws ParseException, IOException, URISyntaxException
    {
        YPAPI g = new YPAPI();
        Suggestion s = g.performSearch("Bombers", 42.652580, -73.756233);
        s.print();
    }
}
