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
    protected Hashtable<String, Integer> cat_count = new Hashtable<String, Integer>();
    //protected ArrayList<String> posAttrTypes = new ArrayList<String>();
    //protected ArrayList<String> negAttrTypes = new ArrayList<String>();

    protected int user_id;
    public Profile(int id)
    {
        this.user_id = id;
        for (int[] p : ratings)
        {
            Arrays.fill(p, -1);
        }
    }

    public Integer getCatRating(String cat) {
        return cat_count.get(cat);
    }

    public Integer getAttrRating(Integer id) {
        return attr_ratings.get(id);
    }
}
