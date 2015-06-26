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

    /**
     * Print the top 50 suggestions for each user profile
     * 
     * Currently only prints for profile 700 for testing purposes
     */
    public static void suggest()
    {
        CSVreader reader = new CSVreader();
        System.out.println("Running CSVReader");
        reader.run();

        //         for (int i=0; i<=115; i++)
        //         {
        //             Profile person = profiles.get(700 + i);
        Profile person = profiles.get(1);
        ArrayList<Suggestion> attractions = theCollection.get(151);

        //Give each attraction a score based one the rating and frequency of a category
        System.out.println("Scoring Attractions");
        for (Suggestion s : attractions)
        {
            System.out.println(s.title);
            //Add the score of each category to the current suggestion's score
            for (String cat : s.category)
            {
                if(person.cat_count.get(cat) != null && !cat.equals("establishment") && !cat.equals("restaurant")
                && !cat.equals("food"))
                {
                    s.score += person.cat_count.get(cat);
                }
                System.out.println("\t" + cat + "\t" + person.cat_count.get(cat));
            }
        }

        //Mergesorts the scored suggestion objects
        Collections.sort(attractions, null);

        System.out.println("50 Sorted Results: ");
        for (int k=0; k<50; k++)
        {
            System.out.println((k+1) + ". " + attractions.get(k).title + "\t" + attractions.get(k).score);
        }

        //     }
    }
}
