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
        String collection = "collection_nyc.csv";//NYC subset used for testing
        //id, city, state, lat, long
        String locations = "contexts2015.csv";
        String coordinates = "contexts2015coordinates.csv";
        //id, attraction, description, website
        String profile100 = "profiles2014-100.csv";//2014 used for testing
        //id, title, description, url
        String pois = "examples2014.csv";//2014 used for testing

        BufferedReader br = null;
        BufferedReader br2 = null;

        try {
            //br for context csv and br2 for coordinates csv
            br = new BufferedReader(new FileReader(Paths.get(trecData + locations).toFile()));
            br2 = new BufferedReader(new FileReader(Paths.get(trecData + coordinates).toFile()));
            buildLocation(br, br2);

            //Reads in examples2014.csv which contains the attractions rated in the profiles
            br = new BufferedReader(new FileReader(Paths.get(trecData + pois).toFile()));
            //bufferedtestBuildPOI();
            //testBuildPOI();
            buildPOI(br);

            //Reads in profiles2014-100.csv which contains all the example profiles
            br = new BufferedReader(new FileReader(Paths.get(trecData + profile100).toFile()));
            buildProfile(br);

            //Reads in collection_2015.csv which contains all possible attractions to suggest
            br = new BufferedReader(new FileReader(Paths.get(trecData + collection).toFile()));
            //buildCollection(br);
            testBuildCollection();
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
        //ContextualSuggestion.suggest();
    }

    /**
     * Creates the Context objects given the file listing the cities
     */
    public void buildLocation(BufferedReader br, BufferedReader br2) throws IOException
    {
        System.out.println("Building Contexts");

        String line = "";
        //read in parameters to ignore them and jump to next line 
        br.readLine();
        br2.readLine();
        while ((line = br.readLine()) != null)
        {
            //combine city id, name, state, and coordinates into one String
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
        System.out.println("Building Examples");
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
     * Read examples from a text file instead of querying the APIs
     */
    public void testBuildPOI() throws IOException
    {
        Scanner in = new Scanner(new File("TestInputExamples.txt"));
        String line = " ";
        String name = "";
        ArrayList<String> cats = new ArrayList<String>();
        int index = 101;
        //Read through the file
        while (in.hasNextLine())
        {
            name = in.nextLine();
            System.out.println(name);
            line = " ";
            //Check for the blank line after the list of categories
            while (!line.equals("") && in.hasNextLine())
            {            
                if(line.equals(""))
                    break;
                line = in.nextLine();
                cats.add(line);
            }
            ContextualSuggestion.pois.put(index, new Suggestion(name, 1, 2, 3, cats));
            index++;
            cats = new ArrayList<String>();
        }
    }

    public void bufferedtestBuildPOI() throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(Paths.get("TestInputExamples.txt").toFile()));
        String line = " ";
        String name = "";
        ArrayList<String> cats = new ArrayList<String>();
        int index = 101;
        //Read through the file
        while( (line = br.readLine()) != null )
        {
            name = line;
            System.out.println(name);
            line = br.readLine();
            while( line != null && !line.equals(""))
            {
                cats.add(line);
                line = br.readLine();
            }
            ContextualSuggestion.pois.put(index, new Suggestion(name, 1, 2, 3, cats));
            index++;
            cats = new ArrayList<String>();
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
        System.out.println("Building Profiles, ratings");
        String line = "";
        br.readLine();
        int person_id = -1;

        while ((line = br.readLine()) != null) 
        {
            // use comma as separator
            String[] context = line.split(",");
            //Get the profile id and check if it is the current profile
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

            //if attr rank is 3 or 4, place in positive category array for profile
            //id attr rank is 0 or 1, place in negative catrgory array for profile
            Suggestion curr = ContextualSuggestion.pois.get(att_id);

            for (String cat : curr.category)
            {
                if(cat.equals("bar") && person_id == 26)
                {
                    System.out.println("Bar: \t" + curr.title + "\t" + t_rating);
                }

                if (person.cat_count.get(cat) == null)
                {
                    person.cat_count.put(cat, 0.0);
                    person.cat_occurance.put(cat, 0);
                }
                double[] scores = new double[]{-4.0, -2.0, 1.0, 2.0, 4.0};
                if(t_rating != -1)
                {
                    person.cat_count.put(cat, person.cat_count.get(cat) + scores[t_rating]);
                    person.cat_occurance.put(cat, person.cat_occurance.get(cat) +1);
                }
                System.out.println("\t\t" + scores[t_rating]);

            }      

        }

        //go through each category in the hash table and divide by its frequency to get avg
        Set<Integer> people = ContextualSuggestion.profiles.keySet();
        for(Integer num : people)
        {
            Profile person = ContextualSuggestion.profiles.get(num);
            Set<String> keys = person.cat_occurance.keySet();
            for(String cat: keys)
            {
                person.cat_count.put(cat, (person.cat_count.get(cat)/person.cat_occurance.get(cat)));
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
        System.out.println("Building Collection");

        String line = "";
        ArrayList<Suggestion> temp = null;
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
        }
        br.close();
    }

    /**
     * Read colleciton from a text file instead of querying the APIs
     */
    public void testBuildCollection() throws IOException
    {
        Scanner in = new Scanner(new File("TestInputCollection.txt"));
        String line = " ";
        String name = "";
        ArrayList<Suggestion> temp = null;
        ArrayList<String> cats = new ArrayList<String>();
        //Read through the file
        while (in.hasNextLine())
        {
            name = in.nextLine();
            line = " ";
            //Check for the blank line after the list of categories
            while (!line.equals("") && in.hasNextLine())
            {
                line = in.nextLine();
                if(line.equals(""))
                    break;
                cats.add(line);
            }

            if (ContextualSuggestion.theCollection.get(151) == null)
            {//If first spot is empty
                temp = new ArrayList<Suggestion>();
                temp.add(new Suggestion(name, 1, 2, 3, cats));
                ContextualSuggestion.theCollection.put(151, temp);
            }
            else
            {//Already contains an arraylist
                temp = ContextualSuggestion.theCollection.get(151);
                temp.add(new Suggestion(name, 1, 2, 3, cats));
                ContextualSuggestion.theCollection.put(151, temp);
            }
            cats = new ArrayList<String>();
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
