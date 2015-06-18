import java.util.*;

import javax.swing.*;
import java.io.IOException;
import org.json.simple.parser.ParseException;
import java.net.*;
/**
 * Write a description of class Merging here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
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
     * 
     */
    public Suggestion searchFourSq(String ll, String name)
    {
        System.out.println("Searching FourSquare for: " + name);
        try
        {
            //ArrayList<Suggestion> fsqResults = 
            Suggestion result = fsqApi.queryAPI(ll, name);
            result.print();
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
     * 
     */
    public Suggestion searchYP(Double lat, Double lng, String name)
    {
        System.out.println("Searching Yellow Pages for: " + name);
        try
        {
            Suggestion result = ypApi.performSearch(name, lat, lng);
            result.print();
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
     * Call the google api 
     * @param latitude
     * @param longitude
     * @param "Name of attraction"
     * @return Suggestion object created by api
     * 
     */
    public Suggestion searchGoogle(double lat, double lng, String name) 
    {
        System.out.println("Searching GooglePlaces for: " + name);
        try
        {
            //ArrayList<Suggestion> googleResults = new ArrayList<Suggestion>();
            Suggestion result = googleApi.performSearch(name, lat, lng);
            result.print();
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
     * Call the foursquare api 
     * @param Suggestion result from yelp
     * @param Suggestion result from foursquare
     * @param Suggestion result from google
     * @return merged Suggestion object
     * 
     */
    public Suggestion mergeApis(Suggestion four, Suggestion goog, Suggestion yp)
    {
        int yp_count = 0;
        int four_count = 0;
        int goog_count = 0;

        String name = four.name; //use foursquare by default
        //use the most common name
        //make not of api not being used by decrementing api_count variable
        //         if(four.name == null)
        //         {
        //             name = goog.name;
        //         }
        if((yp.name).equals(four.name) && (yp.name).equals(goog.name)) {
            name = yp.name;
        }
        else if( (yp.name).equals(four.name) ) {
            name = yp.name;
            goog_count -= 1;
        }
        else if( (yp.name).equals(goog.name) ) {
            name = yp.name;
            four_count -= 1;
        }
        else if( (four.name).equals(goog.name) ) {
            name = four.name;
            yp_count -= 1;
        }

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
        if(Math.abs(ylat-flat) < 0.01){lat = ylat;}
        else if(Math.abs(ylat-glat) < 0.01){lat = ylat;}
        else if(Math.abs(flat-glat) < 0.01){lat = flat;}

        if(Math.abs(ylng-flng) < 0.01){lng = ylng;}
        else if(Math.abs(ylng-glng) < 0.01){lng = ylng;}
        else if(Math.abs(flng-glng) < 0.01){lng = flng;}

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
                rating += 1;
            }
        } catch(Exception e){}
        try{
            if(four_count == 0) {
                four_rating = Double.parseDouble(four.rating);
                rating += 1;
            }
        } catch(Exception e){}
        try{
            if(goog_count == 0) {
                goog_rating = Double.parseDouble(goog.rating);
                rating += 1;
            }
        } catch(Exception e){}
        //take average of all used ratings
        rating = (four_rating + goog_rating + yp_rating) / rating;

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
        result.printFinal();
        System.out.println();
        return result;
    }

    /**
     * Given a name and a location, find the info on the attraction
     * @param the attraction name
     * @param the context id for the ciry the attraction is located in
     * @return the merged suggestion object
     */
    public static Suggestion merge(String attr, int contextID)
    {
        Merging m = new Merging();
        String name = attr;
        //get the Context object from the hashtable
        Context cur = ContextualSuggestion.contexts.get(contextID);
        String lat = cur.latitude + "";
        String lng = cur.longitude + "";
        String city = cur.name + ", " + cur.state;
        Double lati = cur.latitude;
        Double lngi = cur.longitude;
        //search the three api's
        Suggestion four = m.searchFourSq((lat + "," + lng), name);
        Suggestion yp = m.searchYP(lati, lngi, name);
        Suggestion goog = m.searchGoogle(lati, lngi, name);
        System.out.println();
        return m.mergeApis(four, goog, yp);        
    }
}
