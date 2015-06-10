import java.io.*;
import javax.swing.*;
import java.util.*;

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
        String csvFile = "/Users/Trees/Desktop/REU/contextual_suggestion/2014example/";
        //id, city, state, lat, long
        String locations = "contexts2014.csv";
        //id, attraction, description, website
        String profile70 = "profiles2014-70.csv";
        String profile100 = "profiles2014-100.csv";
        //id, title, description, url
        String pois = "examples2014.csv";

        //String csvFile = JOptionPane.showInputDialog("Path to locate file: ", null);

        BufferedReader br = null;
        String line = "";

        try {
            br = new BufferedReader(new FileReader(csvFile + locations));
            buildLocation(br);
            
            br = new BufferedReader(new FileReader(csvFile + pois));
            buildPOI(br);

            br = new BufferedReader(new FileReader(csvFile + profile100));
            buildProfile(br);

            br = new BufferedReader(new FileReader(csvFile + profile70));
            buildProfile(br);
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
        test.suggest();
    }

    /**
     * Creates the Context objects given the file listing the cities
     */
    public void buildLocation(BufferedReader br) throws IOException
    {
        String line = "";
        line = br.readLine();
        String[] params = line.split(",");
        while ((line = br.readLine()) != null) 
        {
            // use comma as separator
            String[] context = line.split(",");
            test.cities.add(new Context(Integer.parseInt(context[0]), context[1], context[2],
                    Double.parseDouble(context[3]), Double.parseDouble(context[4])));
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
        line = br.readLine();
        String[] params = line.split(",");
        while ((line = br.readLine()) != null) 
        {
            // use comma as separator
            String[] context = new String[4];
            context = CVSSplitter.split(line, 4);
            test.attractions.add(new POI(Integer.parseInt(context[0]), context[1], context[2], context[3]));
        }
        br.close();
    }

    /**
     * Creates the Profile objects and adds the associated ratings
     * given the file on profiles. 
     */
    public void buildProfile(BufferedReader br) throws IOException
    {
        String line = "";
        line = br.readLine();
        String[] params = line.split(",");
        int person_id = -1;
        
        int start = test.attractions.get(0).id_num;
        while ((line = br.readLine()) != null) 
        {
            // use comma as separator
            String[] context = line.split(",");
            int num = Integer.parseInt(context[0]);
            if(num != person_id)
            {
                person_id = num;
                test.people.add(new Profile(num));
                if(first == -1)
                {
                    first = person_id;
                }
            }
            int att_id = Integer.parseInt(context[1]);
            int t_rating = Integer.parseInt(context[2]);
            int u_rating = Integer.parseInt(context[3]);
            Profile person = test.people.get(num-first);
            person.ratings[att_id - start][0] = t_rating;
            person.ratings[att_id - start][1] = u_rating;
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
        while ((line = s.nextLine()) != null) {
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
