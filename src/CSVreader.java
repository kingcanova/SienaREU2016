import java.io.*;
import javax.swing.*;
import java.util.*;
import java.nio.*;
import java.nio.file.*;

public class CSVreader 
{
    protected ContextualSuggestion test = new ContextualSuggestion();
    int first = -1;
    /**
     * Reads in all the data from the four files containing the profile info (2 files),
     * list of cities, and the list of attractions.
     */
    public void run() 
    {
        String trecData = "../TRECData/";

        String collection = "collection_2015.csv";
        //id, city, state, lat, long
        String locations = "contexts2015.csv";
        String coordinates = "contexts2015coordinates.csv";
        //id, attraction, description, website
        String profile100 = "profiles2014-100.csv";
        //id, title, description, url
        String pois = "examples2014.csv";

        BufferedReader br = null;
        BufferedReader br2 = null;
        String line = "";

        try {
            //br for context csv and br2 for coordinates csv
            br = new BufferedReader(new FileReader(Paths.get(trecData + locations).toFile()));
            br2 = new BufferedReader(new FileReader(Paths.get(trecData + coordinates).toFile()));
            buildLocation(br, br2);

            br = new BufferedReader(new FileReader(Paths.get(trecData + pois).toFile()));
            buildPOI(br);

            br = new BufferedReader(new FileReader(Paths.get(trecData + profile100).toFile()));
            buildProfile(br);

            br = new BufferedReader(new FileReader(Paths.get(trecData + collection).toFile()));
            //build(br);
        }
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        finally
        {
            if (br != null) 
            {
                try 
                {
                    br.close();
                } 
                catch (IOException e) 
                {
                    e.printStackTrace();
                }
            }
        }
        ContextualSuggestion.suggest();
    }

    /**
     * Creates the Context objects given the file listing the cities
     */
    public void buildLocation(BufferedReader br, BufferedReader br2) throws IOException
    {
        String line = "";
        //read in parameters to ignore them and jump to next line 
        br.readLine();
        br2.readLine();
        while (line != null)
        {
            //combine city id, name, state, and coordinates into one String
            line = br.readLine();
            if (line == null) { break; }
            line += ",";
            line += br2.readLine();
            //Separate String by every 6 commas to place values in "context" array.
            String[] context = CSVSplitter.split(line,6);
            //Populate hashtable using context ID as a key to reference a Context object
            ContextualSuggestion.contexts.put(Integer.parseInt(context[0]), new Context(Integer.parseInt(context[0]), context[1], context[2],
                    Double.parseDouble(context[4]), Double.parseDouble(context[5])));
        } 
        br.close();
    }

    /**
     * Creates the POI objects given the file listing the attractions
     * because of commas in the description the first second and last are located
     */
    public void buildPOI(BufferedReader br) throws IOException
    {
        String line = "";
        //read in paramters to skip them
        br.readLine();
       // while ((line = br.readLine()) != null) 
       for(int i=0;i<10;i++)
        {
            line = br.readLine();
            //separate string by commas, place data into "context" array
            String[] context = new String[5];
            context = CSVSplitter.split(line, 5);
            //populate hashtable using attr ID as a key to reference the merged Suggestion object
            ContextualSuggestion.pois.put(Integer.parseInt(context[0]), Merging.merge(context[2], Integer.parseInt(context[1])));
            i++;
        }
        br.close();
    }

    /**
     * Creates the Profile objects and adds the associated ratings
     * given the file on profiles. 
     */
    public void buildProfile(BufferedReader br) throws IOException
    {
        //         String line = "";
        //         line = br.readLine();
        //         String[] params = line.split(",");
        //         int person_id = -1;
        // 
        //         int start = ContextualSuggestion.attractions.get(0).id_num;
        //         while ((line = br.readLine()) != null) 
        //         {
        //             // use comma as separator
        //             String[] context = line.split(",");
        //             int num = Integer.parseInt(context[0]);
        //             if(num != person_id)
        //             {
        //                 person_id = num;
        //                 ContextualSuggestion.people.add(new Profile(num));
        //                 if(first == -1)
        //                 {
        //                     first = person_id;
        //                 }
        //             }
        //             int att_id = Integer.parseInt(context[1]);
        //             int t_rating = Integer.parseInt(context[2]);
        //             int u_rating = Integer.parseInt(context[3]);
        //             Profile person = ContextualSuggestion.people.get(num-first);
        //             person.ratings[att_id - start][0] = t_rating;
        //             person.ratings[att_id - start][1] = u_rating;
        //         }
        //         br.close();
    }

    /**
     * csv file must be sorted 
     */
    public static ArrayList<String> getLocations(String id, File csvFile) throws IOException
    {
        // TODO: binary-serachify this
        String line;
        ArrayList<String> arr = new ArrayList<>();
        Scanner s = new Scanner(csvFile);
        while (s.hasNextLine()) {
            line = s.nextLine();
            String lineID = getCSVElement(1, line);
            if (id.compareTo(lineID) < 0) // passed ID 
                break;
            else if (id.equals(lineID))
                arr.add(line);
        }
        s.close();
        return arr;
    }

    public static String getCSVElement(int elemIndex, String csvLine)
    {
        return csvLine.split(",", elemIndex + 2)[elemIndex];
    }

    public static void test() throws IOException {
        ArrayList<String> lol = getLocations("777", new File("../collection_sorted_2015.csv"));
        for (String lel : lol)
            System.out.println(lel);
        System.out.println("done");
    }

    public static void main(String[] args) 
    {
        CSVreader obj = new CSVreader();
        obj.run();
    }
}
