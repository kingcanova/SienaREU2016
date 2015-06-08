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

    public ArrayList<Suggestion> performSearch(final String types, final double lat, 
                              final double lon) 
                              throws ParseException, IOException, URISyntaxException
    {
        ArrayList<Suggestion> sugg = new ArrayList<Suggestion>();
        
        final URIBuilder builder = new URIBuilder().setScheme("https").setHost("maps." + 
                                        "googleapis.com").setPath("/maps/api/place/nearbysearch/json");

        builder.addParameter("location", lat + "," + lon);
        builder.addParameter("radius", "15000");//radius in meters
        builder.addParameter("type", types);
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

        String[] googleTerms = new String[]{"name", "rating", "types","vicinity", "id", "place_id", "geometry", };
        ArrayList<String[]> list = new ArrayList<String[]>();
        JSONArray results = (JSONArray) response.get("results");
        for(int i = 0; i<results.size(); i++)
        {
            String[] temp = new String[googleTerms.length+1];
            JSONObject unk = (JSONObject) results.get(i);
            for(int j = 0; j < googleTerms.length-1; j++)
            {
                if(unk.get(googleTerms[j]) != null)
                {
                    temp[j] = (unk.get(googleTerms[j])).toString();
                    //System.out.println(temp[j]);
                }
            }
            temp[googleTerms.length-1] = (((JSONObject)((JSONObject)unk.get("geometry")).get("location")).get("lat")).toString();
            temp[googleTerms.length]   = (((JSONObject)((JSONObject)unk.get("geometry")).get("location")).get("lng")).toString();
            sugg.add(new Suggestion(temp[0],temp[1], temp[2], 
                        temp[3], temp[4], temp[5], temp[6],temp[7]));
        }
        return sugg;
    }
    public static void main(final String[] args) throws ParseException, IOException, URISyntaxException
    {
        ArrayList<Suggestion> s = new ArrayList<Suggestion>();
        s = new GooglePlacesAPI().performSearch("bar",42.6525793, -73.7562317); //albany,ny
        for(Suggestion sug : s)
        {
            sug.print();
        }
    }
}
