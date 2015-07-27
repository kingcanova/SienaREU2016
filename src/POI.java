
/**
 * Point of Interest, or Attraction. 
 * Each attraction has a name, description, url, and ID number
 * These POIs will later be ranked based on someone's profile 
 * @version May
 */
public class POI
{
    protected int id_num;
    protected String title;
    protected String description;
    protected String url;
    protected int context = 101;//ignore, used for testing purposes
    public POI(int id, String title, String description, String url)
    {
        this.id_num = id;
        this.title = title;
        this.description = description;
        this.url = url;
    }
}
