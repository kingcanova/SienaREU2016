import java.util.*;
import javax.swing.*;
import java.io.IOException;
import org.json.simple.parser.ParseException;
import java.net.*;
import java.math.*;
/**
 * Merges the results from each API into one suggestion object
 * 
 * @author Aidan, Tom, Kevin, Zach
 * @version Final
 */
public class Merging
{
    FSqAPI fsqApi = new FSqAPI();
    YPAPI ypApi = new YPAPI();
    GooglePlacesAPI googleApi = new GooglePlacesAPI();

    /**
     * Call the foursquare api 
     * @param "latitude, longitude"
     * @param "Name of attraction"
     * @return Suggestion object created by api
     */
    public Suggestion searchFourSq(String ll, String name)
    {
        //         System.out.println("Searching FourSquare for: " + name);
        try
        {
            //ArrayList<Suggestion> fsqResults = 
            Suggestion result = fsqApi.queryAPI(ll, name);
            //result.print();
            return result;
        }
        catch(Exception e)
        {
            System.err.println(e);
            System.out.println(e);
            return new Suggestion();
        }
    }

    /**
     * Call the yp api 
     * @param "City, State abbr"
     * @param "Name of attraction"
     * @return Suggestion object created by api
     */
    public Suggestion searchYP(double lat, double lng, String name)
    {
        //         System.out.println("Searching Yellow Pages for: " + name);
        try
        {
            Suggestion result = ypApi.performSearch(name, lat, lng);
            //result.print();
            return result;
        }
        catch(Exception e)
        {
            System.err.println(e);
            return new Suggestion();
        }
    }

    /**
     * Call the google api 
     * @param latitude
     * @param longitude
     * @param "Name of attraction"
     * @return Suggestion object created by api
     */
    public Suggestion searchGoogle(double lat, double lng, String name) 
    {
        //         System.out.println("Searching GooglePlaces for: " + name);
        try
        {
            //ArrayList<Suggestion> googleResults = new ArrayList<Suggestion>();
            Suggestion result = googleApi.performSearch(name, lat, lng);
            //             result.print();
            return result;
        }
        catch(Exception e)
        {
            System.err.println(e);
            return new Suggestion();
        }
    }

    /**
     * Replace all spaces, "and"s, and punctuation from the attraction name
     */
    public static String simplify(String original)
    {
        original = original.toLowerCase();
        original = original.replaceAll(" ", "");
        original = original.replaceAll("[^a-z0-9]+", "");
        original = original.replaceAll("and", "");
        //         System.out.println(original);
        return original;
    }

    /**
     * Call the foursquare api 
     * @param the original name being searched for
     * @param Suggestion result from yelp
     * @param Suggestion result from foursquare
     * @param Suggestion result from google
     * @return merged Suggestion object
     */
    public Suggestion mergeApis(String original, Suggestion four, Suggestion goog, Suggestion yp)
    {
        int yp_count = -1;
        int four_count = -1;
        int goog_count = -1;

        String o = Merging.simplify(original); 
        String f = Merging.simplify(four.name); 
        String g = Merging.simplify(goog.name); 
        String y = Merging.simplify(yp.name); 

        //use the most common name
        //keep track of which api's have accurate names
        //use original by default
        String name = original;
        if( y.contains(o) || o.contains(y) && !y.equals("")) { yp_count += 1;}
        if( f.contains(o) || o.contains(f) && !f.equals("")) { four_count += 1;}
        if( g.contains(o) || o.contains(g) && !g.equals("")) { goog_count += 1;}

        //save lat/lng, if their count is not zero lat/lng will be 100 degrees off
        double ylat = Double.parseDouble(yp.lat) + (yp_count * 100);
        double flat = Double.parseDouble(four.lat) + (four_count * 100);
        double glat = Double.parseDouble(goog.lat) + (goog_count * 100);
        double ylng = Double.parseDouble(yp.lng) + (yp_count * 100);
        double flng = Double.parseDouble(four.lng) + (four_count * 100);
        double glng = Double.parseDouble(goog.lng) + (goog_count * 100);

        double lat = glat; //default
        double lng = glng; //default

        //compare lat's with a tolerance of 0.01 degrees
        //use most common lat
        if (Math.abs(ylat-flat) < 0.01)
            lat = ylat;
        else if (Math.abs(ylat-glat) < 0.01)
            lat = ylat;
        else if (Math.abs(flat-glat) < 0.01)
            lat = flat;

        if (Math.abs(ylng-flng) < 0.01)
            lng = ylng;
        else if (Math.abs(ylng-glng) < 0.01)
            lng = ylng;
        else if (Math.abs(flng-glng) < 0.01)
            lng = flng;

        double rating = 0.0;
        double yp_rating = 0.0;
        double four_rating = 0.0;
        double goog_rating = 0.0;
        //look at each rating and 
        //increment rating variable if using a rating
        //rating may or may not exist, ignore if doesn't
        try{
            if(yp_count == 0) {
                yp_rating = Double.parseDouble(yp.rating);
                if(yp_rating != 0)
                    rating += 1;
            }
        } catch(Exception e){}
        try{
            if(four_count == 0) {
                four_rating = Double.parseDouble(four.rating);
                if(four_rating != 0)
                    rating += 1;
            }
        } catch(Exception e){}
        try{
            if(goog_count == 0) {
                goog_rating = Double.parseDouble(goog.rating);
                if(goog_rating != 0)
                    rating += 1;
            }
        } catch(Exception e){}
        if(goog_rating == 0 && four_rating == 0 && yp_rating == 0)
            rating+=1;
        //take average of all used ratings
        rating =round(((four_rating + goog_rating + yp_rating) / rating),2);

        //add all categories of used api's into unified list
        ArrayList<String> cats = new ArrayList<String>();
        if(yp_count == 0) {
            for(String cat : yp.categories)
            {
                if(!cats.contains(cat))
                {
                    cats.add(cat);
                }
            }
        }
        if(four_count == 0) {
            for(String cat : four.categories)
            {
                if(!cats.contains(cat))
                {
                    cats.add(cat);
                }
            }
        }
        if(goog_count == 0) {
            for(String cat : goog.categories)
            {
                if(!cats.contains(cat))
                {
                    cats.add(cat);
                }
            }
        }

        //create the unified suggestion
        Suggestion result = new Suggestion(name, rating, lat, lng, cats);
        //result.printFinal();
        //System.out.println();
        return result;
    }

    /**
     * Round value to specific decimal place
     */    
    public static double round(double value, int places) 
    {
        if (places < 0 || value == Double.NaN) 
        {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Given a name and a location, find the info on the attraction
     * @param the attraction name
     * @param the context id for the ciry the attraction is located in
     * @return the merged suggestion object
     */
    public static Suggestion merge(String attr, int contextID)
    {
        System.out.println();

        Merging m = new Merging();
        String name = attr;
        //get the Context object from the hashtable
        Context cur = ContextualSuggestion.contexts.get(contextID);
        String lat = cur.latitude + "";
        String lng = cur.longitude + "";
        String city = cur.name + ", " + cur.state;
        Double lati = cur.latitude;
        Double lngi = cur.longitude;

        //System.out.println("Attr:\t" + attr);
        //System.out.println("City:\t" + city);
        //search the three api's
        Suggestion four = m.searchFourSq((lat + "," + lng), name);
        Suggestion yp = m.searchYP(lati, lngi, name);
        Suggestion goog = m.searchGoogle(lati, lngi, name);
        //System.out.println();
        return m.mergeApis(attr, four, goog, yp);        
    }
}
