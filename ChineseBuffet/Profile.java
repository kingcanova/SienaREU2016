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
   protected int user_id;
   
   public Profile(int id)
   {
       this.user_id = id;
       Arrays.fill(ratings, -1);
   }
}
