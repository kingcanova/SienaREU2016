import java.util.*;

/**
 * Every person has a profile with an ID assciated with him/her.
 * The profile contains a list of ratings he/she gave to example attractions
 * Also contains an array of candidates(possible suggestions that can be rated for the person),
 * a context ID based on where the candidates are all located, 
 * and other information based on their trip
 * @version May
 */
public class Profile
{
    protected int[][] ratings = new int[100][1];//array of ratings given by individual indexed by attraction
    protected int user_id; //id given to one specific individual 
    protected int response_id; // id of individual response given by TREC..user_ids can be repeated
    protected Double age;
    protected String group; //Alone,Friends,Family,Other
    protected String season;//Winter, Summer, Autumn, Spring
    protected String gender;//male,female
    protected String duration;//Night out, Day trip, Weekend trip, Longer
    protected String tripType;//Business, Holiday, Other
    protected int context_id; // ID of where candidate suggestions are located
    ArrayList<Integer> candidates; // array list of possible suggestions

    //maps an example attraction ID that they rated, to the rating that it was given
    protected Hashtable<Integer, Integer> attr_ratings = new Hashtable<Integer, Integer>();
    //maps each attraction category to its specific score
    protected Hashtable<String, Double> cat_count = new Hashtable<String, Double>();
    //maps each attraction category to the amount of times its been rated
    protected Hashtable<String, Integer> cat_occurance = new Hashtable<String, Integer>();

    public Profile(int id, int responseId, int contextId, Double age, String group, String season, String gender, String duration, String tripType, ArrayList<Integer> candidates )
    {
        this.user_id = id;
        this.age = age;
        this.group = group;
        this.season = season;
        this.gender = gender;
        this.duration = duration;
        this.tripType = tripType;
        this.response_id = response_id;
        this.context_id = contextId;
        this.candidates = candidates;
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
