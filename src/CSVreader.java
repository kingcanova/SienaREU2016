import java.io.*;
import javax.swing.*;
import java.util.*;
import java.nio.*;
import java.nio.file.*;

/**
 * Reads in all the neccesary csv files and loads the data into the 
 * 
 * @author Aidan, Tom, Kevin, Zach
 * @version Final
 */
public class CSVreader 
{
    /**
     * Reads in all the data from the four files containing the profile info (2 files),
     * list of cities, and the list of attractions.
     */
    public void run() 
    {
        String trecData = "../TRECData/";

        //String collection = "collection_2015.csv";
        String collection = "collection_nyc.csv";
        //id, city, state, lat, long
        String locations = "contexts2015.csv";
        String coordinates = "contexts2015coordinates.csv";
        //id, attraction, description, website
        String profile100 = "profiles2014-100.csv";
        //id, title, description, url
        String pois = "examples2014.csv";

        BufferedReader br = null;
        BufferedReader br2 = null;

        //using 2014 for testing
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
            buildCollection(br);
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
            ContextualSuggestion.contexts.put(Integer.parseInt(context[0]), 
                new Context(Integer.parseInt(context[0]), context[1], context[2],
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
        while((line = br.readLine())!=null)//Limit search to first 5 examples to avoid going over quota
        {
            //separate string by commas, place data into "context" array
            String[] context = new String[5];
            context = CSVSplitter.split(line, 5);
            //populate hashtable using attr ID as a key to reference the merged Suggestion object
            ContextualSuggestion.pois.put(Integer.parseInt(context[0]), Merging.merge(context[2], Integer.parseInt(context[1])));
        }
        br.close();
    }

    /**
     * Creates the Profile objects and adds the associated ratings for the att title
     *  --- Yet to implement creating the category rating
     * @param br Profile100 buffered reader 
     */
    public void buildProfile(BufferedReader br) throws IOException
    {
        String line = "";
        br.readLine();
        int person_id = -1;

        //int start = ContextualSuggestion.attractions.get(0).id_num;
        while ((line = br.readLine()) != null) 
        {
            // use comma as separator
            String[] context = line.split(",");
            int temp = Integer.parseInt(context[0]);
            if(temp != person_id)
            {
                person_id = temp;
                ContextualSuggestion.profiles.put(person_id, new Profile(person_id));
            }
            int att_id = Integer.parseInt(context[1]);
            int t_rating = Integer.parseInt(context[2]);
            int u_rating = Integer.parseInt(context[3]);
            Profile person = ContextualSuggestion.profiles.get(person_id);
            person.attr_ratings.put(att_id, t_rating); //only title rating for now
            //person.ratings[att_id][1] = u_rating;

            //if attr rank is 3 or 4, place in positive category array for profile
            //id attr rank is 0 or 1, place in negative catrgory array for profile
            Suggestion curr = ContextualSuggestion.pois.get(att_id);
            for(String cat : curr.category)
            {
                if(t_rating >=3)
                {
                    //Add 1 or 4 to the category total based on the rating
                    if (person.cat_count.get(cat) == null)
                        person.cat_count.put(cat, ((t_rating-3)*3)+1);                        
                    else                        
                        person.cat_count.put(cat, person.cat_count.get(cat)+(((t_rating-3)*3)+1));                    
                }
                else if(t_rating <=1)
                { 
                    //Subtract 1 or 4 to the category total based on the rating
                    if (person.cat_count.get(cat) == null)
                        person.cat_count.put(cat, ((t_rating-1)*3)-1);
                    else
                        person.cat_count.put(cat, person.cat_count.get(cat)+(((t_rating-1)*3)-1));
                }
            }
        }

        br.close();
    }

    /**
     * Read in the collection and creat the suggestion objects for all entries
     * 
     * ---Currently limited to first 10 for testing purposes
     * 
     * @param Collection Buffered Reader
     */
    public void buildCollection(BufferedReader br) throws IOException
    {
        String line = "";
        ArrayList<Suggestion> temp = null;
        //no paramters in collection
        //         for (int i=0; i<5; i++)
        //         {
        while ((line = br.readLine()) != null)
        {
            //line = br.readLine();
            //separate string by commas, place data into "context" array
            String[] context = CSVSplitter.split(line, 4);
            String attrID = (context[0].split("-"))[1];

            //populate hashtable using attr ID as a key to reference the merged Suggestion object
            if (ContextualSuggestion.theCollection.get(Integer.parseInt(context[1])) == null)
            {//If first spot is empty
                temp = new ArrayList<Suggestion>();
                temp.add(Merging.merge(context[3], Integer.parseInt(context[1])));
                ContextualSuggestion.theCollection.put(Integer.parseInt(context[1]), temp);
            }
            else
            {//Already contains an arraylist
                temp = ContextualSuggestion.theCollection.get(Integer.parseInt(context[1]));
                temp.add(Merging.merge(context[3], Integer.parseInt(context[1])));
                ContextualSuggestion.theCollection.put(Integer.parseInt(context[1]), temp);
            }
            //         }
            br.close();
        }
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

    /**
     * Get the n'th element of a csv line starting at zero (no quotes)
     * @param elemIndex n
     * @param csvLine csv line
     * @return the n'th element of a csv line starting at zero
     */
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
