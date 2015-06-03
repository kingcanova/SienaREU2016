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

/**
 * Write a description of class FoursquareApi here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class FSqAPI
{
    protected String client_id = "0UXKNKMOAUUDQZNUEB3FFOZ2DJXIZMNPZQS1UWZXCFFM4KNB";
    protected String client_secret = "SZTVMMSNH4EUXUWT2TF3UQBMYQJHSLEB54Z2THED5G5AI0QG";
    protected String client_id = Secret.FOURSQUARE_CLIENT_ID;
    protected String client_secret = Secret.FOURSQUARE_CLIENT_SECRET;
    protected String version = "20120609";

    public void searchVenues(String ll) throws FoursquareApiException {
        // First we need a initialize FoursquareApi. 
        FoursquareApi foursquareApi = new FoursquareApi("0UXKNKMOAUUDQZNUEB3FFOZ2DJXIZMNPZQS1UWZXCFFM4KNB", 
                "SZTVMMSNH4EUXUWT2TF3UQBMYQJHSLEB54Z2THED5G5AI0QG", "http://www.siena.edu");
        FoursquareApi foursquareApi = new FoursquareApi(client_id, 
                client_secret, "http://www.siena.edu");

        foursquareApi.setVersion("20120609");

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

    public String buildURL(String ll, String query)
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

        InputStream in = new URL(url).openStream();
        JSONObject object = null;
        String jsontext = "Error - not initialized";
        try {
            JsonReader jsonReader = Json.createReader(in);
            object = jsonReader.readObject();
            jsonReader.close();
        }
        catch(Error e)
        {
            System.out.println(e);
        }
        finally
        {
            in.close();
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

        JSONParser parser = new JSONParser();
        JSONObject response = null;
        try {
            response = (JSONObject) parser.parse(object);
        } catch (ParseException pe) {
            System.out.println("Error: could not parse JSON response:");
            System.out.println(object);
            System.exit(1);
        }        
        return jsono.toString();
        // marked for deletion (old)
        // InputStream in = new URL(url).openStream();
        // JSONObject object = null;
        // try {
        //     JsonReader jsonReader = Json.createReader(in);
        //     object = jsonReader.readObject();
        //     jsonReader.close();
        // }
        // catch(Error e)
        // {
        //     System.out.println(e);
        // }
        // finally
        // {
        //     in.close();
        // }

        // JSONParser parser = new JSONParser();
        // JSONObject response = null;
        // try {
        //     response = (JSONObject) parser.parse(object);
        // } catch (ParseException pe) {
        //     System.out.println("Error: could not parse JSON response:");
        //     System.out.println(object);
        //     System.exit(1);
        // }        
        // return jsono.toString();
    }

    public static void main(String[] args)
    {
        FSqAPI test = new FSqAPI();
        //         try{
        //             test.searchVenues("42.65,-73.75");
        //         }
        //         catch (FoursquareApiException p)
        //         {
        //             System.out.println(p);
        //         }
        String text = test.buildURL("42.65,-73.75", "burrito");
        System.out.println(text);
        try {
            String text = test.buildURL("42.65,-73.75", "burrito");
            System.out.println(text);
        } catch (Exception e) {
            System.err.println("AHHH!\n" + e);
        }
    }
}
