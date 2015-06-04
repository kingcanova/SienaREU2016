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

        System.out.println(r);

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
            temp[googleTerms.length-2] = (((JSONObject)((JSONObject)unk.get("geometry")).get("location")).get("lat")).toString();
            temp[googleTerms.length-1] = (((JSONObject)((JSONObject)unk.get("geometry")).get("location")).get("lng")).toString();
            list.add(temp);
        }
        for(int i = 0; i < list.size(); i++)
        {
            for(int j = 0; j < googleTerms.length; j++)
            {
                System.out.println(googleTerms[j] + ":\t" + list.get(i)[j]);
            }
            System.out.println("\n");
        }

        //         for(int i = 0; i < businesses.size(); i++)
        //         {
        //             JSONObject currBusiness = (JSONObject) businesses.get(i);
        //             //String firstBusinessID = firstBusiness.get("id").toString();
        //             //             System.out.println(String.format(
        //             //                     "%s businesses found, querying business info for the top result \"%s\" ...",
        //             //                     businesses.size(), firstBusinessID));
        // 
        //             // Select the first business and display business details
        //             //String businessResponseJSON = yelpApi.searchByBusinessId(firstBusinessID.toString());
        //             //             System.out.println(String.format("Result for business \"%s\" found:", firstBusinessID));
        //             //System.out.println(businessResponseJSON);
        //             //Scanner in = new Scanner(businessResponseJSON);
        // 
        //             String[] googleTerms = new String[]{"rating", "name", "types",
        //                     "vicinity", "id", "place_id", "geometry", };
        // 
        //             String[] elements = new String[googleTerms.length];
        //             for(int j=0; j<googleTerms.length; j++)
        //             {
        //                 elements[i] = currBusiness.get(googleTerms[j]).toString();
        //                 System.out.println(elements[i]);
        //             }
        // 
        //         }
    }

    public static void main(final String[] args) throws ParseException, IOException, URISyntaxException
    {
        ArrayList<Suggestion> s = new ArrayList<Suggestion>();
        new GooglePlacesAPI().performSearch("restaurant",42.6525793, -73.7562317,s); //albany,ny
        //         for(Suggestion sug : s)
        //         {
        //             sug.print();
        //         }

    }
}
