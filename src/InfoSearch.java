import java.util.*;
import java.io.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.*;
import org.jsoup.select.*;
import org.jsoup.helper.*;
import javax.swing.*;
/**
 * beginning of program that would retrieve information from an attraction's url
 * not used by our final project
 */
public class InfoSearch
{
    public static void main(String args[]) throws IOException
    {
        //Prompt the user for search input
        String url = JOptionPane.showInputDialog("Provide url:", null);
        Document doc = Jsoup.connect(url).get();

        //Search the url for the business names
        String name = doc.select("meta[property=\"og:title\"]").first().attr("content");
        String rating = doc.select("meta[itemprop=\"ratingValue\"]").first().attr("content");
        Element priceelem = doc.select(
                    "span[class=\"business-attribute price-range\"]"
                     +  "[itemprop=\"priceRange\"]").first();
        String price = priceelem instanceof Object ? priceelem.text() : "n/a";
        String cats = doc.select("span[class=\"category-str-list\"]").first().text();
        String addr = doc.select("address").first().text();
        //System.out.println(name + "\n" + rating + "\n" + price + "\n" + cats + "\n" + addr);
        System.out.printf("Name:\t\t%s\nRating:\t\t%s\nPrice:\t\t%s\nCategories:\t%s\nAddress:\t%s\n", 
                           name, rating, price, cats, addr);
    }
}
