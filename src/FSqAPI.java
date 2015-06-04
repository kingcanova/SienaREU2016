import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.CompactVenue;
import fi.foyt.foursquare.api.entities.VenuesSearchResult;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.json.JsonReader;
import javax.json.Json;
import java.util.Scanner;

import java.util.*;

/**
 * Write a description of class FoursquareApi here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class FSqAPI
{
    protected String client_id = Secret.FOURSQUARE_CLIENT_ID, 
        client_secret = Secret.FOURSQUARE_CLIENT_SECRET,
        version = "20120609";

    public void searchVenues(String ll) throws FoursquareApiException {
        // First we need a initialize FoursquareApi. 
        FoursquareApi foursquareApi = new FoursquareApi(client_id, 
                client_secret, "http://www.siena.edu");

        foursquareApi.setVersion(version);

        // After client has been initialized we can make queries.
        Result<VenuesSearchResult> result = 
            foursquareApi.venuesSearch(ll, null, null, null, null, null, null, null, null, null, null);

        if (result.getMeta().getCode() == 200) {
            // if query was ok we can finally we do something with the data
            for (CompactVenue venue : result.getResult().getVenues()) {
                // TODO: Do something we the data
                System.out.println(venue.getName());
            }
        } else {
            // TODO: Proper error handling
            System.out.println("Error occured: ");
            System.out.println("  code: " + result.getMeta().getCode());
            System.out.println("  type: " + result.getMeta().getErrorType());
            System.out.println("  detail: " + result.getMeta().getErrorDetail()); 
        }
    }
    /**
     * returns a string representing the json or maybe a JSON object
     * @param ll lat/long
     * @param query thing to search for
     * @return a string representing the json or maybe a JSON object
     */
    public String buildURL(String ll, String query) throws java.net.MalformedURLException, IOException
    {
        String url = String.format("https://api.foursquare.com/v2/venues/search" +
                "?client_id=%s" +
                "&client_secret=%s" +
                "&v=%s" +
                "&ll=%s" + 
                "&query=%s", 
                client_id, client_secret, version, ll, query);
        String jsontext = "Error - not initialized";
        try {
            InputStream source = new URL(url).openStream();
            jsontext = new Scanner(source).useDelimiter("\\A").next();
            // gets the entire source into a string -- courtesy of
            // https://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner.html
        } catch (java.net.MalformedURLException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }
        return jsontext;
    }

    public void stringToJson(String in)
    {
        JSONParser parser = new JSONParser();
        JSONObject response = null;
        try {
            response = (JSONObject) parser.parse(in);
        } catch (ParseException pe) {
            System.out.println("Error: could not parse JSON response:");
            System.exit(1);
        }

        String bob = (response.toString()).replace('{','\n');
        bob = bob.replace(',', '\t');
        bob = bob.replace('}', '\n');
        System.out.println(bob);

        String[] fqTerms = new String[]{"name", "location", "id", "contact", "categories"};
        ArrayList<String[]> list = new ArrayList<String[]>();
        JSONObject venues = (JSONObject) response.get("response");
        JSONArray results = (JSONArray) venues.get("venues");
        for(int i = 0; i<results.size(); i++)
        {
            String[] temp = new String[7];//name, lat, lng, id, contact, categoriesID, catName
            JSONObject curr = (JSONObject) results.get(i);
            temp[0] = (curr.get("name")).toString();
            temp[1] = ((JSONObject)(curr.get("location"))).get("lat").toString();
            temp[2] = ((JSONObject)(curr.get("location"))).get("lng").toString();
            temp[3] = (curr.get("id")).toString();
            temp[4] = (curr.get("contact")).toString();
            temp[5] = ((JSONObject)(curr.get("categories"))).get("id").toString();
            temp[6] = ((JSONObject)(curr.get("categories"))).get("name").toString();
            list.add(temp);
        }
        for(int i = 0; i < list.size(); i++)
        {
            for(int j = 0; j < list.get(0).length; j++)
            {
                System.out.println(list.get(i)[j]);
            }
            System.out.println("\n");
        }
    }

    public static void main(String[] args)
    {
        FSqAPI test = new FSqAPI();
        try {
            String text = test.buildURL("42.65,-73.75", "burrito");
            test.stringToJson(text);
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
