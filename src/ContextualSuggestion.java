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
    protected static Hashtable<Integer, Suggestion> theCollection = new Hashtable<Integer, Suggestion>();
    // maybe have the collection so a city ID gets all the venues in that city?
    protected static Hashtable<Integer, ArrayList<Suggestion>> otherCollection = new Hashtable<>();

    protected String groupID = "Siena";
    protected String runID = "test";

    public ArrayList<Suggestion> getNearbyVenues(Integer cityID)
    {
        return otherCollection.get(cityID);
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
        //         try
        //         {
        //             PrintWriter out_file = new PrintWriter( "out_file.csv"); //change to csv for final copy
        // 
        //             out_file.println("groupid,runid,profile,context,rank,title,description,url,docId");
        //             for(Profile person : profiles)
        //             {
        //                 for(int i = 0; i< person.ratings.length; i++)
        //                 {
        //                     int rating = i+1;
        //                     if(rating>50){rating -= 50;}
        //                     out_file.println("group44" + "," + "run44a" + "," + person.user_id + "," 
        //                         + (attractions.get(i).context + i/50)  + "," + rating + "," + 
        //                         attractions.get(i).title + "," + "description" + "," +
        //                         attractions.get(i).url + ",");
        // 
        //                     //                     out_file.println(person.user_id + " - " + attractions.get(i).id_num 
        //                     //                         + "\t" + person.ratings[i][0] + " " + person.ratings[i][1] + " \t" + 
        //                     //                         attractions.get(i).title + "," + attractions.get(i).url );
        // 
        //                 }
        //             }
        // 
        //             out_file.close();
        //         }
        //         catch (IOException e)
        //         {
        //             System.out.println("Error creating output file");
        //             System.exit(-1);
        //         }
    }
}
