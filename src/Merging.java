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
        int yelp_count = 0;
        int four_count = 0;
        int goog_count = 0;

        String name = yelp.name; //default
        if( (yelp.name).equals(four.name) && (yelp.name).equals(goog.name) ) {
            name = yelp.name;
        }
        else if( (yelp.name).equals(four.name) ) {
            name = yelp.name;
            goog_count -= 1;
        }
        else if( (yelp.name).equals(goog.name) ) {
            name = yelp.name;
            four_count -= 1;
        }
        else if( (four.name).equals(goog.name) ) {
            name = four.name;
            yelp_count -= 1;
        }

        double ylat = Double.parseDouble(yelp.lat) + (yelp_count * 100);
        double flat = Double.parseDouble(four.lat) + (four_count * 100);
        double glat = Double.parseDouble(goog.lat) + (goog_count * 100);
        double ylng = Double.parseDouble(yelp.lng) + (yelp_count * 100);
        double flng = Double.parseDouble(four.lng) + (four_count * 100);
        double glng = Double.parseDouble(goog.lng) + (goog_count * 100);

        double lat = glat; //default
        double lng = glng; //default
        if(Math.abs(ylat-flat) < 0.01){lat = ylat;}
        else if(Math.abs(ylat-glat) < 0.01){lat = ylat;}
        else if(Math.abs(flat-glat) < 0.01){lat = flat;}

        if(Math.abs(ylng-flng) < 0.01){lng = ylng;}
        else if(Math.abs(ylng-glng) < 0.01){lng = ylng;}
        else if(Math.abs(flng-glng) < 0.01){lng = flng;}

        double rating = 0.0;
        double yelp_rating = 0.0;
        double four_rating = 0.0;
        double goog_rating = 0.0;
        try{
            if(yelp_count == 0) {
                yelp_rating = Double.parseDouble(yelp.rating);
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
        rating = (yelp_rating + four_rating + goog_rating) / rating;

        ArrayList<String> cats = new ArrayList<String>();
        if(yelp_count == 0) {
            for(String cat : yelp.categories)
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

        Suggestion result = new Suggestion(name, rating, lat, lng, cats);
        result.printFinal();
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
        Suggestion four = m.searchFourSq((lat + "," + lng), name);
        Suggestion yelp = m.searchYelp(city, name);
        Suggestion goog = m.searchGoogle(lati, lngi, name);
        m.mergeApis(yelp, four, goog);
    }
}
