import java.util.*;
import java.io.*;
import java.nio.*;
import java.nio.file.*;
/**
 * This program takes in a text file that has an attraction name and categories underneath it.
 * It grabs the associated attraction ID from our subset collection and writes it to the previously
 * mentioned file.
 * This new text file with attraction name, ID, and categories is what our testBuildPOI and 
 * testBuildCollection methods use inside of the CSVreader class
 */
public class BatchExamplesProgram
{
    public static void main(String[] args)throws Exception 
    {
        BufferedReader br, br2, br3 = null;
        PrintWriter pw = new PrintWriter("batchCollectionCategorizedWithId2.txt");
        String examples = "fullBatchExamples.csv";
        String collection = "fullBatchCollection2.csv";
        //Hashtable<String, String> table = new Hashtable<String, String>();
        String line = "";

        //br = new BufferedReader(new FileReader(Paths.get("../TRECData/" + examples).toFile()));
        br = new BufferedReader(new FileReader(Paths.get("../TRECData/" + collection).toFile()));
        br3 = new BufferedReader(new FileReader(Paths.get("../TRECData/" + "72AttractionsCategorized.txt").toFile()));
        //br.readLine();
        int count = 0;
        int curr = 0;
        while ((line = br3.readLine()) != null)
        {
            String name = line;
            pw.println(name);

            String id = br.readLine();
            String[] temp = CSVSplitter.split(id, 4);
            String nameEx = temp[3].replace("\"", "");
            String[] trecID = temp[0].split("-");
            String attId = trecID[1];
            String contextId = trecID[2];

            if(name.equals(nameEx))
            {
                pw.println(attId);
                pw.println(contextId);
                count++;
            }
            else
            {
                System.out.println(curr + "\t" + "Categorized: " 
                                    + "\t" + name + "\n\t" + "Examples: "
                                    + "\t" + nameEx);
            }
            line = br3.readLine();
            while( line != null && !line.equals(""))
            {
                pw.println(line);
                line = br3.readLine();
            }
            pw.println();
            curr++;
        }    

        System.out.println(count);
        pw.close();
        br.close();
        //br2.close();
        br3.close();
    }
}
