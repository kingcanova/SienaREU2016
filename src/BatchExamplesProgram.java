import java.util.*;
import java.io.*;
import java.nio.*;
import java.nio.file.*;
public class BatchExamplesProgram
{
    public static void main(String[] args)throws Exception 
    {
        BufferedReader br, br2, br3 = null;
        PrintWriter pw = new PrintWriter("batchCollectionCategorizedWithId.txt");
        String examples = "fullBatchExamples.csv";
        String collection = "fullBatchCollection.csv";
        //Hashtable<String, String> table = new Hashtable<String, String>();
        String line = "";

        //br = new BufferedReader(new FileReader(Paths.get("../TRECData/" + examples).toFile()));
        br = new BufferedReader(new FileReader(Paths.get("../TRECData/" + collection).toFile()));
        br3 = new BufferedReader(new FileReader(Paths.get("./" + "BatchCollectionCategorized.txt").toFile()));
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
