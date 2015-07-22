public class TrimCollection
{
    public static void main(String args[]) throws IOException {
        String coll = "collhead2015.txt", jsonfilename = "uhh.txt";
        JSONParser parser = new JSONParser();
        JSONObject profile = null;
        ArrayList<String> ids = new ArrayList<String>();
        File collection = new File(coll),
             jsonfile   = new File(jsonfilename);
        BufferedReader br = new BufferedReader(jsonfile);
        String line;
        while ((line = br.readline()) != null) {
            try {
                profile = (JSONObject) parser.parse(line);
            } catch (ParseException pe) {
                System.err.println("Parse exception");
                System.err.println(pe);
            }
        }
        
        
    }

    private void 
}
