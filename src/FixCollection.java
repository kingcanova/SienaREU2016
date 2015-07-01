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

    public static void fourCount() throws IOException, FileNotFoundException
    {
        String trecData = "../TRECData/collection_sorted_2015.csv"; 
        BufferedReader br = new BufferedReader(
                new FileReader(Paths.get(trecData).toFile()));
        PrintWriter foursquare = new PrintWriter( new File("../TRECData/all_four.csv"));
        String line = "";
        int i = 0;
        int[][] cities = new int[272][2];
        cities[0][0] = 151;
        while( (line = br.readLine()) != null)
        {
            String[] contents = CSVSplitter.split(line, 4);
            if(contents[2].contains("foursquare"))
            {
                foursquare.println(line);
                if(cities[i][0] != Integer.parseInt(contents[1]))
                {
                    i++;
                    cities[i][0] = Integer.parseInt(contents[1]);
                }
                cities[i][1]++;
            }
        }
        for(int j = 0; j<272; j++)
        {
            System.out.println("City: " + cities[j][0] + "\tfour_count: " + cities[j][1]);
        }
        br.close();
        foursquare.close();
    }

    public static void removeSpam() throws FileNotFoundException, IOException
    {
        buildCats();

        String trecData = "../TRECData/collection_sorted_2015.csv"; 
        BufferedReader br = new BufferedReader(
                new FileReader(Paths.get(trecData).toFile()));

        PrintWriter pw = new PrintWriter( new File("../TRECData/151_com.csv"));
        PrintWriter foursquare = new PrintWriter( new File("../TRECData/151_foursquare.csv"));
        PrintWriter yelp = new PrintWriter( new File("../TRECData/151_yelp.csv"));
        PrintWriter tripadvisor = new PrintWriter( new File("../TRECData/151_trip.csv"));
        PrintWriter other = new PrintWriter( new File("../TRECData/151_all.csv"));
        PrintWriter blogs = new PrintWriter( new File("../TRECData/151_blogs.csv"));
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
                //                 int word_count = contents[3].split(" ").length;
                //                 int len = contents[3].length();
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
                if(contents[2].contains("blog") || contents[3].contains("blog")
                    || contents[2].contains("forum") || contents[3].contains("forum"))
                {
                    blogs.println(line);
                }
                else if(contents[2].endsWith(".com") || contents[2].endsWith(".com/"))
                {
                    pw.println(line);
                }
                else
                {
                    other.println(line);
                }
            }

            line = br.readLine();
            contents = CSVSplitter.split(line, 4);
        }
        //Collections.sort(attr, null);
        //Collections.sort(webmultarray, null);
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
        other.close();
        tripadvisor.close();
        blogs.close();
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

    public static void urlReader(String param) throws IOException
    {
        try
        {
            Document doc = Jsoup.connect(param).get();
            String html = doc.toString();
            for(String cat : cats)
            {
                if(html.contains(cat+" "))
                {
                    System.out.println(cat);
                    //return true;
                }
            }
        }
        catch(Exception e) 
        {
            System.out.println(e);
        }
        //return false; 
    }
}

