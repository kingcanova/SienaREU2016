import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;
import java.util.*;

public class CSVreader {

    public void run() {
        ContextualSuggestion test = new ContextualSuggestion();

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
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(csvFile + locations));

            line = br.readLine();
            String[] params = line.split(cvsSplitBy);

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] context = line.split(cvsSplitBy);
                
                test.cities.add(new Context(Integer.parseInt(context[0]), context[1], context[2],
                                Double.parseDouble(context[3]), Double.parseDouble(context[4])));
                
            }
            
            br = new BufferedReader(new FileReader(csvFile + pois));

            line = br.readLine();
            params = line.split(cvsSplitBy);

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] context = line.split(cvsSplitBy);
                
                
            }

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
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Done");
    }

    public static void main(String[] args) {
        CSVreader obj = new CSVreader();
        obj.run();

    }
}
