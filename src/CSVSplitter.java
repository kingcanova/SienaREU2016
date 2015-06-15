import java.util.ArrayList;
import java.util.Arrays;
public class CSVSplitter
{
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

