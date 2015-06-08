import org.json.simple.*;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Something
{
    /**
     * just gets the first thing for now
     */
    public static JSONObject googleExtract(String jsonResponse) {
        Object obj = JSONValue.parse(jsonResponse);
        JSONObject jo = (JSONObject) obj;
        JSONArray results = (JSONArray)jo.get("results");
        return (JSONObject)results.get(0);
    }

    public static JSONObject foursquareExtract(String jsonResponse) {
        Object obj = JSONValue.parse(jsonResponse);
        JSONObject jo = (JSONObject) obj;
        JSONArray results = (JSONArray)jo.get("venues");
        return (JSONObject)results.get(0);
    }

    public static JSONObject yelpExtract(String jsonResponse) {
        Object obj = JSONValue.parse(jsonResponse);
        JSONObject jo = (JSONObject) obj;
        JSONArray results = (JSONArray)jo.get("businesses");
        return (JSONObject)results.get(0);
    }

    public static JSONObject combine(JSONObject google, JSONObject yelp, JSONObject foursquare) {
        JSONObject comb = new JSONObject();
        comb.put("geometry", google.get("geometry"));
        comb.put("name", google.get("name"));
        JSONArray categories = new JSONArray();
        categories.add(google.get("types")); // uggghhh
        categories.add(((JSONObject)yelp.get("categories")).get("shortName"));
        comb.put("categories", categories);
        comb.put("popularity", yelp.get("checkinsCount"));
        return comb;
    }

    public static void main(String args[]) {
        StringBuffer sb = new StringBuffer();
        Scanner s = null;
        try {
            s = new Scanner(new File("../foursqareresponse.json"));
        } catch (FileNotFoundException e) {
            System.err.println(e);
            System.exit(1);
        }
        while (s.hasNextLine())
            sb.append(s.nextLine() + "\n");
        String str = sb.toString();
        JSONObject jo = foursquareExtract(str);
        System.out.println(jo);
    }
}
