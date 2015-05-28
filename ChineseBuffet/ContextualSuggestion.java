import java.util.*;

/**
 * Read in a profile ratings, cities, and attractions and return a CSV file of 
 * suggested POI's
 * 
 * @author Siena - Aidan 
 * @version May
 */
public class ContextualSuggestion
{

    protected ArrayList<Profile> people;
    protected ArrayList<Context> cities;
    protected ArrayList<POI> attractions;

    protected String groupID = "Siena";
    protected String runID = "test";

    /**
     * read in all people and cities;
     */
    public ContextualSuggestion()
    {
        people = new ArrayList<Profile>();
        cities = new ArrayList<Context>();
        attractions = new ArrayList<POI>();
    }

    /**
     * Print out all the attractions that the person has rated
     * testing before final version of writing to a csv file
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    public void suggest()
    {
        for(Profile person : this.people)
        {
            for(int i = 0; i< person.ratings.length; i++)
            {
                //                 System.out.println(groupID + "," + runID + "," + person.user_id + ",(" + attractions.get(i).id_num + ")"
                //                     + "unkownCity" + "," + person.ratings[i][0] + " " + person.ratings[i][1] + ", \t" + 
                //                     attractions.get(i).title + "," + "description" + "," +
                //                     attractions.get(i).url + ",");
                
                System.out.println(person.user_id + " - " + attractions.get(i).id_num 
                    + "\t" + person.ratings[i][0] + " " + person.ratings[i][1] + " \t" + 
                    attractions.get(i).title + "," + attractions.get(i).url );

            }
        }
    }
}
