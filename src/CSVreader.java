import java.io.*;
import javax.swing.*;
import java.util.*;
import java.nio.*;
import java.nio.file.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
        String collection = "fullBatchCollection.csv";//NYC subset used for testing
        //id, city, state, lat, long
        String locations = "contexts2015.csv";
        String coordinates = "contexts2015coordinates.csv";
        //id, attraction, description, website
        String profiles = "batch_requests.json";//2014 used for testing
        //id, title, description, url
        String pois = "fullBatchExamples.csv";//2014 used for testing

        BufferedReader br = null;
        BufferedReader br2 = null;

        try {
            //br for context csv and br2 for coordinates csv
            br = new BufferedReader(new FileReader(Paths.get(trecData + locations).toFile()));
            br2 = new BufferedReader(new FileReader(Paths.get(trecData + coordinates).toFile()));
            buildLocation(br, br2);

            //Reads in examples2014.csv which contains the attractions rated in the profiles
            br = new BufferedReader(new FileReader(Paths.get(trecData + pois).toFile()));
            bufferedtestBuildPOI();
            //testBuildPOI();
            //buildPOI(br);

            //Reads in profiles2014-100.csv which contains all the example profiles
            br = new BufferedReader(new FileReader(Paths.get(trecData + profiles).toFile()));
            buildProfile(br);

            //Reads in collection_2015.csv which contains all possible attractions to suggest
            br = new BufferedReader(new FileReader(Paths.get(trecData + collection).toFile()));
            //buildCollection(br);
            //testBuildCollection();
            bufferedtestBuildCollection();
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
        //System.out.println("Building Contexts");

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
        //System.out.println("Building Examples");
        String line = "";
        //read in paramters to skip them
        br.readLine();
        while((line = br.readLine())!=null)//Limit search to first 5 examples to avoid going over quota
        {
            //separate string by commas, place data into "context" array
            String[] context = new String[5];
            context = CSVSplitter.split(line, 5);
            String trecID = context[0];
            String[] elements = trecID.split("-");
            //populate hashtable using attr ID as a key to reference the merged Suggestion object
            ContextualSuggestion.pois.put(Integer.parseInt(elements[1]), Merging.merge(context[3], Integer.parseInt(context[1])));
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
        BufferedReader br = new BufferedReader(new FileReader(Paths.get("batchExamplesCategorizedWithId.txt").toFile()));
        String line = " ";
        String name = "";
        ArrayList<String> cats = new ArrayList<String>();
        int index = 0;
        //Read through the file
        try{
            while( (line = br.readLine()) != null )
            {
                name = line;
                index = Integer.parseInt(br.readLine());
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
        }
        catch(Exception e)
        {
            System.err.print(e);
            System.out.println(index);
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
        //System.out.println("Building Profiles, ratings");
        String line = "";
        JSONParser parser = new JSONParser();
        JSONObject response = null;
        while((line = br.readLine())!=null)
        {
            //JSON parsing for response JSON file given to us by TREC
            try {
                response = (JSONObject) parser.parse(line);
            } catch (ParseException pe) {
                System.err.println("Error: could not parse JSON response:");
                System.out.println(line);
                System.exit(1);
            }
            catch (NullPointerException e) {
                System.err.println("Error: null pointer" + e);
            }

            JSONArray cands = (JSONArray) response.get("candidates");
            ArrayList<Integer> candidates = new ArrayList<Integer>();
            for(int i=0;i<cands.size();i++)
            {
                String trec_id = cands.get(i).toString();
                String[] elements = trec_id.split("-");
                candidates.add(Integer.parseInt(elements[1]));
            }

            JSONObject body = (JSONObject) response.get("body");
            int response_id = ((Long)response.get("id")).intValue();
            //             String group = body.get("group").toString();
            //             String season = body.get("season").toString();
            //             String trip_type = body.get("trip_type").toString();
            //             String duration = body.get("duration").toString();
            JSONObject location = (JSONObject) body.get("location");
            int contextId = ((Long)location.get("id")).intValue();
            JSONObject individual = (JSONObject) body.get("person");
            //             String gender = individual.get("gender").toString();
            Double age = (Double)individual.get("age");
            int person_id = ((Long)individual.get("id")).intValue();
            
            //declare new profile object with parsed information
            Profile person = new Profile(person_id, response_id, contextId, age, "", 
                    "", "", "","", candidates);
            //populate hashtable assiging the person's id number to its matching profile                          
            ContextualSuggestion.profiles.put(response_id, person);

            //Preferences is an array containing the list of attractions a profile rated
            //This cycles through the array, grabbing the attraction ID and rating, also
            //obtaining the tags and categories for an attraction and assigning them scores
            JSONArray preferences = (JSONArray) individual.get("preferences");
            for(int i = 0; i<preferences.size(); i++)
            {
                //obtain TREC id and parse out the attraction id
                JSONObject pref = (JSONObject)preferences.get(i);
                String trec_id = pref.get("documentId").toString();
                String[] elem = trec_id.split("-");
                int att_id = Integer.parseInt(elem[1]);
                int rating = Integer.parseInt(pref.get("rating").toString());
                JSONArray tags = (JSONArray) pref.get("tags");
                //populate hashtable with assiging an attraction id to its rating
                person.attr_ratings.put(att_id,rating);
                //retrieve the suggestion corresponding to the attr id 
                Suggestion curr = ContextualSuggestion.pois.get(att_id);
                double[] scores = new double[]{-4.0, -2.0, 1.0, 2.0, 4.0};
                //treat tags of examples as more categories,and assign them a score based on rating
                if (tags != null)
                {
                    for (Object t : (JSONArray)tags)
                    {
                        String tag = t.toString();
                        if (person.cat_count.get(tag) == null)
                        {
                            person.cat_count.put(tag, 0.0);
                            person.cat_occurance.put(tag, 0);
                        }
                        if(rating != -1)
                        {
                            person.cat_count.put(tag, person.cat_count.get(tag) + scores[rating]);
                            person.cat_occurance.put(tag, person.cat_occurance.get(tag) +1);
                        }
                    }      
                }
                //give categories of the attraction their scores based on rating
                if (curr != null)
                {
                    for (String cat : curr.category)
                    {
                        if (person.cat_count.get(cat) == null)
                        {
                            person.cat_count.put(cat, 0.0);
                            person.cat_occurance.put(cat, 0);
                        }
                        if(rating != -1)
                        {
                            person.cat_count.put(cat, person.cat_count.get(cat) + scores[rating]);
                            person.cat_occurance.put(cat, person.cat_occurance.get(cat) +1);
                        }
                    }      
                }
            }         
        }      

        //go through each category/tag in the hash table and divide by its frequency to get avg
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
        Scanner in = new Scanner(new File("batchCollectionCategorizedWithId.txt"));
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
     * Read colleciton from a text file instead of querying the APIs
     */
    public void bufferedtestBuildCollection() throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(
                    Paths.get("batchCollectionCategorizedWithId.txt").toFile()));
        //Scanner in = new Scanner(new File("TestInputCollectionAlbany.txt"));
        String line = " ";
        String name = "";
        int attrID, conID;
        ArrayList<Suggestion> temp = null;
        ArrayList<String> cats = new ArrayList<String>();
        //Read through the file
        while( (line = br.readLine()) != null )
        {
            name = line;
            attrID = Integer.parseInt(br.readLine());
            conID = Integer.parseInt(br.readLine());
            line = br.readLine();
            while( line != null && !line.equals(""))
            {
                cats.add(line);
                line = br.readLine();
            }            
            if (ContextualSuggestion.theCollection.get(conID) == null)
            {//If first spot is empty
                temp = new ArrayList<Suggestion>();
                temp.add(new Suggestion(name, cats, attrID));
                ContextualSuggestion.theCollection.put(conID, temp);
            }
            else
            {//Already contains an arraylist
                temp = ContextualSuggestion.theCollection.get(conID);
                temp.add(new Suggestion(name, cats, attrID));
                ContextualSuggestion.theCollection.put(conID, temp);
            }
            cats = new ArrayList<String>();
        }
        br.close();
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
