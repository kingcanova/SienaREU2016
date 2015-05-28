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

    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    public void suggest()
    {
        for(Profile person : people)
        {
            for(Context city : person.cities)
            {
                for(POI attraction : city.attractions)
                {
                    System.out.println(groupID + ", " + runID + ", " + person.user_id + ", " +
                        city.id_num + ", " + attraction.rank + ", " + 
                        attraction.title + ", " + attraction.description + ", " +
                        attraction.url + ", \n");
                }
            }
        }
    }


}
