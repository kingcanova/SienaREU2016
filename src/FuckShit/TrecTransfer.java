import java.util.*;
import java.io.*;
/**
 * Write a description of class TrecTransfer here.
 * 
 * @author Aidan Trees
 * @version (a version number or a date)
 */
public class TrecTransfer
{
    public static void main(String[] args)
    {
        try{
            Scanner batchCollection = new Scanner(new File("batch_collection.txt"));
            Scanner examples = new Scanner(new File("batch_examples.txt"));
            BufferedReader collection = new BufferedReader(new FileReader("partialCollection.txt"));
            ArrayList<String> idListCollection = new ArrayList<String>();
            ArrayList<String> idListExamples = new ArrayList<String>();
            while (batchCollection.hasNextLine())
                idListCollection.add(batchCollection.nextLine());
            while (examples.hasNextLine())
                idListExamples.add(examples.nextLine());
            String[] idArrayCollection = idListCollection.toArray(new String[0]);
            String[] idArrayExamples = idListExamples.toArray(new String[0]);
            Arrays.sort(idArrayCollection);
            Arrays.sort(idArrayExamples);

            PrintWriter ftw = new PrintWriter("fullBatchCollectionFinal2.csv");
            FileWriter ftw2 = new FileWriter("fullBatchExamplesFinal.csv");
            
            String line;
            String[] content;
            while ((line = collection.readLine()) != null)
            {
                //line = collection.readLine();
                content = CSVSplitter.split(line, 4);

                System.out.println(content[0]);

                int temp = Arrays.binarySearch(idArrayCollection, content[0]);
                if (Arrays.binarySearch(idArrayCollection, content[0]) > -1)
                    ftw.write(String.format("%s,%s,%s,%s\n",
                            content[0], content[1], content[2], content[3]));
                if (Arrays.binarySearch(idArrayExamples, content[0]) > -1)
                    ftw2.write(String.format("%s,%s,%s,%s\n",
                            content[0], content[1], content[2], content[3]));

            }

            ftw.close();
            ftw2.close();
            batchCollection.close();
            examples.close();
            collection.close();
        }
        catch(Exception e)
        {
            System.err.print(e);
        }
    }

    public static void sortTheFile() throws Exception
    {
        BufferedReader batchCollection = new BufferedReader(new FileReader("batch_collection.txt"));
        ArrayList<String> idListCollection = new ArrayList<String>();
        String line;
        while ((line = batchCollection.readLine()) != null)
            idListCollection.add(line);
        String[] idArrayCollection = idListCollection.toArray(new String[0]);
        Arrays.sort(idArrayCollection);
        FileWriter fw = new FileWriter("939.txt");
        for (String cur : idArrayCollection)
        {
            fw.write(cur + "\n");
        }
        fw.close();
    }
}
