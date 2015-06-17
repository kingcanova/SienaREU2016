import java.util.*;

/**
 * Every person has a profile, containing a list of cities
 * containing the attractions they have rated or will be suggested 
 * 
 * @author Siena - Aidan
 * @version May
 */
public class Profile
{
    protected int[][] ratings = new int[100][2]; 
    
    //saves the ratings of each profile example attraction
    protected Hashtable<Integer, Integer> attr_ratings = new Hashtable<Integer, Integer>();
    
    //saves the categories of those attractions and the matching rating
    protected Hashtable<String, Integer> cat_ratings = new Hashtable<String, Integer>();
    
    protected int user_id;

    public Profile(int id)
    {
        this.user_id = id;
        for(int[] p : ratings)
        {
            Arrays.fill(p, -1);
        }
    }
}
