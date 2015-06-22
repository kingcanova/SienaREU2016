import java.util.*;
import java.io.*;

/**
 * Read in a profile ratings, cities, and attractions and return a CSV file of 
 * suggested POI's
 * 
 * @author Siena - Aidan 
 * @version May
 */
public class ContextualSuggestion
{
    protected static Hashtable<Integer, Context> contexts = new Hashtable<Integer, Context>();
    protected static Hashtable<Integer, Suggestion> pois = new Hashtable<Integer, Suggestion>();
    protected static Hashtable<Integer, Profile> profiles = new Hashtable<Integer, Profile>();
    protected static Hashtable<Integer, ArrayList<Suggestion>> theCollection = new Hashtable<Integer, ArrayList<Suggestion>>();

    protected String groupID = "Siena";
    protected String runID = "test";

    /**
     * Returns an arraylist of all the attractions located in a certain city
     */
    public ArrayList<Suggestion> getNearbyVenues(Integer cityID)
    {
        return theCollection.get(cityID);
    }

    // public void rank(Profile user, ArrayList<Suggestion> venues)
    // {
    //     return;
    // }

    /**
     * Print out all the attractions that the person has rated
     * testing before final version of writing to a csv file
     * 
     */
    public static void suggest()
    {
        CSVreader reader = new CSVreader();
        reader.run();

        Profile person = profiles.get(700);
        ArrayList<Suggestion> attractions = theCollection.get(151);
        
        for (Suggestion s : attractions)
        {
            for (String cat : s.category)
            {
                s.score += person.cat_count.get(cat);
            }
        }
        
        Collections.sort(attractions, null);
        
        for (int i=0; i<50; i++)
        {
            System.out.println((i+1) + ". " + attractions.get(i).name + "   " + attractions.get(i).score);
        }

    }
}
