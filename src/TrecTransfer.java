import java.util.*;
import java.io.*;
/**
 * This separate program takes the list of TREC ID's we need for the possible suggestions and examples,
 * and converts them into the collection form (TREC ID, Context ID, URL, Title)
 * As a result, we get rid of all of the attractions we don't need and 
 * have a subset of the TREC Collection csv file
 * 
 * @version (a version number or a date)
 */
public class TrecTransfer
{
    public static void main(String[] args)
    {
        try{
            Scanner batchCollection = new Scanner(new File("../TRECData/batch_collection.txt"));
            Scanner batchExamples = new Scanner(new File("../TRECData/batch_examples.txt"));
            Scanner collection = new Scanner(new File("../TRECData/newCollection.csv"));
            ArrayList<String> idListCollection = new ArrayList<String>();
            ArrayList<String> idListExamples = new ArrayList<String>();
            //fill an arraylist with all of the TREC ID's needed for our collection
            while (batchCollection.hasNextLine())
                idListCollection.add(batchCollection.nextLine());
            //fill an arraylist with all of the TREC ID's needed for our examples
            while (batchExamples.hasNextLine())
                idListExamples.add(batchExamples.nextLine());

            //convert arraylists to arrays
            String[] idArrayCollection = idListCollection.toArray(new String[0]);
            String[] idArrayExamples = idListExamples.toArray(new String[0]);

            //sort our arrays based on ID
            Arrays.sort(idArrayCollection);
            Arrays.sort(idArrayExamples);
            //buffered writers for collection file and examples file
            Writer ftw = new BufferedWriter(new FileWriter("fullBatchCollectionFinal.csv"));
            Writer ftw2 = new BufferedWriter(new FileWriter("fullBatchExamplesFinal.csv"));
            
            String line = "";
            //array that will hold all info on attraction from collection
            String[] content;
            //go through each item in the collection given by TREC, see if the TREC ID from
            //that collection is one that we need. If we need it, print to file.
            while (collection.hasNextLine())
            {
                line = collection.nextLine();
                //split line based on TREC ID, URL, Title and store in "content"
                content = CSVSplitter.split(line, 4);

                if (Arrays.binarySearch(idArrayCollection, content[0]) > -1)   
                {
                    ftw.write(String.format("%s,%s,%s,%s\n",
                            content[0], content[1], content[2], content[3]));
                }
                if (Arrays.binarySearch(idArrayExamples, content[0]) > -1)
                {
                    ftw2.write(String.format("%s,%s,%s,%s\n",
                            content[0], content[1], content[2], content[3]));
                }

            }      
            ftw.flush();
            ftw.close();
            ftw2.flush();
            ftw2.close();
            batchCollection.close();
            batchExamples.close();
            collection.close();
        }
        catch(Exception e)
        {
            System.err.print(e);
        }
    }
}
