import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.CompactVenue;
import fi.foyt.foursquare.api.entities.VenuesSearchResult;
/**
 * Write a description of class FoursquareApi here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class FSqAPI
{
    public void searchVenues(String ll) throws FoursquareApiException {
        // First we need a initialize FoursquareApi. 
        FoursquareApi foursquareApi = new FoursquareApi("0UXKNKMOAUUDQZNUEB3FFOZ2DJXIZMNPZQS1UWZXCFFM4KNB",
                "SZTVMMSNH4EUXUWT2TF3UQBMYQJHSLEB54Z2THED5G5AI0QG", 
                "http://www.siena.edu");

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

    public static void main(String[] args)
    {
        FSqAPI test = new FSqAPI();
        try{
            test.searchVenues("Albany");
        }
        catch (FoursquareApiException p)
        {
            System.out.println(p);
        }

    }
}
