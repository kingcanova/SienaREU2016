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
    YelpAPI yelpApi = new YelpAPI();
    GooglePlacesAPI googleApi = new GooglePlacesAPI();

    public Suggestion searchFourSq(String ll, String name)
    {
        System.out.println("Searching FourSquare for: " + name);
        try
        {
            //ArrayList<Suggestion> fsqResults = 
            Suggestion result = fsqApi.stringToJson(fsqApi.queryAPI(ll, name));
            result.print();
            return result;
            //             for(Suggestion sug : fsqResults)
            //             {
            //                 sug.print();
            //             }
        }
        catch(Exception e)
        {
            System.err.println(e);
            System.out.println(e);
            return null;
        }
    }

    public Suggestion searchYelp(String cityName, String name)
    {
        System.out.println("Searching Yelp for: " + name);
        try
        {
            //ArrayList<Suggestion> yelpResults = new ArrayList<Suggestion>();
            Suggestion result = yelpApi.queryAPI(yelpApi, name, cityName);
            result.print();
            return result;
            //             for(Suggestion sug : yelpResults)
            //             {
            //                 sug.print();
            //             }
        }
        catch(Exception e)
        {
            System.err.println(e);
            System.out.println(e);
            return null;
        }
    }

    public Suggestion searchGoogle(double lat, double lng, String name) 
    {
        System.out.println("Searching GooglePlaces for: " + name);
        try
        {
            //ArrayList<Suggestion> googleResults = new ArrayList<Suggestion>();
            Suggestion result = googleApi.performSearch(name, lat, lng);
            result.print();
            return result;
            //             for(Suggestion sug : googleResults)
            //             {
            //                 sug.print();
            //             }
        }
        catch(Exception e)
        {
            System.err.println(e);
            System.out.println(e);
            return null;
        }
    }

    public void mergeApis(Suggestion yelp, Suggestion four, Suggestion goog)
    {
        //if( (yelp.name).equals(four.name)
    }

    public static void main(String[] args)
    {
        Merging m = new Merging();
        String name = JOptionPane.showInputDialog("Attraction?:", null);
        String lat = "42.65";
        String lng = "-73.75";
        String city = "Albany, NY";
        Double lati = Double.parseDouble(lat);
        Double lngi = Double.parseDouble(lng);
        Suggestion yelp = m.searchFourSq((lat + "," + lng), name);
        Suggestion four = m.searchYelp(city, name);
        Suggestion goog = m.searchGoogle(lati, lngi, name);
        m.mergeApis(yelp, four, goog);
    }
}
