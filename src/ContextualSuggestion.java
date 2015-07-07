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
    protected static ArrayList<String> ignoredCats = new ArrayList<String>();

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
    public static void suggest() throws IOException
    {
        theCollection.clear();//resets scores since hashtables are static and are saved unless JVM is reset
        CSVreader reader = new CSVreader();
        //fill up array with categories we want to ignore for scoring purposes
        Scanner in = new Scanner(new File("UneccessaryCats.txt"));
        String line = " ";
        while (in.hasNextLine())
        {
            ignoredCats.add(in.nextLine());
        }

        System.out.println("Running CSVReader");
        reader.run();

        Profile person = profiles.get(Secret.me);
        ArrayList<Suggestion> attractions = theCollection.get(151);

        //Give each attraction a score based one the rating and frequency of a category
        System.out.println("Scoring Attractions");
        for (Suggestion s : attractions)
        {
            System.out.println(s.title);
            //Add the score of each category to the current suggestion's score,
            //if it was rated by the user and isn't an ignored category
            for (String cat : s.category)
            {
                if(person.cat_count.get(cat) != null && !ignoredCats.contains(cat))
                {
                    s.score += person.cat_count.get(cat);
                    System.out.println("\t" + cat + "\t" + person.cat_count.get(cat));
                    s.count += 1;
                }
            }

            //taking the average of all the categories of the attraction, 
            // rather than aggregate the score    
            if(s.count > 0)
            {
                s.score = s.score / s.count;
                System.out.println("Score: " + s.score);
            }
        }

        //Mergesorts the scored suggestion objects
        Collections.sort(attractions);

        System.out.println("50 Sorted Results: ");
        for (int k=0; k<50; k++)
        {
            System.out.printf("%2d) %-35s %5.2f\n",
                              k+1, attractions.get(0).title, attractions.get(0).score);

            Suggestion prev = attractions.remove(0);
            for(Suggestion s : attractions)
            {
                for(String cat : s.category)
                {
                    if(prev.category.contains(cat))
                    {
                        s.score -= .5;
                        break;
                    }
                }
            }
            Collections.sort(attractions);

        }
    }
}
