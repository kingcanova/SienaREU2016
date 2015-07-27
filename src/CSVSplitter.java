
import java.util.ArrayList;
import java.util.Arrays;
/**
 * separate program created to split a csv file while ignoring quotations and commas that are
 * in the actual information
 */
public class CSVSplitter
{
    /**
     * Point of Interest, or Attraction. 
     * Each attraction has a name, description, url, and ID number
     * These POIs will later be ranked based on someone's profile 
     * @version May
     */
    public static String[] split(String csvline, int len)
    {
        boolean inquote = false;
        String[] vals = new String[len];
        Arrays.fill(vals, "");
        int valindex = 0;
        for (int i=0; i<csvline.length(); i++)
        {
            if (csvline.charAt(i) == '"')
                inquote = !inquote;
            if (csvline.charAt(i) == ',' && !inquote)
            {
                valindex++;
                continue;
            }
            vals[valindex] += csvline.charAt(i);
        }
        return vals;
    }
}

