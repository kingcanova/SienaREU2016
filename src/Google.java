import java.util.*;
import java.io.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.*;
import org.jsoup.select.*;
import org.jsoup.helper.*;
import javax.swing.*;
/**
 * Write a description of class Google here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Google
{
    public static void main(String args[]) throws IOException
    {
        String url = "https://www.google.com/#q=bombers+albany+yelp";
        Document doc = Jsoup.connect(url).get();
        System.out.println(doc);
    }
}
