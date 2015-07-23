import java.util.*;
import java.io.*;

/**
 * Read in a profile ratings, cities, and attractions and return a CSV file of 
 * suggested POI's
 * 
 * @author Siena - Aidan 
 * @version May
 */
public class ContextualSuggestion
{
    protected static Hashtable<Integer, Context> contexts = new Hashtable<Integer, Context>();
    protected static Hashtable<Integer, Suggestion> pois = new Hashtable<Integer, Suggestion>();
    protected static Hashtable<Integer, Profile> profiles = new Hashtable<Integer, Profile>();
    protected static Hashtable<Integer, ArrayList<Suggestion>> theCollection = new Hashtable<Integer, ArrayList<Suggestion>>();

    protected String groupID = "Siena";
    protected String runID = "test";

    /**
     * Returns an arraylist of all the attractions located in a certain city
     */
    public ArrayList<Suggestion> getNearbyVenues(Integer cityID)
    {
        return theCollection.get(cityID);
    }

    /**
     * Print the top 50 suggestions for each user profile
     * 
     * Currently only prints for profile 700 for testing purposes
     */
    public static void suggest() throws IOException
    {
        PrintWriter pw = new PrintWriter("SienaFinalOutput.json");

        theCollection.clear();//resets scores since hashtables are static and are saved unless JVM is reset
        CSVreader reader = new CSVreader();
        //fill up array with categories we want to ignore for scoring purposes
        Scanner in = new Scanner(new File("UneccessaryCats.txt"));
        ArrayList<String> ignoredCats = new ArrayList<String>();
        String line = " ";
        while (in.hasNextLine())
        {
            ignoredCats.add(in.nextLine());
        }

        //System.out.println("Running CSVReader");
        reader.run();

        Set<Integer> people = profiles.keySet();
        for(Integer num : people)
        {
            Profile person = profiles.get(num);
            ArrayList<Suggestion> attractions = new ArrayList<Suggestion>();

            for (Suggestion sug : theCollection.get(person.context_id)) 
            {
                if (person.candidates.contains(sug.id))
                    attractions.add(sug);
            }

            ArrayList<Suggestion> ignoredAttractions = new ArrayList<Suggestion>();
            //Give each attraction a score based one the rating and frequency of a category
            //System.out.println("Scoring Attractions");
            for (Suggestion s : attractions)
            {
                //System.out.println(s.title);
                boolean hasCategories = false; 
                //Add the score of each category to the current suggestion's score,
                //if it was rated by the user and isn't an ignored category
                for(String cat : s.category)
                {
                    hasCategories = true;
                    if(person.cat_count.get(cat) != null && !ignoredCats.contains(cat))
                    {
                        s.score += person.cat_count.get(cat);
                        //System.out.println("\t" + cat + "\t" + person.cat_count.get(cat));
                        //System.out.printf( "\t %-25s %3.2f \n",cat, person.cat_count.get(cat));
                        s.count += 1;
                    }
                    else if(person.cat_count.get(cat) == null)
                    // System.out.printf( "\t %-25s %s \n",cat, "not rated");
                        System.out.print("");
                    //System.out.println("\t" + cat + "\t" + "not rated");
                    else
                    {
                        //System.out.printf( "\t %-25s %s \n",cat, "ignored");
                        System.out.print("");
                        //s.category.remove(cat);
                    }
                }

                //taking the average of all the categories of the attraction, 
                // rather than aggregate the score   
                if(s.count > 0)
                {
                    s.score = s.score / s.count;
                    //System.out.printf("\t %s %.2f\n\n","Score:", s.score );
                }

                //remove all attractions from list that have no rated score
                //             else if(s.count == 0)
                //                 ignoredAttractions.add(s);

                //remove all attractions from list that have 0 categories
                else if(!hasCategories)
                    ignoredAttractions.add(s);
            }

            //remove all the suggestions from list that were previously just stored in ignoredAttractions ^
            for(Suggestion attr: ignoredAttractions)
                attractions.remove(attr);

            //Mergesorts the scored suggestion objects
            Collections.sort(attractions);
            for(int i = 0; i<attractions.size(); i++)
            {
                //System.out.printf("%2d) %-35s %5.2f\n",
                //    i+1, attractions.get(i).title, attractions.get(i).score);
            }

            //System.out.println("Sorted Results:     " + person.user_id);

            pw.print("{\"body\": {\"suggestions\": [");

            Hashtable<String, Integer> catCounter = new Hashtable<String, Integer>();
            int size = attractions.size();
            for (int k=0; k<size; k++)
            {
                //                 System.out.printf("%2d) %-35s %5.2f\n",
                //                     k+1, attractions.get(0).title, attractions.get(0).score);
                int currID = attractions.get(0).id;
                String id = "00000000" + currID;
                id = id.substring(id.length()-8);
                pw.print("\"TRECCS-" + id + "-" + person.context_id + "\"");
                if(attractions.size() > 1) {pw.print(",");}

                Suggestion prev = attractions.remove(0);
                for(Suggestion s : attractions)
                {
                    int max = 0;
                    s.score = 0.0;
                    for(String cat : s.category)
                    {
                        //                     if(prev.category.contains(cat) && !ignoredCats.contains(cat))
                        //                     {
                        //                         s.score -= .5;
                        //                         break;
                        //                     }
                        if(!ignoredCats.contains(cat)) //if a valid category
                        {
                            if(catCounter.get(cat) == null)
                            {
                                catCounter.put(cat, 1);
                            }
                            else
                            {
                                catCounter.put(cat, catCounter.get(cat) + 1);
                            }
                            if(person.cat_count.get(cat) != null)
                            {
                                if(prev.category.contains(cat))
                                {
                                    person.cat_count.put(cat, person.cat_count.get(cat) - catCounter.get(cat)/10);
                                    //max = Math.max(max, catCounter.get(cat));
                                }
                                s.score += person.cat_count.get(cat);
                            }
                        }
                    }
                    if(s.count > 0)
                    {
                        s.score = s.score / s.count;
                    }
                    //s.score -= max/10;
                }

                Collections.sort(attractions);
            }
            pw.print("]}, \"groupid\": \"Siena_SUCCESS\", \"id\": " + person.response_id +
                ", \"runid\": \"runA\"}");
            pw.println();
        }
        pw.close();
    }
}
