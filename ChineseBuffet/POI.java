
/**
 * Point of Interest, or Attraction. 
 * Each attraction as a name, description, and url.
 * These POI will then be ranked 1-50
 * 
 * @author Siena - Aidan
 * @version May
 */
public class POI
{
    protected String title;
    protected String description;
    protected String url;
    
    public POI(String title, String description, String url)
    {
        this.title = title;
        this.description = description;
        this.url = url;
    }
}
