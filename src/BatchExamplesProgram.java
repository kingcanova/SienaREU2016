import java.util.*;
import java.io.*;
import java.nio.*;
import java.nio.file.*;
public class BatchExamplesProgram
{
    public static void main(String[] args)throws Exception 
    {
        BufferedReader br, br2, br3 = null;
        PrintWriter pw = new PrintWriter("batchExamplesCategorizedWithId.txt");
        String examples = "fullBatchExamples.csv";
        String collection = "fullBatchCollection.csv";
        Hashtable<String, String> table = new Hashtable<String, String>();
        String line = "";

        br = new BufferedReader(new FileReader(Paths.get("../TRECData/" + examples).toFile()));
        br2 = new BufferedReader(new FileReader(Paths.get("../TRECData/" + collection).toFile()));
        br3 = new BufferedReader(new FileReader(Paths.get("./" + "BatchExamplesCategorized.txt").toFile()));
        while ((line = br.readLine()) != null)
        {
            String[] temp = line.split(",");
            String name = temp[3];
            String[] trecID = temp[0].split("-");
            String attId = trecID[1];
            if(table.get(name)==null)
                table.put(name, attId);
        }
        line = "";
        while ((line = br2.readLine()) != null)
        {
            String[] temp = line.split(",");
            String name = temp[3];
            String[] trecID = temp[0].split("-");
            String attId = trecID[1];
            if(table.get(name)==null)
                table.put(name, attId);
        }
        while ((line = br3.readLine()) != null)
        {
            String name = line;
            pw.println(name);
            pw.println(table.get(name));
            line = br3.readLine();
            while( line != null && !line.equals(""))
            {
                pw.println(line);
                line = br3.readLine();
            }
            pw.println();
        }    
        pw.close();
        br.close();
        //br2.close();
        br3.close();
    }
}
