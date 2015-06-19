import java.util.*;
import java.nio.*;
import java.nio.file.*;
import java.io.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.*;
import org.jsoup.select.*;
import org.jsoup.helper.*;
/**
 * Write a description of class FixCollection here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class FixCollection
{
    protected static ArrayList<String> cats = new ArrayList<String>();

    public static void removeSpam() throws FileNotFoundException, IOException
    {
        buildCats();

        String trecData = "../TRECData/collection_sorted_2015.csv"; 
        BufferedReader br = new BufferedReader(
                new FileReader(Paths.get(trecData).toFile()));

        PrintWriter pw = new PrintWriter( new File("../TRECData/151_all.csv"));
        PrintWriter foursquare = new PrintWriter( new File("../TRECData/151_foursquare.csv"));
        PrintWriter yelp = new PrintWriter( new File("../TRECData/151_yelp.csv"));
        PrintWriter tripadvisor = new PrintWriter( new File("../TRECData/151_trip.csv"));
        PrintWriter webmult = new PrintWriter( new File("../TRECData/151_web.csv"));
        String line =  br.readLine();
        String[] contents = CSVSplitter.split(line, 4);
        ArrayList<Temp> attr = new ArrayList<Temp>(); 
        ArrayList<Temp> webmultarray = new ArrayList<Temp>(); 
        ArrayList<String> web = new ArrayList<String>();
        while( contents[1].equals("151") )
        //for(int i = 0; i<200; i++)
        {
            if(contents[2].contains("yelp"))
                yelp.println(line);

            else if(contents[2].contains("foursquare"))
                foursquare.println(line);

            else if(contents[2].contains("tripadvisor"))
                tripadvisor.println(line); 

            else
            {
                if(urlReader(contents[2]))
                {
                    pw.println(line);
                }
                else
                {
                    webmult.println(line);
                }
                //                 //pw.print(line);
                //                 int word_count = contents[3].split(" ").length;
                //                 //pw.print(","+word_count);
                //                 int len = contents[3].length();
                //                 //pw.println(","+len);
                //                 int url = contents[2].length();
                //                 int slash = -1;
                //                 int index = 0;
                //                 while(index != -1)
                //                 {
                //                     index = contents[2].indexOf("/", index+1);
                //                     slash++;
                //                 }
                //                 int end = contents[2].indexOf(".", 7);
                //                 String site = contents[2].substring(7, end);
                //                 if(web.contains(site))
                //                 {
                //                     webmultarray.add(new Temp(line, url, slash, word_count, len));
                //                 }
                //                 else
                //                 {
                //                     web.add(site);
                //                     attr.add(new Temp(line, url, slash, word_count, len));
                //                 }

                line = br.readLine();
                contents = CSVSplitter.split(line, 4);
            }
        }
        //         Collections.sort(attr, null);
        //         Collections.sort(webmultarray, null);
        //         for(int i = 0; i < attr.size(); i++)
        //         {
        //             pw.print(attr.get(i).toString());
        //         }
        //         for(int i = 0; i < webmultarray.size(); i++)
        //         {
        //             webmult.print(webmultarray.get(i).toString());
        //         }
        pw.close();
        foursquare.close();
        yelp.close();
        webmult.close();
        tripadvisor.close();
    }

    public static void buildCats() throws FileNotFoundException, IOException
    {
        String trecData = "../TRECData/four_cat.txt"; 
        BufferedReader br = new BufferedReader(
                new FileReader(Paths.get(trecData).toFile()));
        String line = "";
        while( (line = br.readLine()) != null )
        {
            cats.add(line);
        }
    }

    public static boolean urlReader(String param) throws IOException
    { 
        try
        {
            Document doc = Jsoup.connect(param).get();
            String html = doc.toString();
            for(String cat : cats)
            {
                if(html.contains(cat))
                {
                    return true;
                }
            }
        }
        catch(Exception e) 
        {
            System.out.println(e);
        }
        return false; 
    }
}

