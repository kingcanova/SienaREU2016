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
    protected int[][] ratings = new int[100][1];
    protected int user_id;
    protected int response_id;
    protected int age;
    protected String group; //Alone,Friends,Family,Other
    protected String season;//Winter, Summer, Autumn, Spring
    protected String gender;//male,female
    protected String duration;//Night out, Day trip, Weekend trip, Longer
    protected String tripType;//Business, Holiday, Other
    
    //saves the ratings of each profile example attraction
    protected Hashtable<Integer, Integer> attr_ratings = new Hashtable<Integer, Integer>();
    //saves the categories of those attractions and the matching rating
    protected Hashtable<String, Double> cat_count = new Hashtable<String, Double>();
    protected Hashtable<String, Integer> cat_occurance = new Hashtable<String, Integer>();

    public Profile(int id, int responseId, int age, String group, String season, String gender, String duration, String tripType )
    {
        this.user_id = id;
        this.age = age;
        this.group = group;
        this.season = season;
        this.gender = gender;
        this.duration = duration;
        this.tripType = tripType;
        this.response_id = response_id;
        for (int[] p : ratings)
        {
            Arrays.fill(p, -1);
        }
    }

    public Double getCatRating(String cat) {
        return cat_count.get(cat);
    }

    public Integer getAttrRating(Integer id) {
        return attr_ratings.get(id);
    }
}
