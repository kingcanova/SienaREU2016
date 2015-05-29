import java.util.ArrayList;
import java.util.Arrays;
public class CVSSplitter
{
    public static String[] split(String cvsline, int len)
    {
        boolean inquote = false;
        String[] vals = new String[len];
        Arrays.fill(vals, "");
        int valindex = 0;
        for (int i=0; i<cvsline.length(); i++)
        {
            if (cvsline.charAt(i) == '"')
                inquote = !inquote;
            if (cvsline.charAt(i) == ',' && !inquote)
            {
                valindex++;
                continue;
            }
            vals[valindex] += cvsline.charAt(i);
        }
        return vals;
    }

    public static void main(String args[]) {
        String line = "test,test2,test3,\"test4,stilltest4\",test5";
        String[] linevals = CVSSplitter.split(line, 5);
        for (String val : linevals)
        {
            System.out.println(val);
        }
    }
}

