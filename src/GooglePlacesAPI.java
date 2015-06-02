
import java.text.ParseException;
import java.io.IOException;
import java.net.*;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.methods.*;
import org.apache.http.util.*; 


public class GooglePlacesAPI
{
    private static final String GOOGLE_API_KEY  = "AIzaSyCVJRipDs6aiMO4Qu8eVHS1Yh1PBiePQb0";
    private final HttpClient client = HttpClientBuilder.create().build();
    
    public void performSearch(final String types, final double lat, final double lon) throws ParseException, IOException, URISyntaxException
    {
        final URIBuilder builder = new URIBuilder().setScheme("https").setHost("maps.googleapis.com").setPath("/maps/api/place/nearbysearch/json");

        builder.addParameter("location", lat + "," + lon);
        builder.addParameter("radius", "14000");//radius in meters
        builder.addParameter("types", types);
        //builder.addParameter("sensor", "true"); -> googleplaces states "sensor" is no longer needed
        builder.addParameter("key", GooglePlacesAPI.GOOGLE_API_KEY);

        final HttpUriRequest request = new HttpGet(builder.build());

        final HttpResponse execute = this.client.execute(request);

        final String response = EntityUtils.toString(execute.getEntity());
        
        System.out.println(response);
    }

    public static void main(final String[] args) throws ParseException, IOException, URISyntaxException
    {
        new GooglePlacesAPI().performSearch("restaurant",42.6525793, -73.7562317); //albany,ny
    }
}