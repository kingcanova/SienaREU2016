import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.CompactVenue;
import fi.foyt.foursquare.api.entities.VenuesSearchResult;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.json.JsonReader;
import javax.json.Json;
import java.util.Scanner;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.methods.*;
import org.apache.http.util.*;
import java.util.*;

/**
 * Write a description of class FoursquareApi here.
 * 
 * @author Aidan, Tom, Kevin, Zach
 * @version Final
 */
public class FSqAPI
{
    protected String client_id = Secret.FOURSQUARE_CLIENT_ID, 
    client_secret = Secret.FOURSQUARE_CLIENT_SECRET,
    version = "20120609";

    public Suggestion queryAPI(String ll, String name) throws URISyntaxException, IOException
    {
        final URIBuilder builder = 
            new URIBuilder().setScheme("https").setHost(
            "api.foursquare.com").setPath("/v2/venues/search");

        builder.addParameter("client_id", client_id);
        builder.addParameter("client_secret", client_secret);//radius in meters
        builder.addParameter("v", version);
        builder.addParameter("ll", ll);
        builder.addParameter("query", name);

        final HttpUriRequest request = new HttpGet(builder.build());

        HttpClient client = HttpClientBuilder.create().build();
        final HttpResponse execute = client.execute(request);

        final String r = EntityUtils.toString(execute.getEntity());

        JSONParser parser = new JSONParser();
        JSONObject response = null;
        try {
            response = (JSONObject) parser.parse(r);
        } catch (ParseException pe) {
            System.err.println("Error: could not parse JSON response:");
            System.exit(1);
        }
        catch (NullPointerException e)
        {
            //System.err.println(e);
            System.out.println(e);
            return new Suggestion();
            //System.exit(1);
        }
        
        //System.out.println(response);

        String[] fqTerms = new String[]{"name", "location", "id", "contact", "categories"};
        ArrayList<String[]> list = new ArrayList<String[]>();
        JSONObject venues = (JSONObject) response.get("response");
        JSONArray results = (JSONArray) venues.get("venues");
        
        if (results.size() == 0)
        {
            return new Suggestion();
        }
        
        String[] temp = new String[7];//name, lat, lng, id, contact, categoriesID, catName
        JSONObject curr = (JSONObject) results.get(0);
        temp[0] = (curr.get("name")).toString();
        temp[1] = ((JSONObject)(curr.get("location"))).get("lat").toString();
        temp[2] = ((JSONObject)(curr.get("location"))).get("lng").toString();
        temp[3] = (curr.get("id")).toString();
        temp[4] = "";
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
        return new Suggestion(temp[0],temp[1],temp[2],temp[3],temp[4],types);
    }
    
    public static void main(String[] args) throws ParseException, IOException, URISyntaxException
    {
        FSqAPI f = new FSqAPI();
        Suggestion s = f.queryAPI("42.652580, -73.756233", "Bombers");
        s.print();
        
    }
}
