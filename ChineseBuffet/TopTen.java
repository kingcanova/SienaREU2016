import java.util.*;
import java.io.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.*;
import org.jsoup.select.*;
import org.jsoup.helper.*;
import javax.swing.*;
/**
 * Find the first ten restaurants listed by yelp
 * from the source code of webpage
 * 
 * @author Aidan, Kevin, Zach
 * @version 5/18/15
 */
public class TopTen
{
    /**
     * 
     */
    public static void main(String[] args) throws IOException
    {
        //Prompt the user for search input
        String keyword = JOptionPane.showInputDialog("Search for:", null);
        String city = JOptionPane.showInputDialog("City:", null);
        String state = JOptionPane.showInputDialog("State abbreviation:", null);

        //Create the url based on the search input
        String url = String.format("http://www.yelp.com/search?find_desc=%s&find_loc=%s%%2C+%s",
                keyword, city, state);
        Document doc = Jsoup.connect(url).get();

        //Search the url for the business names
        Elements img = doc.select("img[class=\"photo-box-img\"][height=\"90\"]");
        for(Element name: img)
        {
            System.out.println(name.attr("alt"));
        }

        //         
        //         //read in the source code file called yelp.txt
        //         Scanner in = new Scanner(new File("search.html"));
        //         ArrayList<String> names = new ArrayList<String>();
        //         int count = 0;
        //         String line = "";
        //         String prev = "";
        //         //scan every line
        //         while(in.hasNextLine() && names.size() < 10)
        //         {    
        //             prev = line;
        //             line = in.nextLine();
        //             //check if contains class="biz-name"
        //             //tag for the name of the restaurants
        //             if(line.contains("photo-box-img") && line.contains("height=\"90\"")
        //                               && !prev.contains("adredir"))
        //             {
        //                 //System.out.println(line);
        //                 //line = line.replace("^.*img alt=\"([^\"])*".*$", "$1");
        //                 int index = line.indexOf("img alt=\"");
        //                 int ques = line.indexOf("\"", index+9);
        //                 line = line.substring(index+9, ques);
        //                 line = line.replaceAll("&amp;", "&");
        //                 line = line.replaceAll("&#39;", "'");
        //                 names.add(line);
        //                 System.out.println(line);
        //             }
        //         }
    }
}
