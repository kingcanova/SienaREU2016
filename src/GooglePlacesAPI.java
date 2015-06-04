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

public class GooglePlacesAPI
{
    private final String GOOGLE_API_KEY  = Secret.GOOGLE_API_KEY;

    private final HttpClient client = HttpClientBuilder.create().build();

    public void performSearch(final String types, final double lat, final double lon, ArrayList<Suggestion> sugg) throws ParseException, IOException, URISyntaxException
    {
        final URIBuilder builder = new URIBuilder().setScheme("https").setHost("maps.googleapis.com").setPath("/maps/api/place/nearbysearch/json");

        builder.addParameter("location", lat + "," + lon);
        builder.addParameter("radius", "15000");//radius in meters
        builder.addParameter("types", types);
        //builder.addParameter("sensor", "true"); -> googleplaces states "sensor" is no longer needed
        builder.addParameter("key", GOOGLE_API_KEY);

        final HttpUriRequest request = new HttpGet(builder.build());

        final HttpResponse execute = this.client.execute(request);

        final String r = EntityUtils.toString(execute.getEntity());

        //System.out.println(r);

        JSONParser parser = new JSONParser();
        JSONObject response = null;
        try {
            response = (JSONObject) parser.parse(r);
        } catch (ParseException pe) {
            System.out.println("Error: could not parse JSON response:");
            System.out.println(r);
            System.exit(1);
        }

        String[] googleTerms = new String[]{"name", "rating", "types",
                "vicinity", "id", "place_id", "geometry", };
        ArrayList<String[]> list = new ArrayList<String[]>();
        JSONArray results = (JSONArray) response.get("results");
        for(int i = 0; i<results.size(); i++)
        {
            String[] temp = new String[googleTerms.length];
            for(int j = 0; j < googleTerms.length; j++)
            {
                JSONObject unk = (JSONObject) results.get(i);
                if(unk.get(googleTerms[j]) != null)
                {
                    temp[j] = (unk.get(googleTerms[j])).toString();
                    //System.out.println(temp[j]);
                }
            }
            list.add(temp);
        }
        String[] elements = new String[7];
        for(int i = 0; i < list.size(); i++)
        {
            for(int j = 0; j < googleTerms.length; j++)
            {
                //System.out.println(googleTerms[j] + ":\t" + list.get(i)[j]);
                elements[j] = list.get(i)[j];
            }
            sugg.add(new Suggestion(elements[0], elements[1], elements[2], elements[3], elements[4], elements[5], elements[6]));
            //System.out.println("\n");   
        }
        
    }
    public static void main(final String[] args) throws ParseException, IOException, URISyntaxException
    {
        ArrayList<Suggestion> s = new ArrayList<Suggestion>();
        new GooglePlacesAPI().performSearch("restaurant",42.6525793, -73.7562317,s); //albany,ny
        for(Suggestion sug : s)
        {
            sug.print();
        }

    }
}
