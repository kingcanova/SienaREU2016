
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
    protected int id_num;
    protected String title;
    protected String description;
    protected String url;
    protected int context = 101;
    public POI(int id, String title, String description, String url)
    {
        this.id_num = id;
        this.title = title;
        this.description = description;
        this.url = url;
    }
}
