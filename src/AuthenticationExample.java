import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;
/**
 * 
 */
public class AuthenticationExample {

    public void authenticationRequest(HttpServletRequest request, HttpServletResponse response) {
        FoursquareApi foursquareApi = new FoursquareApi("0UXKNKMOAUUDQZNUEB3FFOZ2DJXIZMNPZQS1UWZXCFFM4KNB",
                "SZTVMMSNH4EUXUWT2TF3UQBMYQJHSLEB54Z2THED5G5AI0QG", 
        FoursquareApi foursquareApi = new FoursquareApi(Secret.FOURSQUARE_CLIENT_ID,
                Secret.FOURSQUARE_CLIENT_SECRET, 
                "http://www.siena.edu");
        try {
            // First we need to redirect our user to authentication page. 
            response.sendRedirect(foursquareApi.getAuthenticationUrl());
        } catch (IOException e) {
            // TODO: Error handling
        }
    }

    public void handleCallback(HttpServletRequest request, HttpServletResponse response) {
        // After user has logged in and confirmed that our program may access user's Foursquare account
        // Foursquare redirects user back to callback url. 
        FoursquareApi foursquareApi = new FoursquareApi("0UXKNKMOAUUDQZNUEB3FFOZ2DJXIZMNPZQS1UWZXCFFM4KNB", 
        "SZTVMMSNH4EUXUWT2TF3UQBMYQJHSLEB54Z2THED5G5AI0QG", "http://www.siena.edu");
        FoursquareApi foursquareApi = new FoursquareApi(Secret.FOURSQUARE_CLIENT_ID,
        Secret.FOURSQUARE_CLIENT_SECRET, "http://www.siena.edu");
        // Callback url contains authorization code 
        String code = request.getParameter("code");
        try {
            // finally we need to authenticate that authorization code 
            foursquareApi.authenticateCode(code);
            // ... and voilà we have a authenticated Foursquare client
        } catch (FoursquareApiException e) {
            // TODO: Error handlings
        }
    }

}}
