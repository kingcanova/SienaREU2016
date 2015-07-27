import java.util.*;
import java.io.*;

/**
 * Our project's run file.
 * 
 * Read in a JSON file of hundreds of requests for profiles, cities, and attractions and return a JSON file of responses 
 * that include ranked suggested POI's for each request
 */
public class ContextualSuggestion
{
    protected static Hashtable<Integer, Context> contexts = new Hashtable<Integer, Context>();
    protected static Hashtable<Integer, Suggestion> pois = new Hashtable<Integer, Suggestion>();
    protected static Hashtable<Integer, Profile> profiles = new Hashtable<Integer, Profile>();
    protected static Hashtable<Integer, ArrayList<Suggestion>> theCollection = new Hashtable<Integer, ArrayList<Suggestion>>();

    /**
     * main run method for project, returns our final JSON file for submission to TREC
     */
    public static void suggest() throws IOException
    {
        PrintWriter pw = new PrintWriter("SienaFinalOutput.json");
        //reset scores in hashtable since it's static and are saved unless JVM is reset
        theCollection.clear();      
        CSVreader reader = new CSVreader();

        //fill up array with categories we want to ignore for scoring purposes i.e. establishment, point of interest, etc
        Scanner in = new Scanner(new File("UneccessaryCats.txt"));
        ArrayList<String> ignoredCats = new ArrayList<String>();
        String line = " ";
        while (in.hasNextLine())
        {
            ignoredCats.add(in.nextLine());
        }

        //System.out.println("Running CSVReader");
        reader.run();

        //cycles through the profiles in the hashtable
        Set<Integer> people = profiles.keySet();
        for(Integer num : people)
        {
            Profile person = profiles.get(num);
            ArrayList<Suggestion> attractions = new ArrayList<Suggestion>();
            //get the list of all attractions from the location being suggested to the person,
            //and add to the attractions array if it's one of the person's candidates
            for (Suggestion sug : theCollection.get(person.context_id)) 
            {
                if (person.candidates.contains(sug.id))
                    attractions.add(sug);
            }

            ArrayList<Suggestion> ignoredAttractions = new ArrayList<Suggestion>();
            //Give each attraction a score based one the rating and frequency of a category
            //System.out.println("Scoring Attractions");

            //cycle through the person's candidates
            for (Suggestion s : attractions)
            {
                //System.out.println(s.title);
                boolean hasCategories = false; 
                for(String cat : s.category)
                {
                    //the current attraction must have categories if this loop is entered into
                    hasCategories = true;
                    //Add the score of each category to the current suggestion's total score,
                    //if it was rated by the user and isn't an ignored category
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

                //place the attractions that don't have categories at the very end of the suggested list
                else if(!hasCategories)
                    s.score = -Double.MAX_VALUE;
            }

            //remove all the suggestions from list that were previously just stored in ignoredAttractions ^
            //             for(Suggestion attr: ignoredAttractions)
            //                 attractions.remove(attr);

            //Mergesorts the scored suggestion objects

            //sort attractions before the penalty function
            Collections.sort(attractions);

            //print out the attrations and their scores before the penalty function
            //             for(int i = 0; i<attractions.size(); i++)
            //             {
            //                 //System.out.printf("%2d) %-35s %5.2f\n",
            //                 //    i+1, attractions.get(i).title, attractions.get(i).score);
            //             }

            //System.out.println("Sorted Results:     " + person.user_id);

            pw.print("{\"body\": {\"suggestions\": [");

            Hashtable<String, Integer> catCounter = new Hashtable<String, Integer>();
            int size = attractions.size();
            //PENALTY FUNCTION
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
                    boolean hasCategories = false;
                    for(String cat : s.category)
                    {
                        //                     if(prev.category.contains(cat) && !ignoredCats.contains(cat))
                        //                     {
                        //                         s.score -= .5;
                        //                         break;
                        //                     }
                        hasCategories = true;
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
                    else if(!hasCategories)
                    {
                        s.score = -Double.MAX_VALUE;
                    }
                    //s.score -= max/10;
                }
                //sort attractions after the penalty function
                Collections.sort(attractions);
            }
            pw.print("]}, \"groupid\": \"Siena_SUCCESS\", \"id\": " + num +
                ", \"runid\": \"SCIAIrunA\"}");
            pw.println();
        }
        pw.close();
    }
}
